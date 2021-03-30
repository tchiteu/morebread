package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import api.morebread.model.Venda;
import api.morebread.model.Produto;
import api.morebread.model.Retorno;
import api.morebread.connection.ConnectionFactory;

public class VendaDAO {
  private Connection conexao = ConnectionFactory.getConnection();
  
  public Retorno cadastraVenda(Venda venda) {
    Retorno retorno = new Retorno(false);
    PreparedStatement stmt = null;

    try {
      String query = "INSERT INTO vendas (valor_total, usuario_id) VALUES(?, ?)";
      stmt = this.conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      
      stmt.setFloat(1, venda.getValorTotal());
      stmt.setInt(2, venda.getUsuarioId());

      stmt.executeUpdate();
      
      ResultSet rs = stmt.getGeneratedKeys();
      
      if (rs.next()) {
    	Integer venda_id;
	    venda_id = rs.getInt(1);
	    
	    for (Produto produto : venda.getProdutos()) {
	      stmt = this.conexao.prepareStatement("INSERT INTO detalhes_venda (venda_id, produto_id, quantidade, valor) VALUES (?, ?, ?, ?)");
	      
	      stmt.setInt(1, venda_id);
	      stmt.setInt(2, produto.getId());
	      stmt.setInt(3, produto.getQuantidade());
	      stmt.setFloat(4, produto.getValor());
	      
	      stmt.execute();
	    }
      }
    } catch (SQLException ex) {
      retorno.setErro(true);
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }
}