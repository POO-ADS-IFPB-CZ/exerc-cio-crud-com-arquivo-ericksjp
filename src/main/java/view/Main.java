package view;

import java.io.IOException;
import java.sql.SQLException;
import java.util.InputMismatchException;
import java.util.Scanner;

import dao.PessoaDAO;
import dao.PessoaDAOFile;
import dao.PessoaDAOPGSQL;
import model.Pessoa;

public class Main {
  private static final Scanner sc = new Scanner(System.in);
  private static PessoaDAO pessoas;

  public static void main(String[] args) throws ClassNotFoundException, IOException {
    try {
      init();
      executarMenu();
    } catch (Exception e) {
      System.out.println("Erro ao executar o sistema: " + e.getMessage());
      reiniciar();
    } finally {
      sc.close();
    }
  }

  private static void executarMenu() {
    while (true) {
      exibirOpcoes();
      int option = obterOpcao();
      processarOpcao(option);
    }
  }

  private static void exibirOpcoes() {
    System.out.println("Digite o número da opção desejada:");
    System.out.println("1 - Cadastrar pessoa");
    System.out.println("2 - Atualizar pessoa");
    System.out.println("3 - Remover pessoa");
    System.out.println("4 - Buscar pessoa por email");
    System.out.println("5 - Buscar pessoa por nome");
    System.out.println("6 - Listar todas as pessoas");
    System.out.println("7 - Remover todas as pessoas");
    // System.out.println("8 - Exibir informações do arquivo de dados");
    System.out.println("8 - Recarregar sistema");
    System.out.println("0 - Sair");
    System.out.print("-> ");
  }

  private static int obterOpcao() {
    int option;
    try {
      option = sc.nextInt();
    } catch (InputMismatchException e) {
      option = -1;
    }
    sc.nextLine();
    return option;
  }

  private static void processarOpcao(int option) {
    System.out.println();
    switch (option) {
      case 1 -> cadastro();
      case 2 -> atualizar();
      case 3 -> remover();
      case 4 -> encontrarEmail();
      case 5 -> encontrarNome();
      case 6 -> listarPessoas();
      case 7 -> removerPessoas();
      // case 8 -> exibirInfoArquivo();
      case 8 -> init();
      case 0 -> sair();
      default -> System.out.println("Opção inválida!");
    }
    System.out.println();
  }

  private static void init() {
    System.out.println("Bem-vindo ao sistema de cadastro de pessoas!");
    try {
      inicializarDAO();
    } catch (ClassNotFoundException | IOException | SQLException e) {
      System.out.println("Erro ao iniciar o sistema: " + e.getMessage());
      reiniciar();
    }
  }

  private static void inicializarDAO() throws IOException, ClassNotFoundException, SQLException {
    System.out.println("Digite o número da opção desejada:");
    System.out.println("1 - Usar arquivo de dados padrão 'pessoas.ser'");
    System.out.println("2 - Informar arquivo caminho do arquivo de dados (.ser / .csv)");
    System.out.println("3 - Usar Banco de Dados Local(PostgreSQL)");
    System.out.println("0 - Sair");
    System.out.print("-> ");
    int option = obterOpcao();
    switch (option) {
      case 1: {
        pessoas = new PessoaDAOFile("pessoas.ser");
        System.out.println("\nUsando 'pessoas.ser' como arquivo de dados");
        break;
      }
      case 2: {
        System.out.print("\nDigite o caminho do arquivo de dados: ");
        String path = sc.nextLine();
        pessoas = new PessoaDAOFile(path);
        System.out.println("\nUsando '" + path + "' como arquivo de dados");
        break;
      }
      case 3: {
        String nomeBanco;
        String usuario;
        String senha;

        System.out.print("\nInforme o usuario do Banco: ");
        usuario = sc.nextLine();
        System.out.print("Informe a senha desse usuario: ");
        senha = sc.nextLine();
        System.out.print("Informe o nome do Banco: ");
        nomeBanco = sc.nextLine();

        String cString = "jdbc:postgresql://localhost:5432/" + nomeBanco;
        pessoas = new PessoaDAOPGSQL(cString, usuario, senha);
        System.out.println("\nUsando '" + cString + "' como string de conexão com o Banco de Dados");
        break;
      }
      case 0:
        sair();
        break;
      default:
        System.out.println("\nOpção inválida!");
        inicializarDAO();
        break;
    }
  }

  private static void reiniciar() {
    System.out.println("\nDigite 1 para recarregar o sistema:");
    System.out.print("-> ");
    if (obterOpcao() == 1) {
      init();
    } else {
      sair();
    }
  }

  private static void sair() {
    System.out.println("Saindo...");
    sc.close();
    System.exit(0);
    return;
  }

  private static void cadastro() {
    System.out.print("Informe o nome da pessoa: ");
    String nome = sc.nextLine();
    System.out.print("Informe o email da pessoa: ");
    String email = sc.nextLine();

    if (pessoas.create(new Pessoa(nome, email))) {
      System.out.println("Pessoa cadastrada com sucesso!");
    } else {
      System.out.println("Erro ao cadastrar pessoa!");
    }
  }

  private static void atualizar() {
    System.out.print("Informe o novo nome da pessoa: ");
    String novoNome = sc.nextLine();
    System.out.print("Digite o email da pessoa a ser atualizada: ");
    String email = sc.nextLine();
    System.out.print("Informe o novo email da pessoa: ");
    String novoEmail = sc.nextLine();

    if (pessoas.update(email, new Pessoa(novoEmail, novoNome))) {
      System.out.println("Pessoa atualizada com sucesso!");
      return;
    }
    System.out.println("Pessoa não encontrada!");
  }

  private static void remover() {
    System.out.print("Digite o email da pessoa a ser removida: ");
    String email = sc.nextLine();
    if (pessoas.delete(email)) {
      System.out.println("Pessoa removida com sucesso!");
    } else {
      System.out.println("Pessoa não encontrada!");
    }
  }

  private static void encontrarEmail() {
    System.out.print("Digite o email da pessoa a ser buscada: ");
    String email = sc.nextLine();
    Pessoa p = pessoas.encontrarPorEmail(email);
    exibirResultado(p);
  }

  private static void removerPessoas() {
    if (pessoas.removeAll()) {
      System.out.println("Pessoas removidas com sucesso!");
    } else {
      System.out.println("Erro ao remover pessoas!");
    }
  }

  private static void encontrarNome() {
    System.out.print("Digite o nome da pessoa a ser buscada: ");
    String nome = sc.nextLine();
    Pessoa[] ps = pessoas.encontrarPorNome(nome);
    exibirResultados(ps);
  }

  private static void listarPessoas() {
    exibirResultados(pessoas.readAll());
  }

  public static void exibirResultado(Pessoa p) {
    if (p == null) {
      System.out.println("Pessoa não encontrada!");
    } else {
      System.out.println("Pessoa encontrada: " + p);
    }
  }

  private static void exibirResultados(Pessoa[] ps) {
    if (ps == null || ps.length == 0) {
      System.out.println("Nenhuma pessoa encontrada!");
    } else {
      System.out.println("Pessoas encontradas: ");
      for (Pessoa p : ps) {
        System.out.println("Pessoa: " + p);
      }
    }
  }

  // private static void exibirInfoArquivo() {
  // System.out.println("Informações sobre o arquivo de dados atual.");
  // System.out.println(pessoas.fileInfo());
  // }
}
