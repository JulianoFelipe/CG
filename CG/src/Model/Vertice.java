package Model;

/**
 * Classe da estrutura e manipulacao de vertices
 * @author Anderson
 */
public class Vertice {
    private static long INSTANCES;
    private final long ID;
    
    /**
     * Valores x,y,z e w do ponto
     */
    public float x;
    public float y;
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
    public Vertice(float x, float y) {
        this.x = x;
        this.y = y;
        this.w = 1;
        
        ID = INSTANCES;
        INSTANCES++;
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
    
    public void setAll(float x, float y, float w ){
        this.x=x;
        this.y=y;
        this.w=w;
    }
//</editor-fold>
    
    @Override
    public String toString() {
        return"V=("+x+";"+y+";"+w+")";
    }
    
    public String toString(String nome) {
        return nome+"=("+x+";"+y+";"+w+")";
    }
    
    @Override
    public int hashCode() {
        int hash = 3;
        hash = 97 * hash + Float.floatToIntBits(this.x);
        hash = 97 * hash + Float.floatToIntBits(this.y);
        return hash;
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
        return Float.floatToIntBits(this.y) == Float.floatToIntBits(other.y);
    }
}
