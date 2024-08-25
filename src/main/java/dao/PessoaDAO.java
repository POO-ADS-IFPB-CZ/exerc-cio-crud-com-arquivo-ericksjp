package dao;

import model.Pessoa;

public interface PessoaDAO {
  public boolean update(String email, Pessoa p);

  public boolean delete(String email);

  public boolean create(Pessoa p);

  public Pessoa encontrarPorEmail(String nome);

  public boolean temPessoa(String email);

  public Pessoa[] encontrarPorNome(String email);

  public Pessoa[] readAll();

  public boolean removeAll();
}
