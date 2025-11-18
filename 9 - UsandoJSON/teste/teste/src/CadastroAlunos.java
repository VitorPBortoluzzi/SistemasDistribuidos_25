import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class CadastroAlunos {
    private static final String ARQUIVO = "alunos.json";
    private static List<Aluno> alunos = new ArrayList<>();
    private static Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        // Carrega os alunos do arquivo JSON, se existir
        carregarAlunos();

        System.out.print("Digite o nome do aluno: ");
        String nome = scanner.nextLine();
        System.out.print("Digite a idade: ");
        int idade = Integer.parseInt(scanner.nextLine());
        System.out.print("Digite a matrícula: ");
        String matricula = scanner.nextLine();

        Aluno novoAluno = new Aluno(nome, idade, matricula);
        alunos.add(novoAluno);

        // Salva a lista de alunos no arquivo JSON
        salvarAlunos();

        // Mostra todos os alunos cadastrados
        System.out.println("\nLista de alunos cadastrados:");
        for (Aluno a : alunos) {
            System.out.println(a);
        }
    }

    private static void salvarAlunos() {
        try (FileWriter writer = new FileWriter(ARQUIVO)) {
            gson.toJson(alunos, writer);
            System.out.println("\nAlunos salvos no arquivo: " + ARQUIVO);
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
            System.out.println("Alunos carregados do arquivo: " + ARQUIVO);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}