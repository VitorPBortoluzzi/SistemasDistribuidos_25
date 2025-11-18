import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

class Aluno {
    private String nome;
    private int idade;
    private String matricula;

    public Aluno(String nome, int idade, String matricula) {
        this.nome = nome;
        this.idade = idade;
        this.matricula = matricula;
    }

    public String getMatricula() { return matricula; }

    @Override
    public String toString() {
        return "Aluno(nome='" + nome + "', idade=" + idade + ", matricula='" + matricula + "')";
    }
}

public class SistemaCadastro {
    private static final String ARQUIVO = "alunos.json";
    private static List<Aluno> alunos = new ArrayList<>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        carregarAlunos();

        while (true) {
            System.out.println("\n=== Sistema de Cadastro de Alunos ===");
            System.out.println("1. Adicionar aluno");
            System.out.println("2. Listar alunos");
            System.out.println("3. Remover aluno");
            System.out.println("4. Salvar e sair");
            System.out.print("Escolha uma opção: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1": adicionarAluno(); break;
                case "2": listarAlunos(); break;
                case "3": removerAluno(); break;
                case "4": salvarAlunos(); System.out.println("Saindo..."); return;
                default: System.out.println("Opção inválida!");
            }
        }
    }

    private static void adicionarAluno() {
        System.out.print("Nome: ");
        String nome = scanner.nextLine();
        System.out.print("Idade: ");
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("Matrícula: ");
        String matricula = scanner.nextLine();

        alunos.add(new Aluno(nome, idade, matricula));
        System.out.println("Aluno adicionado!");
    }

    private static void listarAlunos() {
        if (alunos.isEmpty()) {
            System.out.println("Nenhum aluno cadastrado.");
            return;
        }
        System.out.println("\nLista de Alunos:");
        for (Aluno aluno : alunos) {
            System.out.println(aluno);
        }
    }

    private static void removerAluno() {
        System.out.print("Digite a matrícula do aluno a remover: ");
        String matricula = scanner.nextLine();

        boolean removido = alunos.removeIf(a -> a.getMatricula().equalsIgnoreCase(matricula));
        if (removido) {
            System.out.println("Aluno removido com sucesso!");
        } else {
            System.out.println("Aluno não encontrado.");
        }
    }

    private static void salvarAlunos() {
        try (FileWriter writer = new FileWriter(ARQUIVO)) {
            gson.toJson(alunos, writer);
            System.out.println("Alunos salvos no arquivo " + ARQUIVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void carregarAlunos() {
        File file = new File(ARQUIVO);
        if (!file.exists()) return;

        try (FileReader reader = new FileReader(file)) {
            Type listaAlunoType = new TypeToken<List<Aluno>>(){}.getType();
            alunos = gson.fromJson(reader, listaAlunoType);
            System.out.println("Alunos carregados do arquivo " + ARQUIVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
