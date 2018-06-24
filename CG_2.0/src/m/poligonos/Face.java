/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class Face extends CGObject{ //extends CGObject{
    private List<Aresta> listaAresta;
    
    public Face (List<Aresta> lista){
        super(lista.size()*2);
        throw new IllegalArgumentException("Arrumar implementação");
    }
    
    public Face (Face f){
        super(f.getNumberOfArestas()*2);
        throw new IllegalArgumentException("Arrumar implementação");
    }

    @Override
    public String toString() {
        
        return "Face{" + listaAresta.size() + " arestas." + '}';
    }
    
    public int getNumberOfArestas(){
        return listaAresta.size();
    }
}
