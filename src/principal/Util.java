package principal;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jessica, Roseane, Bianca, Ayla, Luana, Kleber
 */
public class Util {

    public List<Item> getItens(String arquivo) {
        BufferedReader br;
        String linha = "";
        List<Item> itens = new ArrayList<Item>();
        try {
            br = new BufferedReader(new FileReader(arquivo));
            
            int j;
            while ((linha = br.readLine()) != null) {
                j=0;
                String[] infoItem = linha.split(";");
                if(infoItem.length > 1){
                    Item i = new Item();
                    i.setId(Integer.parseInt(infoItem[j++]));
                    i.setNome(infoItem[j++]);
                    i.setPeso(Double.parseDouble(infoItem[j++]));
                    i.setVolume(Double.parseDouble(infoItem[j++]));
                    i.setPreco(Double.parseDouble(infoItem[j++]));

                    itens.add(i);
                }
            }
           
        } catch (IOException e) {
            e.printStackTrace();
        }
        return itens;
    }
}
