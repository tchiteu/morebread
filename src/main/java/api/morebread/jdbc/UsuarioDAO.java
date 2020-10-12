package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import api.morebread.model.Usuario;
import api.morebread.connection.ConnectionFactory;

public class UsuarioDAO {

  public Boolean cadastra(Usuario usuario) {
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
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }

  public List<Usuario> busca() {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;

    List<Usuario> usuarios = new ArrayList<Usuario>();

    try {
      stmt = conexao.prepareStatement("SELECT id, nome, email, cargo FROM usuarios");
      ResultSet resultado = stmt.executeQuery();

      while(resultado.next()) {
        Usuario usuario = new Usuario();
        
        usuario.setId(resultado.getInt("id"));
        usuario.setNome(resultado.getString("nome"));
        usuario.setEmail(resultado.getString("email"));
        usuario.setCargo(resultado.getString("cargo"));

        usuarios.add(usuario);
      }

    } catch(SQLException ex) {
      ex.printStackTrace();
    }

    return usuarios;
  }
}
