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
    private static final Logger LOG = Logger.getLogger("CG");
    private static long INSTANCES;
    private final long ID;
    
    private ArrayList<Vertice> vertices = new ArrayList();
    private Color corFundo = null;
    private Color corBorda = Color.black;
 
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    public Poligono(){
        vertices = new ArrayList();
        
        ID = INSTANCES++;
    }
    
    public Poligono(List<Vertice> vertices){
        this.vertices = new ArrayList(vertices);
        
        ID = INSTANCES++;
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
    }
    
    public void addVertice (Vertice novoVertice){
        vertices.add(novoVertice);
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
    
}
