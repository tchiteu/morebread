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
import api.morebread.model.Usuario;
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
}