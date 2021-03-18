package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.math.BigInteger;
import java.security.MessageDigest;

import api.morebread.model.Usuario;
import api.morebread.connection.ConnectionFactory;

public class UsuarioDAO {
  public Boolean cadastrar(Usuario usuario) {
    Connection conexao = ConnectionFactory.getConnection();

    Boolean retorno = false;
    PreparedStatement stmt = null;

    try {
      stmt = conexao.prepareStatement("INSERT INTO usuarios (nome,cargo,email,senha) VALUES(?,?,?,?)");
      String senhaCripto = criptografaSenha(usuario.getSenha());
      
      stmt.setString(1, usuario.getNome());
      stmt.setString(2, usuario.getCargo());
      stmt.setString(3, usuario.getEmail());
      stmt.setString(4, senhaCripto);

      stmt.executeUpdate();
      retorno = true;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }

  public List<Usuario> buscar() {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;

    List<Usuario> usuarios = new ArrayList<Usuario>();

    try {
      stmt = conexao.prepareStatement("SELECT id, nome, email, cargo FROM usuarios");
      ResultSet resultado = stmt.executeQuery();

      while (resultado.next()) {
        Usuario usuario = new Usuario();

        usuario.setId(resultado.getInt("id"));
        usuario.setNome(resultado.getString("nome"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setCargo(resultado.getString("cargo"));

        usuarios.add(usuario);
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return usuarios;
  }

  public Usuario buscarPorId(int id) {
    Connection conexao = ConnectionFactory.getConnection();
    Usuario usuario = new Usuario();
    
    PreparedStatement stmt = null;

    try {
      stmt = conexao.prepareStatement("SELECT id, nome, email, cargo FROM usuarios WHERE id = ?");
      stmt.setInt(1, id);
      ResultSet resultado = stmt.executeQuery();

      if(resultado.next()) {
        usuario.setId(resultado.getInt("id"));
        usuario.setNome(resultado.getString("nome"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setCargo(resultado.getString("cargo"));
      }

    } catch(SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return usuario;
  }

  public Boolean editar(int id, Usuario usuario) {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;

    Boolean retorno = false;

    if(buscarPorId(id).getNome() == null) {
      return false;
    }

    try {
      stmt = conexao.prepareStatement("UPDATE usuarios SET nome = ?, email = ?, cargo = ? WHERE id = ?");
      stmt.setString(1, usuario.getNome());
      stmt.setString(2, usuario.getEmail());
      stmt.setString(3, usuario.getCargo());
      stmt.setInt(4, id);

      stmt.executeUpdate();
      retorno = true;
    } catch (SQLException ex) {
      ex.printStackTrace();
      retorno = false;
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }

  public Boolean deletar(int id) {
    Connection conexao = ConnectionFactory.getConnection();

    PreparedStatement stmt = null;
    Boolean retorno = false;

    if(buscarPorId(id).getNome() == null) {
      return false;
    }

    try {
      stmt = conexao.prepareStatement("DELETE FROM usuarios WHERE id = ?");
      stmt.setInt(1, id);

      stmt.executeUpdate();
      retorno = true;
    } catch(SQLException ex) {
      ex.printStackTrace();
      retorno = false;
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
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
}