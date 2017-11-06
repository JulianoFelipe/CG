package Model;

import java.io.Serializable;

/**
 * Classe da estrutura e manipulacao de vertices
 * @author Anderson
 */
public class Vertice implements Serializable{
    private static long INSTANCES;
    private final long ID;
    
    /**
     * Valores x,y,z e w do ponto
     */
    public float x;
    public float y;
    public float z;
    public float w;
    
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Construtor padrao (0,0,0,1)
     */
    public Vertice() { this(0,0);}
    
    /**
     * Construtor de vertice
     * @param x valor para x
     * @param y valor para y
     * @param z valor para z
     */
    public Vertice(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = 1;
        
        ID = INSTANCES;
        INSTANCES++;
    }
    
    public Vertice(float x, float y){
        this(x,y,0);
    }
    
    public Vertice(int x, int y){
        this((float) x, (float) y);
    }
    
    /**
     * Construtor de copia
     * @param A Vertice a ser copiado
     */
    public Vertice(Vertice A){ this(A.x, A.y); }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
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

    public static long getINSTANCES() {
        return INSTANCES;
    }

    public long getID() {
        return ID;
    }
    
    public void setAll(float x, float y, float z, float w ){
        this.x=x;
        this.y=y;
        this.z=z;
        this.w=w;
    }
//</editor-fold>
    
    @Override
    public String toString() {
        return "("+x+";"+y+";" + z + ")";
    }
    
    public String toString(String nome) {
        return nome+"=("+x+";"+y+";"+z+")";
    }

    
    /**
     * Método equals para comparação entre vértices. Uso:
     * umVertice.equals(outroVertice);
     * <p>
     * Se o parâmetro for nulo ou não for uma instância da
     * classe Vértice, não pode ser igual, então nem compara.
     * Caso contrário, compara as coordenadas (floats) usando
     * o método "Float.floatToIntBits(other.x)" que retorna a
     * representação de float da I3E 754 de floats para comparação,
     * evitando inconsistências do próprio float, e levando em
     * consideração valores especiais como:
     * Float.NAN; Float.NEGATIVE_INFINITY; etc.
     * @param obj para ser comparado com "this".
     * @return True se obj for igual à this, false caso contrário.
     */
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
        if (Float.floatToIntBits(this.x) != Float.floatToIntBits(other.x)) {
            return false;
        }
        if (Float.floatToIntBits(this.y) != Float.floatToIntBits(other.y)) {
            return false;
        }
        if (Float.floatToIntBits(this.z) != Float.floatToIntBits(other.z)) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Float.floatToIntBits(this.x);
        hash = 97 * hash + Float.floatToIntBits(this.y);
        hash = 97 * hash + Float.floatToIntBits(this.z);
        return hash;
    }
}
