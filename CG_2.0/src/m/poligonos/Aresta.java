package m.poligonos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import utils.math.VMath;


/**
 * Classe da estrutura e manipulacao de arestas
 *
 * @author Anderson
 */
public class Aresta extends CGObject{
    protected static final int NO_POINTS = 2;
    
    /**
     * Vertice Inicial (vInicial) e final (vFinal)
     */
    protected Vertice vInicial;
    protected Vertice vFinal;
    
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
        super(A);

        this.vInicial = new Vertice(A.vInicial);
        this.vFinal   = new Vertice(A.vFinal);
        
        this.maxX = A.maxX;
        this.minX = A.minX;
        this.maxY = A.maxY;
        this.minY = A.minY;
        
        this.changed = A.changed;
        this.slope = A.slope;
        this.b = A.b;
        this.interceptX = A.interceptX;
    }
    
    /**
     * Construtor padrao
     * @param I Vertice Inicial
     * @param F Vertice Final
     */
    public Aresta(Vertice I, Vertice F) {
        super();
        
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
    
    public int hash() {
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
    public boolean equalsT(Object obj) {
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

    @Override
    public int getNumberOfPoints() {
        return NO_POINTS;
    }

    @Override
    public Vertice get(int i) {
        switch (i) {
            case 0:
                return vInicial;
            case 1:
                return vFinal;
            default:
                return null;
        }
    }

    @Override
    public void set(int i, Vertice point) {
        if (i == 0){
            vInicial.copyAttributes(point);
        } else if (i == 1){
            vFinal.copyAttributes(point);
        }
    }

    @Override
    public List<Vertice> getPoints() {
        List<Vertice> newL = new ArrayList();
        newL.add(vInicial);
        newL.add(vFinal);
        return newL;
    }
    
    public static void main(String...args){
        Vertice I = new Vertice(0, 0, 0);
        Vertice F = new Vertice(10, 10, 10);
        
        Aresta a = new Aresta(I, F);
        System.out.println(a.getPoints());
        I.setX(666);
        System.out.println(a.getPoints());
    }

    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof Aresta)) throw new IllegalArgumentException("Não é uma instância de Aresta."); //Is this Right?

        Aresta updated = (Aresta) updatedObj;
        
        vInicial.copyAttributes(updated.vInicial);
        vFinal.  copyAttributes(updated.vFinal);

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
}
