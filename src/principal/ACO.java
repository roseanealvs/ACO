/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author Allan, Cleber, Gessica
 */
public class ACO implements Dados {
    
    double[][] distancias;
    double[][] distanciaInv;
    double[][] feromonio;
    
    private List<Formiga> formigas;
    private List<Formiga> melhores;
    double[][] mapa;
    
    public ACO() {
        distancias = new double[NUM_CIDADES][NUM_CIDADES];
        distanciaInv = new double[NUM_CIDADES][NUM_CIDADES];
        feromonio = new double[NUM_CIDADES][NUM_CIDADES];
        mapa = new double[NUM_CIDADES][3];
        formigas = new ArrayList<>();
        melhores = new ArrayList<>();
    }
    // ler arquivo e salvar localizacao das cidades em uma matriz
    public void initMapa() {
        
        int cont = 0;
        String[] aux = new String[3];
        try {
        FileReader arq = new FileReader(ARQUIVO);
        BufferedReader lerArq = new BufferedReader(arq);
        
        String linha = lerArq.readLine(); // lê primeira linha
       
        while (linha != null) {
            aux = linha.split(";");
            mapa[cont][0] = Double.parseDouble(aux[1]); // width
            mapa[cont][1] = Double.parseDouble(aux[2]); // height
            mapa[cont][2] = 0; //status
            cont++;
            linha = lerArq.readLine(); // lê da segunda até a última linha
        }
        System.out.println("Mapa inicializado com sucesso.\n");
        arq.close();
        } catch (IOException e) {
            System.err.printf("Erro na abertura do arquivo: %s.\n",
              e.getMessage());
        }
    }
    
   
    public int getRandomPosicao() {
        Random rand = new Random();
        return rand.nextInt(NUM_CIDADES);
    }
    
    public void initFeromonio() {
        for (int i = 0; i < NUM_CIDADES; i++) {
            for (int j = 0; j < NUM_CIDADES; j++) {
                feromonio[i][j] = INI_FEROMONIO;
            }
        }
    }
    
    public void moverFormiga(Formiga formiga, int cidadeDestino) {
        int aux = formiga.getIterarNoVetor();
        double distanciaPercorrida = calcularDistancia(mapa[formiga.getUltimaPosicao()][0], 
                                                mapa[formiga.getUltimaPosicao()][1],
                                                mapa[cidadeDestino][0], mapa[cidadeDestino][1]);
        formiga.setDistanciaPercorrida(distanciaPercorrida);
        formiga.getRota()[aux++] = cidadeDestino;
        formiga.setUltimaPosicao(cidadeDestino); // set nova posicao após mover a formiga
        formiga.setIterarNoVetor(aux); // armazenar ultima posicao em uso no vetor
        mapa[cidadeDestino][2] = 1; // set status como "true" (1)
    }
    
    public void atualizaStatusMapa() {
        for (int i = 0; i < NUM_CIDADES; i++) {
            mapa[i][2] = 0;
        }
    }
    
    public void atualizarFeromonio(double distancia, int cidadeOrigem, int cidadeDestino) {
        //depositar feromonio
        double delta = Q / (distancia == 0 ? 1 : distancia);
       
        feromonio[cidadeOrigem][cidadeDestino] += delta;
        feromonio[cidadeDestino][cidadeOrigem] += delta;
    }
    
    public void evaporarFeromonio() {
        for (int i = 0; i < NUM_CIDADES; i++) {
            for (int j = 0; j < NUM_CIDADES; j++) {
                feromonio[i][j] *= 1 - RO;
            }
        }
    }
    // regra de transicao
    public double calcularProbabilidade(int cidadeAtual, int cidadeDestino) {
        double prob, soma = 0.0;
        for (int i = 0; i < NUM_CIDADES; i++) {
            soma += ((Math.pow(feromonio[cidadeAtual][i], ALFA)) * 
                        (Math.pow((distanciaInv[cidadeAtual][i]), BETA)));
        }
        prob = (Math.pow(feromonio[cidadeAtual][cidadeDestino], ALFA)) * 
                   (Math.pow((distanciaInv[cidadeAtual][cidadeDestino]), BETA)) / soma;
        return prob;
    }
    
    public int obterProxPosicao(int posicaoAtual) {
        double probabilidadeAtual = 0.0;
        double probabilidade;
        int p = posicaoAtual;
        for (int i = 0; i < NUM_CIDADES; i++) {
            probabilidade = calcularProbabilidade(posicaoAtual, i);
            // caclcular a probabilidade da formiga ir para 'i' posicao;
            if ((probabilidade > probabilidadeAtual) && mapa[i][2] == 0) {
                probabilidadeAtual = probabilidade;
                p = i; // armazenar a posicao com probabilidade mais alta
            }
        }
        return p;
    }
    
    // calcular a distancia entre todas as cidades e salvar em uma matriz
    public void initDistancias() {
        for (int i = 0; i < NUM_CIDADES; i++) {
            for (int j = 0; j < NUM_CIDADES; j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else {
                    distancias[i][j] = calcularDistancia(mapa[i][0], //x1
                                                        mapa[i][1],  //y1
                                                        mapa[j][0],  //x2
                                                        mapa[j][1]); //y2
                }
                
            }
        }
        printMatriz(distancias);
        System.out.println("\nDistâncias calculadas.\n");
    }
    
    
    public void printMatriz(double[][] matriz) {
        System.out.print(" X\t");
        for (int i = 0; i < NUM_CIDADES; i++) {
            System.out.print((i + 1) + "\t");
        }
        System.out.println("\n");
        for (int i = 0; i < NUM_CIDADES; i++) {
            System.out.print((i + 1) + "\t");
            for (int j = 0; j < NUM_CIDADES; j++) {
                System.out.print(String.format("%.2f", matriz[i][j]) + "\t");
            }
            System.out.println("\n");
        }
    }
    
    public void initDistanciaInv() {
        distanciaInv = new double[NUM_CIDADES][NUM_CIDADES];
        for (int i = 0; i < NUM_CIDADES; i++) {
            for (int j = 0; j < NUM_CIDADES; j++) {
                if (i == j) {
                    distancias[i][j] = 0.0;
                } else {
                    distanciaInv[i][j] = 1.0 / distancias[i][j];
                }
                
            }
        }
    }
    // calcular distancia entre dois pontos
    public double calcularDistancia(double x1, double y1, double x2, double y2) {
        return (Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2)));
    }
    
    // verificar qual a menor rota dentre um grupo de rotas
    public Formiga selecionarMelhorRota(List<Formiga> forms) {
        double menor = forms.get(0).getDistanciaPercorrida();
        int posicaoSel = 0;
        // comecar em 1, pois o valor da primeira posicao já foi atribuido
        for (int i = 1; i < NUM_FORMIGAS; i++) {
            if (forms.get(i).getDistanciaPercorrida() < menor) {
                menor = forms.get(i).getDistanciaPercorrida();
                posicaoSel = i;
            }
        }
        return forms.get(posicaoSel);
    }
    
    public void executarACO() { 
        initMapa();
        initFeromonio();
        initDistancias();
        initDistanciaInv();
        Formiga formiga;
       
        int rotaSize;
        for (int j = 0; j < NUM_ITERACAO; j++) {
            System.out.println("\n========================== ITERAÇÃO " + (j + 1) + " ==========================\n");
            for (int i = 0; i < NUM_FORMIGAS; i++) {
                formiga = new Formiga();
                int destino;

                while (formiga.getIterarNoVetor() < NUM_CIDADES) {
                    if (formiga.getIterarNoVetor() != 0) {
                        destino = obterProxPosicao(formiga.getUltimaPosicao());
                    } else if (formiga.getIterarNoVetor() == NUM_CIDADES - 1) {
                        destino = obterProxPosicao(formiga.getRota()[0]); // se for a ultima cidade, voltar pra primeira
                    } else {
                        destino = obterProxPosicao(getRandomPosicao()); // se for a primeira cidade
                    }
                    moverFormiga(formiga, destino);
                }
                
                formigas.add(formiga);
                atualizaStatusMapa();
            }
            
            // atualizar taxa de feromonio onde as formigas passaram
            int cont = 1;
            for (Formiga f : formigas) {
                rotaSize = f.getRota().length;
                System.out.println("=================== Formiga " + cont + " ===================\n");
                for (int k = 0; k < rotaSize; k++) {
                    System.out.print(f.getRota()[k] + " | ");
                    if (k == rotaSize - 1) { // atualizar feromonio entre a ultima e a primeira cidade
                        atualizarFeromonio(f.getDistanciaPercorrida(), f.getRota()[k], f.getRota()[0]);
                    } else {
                        atualizarFeromonio(f.getDistanciaPercorrida(), f.getRota()[k], f.getRota()[k+1]);
                    }
                }
                System.out.println("\nDistância Percorrida: " + String.format("%.2f", f.getDistanciaPercorrida()));
                System.out.println("\n");
                cont++;
            } 
            evaporarFeromonio();
            melhores.add(selecionarMelhorRota(formigas));
            formigas.removeAll(formigas);
        }
        avaliacao();
    }
    
    // verificar dentre as melhores soluções de cada iteração qual a melhor
    public void avaliacao() {
        System.out.println("\n================== Melhores de cada Iteração ====================== \n");
        int cont = 1;
        for (Formiga f : melhores) {
            System.out.println("\n---- Iteração " + cont + " ----");
            for (int i = 1; i < NUM_CIDADES; i++) {
                System.out.print(f.getRota()[i] + " | "); 
            }
            System.out.println("\nDistância Percorrida: " + String.format("%.2f", f.getDistanciaPercorrida())); 
            cont++;
        }
        System.out.println("\n================== Melhor Solução ================== \n");
        Formiga formiga = selecionarMelhorRota(melhores);
        for (int i = 1; i < NUM_CIDADES; i++) {
                System.out.print(formiga.getRota()[i] + " | "); 
        }
        System.out.println("\nDistância Percorrida: " + String.format("%.2f", formiga.getDistanciaPercorrida())); 
    }
}
