package api.morebread.auth;

import java.security.Key;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwsHeader;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import api.morebread.model.Usuario;

public class JWT {
	private static String SECRET_KEY = "oeRaYY7Wo24sDqKSX3IM9ASGmdGPmkTd9jo1QTy4b7P9Ze5";
	
	public String encode(Usuario usuario, int tempo) {
		Instant now = Instant.now();
		
		SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
		byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
	    Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());
	    
	    JwtBuilder builder = Jwts.builder()
			.setId(Integer.toString(usuario.getId()))
			.setSubject(usuario.getNome())
			.setHeaderParam("usuario", usuario)
			.setIssuedAt(Date.from(now))
			.setExpiration(Date.from(now.plus(tempo, ChronoUnit.HOURS)))
			.signWith(signatureAlgorithm, signingKey);
	    
	    return builder.compact();
	}
	
	public Boolean valid(String token, Connection conexao) throws SQLException {
		try {
			Claims body = Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
	        
	        Date expiresAt = body.getExpiration();
	        Date now = new Date();
	        
	        if (expiresAt.before(now)) {
	        	return false;
	        }		
		}
		catch(Exception e) {
			e.printStackTrace();
			
			return false;
		}
        
		String buscaToken = "SELECT * FROM tokens WHERE token = ?";
		
		PreparedStatement p = conexao.prepareStatement(buscaToken);
		
		p.setString(1, token);
		
		ResultSet rs = p.executeQuery();
		
		if (rs.next()) {
			return true;
		}
		else {
			return false;			
		}
	}
}
