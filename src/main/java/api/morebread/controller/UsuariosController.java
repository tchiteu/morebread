package api.morebread.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import api.morebread.model.Usuario;
import api.morebread.connection.Database;

public class UsuariosController {
  // public static void main(String[] args) {
  //   Connection conexao = Database.getConnection();
  //   String query = "SELECT * FROM usuarios";
    
  //   catch(SQLException ex) {
  //     System.out.println("SQLException: " + ex.getMessage(String query = "SELECT * FROM usuarios";));
  //     System.out.println("SQLState: " + ex.getSQLState());
  //     System.out.println("VendorError: " + ex.getErrorCode());
  //   }
  // }

  public static ArrayList<Usuario> buscaTodos() {
    Connection conexao = Database.getConnection();
    
    ArrayList<Usuario> usuarios = new ArrayList<>();
    
    try {
      String query = "SELECT * FROM usuarios";

      Statement statement = conexao.createStatement();
      ResultSet result = statement.executeQuery(query);

      while(result.next()) {
        Usuario usuario = new Usuario();

        usuario.setId(result.getLong("id"));
        usuario.setNome(result.getString("nome"));
        usuario.setCargo(result.getString("cargo"));
        usuario.setEmail(result.getString("email"));

        usuarios.add(usuario);
      }
    } 
    catch(SQLException ex) {
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }

    return usuarios;
  }
}
