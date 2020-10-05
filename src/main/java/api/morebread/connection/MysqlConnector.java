package api.morebread.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnector {
  public static void main(String[] args) {
    Connection test = getConnection();
    System.out.println(test);
  }

  public static Connection getConnection() {
    Connection conexao = null;
    
    try {
      conexao = DriverManager.getConnection("jdbc:mysql://localhost/padaria?" + "user=root&password=root");
    } catch (SQLException ex) {
      // Manipulando erros
      System.out.println("SQLException: " + ex.getMessage());
      System.out.println("SQLState: " + ex.getSQLState());
      System.out.println("VendorError: " + ex.getErrorCode());
    }

    return conexao;
  }
}
