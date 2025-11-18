import json
import os

ARQUIVO = "alunos.json"

class Aluno:
    def __init__(self, nome, idade, matricula):
        self.nome = nome
        self.idade = idade
        self.matricula = matricula

    def __repr__(self):
        return f"Aluno(nome='{self.nome}', idade={self.idade}, matricula='{self.matricula}')"

def salvar_alunos(alunos):
    with open(ARQUIVO, "w", encoding="utf-8") as f:
        json.dump([aluno.__dict__ for aluno in alunos], f, indent=4)
    print(f"\nAlunos salvos no arquivo {ARQUIVO}!")

def carregar_alunos():
    if not os.path.exists(ARQUIVO):
        return []
    with open(ARQUIVO, "r", encoding="utf-8") as f:
        alunos_json = json.load(f)
        return [Aluno(d["nome"], d["idade"], d["matricula"]) for d in alunos_json]

def adicionar_aluno(alunos):
    nome = input("Nome: ")
    idade = int(input("Idade: "))
    matricula = input("Matrícula: ")
    alunos.append(Aluno(nome, idade, matricula))
    salvar_alunos(alunos)

def listar_alunos(alunos):
    if not alunos:
        print("Nenhum aluno cadastrado.")
    else:
        print("\nLista de alunos:")
        for aluno in alunos:
            print(aluno)

def remover_aluno(alunos):
    matricula = input("Digite a matrícula do aluno a remover: ")
    novo_alunos = [a for a in alunos if a.matricula != matricula]
    if len(novo_alunos) < len(alunos):
        print("Aluno removido com sucesso!")
        alunos[:] = novo_alunos  # atualiza a lista original
        salvar_alunos(alunos)
    else:
        print("Aluno não encontrado.")

def main():
    alunos = carregar_alunos()
    print("=== Sistema de Cadastro de Alunos ===")

    while True:
        print("\n1. Adicionar aluno")
        print("2. Listar alunos")
        print("3. Remover aluno")
        print("4. Sair")
        opcao = input("Escolha uma opção: ")

        if opcao == "1":
            adicionar_aluno(alunos)
        elif opcao == "2":
            listar_alunos(alunos)
        elif opcao == "3":
            remover_aluno(alunos)
        elif opcao == "4":
            print("Saindo...")
            break
        else:
            print("Opção inválida!")

if __name__ == "__main__":
    main()