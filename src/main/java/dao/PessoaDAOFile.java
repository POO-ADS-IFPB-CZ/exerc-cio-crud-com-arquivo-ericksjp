package dao;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Pessoa;
import utils.CSVFileHandler;
import utils.FileHandler;
import utils.SERFileHandler;

public class PessoaDAOFile implements PessoaDAO {
  private File file;
  private Map<String, Pessoa> pessoasCache;
  private FileHandler fileHandler;

  public PessoaDAOFile(String filePath) throws ClassNotFoundException, IOException {
    if (filePath.endsWith(".csv")) {
      fileHandler = new CSVFileHandler();
    } else if (filePath.endsWith(".ser")) {
      fileHandler = new SERFileHandler();
    } else {
      throw new IllegalArgumentException("Invalid file extension!");
    }
    file = new File(filePath);

    if (!file.exists() && !file.createNewFile())
      throw new IOException("Error creating file!");

    pessoasCache = fileHandler.loadFromFile(file);
  }

  public File getFile() {
    return new File(file.getAbsolutePath());
  }

  public Map<String, Pessoa> getPessoasCache() {
    return new HashMap<>(pessoasCache);
  }

  @Override
  public boolean temPessoa(String email) {
    return pessoasCache.containsKey(email);
  }

  @Override
  public boolean update(String email, Pessoa p) {
    Pessoa oldp = pessoasCache.remove(email);
    if (oldp == null)
      return false;

    pessoasCache.put(p.getEmail(), p);
    if (fileHandler.saveToFile(file, pessoasCache))
      return true;

    pessoasCache.remove(p.getEmail());
    pessoasCache.put(oldp.getEmail(), oldp);
    return false;
  }

  @Override
  public boolean delete(String email) {
    Pessoa oldp = pessoasCache.remove(email);

    if (oldp == null)
      return false;
    if (fileHandler.saveToFile(file, pessoasCache))
      return true;

    pessoasCache.put(oldp.getEmail(), oldp);
    return false;
  }

  @Override
  public boolean create(Pessoa p) {
    if (pessoasCache.containsKey(p.getEmail()))
      return false;

    pessoasCache.put(p.getEmail(), p);
    if (fileHandler.saveToFile(file, pessoasCache))
      return true;

    pessoasCache.remove(p.getEmail());
    return false;
  }

  @Override
  public Pessoa[] readAll() {
    return pessoasCache.values().toArray(Pessoa[]::new);
  }

  @Override
  public Pessoa encontrarPorEmail(String email) {
    return pessoasCache.get(email);
  }

  @Override
  public Pessoa[] encontrarPorNome(String nome) {
    return pessoasCache.values().stream().filter(p -> p.getNome().equals(nome)).toArray(Pessoa[]::new);
  }

  @Override
  public boolean removeAll() {
    var copy = new HashMap<>(pessoasCache);

    pessoasCache.clear();
    if (fileHandler.saveToFile(file, pessoasCache))
      return true;

    pessoasCache = copy;
    return false;
  }

}
