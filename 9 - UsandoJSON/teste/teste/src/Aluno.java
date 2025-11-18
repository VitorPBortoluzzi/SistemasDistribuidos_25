public class Aluno {
    private String nome;
    private int idade;
    private String matricula;

    public Aluno(String nome, int idade, String matricula) {
        this.nome = nome;
        this.idade = idade;
        this.matricula = matricula;
    }

    // Certifique-se de ter este método público
    public String getMatricula() {
        return matricula;
    }

    @Override
    public String toString() {
        return "Aluno(nome='" + nome + "', idade=" + idade + ", matricula='" + matricula + "')";
    }
}