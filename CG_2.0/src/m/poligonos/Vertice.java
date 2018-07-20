package m.poligonos;

import java.io.Serializable;

/**
 * Classe da estrutura e manipulacao de vertices
 * @author Anderson
 */
public class Vertice implements Serializable{
    private static long INSTANCES=0;
    private final long ID;
    private float x,y,z,w;

    //private BooleanProperty changedProperty = new SimpleBooleanProperty(false);
    //Sem necessidade porque objetos que tem pontos saberão quando estes forem alterados
    //(Dependendo do método chamado, por ex.)
    
    //<editor-fold defaultstate="collapsed" desc="Construtores">   

    public Vertice(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
        
        ID=INSTANCES++;
    }

    public Vertice(Vertice A){
        x = A.x;
        y = A.y;
        z = A.z;
        w = A.w;
        ID = A.ID;
    }
    
    public Vertice(float x, float y, float z) { this(x, y, z, (float) 1.0); }
    
    public Vertice(int x, int y, int z) { this((float)x, (float)y, (float)z); }
    
    public Vertice(float x, float y){ this(x,y,0);   }
    
    public Vertice(int x, int y){  this((float) x, (float) y);   }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">

    public long getID() {
        return ID;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public float getZ() {
        return z;
    }

    public void setZ(float z) {
        this.z = z;
    }

    public float getW() {
        return w;
    }

    public void setW(float w) {
        this.w = w;
    }
    
    public void setAll(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public void setAll(float x, float y, float z, float w) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }
//</editor-fold>
    
    @Override
    public String toString() {
        return "Vertice "+ID+"=("+getX()+";"+getY()+";" + getZ() + ";" + getW() + ")";
    }   

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + (int) (this.ID ^ (this.ID >>> 32));
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
        final Vertice other = (Vertice) obj;
        return this.ID == other.ID;
    }
    
    public boolean equalPoints(Vertice that){
        if (this.x!=that.x) return false;
        if (this.y!=that.y) return false;
        return this.z == that.z;
    }
    
    public void copyAttributes(Vertice v){
        this.x = v.x;
        this.y = v.y;
        this.z = v.z;
        this.w = v.w;
    }
}
