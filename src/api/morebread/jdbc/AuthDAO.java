package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.math.BigInteger;
import java.security.MessageDigest;

import api.morebread.model.Usuario;
import api.morebread.model.Retorno;
import api.morebread.connection.ConnectionFactory;
import api.morebread.auth.JWT;

public class AuthDAO {
  private JWT jwt = new JWT();
  
  public Retorno login(Usuario usuario) {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;
    Retorno retorno = new Retorno(true);

    try {
      stmt = conexao.prepareStatement("SELECT id, nome, cargo FROM usuarios WHERE email = ? AND senha = ?;");
      
      String senhaCripto = criptografaSenha(usuario.getSenha());

      stmt.setString(1, usuario.getEmail());
      stmt.setString(2, senhaCripto);

      ResultSet resultado = stmt.executeQuery();
      
      if (resultado.next()) {
    	usuario.setId(resultado.getInt("id"));
    	usuario.setNome(resultado.getString("nome"));
    	usuario.setSenha(null);
      usuario.setCargo(resultado.getString("cargo"));

      String token = geraToken(usuario);
      
      retorno.setErro(false);
      retorno.setToken(token);
      retorno.setUsuario(usuario);
      }
    }
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }

  public Boolean verificaToken(String token) {
    Connection conexao = ConnectionFactory.getConnection();
    
    try {
		if(jwt.valid(token, conexao)) {
		  return true;
		}
		else {
		  return false;
		}
	} 
    catch (SQLException e) {
		e.printStackTrace();
		return false;
	}
    finally {
        ConnectionFactory.closeConnection(conexao);
      }
  }

  public Boolean logout(String token) {
	Connection conexao = ConnectionFactory.getConnection();
	PreparedStatement stmt = null;
	
	try {
      stmt = conexao.prepareStatement("DELETE FROM tokens WHERE token = ?");
      stmt.setString(1, token);

      stmt.executeUpdate();
      return true;
	}
	catch (SQLException e) {
	  e.printStackTrace();
	  return false;
	}
	finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }
  }
  
  private String criptografaSenha(String senha) {
    String senhaCripto = "";
    
    MessageDigest md = null;
    try {
      md = MessageDigest.getInstance("MD5");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    
    md.update(senha.getBytes(), 0, senha.length());
		
    senhaCripto = new BigInteger(1, md.digest()).toString(16);
    
    return senhaCripto;
  }
  
  private String geraToken(Usuario usuario) {
	Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;
    String token = jwt.encode(usuario, 12);
    
    try {
	  stmt = conexao.prepareStatement("INSERT INTO tokens (token, usuario_id) VALUES(?, ?)");
	  stmt.setString(1, token);
	  stmt.setInt(2, usuario.getId());
	  	
	  stmt.executeUpdate();
    }    
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }
  	
  	return token;
  }
  
}