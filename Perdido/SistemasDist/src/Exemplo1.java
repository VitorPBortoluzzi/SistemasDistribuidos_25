
import java.util.ArrayList;
import java.util.Random;

class ThreadTarefa1 extends Thread{
    @Override
    public void run(){
        ArrayList<Integer> lista = new ArrayList<>();
        Random gerador = new Random();
        int n = 100;
        for (; n> 0 ; n--)
            lista.add(gerador.nextInt(10000));
        System.out.println(lista);
        System.out.println("Tarefa 1 Concluida");
    }
}

class ThreadTarefa2 extends Thread{
    public void run(){
        Random gerador = new Random();
        for (int n = 100; n > 0 ; n--){
            System.out.println(gerador.nextInt(1500,2000));
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

public class Exemplo1 {
    /*public static void main(String[] args) throws InterruptedException {
        ThreadTarefa1 t1 = new ThreadTarefa1();
        t1.start();
        t1.join();
        
        ThreadTarefa2 t2 = new ThreadTarefa2();
        t2.start();
        
        ThreadTarefa3 t3 = new ThreadTarefa3();
        t3.start();
    }*/
}
