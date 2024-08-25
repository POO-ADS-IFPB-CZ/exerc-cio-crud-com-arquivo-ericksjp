package dao;

import java.io.IOException;

public class PessoaDAOSerFile extends PessoaDAOFile {
  public PessoaDAOSerFile(String filePath) throws ClassNotFoundException, IOException {
    super(filePath);
  }
}
