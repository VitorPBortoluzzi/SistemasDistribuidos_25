
import java.util.ArrayList;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class ThreadTarefa2 extends Thread{
    public void run(){
        Random gerador = new Random();
        for (int n = 100; n > 0 ; n--){
            new Thread(){
                public void run(){
                    System.out.println(gerador.nextInt(1500,2000));    
                }
            }.start();
        }
        System.out.println("Tarefa 2 Concluida");
    }
}

class ThreadTarefa3 extends Thread{
    public void run (){
    Random gerador = new Random();
    ArrayList<Integer> outraLista = new ArrayList<>();
    for(int n=50; n > 0; n--){
        outraLista.add(gerador.nextInt(5000,60000));
        }
        System.out.println("Tarefa 3 Concluida");
    }

}

public class Exemplo2 {
    public static void main(String[] args) throws InterruptedException {
        new Thread(){
            public void run(){
                ArrayList<Integer> lista = new ArrayList<>();
                Random gerador = new Random();
                int n = 100;
                for (; n> 0 ; n--)
                    lista.add(gerador.nextInt(10000));
                System.out.println(lista);
                System.out.println("Tarefa 1 Concluida");
                }
            }.start();
        
        ThreadTarefa2 t2 = new ThreadTarefa2();
        t2.start();
        
        ThreadTarefa3 t3 = new ThreadTarefa3();
        t3.start();
    }
    
}
