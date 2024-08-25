package utils;

import java.io.File;
import java.io.IOException;
import java.util.Map;

import model.Pessoa;

public interface FileHandler {
  public Map<String, Pessoa> loadFromFile(File file) throws IOException, ClassNotFoundException;

  public boolean saveToFile(File file, Map<String, Pessoa> pessoasCache);
}
