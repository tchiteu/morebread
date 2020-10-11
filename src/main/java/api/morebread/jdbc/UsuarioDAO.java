package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import api.morebread.model.Usuario;
import api.morebread.connection.ConnectionFactory;

public class UsuarioDAO {
  public Boolean create(Usuario usuario) {
    Connection conexao = ConnectionFactory.getConnection();
    
    Boolean retorno = false;
    PreparedStatement stmt = null;

    try {
      stmt = conexao.prepareStatement("INSERT INTO usuarios (nome,cargo,email,senha) VALUES(?,?,?,?)");
      
      stmt.setString(1, usuario.getNome());
      stmt.setString(2, usuario.getCargo());
      stmt.setString(3, usuario.getEmail());
      stmt.setString(4, usuario.getSenha());
      
      stmt.executeUpdate();

      System.out.println("Salvo com sucesso");
      retorno = true;
    } 
    catch (SQLException ex) {
      ex.printStackTrace();
    }
    finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }
}
