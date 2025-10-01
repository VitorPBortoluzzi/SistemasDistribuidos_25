import java.io.Serializable;
/**
 *
 * @author alexandrezamberlan
 */
public class Componente implements Serializable {
    public static final int FRUTA = 1, JOGADOR = 2, PLACAR = 3;
    public int x;
    public int y;
    public int largura;
    public int altura;
    public int tipo;
    public int placarJ1,placarJ2;
    public int idJogador;

    public Componente(int x, int y, int largura, int altura, int tipo, int idJogador) {
        this.x = x;
        this.y = y;
        this.largura = largura;
        this.altura = altura;
        this.tipo = tipo;
        this.idJogador = idJogador;
        this.placarJ1 = 0;
        this.placarJ2 = 0;
    }
    
    public Componente(int xFruta, int yFruta, int placar1, int placar2) {
        this.x = xFruta; // Posição X da fruta
        this.y = yFruta; // Posição Y da fruta
        this.placarJ1 = placar1;
        this.placarJ2 = placar2;
        this.tipo = PLACAR;
        this.idJogador = 0; // Não relevante para PLACAR
    }
    
    
}
