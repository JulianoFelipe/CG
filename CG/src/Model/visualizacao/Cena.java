/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

import Model.Poligono;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;

/**
 * John.
 * @author Juliano
 */
public class Cena { //http://avajava.com/tutorials/lessons/how-do-i-write-an-object-to-a-file-and-read-it-back.html
    private HashSet<Poligono> poligonos; //Sem poligonos repetidos.

    public Cena() {
        poligonos = new HashSet<>();
    }

    public Cena(HashSet<Poligono> poligonos) {
        this.poligonos = poligonos;
    }

    public HashSet<Poligono> getPoligonos() {
        return poligonos;
    }

    /**
     * Retorna uma lista de poligonos na cena.
     * @return 
     */
    public List<Poligono> getListaPoligonos() {
        return new ArrayList<>(poligonos);
    }
    
    public void setPoligonos(HashSet<Poligono> novosPoligonos) {
        poligonos = novosPoligonos;
    }
    
    public void addAll(Collection<Poligono> novosPoligonos){
        poligonos.addAll(novosPoligonos);
    }
    
    public void add(Poligono novoPoligono){
        poligonos.add(novoPoligono);
    }
    
    public void clearPoligonos(){
        poligonos = new HashSet<>();
    }
}
