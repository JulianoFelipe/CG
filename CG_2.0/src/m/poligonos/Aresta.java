package m.poligonos;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import m.Eixo;
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
    
    protected float max_y;
    protected float min_y;
    protected float max_x;
    protected float min_x;
    protected float max_z;
    protected float min_z;
        
    protected boolean changed;
    protected double slope;
    protected double b;    //this ->   y= slope*x + b;
    protected double interceptX; //"Double" so it can be null
     
    //<editor-fold defaultstate="collapsed" desc="Construtores">    
    /**
     * Construtor de copia
     * @param A Aresta a ser copiada
     */
    public Aresta(Aresta A) {
        super(A);

        this.vInicial = new Vertice(A.vInicial);
        this.vFinal   = new Vertice(A.vFinal);
        
        this.max_x = A.max_x;
        this.min_x = A.min_x;
        this.max_y = A.max_y;
        this.min_y = A.min_y;
        this.max_z = A.max_z;
        this.min_z = A.min_z;
        
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
            max_x = I.getX();
            min_x = F.getX();
        } else {
            max_x = F.getX();
            min_x = I.getX();
        }
        
        if (I.getY() > F.getY()){
            max_y = I.getY();
            min_y = F.getY();
        } else {
            max_y = F.getY();
            min_y = I.getY();
        }

        if (I.getZ() > F.getZ()){
            max_z = I.getZ();
            min_z = F.getZ();
        } else {
            max_z = F.getZ();
            min_z = I.getZ();
        }
        
        interceptX = min_x;
        changed = true;
    }
    
    protected Aresta(long id){
        super(id);
    }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public Vertice getvInicial() {
        return vInicial;
    }
    
    /*public void setvInicial(Vertice vInicial) {
        this.vInicial = vInicial;
        
        if (vInicial.getX() > max_x)
            max_x = vInicial.getX();
        if (vInicial.getX() < min_x)
            min_x = vInicial.getX();
        
        if (vInicial.getY() > max_y)
            max_y = vInicial.getY();
        if (vInicial.getY() < min_y)
            min_y = vInicial.getY();
        
        changed = true;
    }*/
    
    public Vertice getvFinal() {
        return vFinal;
    }
    
    /*public void setvFinal(Vertice vFinal) {
        this.vFinal = vFinal;
        
        if (vFinal.getX() > max_x)
            max_x = vFinal.getX();
        if (vFinal.getX() < min_x)
            min_x = vFinal.getX();
        
        if (vFinal.getY() > max_y)
            max_y = vFinal.getY();
        if (vFinal.getY() < min_y)
            min_y = vFinal.getY();
        
        changed = true;
    }*/

    public float getMaxY() {
        if (vInicial.getY() > vFinal.getY()){
            return vInicial.getY();
        } else {
            return vFinal.getY();
        }
        //return max_y;
    }

    public float getMinY() {
        if (vInicial.getY() < vFinal.getY()){
            return vInicial.getY();
        } else {
            return vFinal.getY();
        }
        //return min_y;
    }

    public float getMaxX() {
        if (vInicial.getX() > vFinal.getX()){
            return vInicial.getX();
        } else {
            return vFinal.getX();
        }
        //return max_x;
    }

    public float getMinX() {
        if (vInicial.getX() < vFinal.getX()){
            return vInicial.getX();
        } else {
            return vFinal.getX();
        }
        //return min_x;
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
        
        if (this.vInicial.getX() > this.vFinal.getX()){
            max_x = this.vInicial.getX();
            min_x = this.vFinal.getX();
        } else {
            max_x = this.vFinal.getX();
            min_x = this.vInicial.getX();
        }
        
        if (this.vInicial.getY() > this.vFinal.getY()){
            max_y = this.vInicial.getY();
            min_y = this.vFinal.getY();
        } else {
            max_y = this.vFinal.getY();
            min_y = this.vInicial.getY();
        }
        
        if (this.vInicial.getZ() > this.vFinal.getZ()){
            max_z = this.vInicial.getZ();
            min_z = this.vFinal.getZ();
        } else {
            max_z = this.vFinal.getZ();
            min_z = this.vInicial.getZ();
        }
    }

    @Override
    public List<Vertice> getPoints() {
        List<Vertice> newL = new ArrayList();
        newL.add(vInicial);
        newL.add(vFinal);
        return newL;
    }
    
    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof Aresta)) throw new IllegalArgumentException("Não é uma instância de Aresta."); //Is this Right?

        Aresta updated = (Aresta) updatedObj;
        
        vInicial.copyAttributes(updated.vInicial);
        vFinal.  copyAttributes(updated.vFinal);

        if (this.vInicial.getX() > this.vFinal.getX()){
            max_x = this.vInicial.getX();
            min_x = this.vFinal.getX();
        } else {
            max_x = this.vFinal.getX();
            min_x = this.vInicial.getX();
        }
        
        if (this.vInicial.getY() > this.vFinal.getY()){
            max_y = this.vInicial.getY();
            min_y = this.vFinal.getY();
        } else {
            max_y = this.vFinal.getY();
            min_y = this.vInicial.getY();
        }
        
        if (this.vInicial.getZ() > this.vFinal.getZ()){
            max_z = this.vInicial.getZ();
            min_z = this.vFinal.getZ();
        } else {
            max_z = this.vFinal.getZ();
            min_z = this.vInicial.getZ();
        }
        
        interceptX = min_x;
        changed = true;
    }

    @Override
    public boolean contains(float x, float y, Eixo axis) {
        return insideBoundingBox(x, y, axis);
    }

    @Override
    public boolean insideBoundingBox(float x, float y, Eixo axis) {
        float minX, maxX, minY, maxY;
        switch (axis){
            case Eixo_XY:
                minX = min_x;
                maxX = max_x;
                minY = min_y;
                maxY = max_y;
                break;
            case Eixo_XZ:
                minX = min_x;
                maxX = max_x;
                minY = min_z;
                maxY = max_z;
                break;
            case Eixo_YZ:
                minX = min_y;
                maxX = max_y;
                minY = min_z;
                maxY = max_z;
                break;
            default :
                throw new IllegalArgumentException("Eixo " + axis + " não é 2D.");
        }
        
        return !(x < minX || x > maxX || y < minY || y > maxY); //Se menor que min ou maior que max, false
    }

    @Override
    public Vertice getCentroide() {
        return new Vertice(
            (float) ( (vInicial.getX()+vFinal.getX()) /2),
            (float) ( (vInicial.getY()+vFinal.getY()) /2),
            (float) ( (vInicial.getZ()+vFinal.getZ()) /2)
        );
    }
}
