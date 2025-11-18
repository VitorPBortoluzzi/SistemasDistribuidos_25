import json
import random
import string

# Função para gerar uma matrícula no formato ABC1234
def gerar_matricula():
    letras = ''.join(random.choices(string.ascii_uppercase, k=3))
    numeros = ''.join(random.choices(string.digits, k=4))
    return letras + numeros

# Gerar lista de 1000 alunos
alunos = []
for i in range(1, 1001):
    nome = f"Aluno{i}"
    idade = random.randint(18, 30)
    matricula = gerar_matricula()
    alunos.append({
        "nome": nome,
        "idade": idade,
        "matricula": matricula
    })

# Salvar em arquivo JSON
with open("alunos_1000.json", "w", encoding="utf-8") as f:
    json.dump(alunos, f, indent=4)

print("Arquivo 'alunos_1000.json' criado com 1000 alunos!")