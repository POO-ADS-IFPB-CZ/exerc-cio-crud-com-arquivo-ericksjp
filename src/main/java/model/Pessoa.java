package model;

import java.io.Serializable;

public class Pessoa implements Serializable {
  private static final long serialVersionUID = 1L;
  private String nome;
  private String email;

  public Pessoa(String nome, String email) {
    this.nome = nome;
    this.email = email;
  }

  public String getNome() {
    return nome;
  }

  @Override
  public String toString() {
    return "Pessoa{" +
        "nome='" + this.nome + '\'' +
        ", email='" + this.email + '\'' +
        '}';
  }

  public void setnome(String nome) {
    this.nome = nome;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
