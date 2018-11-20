/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos.we_edge;

import java.util.Objects;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class HE_Key {
    //https://stackoverflow.com/questions/14148331/how-to-get-a-hashmap-value-with-three-values
    
    private WE_Aresta aresta;
    private final Vertice ini;
    private final Vertice fin;

    public HE_Key(WE_Aresta aresta) {
        this.aresta = aresta;
        this.ini = aresta.getvInicial();
        this.fin = aresta.getvFinal();
    }

    public HE_Key(Vertice ini, Vertice fin) {
        this.ini = ini;
        this.fin = fin;
    }

    public Vertice getIni() {
        return ini;
    }

    public Vertice getFin() {
        return fin;
    }

    public WE_Aresta getAresta() {
        return aresta;
    }
    
    

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 59 * hash + Objects.hashCode(this.ini);
        hash = 59 * hash + Objects.hashCode(this.fin);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HE_Key other = (HE_Key) obj;
        if (!Objects.equals(this.ini, other.ini)) {
            return false;
        }
        if (!Objects.equals(this.fin, other.fin)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "HE_Key{" + "ini=" + ini + ", fin=" + fin + '}';
    }
    
    
}
