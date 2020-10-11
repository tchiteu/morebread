package api.morebread.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnectionFactory {
  public static Connection getConnection() {
    Connection conexao = null;
    
    try {
      conexao = DriverManager.getConnection("jdbc:mysql://localhost/padaria","root","root");
    } catch (SQLException ex) {
      // Manipulando erros
      throw new RuntimeException("Erro ao se conectar ao banco de dados", ex);
    }

    return conexao;
  }

  public static void closeConnection(Connection conexao) {
    try {
      if(conexao != null) {
        conexao.close();
      }
    }
    catch (SQLException ex) {
      throw new RuntimeException("Erro ao fechar conexão ao banco de dados", ex);
    }
  }

  public static void closeConnection(Connection conexao, PreparedStatement stmt) {
    closeConnection(conexao);

    try {
      if(stmt != null) {
        stmt.close();
      }
    }
    catch (SQLException ex) {
      throw new RuntimeException("Erro ao fechar conexão ao banco de dados", ex);
    }
  }

  public static void closeConnection(Connection conexao, PreparedStatement stmt, ResultSet rs) {
    closeConnection(conexao, stmt);

    try {
      if(rs != null) {
        rs.close();
      }
    }
    catch (SQLException ex) {
      throw new RuntimeException("Erro ao fechar conexão ao banco de dados", ex);
    }
  }
}
