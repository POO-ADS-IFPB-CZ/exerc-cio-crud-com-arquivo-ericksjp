package utils;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import model.Pessoa;

public class CSVFileHandler implements FileHandler {

  @Override
  public HashMap<String, Pessoa> loadFromFile(File file) throws IOException, IllegalArgumentException {
    var pessoasCache = new HashMap<String, Pessoa>();

    try (var reader = new BufferedReader(new FileReader(file))) {
      String line;
      while ((line = reader.readLine()) != null) {
        String[] args = line.split(",");
        if (args.length != 2) {
          throw new IllegalArgumentException("Invalid file format!");
        }
        pessoasCache.put(args[1], new Pessoa(args[0], args[1]));
      }
    }

    return pessoasCache;
  }

  @Override
  public boolean saveToFile(File file, Map<String, Pessoa> mapObj) {
    try (var writer = new BufferedWriter(new FileWriter(file))) {
      for (Pessoa p : mapObj.values()) {
        writer.write(p.getNome() + "," + p.getEmail());
        writer.newLine();
      }
      return true;
    } catch (IOException e) {
      System.out.println("Error saving to file: " + e.getMessage());
      return false;
    }
  }

}
