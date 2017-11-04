
package principal;

/**
 *
 * @author Allan, Cleber, Gessica
 */
public class Formiga implements Dados {
    private int[] rota; // caminho adotado pela formiga
    private int ultimaPosicao;
    private double distanciaPercorrida;
    private int iterarNoVetor;
    
    public Formiga() {
        rota = new int[NUM_CIDADES];
        ultimaPosicao = 0;
        distanciaPercorrida = 0.0;
        iterarNoVetor = 0;
    }

    /**
     * @return the rota
     */
    public int[] getRota() {
        return rota;
    }

    /**
     * @param rota the rota to set
     */
    public void setRota(int[] rota) {
        this.rota = rota;
    }

    /**
     * @return the ultimaPosicao
     */
    public int getUltimaPosicao() {
        return ultimaPosicao;
    }

    /**
     * @param ultimaPosicao the ultimaPosicao to set
     */
    public void setUltimaPosicao(int ultimaPosicao) {
        this.ultimaPosicao = ultimaPosicao;
    }

    /**
     * @return the distanciaPercorrida
     */
    public double getDistanciaPercorrida() {
        return distanciaPercorrida;
    }

    /**
     * @param distanciaPercorrida the distanciaPercorrida to set
     */
    public void setDistanciaPercorrida(double distanciaPercorrida) {
        this.distanciaPercorrida += distanciaPercorrida;
    }

    /**
     * @return the iterarNoVetor
     */
    public int getIterarNoVetor() {
        return iterarNoVetor;
    }

    /**
     * @param iterarNoVetor the iterarNoVetor to set
     */
    public void setIterarNoVetor(int iterarNoVetor) {
        this.iterarNoVetor = iterarNoVetor;
    }
    
}
