package Model;

import java.util.Objects;

/**
 * Classe da estrutura e manipulacao de arestas
 *
 * @author Anderson
 */
public class Aresta {
    private static long INSTANCES;
    
    /**
     * Vertice Inicial (vInicial) e final (vFinal)
     */
    public Vertice vInicial;
    public Vertice vFinal;

    private final long ID;
    
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Construtor Default vInicial e vFinal = (0,0,0,1)
     */
    public Aresta() { this(new Vertice(), new Vertice()); }
    
    /**
     * Construtor de copia
     * @param A Aresta a ser copiada
     */
    public Aresta(Aresta A) { this (A.vInicial, A.vFinal); }
    
    /**
     * Construtor padrao
     * @param I Vertice Inicial
     * @param F Vertice Final
     */
    public Aresta(Vertice I, Vertice F) {
        this.vInicial = I;
        this.vFinal = F;
        
        ID = INSTANCES;
        INSTANCES++;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Vertice getvInicial() {
        return vInicial;
    }
    
    public void setvInicial(Vertice vInicial) {
        this.vInicial = vInicial;
    }
    
    public Vertice getvFinal() {
        return vFinal;
    }
    
    public void setvFinal(Vertice vFinal) {
        this.vFinal = vFinal;
    }
    
    public Vertice getVerticeMaxX() {
        if (vInicial.x > vFinal.x) {
            return vInicial;
        } else {
            return vFinal;
        }
    }
    
    public Vertice getVerticeMaxY() {
        if (vInicial.y > vFinal.y) {
            return vInicial;
        } else {
            return vFinal;
        }
    }
      
    public Vertice getVerticeMinX() {
        if (vInicial.x < vFinal.x) {
            return vInicial;
        } else {
            return vFinal;
        }
    }
    
    public Vertice getVerticeMinY() {
        if (vInicial.y < vFinal.y) {
            return vInicial;
        } else {
            return vFinal;
        }
    }
    
    public Vertice getVerticeMin() {
        if (vInicial.y != vFinal.y) {
            return getVerticeMinY();
        } else {
            return getVerticeMinX();
        }
    }
    
    public Vertice getVerticeMax() {
        if (vInicial.y != vFinal.y) {
            return getVerticeMaxY();
        } else {
            return getVerticeMaxX();
        }
    }

    public long getID() {
        return ID;
    }

    public static long getINSTANCES() {
        return INSTANCES;
    }
    
//</editor-fold>
    
    @Override
    public String toString() {
        return "{" + vInicial.toString() + "," + vFinal.toString() + "}";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + Objects.hashCode(this.vInicial);
        hash = 17 * hash + Objects.hashCode(this.vFinal);
        return hash;
    }

    /**
     * Comparação com direção.
     * @param obj
     * @return 
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aresta other = (Aresta) obj;
        if (!(this.vInicial.equals(other.vInicial))) {
            return false;
        }
        return this.vFinal.equals(other.vFinal);
    }
}
