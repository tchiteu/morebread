package api.morebread.model;

import java.util.List;

public class Retorno {
  private Boolean erro;
  private String mensagem;
  private Integer status_code;
  private Integer id;

  private String token;

  private List<Usuario> usuarios;
  private List<Produto> produtos;
  private List<Venda> vendas;
  private Usuario usuario;
  
  public Retorno () {
  }
  
  public Retorno (Integer status) {
	this.status_code = status;
  }
  
  public Retorno (Boolean erro) {
	this.erro = erro;
  }
  
  public Retorno (String mensagem) {
	this.mensagem = mensagem;
  }
  
  public Retorno (Boolean erro, String mensagem) {
    this.mensagem = mensagem;
    this.erro = erro;
  }
  
  
  public Retorno (Integer status, String mensagem) {
    this.status_code = status;
    this.mensagem = mensagem;
  }

  public Retorno (Boolean erro, String mensagem, Integer status) {
    this.erro = erro;
    this.mensagem = mensagem;
    this.status_code = status;
  }

  // Setters
  public void setId(Integer id) {
	this.id = id;
  }
  
  public Integer getId() {
	  return this.id;
  }
  
  public void setErro (Boolean erro) {
    this.erro = erro;
  }
  
  public Boolean getErro() {
	  return this.erro;
  }

  public void setStatusCode(Integer status_code) {
    this.status_code = status_code;
  }
  
  public Integer getStatusCode() {
	  return this.status_code;
  }
  
  public void setProdutos(List<Produto> produtos) {
	  this.produtos = produtos;
  }
  
  public List<Produto> getProdutos() {
    return this.produtos;
  }  
  
  public void setToken (String token) {
    this.token = token;
  }
  
  public String getToken() {
    return this.token;
  }
  
  public void setUsuarios(List<Usuario> usuarios) {
    this.usuarios = usuarios;
  }
  
  public List<Usuario> getUsuarios() {
	  return this.usuarios;
  }
  
  public void setUsuario(Usuario usuario) {
	this.usuario = usuario;
  }  
  
  public Usuario getUsuario() {
	  return this.usuario;
  }
 
  
  public void setMensagem(String mensagem) {
	  this.mensagem = mensagem;
  }
  
  public String getMensagem() {
    return this.mensagem;
  }
  
  // Utils
  public void toResponse() {
	this.status_code = null;
	this.erro = null;
  }

public List<Venda> getVendas() {
	return vendas;
}

public void setVendas(List<Venda> vendas) {
	this.vendas = vendas;
}
}