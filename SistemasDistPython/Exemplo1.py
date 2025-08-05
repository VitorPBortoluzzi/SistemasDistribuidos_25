import random
import threading
import time

def tarefa1():
    lista = []
    for _ in range(1000):
        lista.append(random.randrange(10000))
    print("Tarefa 1 Concluida")

def tarefa2():
    lista = []
    for _ in range(100):
        print(random.randrange(1500,2000))

    try:
        time.sleep(1)
    except KeyboardInterrupt as ex:
        print("ex")

    print("Tarefa 2 Concluida")

def tarefa3():
    lista = []
    for _ in range(200):
        lista = []
        lista.append(random.randrange(50000,60000))
    print("Tarefa 3 Concluida")

t1 = threading.Thread(target=tarefa1)
t1.start()

t2 = threading.Thread(target=tarefa2)
t2.start();


t3 = threading.Thread(target=tarefa3)
t3.start();