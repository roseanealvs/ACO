
package principal;

/**
 *
 * @author Jessica, Roseane, Bianca, Ayla, Luana, Kleber
 */
public class Formiga implements Comparable{
    private int[] itens; // caminho adotado pela formiga
    private int ultimaPosicao;
    private double preco = 0;
    private double peso = 0;
    private double volume = 0;
    private int iterarNoVetor;
    
    public Formiga(int numItens) {
        itens = new int[numItens];
        ultimaPosicao = 0;
        preco = 0.0;
        this.iterarNoVetor = 0;
    }

    public int[] getItens() {
        return itens;
    }

    public void setItens(int[] itens) {
        this.itens = itens;
    }

    public int getUltimaPosicao() {
        return ultimaPosicao;
    }

    public void setUltimaPosicao(int ultimaPosicao) {
        this.ultimaPosicao = ultimaPosicao;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double distanciaPercorrida) {
        this.preco += distanciaPercorrida;
    }

    public int getIterarNoVetor() {
        return iterarNoVetor;
    }

    public void setIterarNoVetor(int iterarNoVetor) {
        this.iterarNoVetor = iterarNoVetor;
    }

    public double getPeso() {
        return peso;
    }

    public void setPeso(double peso) {
        this.peso = peso;
    }

    public double getVolume() {
        return volume;
    }

    public void setVolume(double volume) {
        this.volume = volume;
    }

    public void somarAoTotalMochila(double pesoItem, double precoItem, double volumeItem) {
        peso += pesoItem;
        volume += volumeItem;
        preco += precoItem;
    }

    @Override
    public int compareTo(Object o) {
        Formiga f = (Formiga) o;
        if(this.preco > f.preco){
            return -1;
        }
        if(this.preco < f.preco){
            return 1;
        }
        return 0;
    }
    
}
