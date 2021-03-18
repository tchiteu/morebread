package api.morebread.model;

public class Produto {
  private Integer id;
  
  private String nome;
  private Float valor;
  private Integer quantidade;

  public void setId(Integer id) {
    this.id = id;
  }

  public Integer getId() {
    return this.id;
  }

  public void setNome(String nome) {
    this.nome = nome;
  }

  public String getNome() {
    return this.nome;
  }

  public void setValor(Float valor) {
    this.valor = valor;
  }

  public Float getValor() {
    return this.valor;
  }

  public void setQuantidade(Integer quantidade) {
    this.quantidade = quantidade;
  }

  public Integer getQuantidade() {
    return this.quantidade;
  }
}