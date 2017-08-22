package Model;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 *
 * @author 
 */
public class Poligono implements Serializable{
    public static final Color DEFAULT_BORDA = Color.BLACK;
    public static final Color DEFAULT_FUNDO = null;
    private static final Logger LOG = Logger.getLogger("CG");
    private static long INSTANCES;
    private final long ID;
    
    private ArrayList<Vertice> vertices = new ArrayList();
    private Color corFundo;
    private Color corBorda;
    
    private Vertice centroid = null;
 
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    /**
     * Constrói um polígono sem vértices e as cores padrão de borda e de fundo,
     * indicadas por {@link #DEFAULT_BORDA} e {@link #DEFAULT_FUNDO},
     * respectivamente.
     */
    public Poligono(){ this(new ArrayList(), DEFAULT_BORDA, DEFAULT_FUNDO); }
    
    /**
     * Constrói um polígono com os vértices passados e as cores padrão de
     * borda e de fundo, indicadas por {@link #DEFAULT_BORDA} e {@link #DEFAULT_FUNDO},
     * respectivamente.
     * 
     * @param vertices Vertices a serem copiados.
     */
    public Poligono(List<Vertice> vertices){ this(vertices, DEFAULT_BORDA, DEFAULT_FUNDO); }
    
    /**
     * Construtor de polígonos com cor estipulada ora para 
     * borda ora para o fundo. 
     * <p>
     * Se o parâmetro "corBorda" for verdadeiro, a cor
     * passada será atribuída para a borda (Se for null,
     * a borda terá a cor padrão de {@link #DEFAULT_BORDA}).
     * Caso contrário, será atribuído para o fundo.
     * 
     * @param vertices Vertices para serem copiados.
     * @param cor      Cor a ser atribuída.
     * @param corBorda Verdadeiro se "cor" é para a borda;
     *                 falso se para o fundo.
     */
    public Poligono(List<Vertice> vertices, Color cor, boolean corBorda){
        ID = INSTANCES++;
        this.vertices = new ArrayList(vertices);
        if (corBorda){
            if (cor != null){
                this.corBorda = cor;
            } else {
                this.corBorda = DEFAULT_BORDA;
            }
        } else {
            corFundo = cor;
        }
    }
    
    /**
     * Construtor de polígonos.
     * 
     * @param vertices Vertices para serem copiados.
     * @param corBorda Cor para a borda diferente de null (Se for null,
     *                 será ignorada e a borda terá o valor indicado por
     *                 {@link #DEFAULT_BORDA}).
     * @param corFundo Cor do fundo. Se for null, fundo é transparente.
     */
    public Poligono(List<Vertice> vertices, Color corBorda, Color corFundo){
        ID = INSTANCES++;
        this.vertices = new ArrayList(vertices);
        if (corBorda != null){
            this.corBorda = corBorda;
        } else {
            this.corBorda = DEFAULT_BORDA;
        }
        this.corFundo = corFundo;
    }    
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">   
    public ArrayList<Vertice> getVertices() {
        return vertices;
    }
    
    public void setVertices(ArrayList<Vertice> vertices) {
        this.vertices = vertices;
    }
    
    public void addAllVertices(List<Vertice> novosVertices){
        vertices.addAll(novosVertices);
        centroid = null;
    }
    
    public void addVertice (Vertice novoVertice){
        vertices.add(novoVertice);
        centroid = null;
    }

    public Color getCorFundo() {
        return corFundo;
    }

    public void setCorFundo(Color corFundo) {
        this.corFundo = corFundo;
    }

    public Color getCorBorda() {
        return corBorda;
    }

    public void setCorBorda(Color corBorda) {
        if (corBorda != null)
            this.corBorda = corBorda;
        else
            LOG.warning("Borda não pode ser transparente!");
    }
//</editor-fold>
 
    public int[] getXpoints(){
        int[] xPts = new int[vertices.size()];
        for(int i=0; i<vertices.size(); i++){
            xPts[i] = (int) vertices.get(i).getX();
        }
        //System.out.println(Arrays.toString(xPts));
        return xPts;
    }
    
    public int[] getYpoints(){
        int[] yPts = new int[vertices.size()];
        for(int i=0; i<vertices.size(); i++){
            yPts[i] = (int) vertices.get(i).getY();
        }
        //System.out.println(Arrays.toString(yPts));
        return yPts;
    }
    
    public Vertice calculaCentroide(){
        Vertice vertice;
        float xMim = Float.MAX_VALUE;
        float xMax = Float.MIN_VALUE;
        float yMim = Float.MAX_VALUE;
        float yMax = Float.MIN_VALUE;
       
        for (Vertice vertice1 : vertices) {
            if (vertice1.getX() < xMim) {
                xMim = vertice1.getX();
            }
            if (vertice1.getX() > xMax) {
                xMax = vertice1.getX();
            }
            if (vertice1.getY() < yMim) {
                yMim = vertice1.getY();
            }
            if (vertice1.getY() > yMax) {
                yMax = vertice1.getY();
            }
        }
       
        float x = (xMax + xMim)/2;
        float y = (yMax + yMim)/2;
        vertice = new Vertice(x, y);
        return vertice;
    }
   
    public Vertice calculaCentroideNegativo(){
        Vertice v = calculaCentroide();
        v.setX(v.getX()*-1);
        v.setY(v.getY()*-1);
       
        return v;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + (int) (this.ID ^ (this.ID >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Poligono other = (Poligono) obj;
        return this.ID == other.ID;
    }

    @Override
    public String toString() {
        return "Polígono {Lados=" + vertices.size() + '}';
    }
    
    //https://stackoverflow.com/questions/2792443/finding-the-centroid-of-a-polygon
    public Vertice getCentroideDaMedia(){
        if (centroid != null) return centroid;
        
        Vertice centroid = new Vertice();
        double signedArea = 0.0;
        Vertice v0, v1;
        double a = 0.0;  // Partial signed area

        // For all vertices except last
        int i=0;
        for (i=0; i<vertices.size()-1; ++i) {
            v0 = vertices.get(i);
            v1 = vertices.get(i+1);
            a = v0.getX()*v1.getY() - v1.getX()*v0.getY();
            signedArea += a;
            centroid.setX( (float)((v0.getX() + v1.getX())*a) + centroid.getX());
            centroid.setY( (float)((v0.getY() + v1.getY())*a) + centroid.getY());
        }

        // Do last vertex separately to avoid performing an expensive
        // modulus operation in each iteration.
        v0 = vertices.get(i);
        v1 = vertices.get(0);
        a = v0.getX()*v1.getY() - v1.getX()*v0.getY();
        signedArea += a;
        centroid.setX( (float)((v0.getX() + v1.getX())*a) + centroid.getX());
        centroid.setY( (float)((v0.getY() + v1.getY())*a) + centroid.getY());

        signedArea *= 0.5;
        centroid.x /= (6.0*signedArea);
        centroid.y /= (6.0*signedArea);

        return centroid;
    }
}
