package m.poligonos;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import m.Camera;
import m.Visao;
import utils.config.StandardConfigCam;
import utils.math.VMath;

/**
 *
 * @author
 */
public class Poligono extends CGObject {

    private final List<IndexList> vertices_das_faces;//Índices dos vértices
    private final boolean[] visibilidade_faces;

    //<editor-fold defaultstate="collapsed" desc="Construtores">     
    public Poligono(float[][] pointMatrix, List<IndexList> faces) {
        super(pointMatrix);

        visibilidade_faces = new boolean[faces.size()];
        vertices_das_faces = new ArrayList(faces);
    }

    public Poligono(Poligono p) {
        super(p.getPointMatrix(), p.getID());
        vertices_das_faces = new ArrayList(p.vertices_das_faces);
        visibilidade_faces = new boolean[p.visibilidade_faces.length];
        System.arraycopy(p.visibilidade_faces, 0, visibilidade_faces, 0, p.visibilidade_faces.length);
    }

//</editor-fold>
    @Override
    public String toString() {
        return "Polígono: ID=" + ID + "; Points=" + getNumberOfPoints() + "; Faces=" + vertices_das_faces.size() + ".";
    }

    public void updateVisibility(Camera cam) {
        //Precisa de 3 pontos para normal.
        int i = 0;
        for (IndexList face : vertices_das_faces) {
            if (face.size() >= 3) {
                System.out.println("FACE: " + face);
                Vertice normal = VMath.obterNormal(getPoint(face.get(1)), getPoint(face.get(0)), getPoint(face.get(2)));
                VMath.normalizar(normal);
                System.out.println("Normal: " + normal);
                double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
                System.out.println("Mult: " + mult);
                visibilidade_faces[i] = mult <= 0; //Se mult>0, face[i] é visível
            }
            i++;
        }
    }

    public List<Integer> getVisibleFaces() {
        List<Integer> lista = new ArrayList();

        for (int i = 0; i < vertices_das_faces.size(); i++) {
            if (visibilidade_faces[i] == true) {
                lista.add(i);
            }
        }

        return lista;
    }

    //<editor-fold defaultstate="collapsed" desc="Some comments">
    //private List<Aresta> scanlines = null;
    //private ArrayList<Vertice> vertices = new ArrayList();
    //private Color corFundo;
    //private Color corBorda;
    //private Vertice centroid = null;
    //private boolean changeScans    = false;
    //private boolean changeCentroid = false;
    /*public int[] getXpoints(){
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
    }*/
    //https://stackoverflow.com/questions/2792443/finding-the-centroid-of-a-polygon
    /*public Vertice getCentroideDaMedia(){
    if (centroid!=null && changeCentroid==false) return centroid;
    
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
    
    changeCentroid = false;
    return centroid;
    }
    
    public List<Aresta> getPaintLines(){
    if (scanlines!=null && changeScans==false) return scanlines;
    
    //ScanLineFill scn = new ScanLineFill(this);
    //scanlines = scn.calculateScans();
    //scanlines = VProperties.calculateScanLines(this);
    
    changeScans = false;
    return scanlines;
    }
    
    //https://rosettacode.org/wiki/Ray-casting_algorithm#Java
    public boolean contains(Vertice v){
    boolean inside = false;
    int len = vertices.size();
    for (int i = 0; i < len; i++) {
    if (intersects(vertices.get(i), vertices.get( (i+1) %len), v))
    inside = !inside;
    }
    return inside;
    }
    
    private boolean intersects(Vertice linePA, Vertice linePB, Vertice P) {
    if (linePA.getY() > linePB.getY())
    return intersects(linePB, linePA, P);
    
    if (P.getY() == linePA.getY() || P.getY() == linePB.getY())
    P.y += 0.0001;
    
    if (P.getY() > linePB.getY() || P.getY() < linePA.getY() || P.getX() > Math.max(linePA.getX(), linePB.getX()))
    return false;
    
    if (P.getX() < Math.min(linePA.getX(), linePB.getX()))
    return true;
    
    double red = (P.getY() - linePA.getY()) / (double) (P.getX() - linePA.getX());
    double blue = (linePB.getY() - linePA.getY()) / (double) (linePB.getX() - linePA.getX());
    return red >= blue;
    }
    
    private List<Face> pontosParaFaces(){
    List<Face> listaDeFaces = new ArrayList<>();
    //vertices = lista De Vertices
    return null;
    }*/
//</editor-fold>
    public static void main(String... args) {
        //Index2 a = new Index2(0, 1);
        //Index2 b = new Index2(1, 0);
        //System.out.println(a.hashCode());
        //System.out.println(b.hashCode());
        //System.out.println(a.equals(b));
        //System.out.println(a.equalsRev(b));

        /*float[][] pol_mat = {
            {  0,  10, 10,  5}, //X
            {  0,   0,  0, 10}, //Y
            {  0,  10,  0,  5}, //Z
            {  1,   1,  1,  1}
            // A    B   C   D
        };
        
        int[] face0 = {0,1,3};
        int[] face1 = {1,2,3};
        int[] face2 = {2,0,3};
        int[] face3 = {0,2,1};*/
        float[][] pol_mat = {
            {30, 35, 25, 20, 30},
            {2, 4, 3, 1, 10},
            {25, 20, 18, 23, (float) 22.5},
            {1, 1, 1, 1, 1}
        //  A    B    C    D    E
        };

        int[] face0 = {0, 3, 2, 1};
        int[] face1 = {0, 1, 4};
        int[] face2 = {1, 2, 4};
        int[] face3 = {2, 3, 4};
        int[] face4 = {3, 0, 4};

        List<IndexList> faces = new ArrayList();
        faces.add(new IndexList(face0));
        faces.add(new IndexList(face1));
        faces.add(new IndexList(face2));
        faces.add(new IndexList(face3));
        faces.add(new IndexList(face4));

        Vertice ViewUp = new Vertice(0, 1, 0);
        Vertice VRP = new Vertice(50, 15, 30);
        Vertice P = new Vertice(20, 6, 15);
        Camera cam = new Camera(ViewUp, VRP, P);

        Poligono p = new Poligono(pol_mat, faces);

        p.updateVisibility(cam);
        System.out.println(p.getVisibleFaces());

        System.out.println(p.vertices_das_faces.get(2));
    }
}
