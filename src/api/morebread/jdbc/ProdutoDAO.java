package api.morebread.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import api.morebread.model.Produto;
import api.morebread.connection.ConnectionFactory;

public class ProdutoDAO {
  public Boolean cadastrar(Produto produto) {
    Connection conexao = ConnectionFactory.getConnection();

    Boolean retorno = false;
    PreparedStatement stmt = null;

    try {
      stmt = conexao.prepareStatement("INSERT INTO produtos (nome,valor,quantidade) VALUES(?,?,?)");
      
      stmt.setString(1, produto.getNome());
      stmt.setFloat(2, produto.getValor());
      stmt.setInt(3, produto.getQuantidade());

      stmt.executeUpdate();
      retorno = true;
    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return retorno;
  }

  public List<Produto> buscar() {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;

    List<Produto> produtos = new ArrayList<Produto>();

    try {
      stmt = conexao.prepareStatement("SELECT id, nome, valor, quantidade FROM produtos");
      ResultSet resultado = stmt.executeQuery();

      while (resultado.next()) {
        Produto produto = new Produto();

        produto.setId(resultado.getInt("id"));
        produto.setNome(resultado.getString("nome"));
        produto.setValor(resultado.getFloat("valor"));
        produto.setQuantidade(resultado.getInt("quantidade"));

        produtos.add(produto);
      }

    } catch (SQLException ex) {
      ex.printStackTrace();
    } finally {
      ConnectionFactory.closeConnection(conexao, stmt);
    }

    return produtos;
  }

  public Boolean editar(Integer id, Produto produto) {
    Connection conexao = ConnectionFactory.getConnection();
    PreparedStatement stmt = null;

    Boolean retorno = false;

    try {
      stmt = conexao.prepareStatement("UPDATE produtos SET nome = ?, valor = ?, quantidade = ? WHERE id = ?");
      stmt.setString(1, produto.getNome());
      stmt.setFloat(2, produto.getValor());
      stmt.setInt(3, produto.getQuantidade());
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

  public Boolean deletar(Integer id) {
    Connection conexao = ConnectionFactory.getConnection();

    PreparedStatement stmt = null;
    Boolean retorno = false;

    try {
      stmt = conexao.prepareStatement("DELETE FROM produtos WHERE id = ?");
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
}