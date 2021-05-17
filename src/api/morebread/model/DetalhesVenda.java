package api.morebread.model;

public class DetalhesVenda {
  private Integer id;
  
  private Integer vendaId;
  private Integer quantidade;
  private Integer produtoId;
  private String produtoNome;
  private String usuarioNome;
  private Float valor;
  private String dataRealizada;
  
  public Integer getVendaId() {
	return vendaId;
  }
  public void setVendaId(Integer vendaId) {
	this.vendaId = vendaId;
  }
  
  public Integer getId() {
	return id;
  }
  public void setId(Integer id) {
	this.id = id;
  }
  
  public Integer getQuantidade() {
	return quantidade;
  }
  public void setQuantidade(Integer quantidade) {
	this.quantidade = quantidade;
  }
  
  public Integer getProdutoId() {
	return produtoId;
  }
  public void setProdutoId(Integer produtoId) {
	this.produtoId = produtoId;
  }
  
  public String getProdutoNome() {
	return produtoNome;
  }
  public void setProdutoNome(String produtoNome) {
	this.produtoNome = produtoNome;
  }
  
  public String getUsuarioNome() {
	return usuarioNome;
  }
  public void setUsuarioNome(String usuarioNome) {
	this.usuarioNome = usuarioNome;
  }
  
  public Float getValor() {
	return valor;
  }
  public void setValor(Float valor) {
	this.valor = valor;
  }
  
  public String getDataRealizada() {
	return dataRealizada;
  }
  public void setDataRealizada(String dataRealizada) {
	this.dataRealizada = dataRealizada;
  }
}