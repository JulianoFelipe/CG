/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.anderson;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class Face implements CGObject{
    private List<Aresta> listaAresta;
    
    public Face (List<Aresta> lista){
        this.listaAresta = new ArrayList<>(lista);
    }

    @Override
    public String toString() {
        
        return "Face{" + listaAresta.size() + " arestas." + '}';
    }
    
    
}
