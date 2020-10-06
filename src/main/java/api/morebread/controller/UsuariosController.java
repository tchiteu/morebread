package api.morebread.controller;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Response;

import api.morebread.connection.ConnectionFactory;
import api.morebread.model.Usuario;

public class UsuariosController {
  public static void main(String[] args) {    
    buscaTodos();
  }

  public static Response buscaTodos() {
    Connection conexao = ConnectionFactory.getConnection();
    
    List<Usuario> usuarios = new ArrayList<Usuario>();
    
    try {
      String query = "SELECT * FROM usuarios;";
      ResultSet result = null;

      PreparedStatement ps = conexao.prepareStatement(query);
      
      result = ps.executeQuery();

      while(result.next()) {
        Usuario usuario = new Usuario();

        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        usuario.setCargo(result.getString("cargo"));
        usuario.setEmail(result.getString("email"));

        System.out.println(result.getString("nome"));

        usuarios.add(usuario);
      }

      result.close();
      ps.close();
      conexao.close();
    } 
    catch(SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }
    
    return Response.status(200).entity(usuarios).build();
  }
}
