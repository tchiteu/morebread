package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import api.morebread.model.Venda;
import api.morebread.model.Produto;
import api.morebread.model.Retorno;
import api.morebread.model.DetalhesVenda;
import api.morebread.connection.ConnectionFactory;

public class VendaDAO {
  private Connection conexao = ConnectionFactory.getConnection();
  
  public Retorno cadastrar(Venda venda) {
    Retorno retorno = new Retorno(false);
    PreparedStatement stmt = null;
    
    try {
      String query = "INSERT INTO vendas (valor_total, usuario_id, usuario_nome, data_realizada) VALUES(?, ?, ?, ?)";
      stmt = this.conexao.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
      
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");  
      LocalDateTime date = LocalDateTime.now();
      
      stmt.setFloat(1, venda.getValorTotal());
      stmt.setInt(2, venda.getUsuarioId());
      stmt.setString(3, venda.getUsuarioNome());
      stmt.setString(4, dateFormatter.format(date));

      stmt.executeUpdate();
      
      ResultSet rs = stmt.getGeneratedKeys();
      
      if (rs.next()) {
    	Integer venda_id;
    	Float valorVendaProduto;
    	
	    venda_id = rs.getInt(1);
	    
	    for (Produto produto : venda.getProdutos()) {
    	  valorVendaProduto = produto.getQuantidade() * produto.getValor(); 
	      stmt = this.conexao.prepareStatement("INSERT INTO detalhes_venda (venda_id, produto_id, produto_nome, quantidade, valor) VALUES (?, ?, ?, ?, ?)");
	      
	      stmt.setInt(1, venda_id);
	      stmt.setInt(2, produto.getId());
	      stmt.setString(3, produto.getNome());
	      stmt.setInt(4, produto.getQuantidade());
	      stmt.setFloat(5, valorVendaProduto);
	      
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
  
  public Retorno listar() {
    Retorno retorno = new Retorno(false);
    PreparedStatement stmt = null;
    
    List<Venda> vendas = new ArrayList<Venda>();
    
    try {
      stmt = conexao.prepareStatement("SELECT * FROM vendas");
      ResultSet resultado = stmt.executeQuery();

      while (resultado.next()) {
        Venda venda = new Venda();

        venda.setId(resultado.getInt("id"));
        venda.setUsuarioNome(resultado.getString("usuario_nome"));
        venda.setValorTotal(resultado.getFloat("valor_total"));
        venda.setDataRealizada(resultado.getString("data_realizada"));

        vendas.add(venda);
      }
      
      retorno.setVendas(vendas);
    } catch (SQLException ex) {
        ex.printStackTrace();
        retorno.setErro(true);
    } finally {
        ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }
  
  public Boolean deletar(Integer id) {
	PreparedStatement stmt = null;
	Boolean retorno = false;

    try {
      // Deleta os detalhes da venda
	  stmt = conexao.prepareStatement("DELETE FROM detalhes_venda WHERE venda_id = ?");
      stmt.setInt(1, id);

	  int delete = stmt.executeUpdate();
	  stmt.close();
	  
	  if (delete == 0) {
		  return retorno;
	  }
	      
	  // Deleta a venda	      
	  stmt = conexao.prepareStatement("DELETE FROM vendas WHERE id = ?");
	  stmt.setInt(1, id);

	  stmt.executeUpdate();
	          
	  retorno = true;
	} 
    catch(SQLException ex) {
	  ex.printStackTrace();
	  retorno = false;
	}
    finally {
      ConnectionFactory.closeConnection(conexao, stmt);
	}
	    
	return retorno;
  }
  
  public Retorno gerarRelatorio(String dt_inicio, String dt_fim) {
    Retorno retorno = new Retorno(false);
    PreparedStatement stmt = null;
    
    List<DetalhesVenda> detalhesVendas = new ArrayList<DetalhesVenda>();
    
    try {
      String query = ("SELECT vendas.id, vendas.usuario_nome, vendas.data_realizada, detalhes_venda.produto_id, detalhes_venda.produto_nome, detalhes_venda.quantidade, detalhes_venda.valor \n"
      		+ " FROM vendas INNER JOIN detalhes_venda ON vendas.id = detalhes_venda.venda_id AND DATE(data_realizada) >= ? AND DATE(data_realizada) <= ?");
      stmt = conexao.prepareStatement(query);
      stmt.setString(1, dt_inicio);
      stmt.setString(2, dt_fim);
      
      ResultSet resultado = stmt.executeQuery();

      while (resultado.next()) {
        DetalhesVenda detalhesVenda = new DetalhesVenda();

        detalhesVenda.setVendaId(resultado.getInt("id"));
        detalhesVenda.setDataRealizada(resultado.getString("data_realizada"));
        detalhesVenda.setUsuarioNome(resultado.getString("usuario_nome"));
        detalhesVenda.setProdutoNome(resultado.getString("produto_nome"));
        detalhesVenda.setQuantidade(resultado.getInt("quantidade"));
        detalhesVenda.setValor(resultado.getFloat("valor"));

        detalhesVendas.add(detalhesVenda);
      }
      
      retorno.setDetalhesVendas(detalhesVendas);
    } catch (SQLException ex) {
        ex.printStackTrace();
        retorno.setErro(true);
    } finally {
        ConnectionFactory.closeConnection(conexao, stmt);
    }
	    
    return retorno;
  }
}