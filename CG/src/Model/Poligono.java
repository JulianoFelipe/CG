package Model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 *
 * @author 
 */
public class Poligono {
    private static long INSTANCES;
    private final long ID;
    
    // listas Auxiliares
    private ArrayList<Aresta> arestas = new ArrayList();
    private ArrayList<Vertice> vertices = new ArrayList();
    private ArrayList<Vertice> vetoresNormaisMedios = new ArrayList();
 
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    public Poligono(){
        arestas = new ArrayList<>();
        vertices = new ArrayList();
        vetoresNormaisMedios = new ArrayList<>();
        
        ID=INSTANCES;
        INSTANCES++;
    }
    
    public Poligono(List<Vertice> vertices){
        this.vertices = new ArrayList(vertices);

        ID=INSTANCES;
        INSTANCES++;
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public ArrayList<Aresta> getArestas() {
        return arestas;
    }
    
    public void setArestas(ArrayList<Aresta> Arestas) {
        this.arestas = Arestas;
        updateVertices();
    }
    
    public void addAllArestas(List<Aresta> novasArestas){
        arestas.addAll(novasArestas);
        updateVertices();
    }
    
    public void addAresta (Aresta novaAresta){
        arestas.add(novaAresta);
    }
    
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
    
    public ArrayList<Vertice> getVetoresNormaisMedios() {
        return vetoresNormaisMedios;
    }
    
    public void setVetoresNormaisMedios(ArrayList<Vertice> vetoresNormaisMedios) {
        this.vetoresNormaisMedios = vetoresNormaisMedios;
    }
   
    public static long getINSTANCES() {
        return INSTANCES;
    }

    public long getID() {
        return ID;
    }
    
//</editor-fold>
 
    public int[] getXpoints(){
        int[] xPts = new int[vertices.size()];
        for(int i=0; i<vertices.size(); i++){
            xPts[i] = (int) vertices.get(i).getX();
        }
        System.out.println(Arrays.toString(xPts));
        return xPts;
    }
    
    public int[] getYpoints(){
        int[] yPts = new int[vertices.size()];
        for(int i=0; i<vertices.size(); i++){
            yPts[i] = (int) vertices.get(i).getY();
        }
        System.out.println(Arrays.toString(yPts));
        return yPts;
    }
    
    /**
     * Limpa lista de vertices
     * e adiciona todos segundo
     * lista de arestas.
     */
    public void updateVertices(){
        HashSet listaHash = new HashSet<>(); //Usa hashset para nao repetir vertices :D
        for (Aresta aresta : arestas){
            listaHash.add(aresta.vInicial);
            listaHash.add(aresta.vFinal);
        }
        vertices = new ArrayList<>(listaHash);
    }
    
    /**
     * Limpa lista de arestas e
     * adiciona todas segundo lista
     * de faces.
     */
    public void updateArestas(){
        
        updateVertices();
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
        return "Polígono {" + vertices + '}';
    }
    
    
}
