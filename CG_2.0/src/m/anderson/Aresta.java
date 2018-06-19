package m.anderson;

import java.util.Objects;
import utils.math.VMath;


/**
 * Classe da estrutura e manipulacao de arestas
 *
 * @author Anderson
 */
public class Aresta extends CGObject{   
    /**
     * Vertice Inicial (vInicial) e final (vFinal)
     */
    private Vertice vInicial;
    private Vertice vFinal;

    private float maxY;
    private float minY;
    private float maxX;
    private float minX;
    
    private boolean changed;
    private double slope;
    private double b;    //this ->   y= slope*x + b;
    private double interceptX; //"Double" so it can be null
     
    //<editor-fold defaultstate="collapsed" desc="Construtores">    
    /**
     * Construtor de copia
     * @param A Aresta a ser copiada
     */
    public Aresta(Aresta A) {
        super(new float[][] {
            {A.vInicial.getX(), A.vFinal.getX()},
            {A.vInicial.getY(), A.vFinal.getY()},
            {A.vInicial.getZ(), A.vFinal.getZ()},
            {                1,               1}},
            A.ID);
        
        this.vInicial = A.vInicial;
        this.vFinal = A.vFinal;
        
        if (this.vInicial.getX() > this.vFinal.getX()){
            maxX = this.vInicial.getX();
            minX = this.vFinal.getX();
        } else {
            maxX = this.vFinal.getX();
            minX = this.vInicial.getX();
        }
        
        if (this.vInicial.getY() > this.vFinal.getY()){
            maxY = this.vInicial.getY();
            minY = this.vFinal.getY();
        } else {
            maxY = this.vFinal.getY();
            minY = this.vInicial.getY();
        }
        
        interceptX = minX;
        changed = true;
    }
    
    /**
     * Construtor padrao
     * @param I Vertice Inicial
     * @param F Vertice Final
     */
    public Aresta(Vertice I, Vertice F) {
        super(new float[][] {
            {I.getX(), F.getX()},
            {I.getY(), F.getY()},
            {I.getZ(), F.getZ()},
            {       1,        1}
        });
        
        this.vInicial = I;
        this.vFinal = F;
        
        if (I.getX() > F.getX()){
            maxX = I.getX();
            minX = F.getX();
        } else {
            maxX = F.getX();
            minX = I.getX();
        }
        
        if (I.getY() > F.getY()){
            maxY = I.getY();
            minY = F.getY();
        } else {
            maxY = F.getY();
            minY = I.getY();
        }
        
        interceptX = minX;
        changed = true;
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Vertice getvInicial() {
        return vInicial;
    }
    
    /*public void setvInicial(Vertice vInicial) {
        this.vInicial = vInicial;
        
        if (vInicial.getX() > maxX)
            maxX = vInicial.getX();
        if (vInicial.getX() < minX)
            minX = vInicial.getX();
        
        if (vInicial.getY() > maxY)
            maxY = vInicial.getY();
        if (vInicial.getY() < minY)
            minY = vInicial.getY();
        
        changed = true;
    }*/
    
    public Vertice getvFinal() {
        return vFinal;
    }
    
    /*public void setvFinal(Vertice vFinal) {
        this.vFinal = vFinal;
        
        if (vFinal.getX() > maxX)
            maxX = vFinal.getX();
        if (vFinal.getX() < minX)
            minX = vFinal.getX();
        
        if (vFinal.getY() > maxY)
            maxY = vFinal.getY();
        if (vFinal.getY() < minY)
            minY = vFinal.getY();
        
        changed = true;
    }*/

    public float getMaxY() {
        return maxY;
    }

    public float getMinY() {
        return minY;
    }

    public float getMaxX() {
        return maxX;
    }

    public float getMinX() {
        return minX;
    }

    public double getSlope() {
        if (changed){
            double[] props = VMath.slopeInterceptForm(this);
            slope = props[0];
            b     = props[1];
            changed = false;
        }
        return slope;
    }

    public double getB() {
        if (changed){
            double[] props = VMath.slopeInterceptForm(this);
            slope = props[0];
            b     = props[1];
            changed = false;
        }
        return b;
    }

    public Double getInterceptX() {
        return interceptX;
    }

    public void setInterceptX(Double interceptX) {
        this.interceptX = interceptX;
    }
//</editor-fold>
    
    @Override
    public String toString() {
        return "Aresta: ID=" + ID + "; Points={" + vInicial.toString() + "," + vFinal.toString() + "}";
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
