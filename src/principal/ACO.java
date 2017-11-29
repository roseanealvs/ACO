package principal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Jessica, Roseane, Bianca, Ayla, Luana, Kleber
 */
public class ACO {
    
    double[] feromonio;
    //OLHARAQUI
    private final double INI_FEROMONIO = 0.5;
    public final double Q = 1; 
    private List<Formiga> formigas;
    private List<Item> itens;
    private Interface form;
    private int volumeMochila; // a definir via interface
    private int pesoMochila; // a definir via interface
    private int quantidadeFormigas;
    private int quantidadeIteracoes;
    private int numItens;
    private double alpha;
    private double beta;
    private double ro;
    private double totalItens;
    
    public ACO(String quantidadeIteracoes, String volumeMochila, String pesoMochila, String quantidaFormigas, List<Item> itens, Interface form, String alpha, String beta, String ro, double totalItens) {
        this.quantidadeFormigas = Integer.parseInt(quantidaFormigas);
        this.volumeMochila = Integer.parseInt(volumeMochila);
        this.pesoMochila = Integer.parseInt(pesoMochila);
        this.quantidadeIteracoes = Integer.parseInt(quantidadeIteracoes);
        this.itens = itens;
        this.form = form;
        numItens = itens.size();
        feromonio = new double[numItens];
        formigas = new ArrayList<>();
        this.alpha = Double.parseDouble(alpha);
        this.ro = Double.parseDouble(ro);
        this.beta = Double.parseDouble(beta);
        this.totalItens = totalItens;
    }
   
    public void initFeromonio() {
        for (int i = 0; i < numItens; i++) {
            feromonio[i] = INI_FEROMONIO;
        }
    }
    
    public void moverFormiga(Formiga formiga, int proximoItem) {
        int aux = formiga.getIterarNoVetor();
        Item proxItem = itens.get(proximoItem);
        formiga.somarAoTotalMochila(proxItem.getPeso(), proxItem.getPreco(), proxItem.getVolume());
        formiga.getItens()[aux++] = proximoItem;
        formiga.setUltimaPosicao(proximoItem); // set nova posicao após mover a formiga
        formiga.setIterarNoVetor(aux); // armazenar ultima posicao em uso no vetor
    }
    
    public void atualizarFeromonio(double preco, int item) {
        //depositar feromonio
        double delta = Q / (totalItens/preco);
        feromonio[item] += delta;
    }
    
    public void evaporarFeromonio() {
        for (int i = 0; i < numItens; i++) {
            feromonio[i] *= 1 - ro;
        }
    }
    // regra de transicao
    public double calcularProbabilidade(int itemDestino, Formiga f, double soma) {
        double prob = 0.0;
        
        prob = (Math.pow(feromonio[itemDestino], alpha) * 
                   Math.pow(calcularHeuristica(itens.get(itemDestino)), beta)) / soma;
        return prob;
    }
    
    public boolean isItemNaMochila(Formiga f, int item){
        for (int i =0; i< f.getIterarNoVetor(); i++) {
            if(f.getItens()[i]==item){
                return true;
            }
        }
        return false;
    }
    
    public int obterProxPosicao(Formiga f) {
        double probabilidadeAtual = 0.0;
        double probabilidade;
        int p = f.getUltimaPosicao();
        double soma = somarItens(f);
        for (int i = 0; i < numItens; i++) {
            if(!isItemNaMochila(f, i)){
                probabilidade = calcularProbabilidade(i, f, soma);
                // caclcular a probabilidade da formiga ir para 'i' posicao;
                if (probabilidade >= probabilidadeAtual) {
                    probabilidadeAtual = probabilidade;
                    p = i; // armazenar a posicao com probabilidade mais alta
                }
            }
        }
        return p;
    }
    
    public double somarItens(Formiga f){
        double soma = 0.0;
        for (int i = 0; i < numItens; i++) {
            //olhar soh itens q nao foram escolhidos e soh somar uma vez
            if(!isItemNaMochila(f, i)){
                soma += (Math.pow(feromonio[i], alpha) * 
                            Math.pow(calcularHeuristica(itens.get(i)), beta));
            }
        }
        return soma;
    }
    
    // calcular custo beneficio do item
    public double calcularHeuristica(Item i) {
        double calc = totalItens/i.getPreco();
        return 1/calc;
    }
    
    public int getRandomPosicao() {
        Random rand = new Random();
        return rand.nextInt(numItens);
    }
    public void executarACO() { 
        
        initFeromonio();
        Formiga formiga;
       
        for (int j = 0; j < quantidadeIteracoes; j++) {
            formigas.removeAll(formigas);
            for (int i = 0; i < quantidadeFormigas; i++) {
                formiga = new Formiga(numItens);
                int destino;
                
                while (formiga.getIterarNoVetor() < numItens && !isMochilaCheia(formiga)) {
                    if(formiga.getIterarNoVetor()== 0){
                        destino = getRandomPosicao();
                    }else{
                        destino = obterProxPosicao(formiga);
                    }
                    if(isItemCabeNaMochila(formiga, itens.get(destino))){
                        moverFormiga(formiga, destino);
                    }else{
                        break;
                    }
                }
                
                formigas.add(formiga);
            }
            
            // atualizar taxa de feromonio onde as formigas passaram
            
            for (Formiga f : formigas) {
                for (int k = 0; k < f.getIterarNoVetor(); k++) {
                    atualizarFeromonio(f.getPreco(), f.getItens()[k]);
                }
            } 
            form.imprimirGeracao(formigas, j);
            evaporarFeromonio();
           
        }
        Collections.sort(formigas);
        form.exibirTop3(formigas);
        form.setEnabledExecutar();
    }

    private boolean isItemCabeNaMochila(Formiga formiga, Item item) {
        return !((formiga.getPeso()+item.getPeso()) >= pesoMochila || (formiga.getVolume()+item.getVolume()) >= volumeMochila);
    }
    
    private boolean isMochilaCheia(Formiga formiga) {
        return formiga.getPeso() >= pesoMochila || formiga.getVolume() >= volumeMochila;
    }
}
