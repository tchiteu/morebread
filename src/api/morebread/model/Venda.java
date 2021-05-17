package api.morebread.model;

import java.util.ArrayList;
import java.util.List;

public class Venda {
  private Integer id;
  
  private Float valorTotal;
  private Integer usuarioId;
  private String usuarioNome;
  private List<Produto> produtos = new ArrayList<Produto>();
  private String dataRealizada;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  public void setValorTotal(Float valorTotal) {
    this.valorTotal = valorTotal;
  }

  public Float getValorTotal() {
    return this.valorTotal;
  }

  public Integer getUsuarioId() {
	return this.usuarioId;
  }

  public void setUsuarioId(Integer usuarioId) {
	this.usuarioId = usuarioId;
  }
  
  public List<Produto> getProdutos() {
	  return this.produtos;
  }

  public void setProdutos(List<Produto> produtos) {
	  this.produtos = produtos;
  }
  
  public String getDataRealizada() {
	return this.dataRealizada;
  }
  
  public void setDataRealizada(String dtRealizado) {
	this.dataRealizada = dtRealizado;
  }

  public String getUsuarioNome() {
	return usuarioNome;
  }

  public void setUsuarioNome(String usuarioNome) {
	this.usuarioNome = usuarioNome;
  }
}