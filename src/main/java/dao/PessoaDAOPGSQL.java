package dao;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import model.Pessoa;

public class PessoaDAOPGSQL implements PessoaDAO {
  private final Connection conn;
  Map<String, Pessoa> pessoasCache;

  public PessoaDAOPGSQL(String url) throws SQLException {
    this(url, null, null);
  }

  public PessoaDAOPGSQL(String url, String user, String password) throws SQLException {
    conn = DriverManager.getConnection(url, user, password);
    Statement st = conn.createStatement();
    st.executeUpdate("CREATE TABLE IF NOT EXISTS pessoa(nome VARCHAR(50) NOT NULL, email VARCHAR(50) PRIMARY KEY);");
    ResultSet r = st.executeQuery("SELECT * FROM pessoa");

    pessoasCache = new HashMap<>();
    while (r.next()) {
      Pessoa ne = new Pessoa(r.getString(1), r.getString(2));
      pessoasCache.put(ne.getEmail(), ne);
    }
  }

  @Override
  public boolean update(String email, Pessoa p) {
    if (pessoasCache.containsKey(email)) {
      try {
        PreparedStatement st = conn.prepareStatement("UPDATE pessoa SET email=?, nome=? WHERE email=?");
        st.setString(1, p.getNome());
        st.setString(2, p.getEmail());
        st.setString(3, email);
        if (st.executeUpdate() > 0) {
          pessoasCache.remove(email);
          pessoasCache.put(p.getEmail(), p);
          st.close();
          return true;
        }
        st.close();
        return false;
      } catch (SQLException e) {
        return false;
      }
    }
    return false;
  }

  @Override
  public boolean delete(String email) {
    try {
      PreparedStatement st = conn.prepareStatement("DELETE FROM pessoa WHERE email=?");
      st.setString(1, email);
      if (st.executeUpdate() > 0) {
        pessoasCache.remove(email);
        st.close();
        return true;
      }
      return false;
    } catch (Exception e) {
      return false;
    }
  }

  @Override
  public boolean create(Pessoa p) {
    if (!pessoasCache.containsKey(p.getNome())) {
      try {
        PreparedStatement st = conn.prepareStatement("INSERT INTO pessoa (email, nome) VALUES (?, ?)");
        st.setString(1, p.getEmail());
        st.setString(2, p.getNome());
        if (st.executeUpdate() > 0) {
          pessoasCache.put(p.getEmail(), p);
          return true;
        }
        return true;
      } catch (SQLException e) {
        return false;
      }
    }
    return false;
  }

  @Override
  public Pessoa encontrarPorEmail(String email) {
    return pessoasCache.get(email);
  }

  @Override
  public boolean temPessoa(String email) {
    return pessoasCache.containsKey(email);
  }

  @Override
  public Pessoa[] encontrarPorNome(String nome) {
    return pessoasCache.values().stream().filter(p -> p.getNome().equals(nome)).toArray(Pessoa[]::new);
  }

  @Override
  public Pessoa[] readAll() {
    return pessoasCache.values().toArray(Pessoa[]::new);
  }

  @Override
  public boolean removeAll() {
    try {
      if (conn.prepareStatement("DELETE from pessoa").executeUpdate() > 0) {
        pessoasCache = new HashMap<>();
        return true;
      }
      return false;
    } catch (SQLException e) {
      return false;
    }
  }
}
