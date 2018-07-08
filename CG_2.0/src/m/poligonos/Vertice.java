package m.poligonos;

/**
 * Classe da estrutura e manipulacao de vertices
 * @author Anderson
 */
public class Vertice extends CGObject{
    //<editor-fold defaultstate="collapsed" desc="Construtores">   
    /**
     * Construtor de vertice
     * @param x valor para x
     * @param y valor para y
     * @param z valor para z
     */
    public Vertice(float x, float y, float z) {
        super(new float[][] { {x}, {y}, {z}, {1} } );
    }
    
    public Vertice(float x, float y, float z, float w) {
        super(new float[][] { {x}, {y}, {z}, {w} } );
    }
    
    /**
     * Construtor de copia
     * @param A Vertice a ser copiado
     */
    public Vertice(Vertice A){
        super(new float[][] { {A.getX()}, {A.getY()}, {A.getZ()}, {1} }, A.ID );
    }
    
    public Vertice(int x, int y, int z) { this((float)x, (float)y, (float)z); }
    
    public Vertice(float x, float y){ this(x,y,0);   }
    
    public Vertice(int x, int y){  this((float) x, (float) y);   }
//</editor-fold>

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public float getX() {
        return pointMatrix[0][0];
    }
    
    public float getY() {
        return pointMatrix[1][0];
    }

    public float getZ() {
        return pointMatrix[2][0];
    }

    public float getW() {
        return pointMatrix[3][0];
    }
    
    public void setX(float x) {
        pointMatrix[0][0] = x;
    }
    
    public void setY(float y) {
        pointMatrix[1][0] = y;
    }

    public void setZ(float z) {
        pointMatrix[2][0] = z;
    }

    public void setW(float w) {
        pointMatrix[3][0] = w;
    }
    
    public void setAll(float x, float y, float z) {
        setX(x);
        setY(y);
        setZ(z);
    }
    
    public void setAll(float x, float y, float z, float w) {
        setX(x);
        setY(y);
        setZ(z);
        setW(w);
    }
    

//</editor-fold>
    
    @Override
    public String toString() {
        return "("+getX()+";"+getY()+";" + getZ() + ";" + getW() + ")";
    }
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 97 * hash + Float.floatToIntBits(getX());
        hash = 97 * hash + Float.floatToIntBits(getY());
        hash = 97 * hash + Float.floatToIntBits(getZ());
        hash = 97 * hash + Float.floatToIntBits(getW());
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
        if (Float.floatToIntBits(getX()) != Float.floatToIntBits(other.getX())) {
            return false;
        }
        if (Float.floatToIntBits(getY()) != Float.floatToIntBits(other.getY())) {
            return false;
        }
        if (Float.floatToIntBits(getZ()) != Float.floatToIntBits(other.getZ())) {
            return false;
        }
        return Float.floatToIntBits(getW()) == Float.floatToIntBits(other.getW());
    }
    
    
}
