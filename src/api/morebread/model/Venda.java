package api.morebread.model;

import java.util.ArrayList;
import java.util.List;

public class Venda {
  private Integer id;
  
  private Float valorTotal;
  private Integer usuarioId;
  private List<Produto> produtos = new ArrayList<Produto>();
  private String dtRealizada;

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
  
  public void setProdutos(List<Produto> produtos) {
	  
  }
  
  public String getDtRealizada() {
	return this.dtRealizada;
  }
  
  public void setDtRealizada(String dtRealizado) {
	this.dtRealizada = dtRealizado;
  }
}