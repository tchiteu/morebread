package api.morebread.model;

public class Usuario {
  private Long id;
  
  private String nome;
  private String email;
	private String cargo;
	private String senha;

  public void setId(Long id) {
    this.id = id;
  }

  public Long getId() {
    return this.id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return this.nome;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getEmail() {
    return email;
  }

  public void setCargo(String cargo) {
    this.cargo = cargo;
  }

  public String getCargo() {
    return this.cargo;
  }

  public void setSenha(String senha) {
    this.senha = senha;
  }

  public String getSenha() {
    return this.senha;
  }
}