package utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;

import model.Pessoa;

public class SERFileHandler implements FileHandler {
  @SuppressWarnings("unchecked")
  public HashMap<String, Pessoa> loadFromFile(File file) throws IOException, ClassNotFoundException {
    if (file.length() == 0) {
      return new HashMap<>();
    }
    try (var ois = new ObjectInputStream(new FileInputStream(file))) {
      return (HashMap<String, Pessoa>) ois.readObject();
    }
  }

  public boolean saveToFile(File file, Map<String, Pessoa> mapObj) {
    try (var oos = new ObjectOutputStream(new FileOutputStream(file))) {
      oos.writeObject(mapObj);
      return true;
    } catch (IOException e) {
      System.out.println("Error saving to file: " + e.getMessage());
      return false;
    }
  }
}
