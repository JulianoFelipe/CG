package m.anderson;

import java.awt.Color;
import java.util.logging.Logger;


/**
 *
 * @author 
 */
public class Poligono extends CGObject{
    public static final Color DEFAULT_BORDA = Color.BLACK;
    public static final Color DEFAULT_FUNDO = null;
    private static final Logger LOG = Logger.getLogger("CG");
    
    //private List<Aresta> scanlines = null;
    //private ArrayList<Vertice> vertices = new ArrayList();
    //private Color corFundo;
    //private Color corBorda;
    
    //private Vertice centroid = null;
    
    //private boolean changeScans    = false;
    //private boolean changeCentroid = false;
 
    //<editor-fold defaultstate="collapsed" desc="Construtores">       
    public Poligono(Poligono p){
        super(p.getPointMatrix(), p.getID());  
    }
    
    public Poligono(float[][] pointMatrix, long ID){
        super(pointMatrix, ID);        
    }
    
    protected Poligono(){ super(); }
    
    protected Poligono (long ID) { super(ID); }
//</editor-fold>
    
    @Override
    public String toString(){
        return "Polígono: ID=" + ID + "; Points=" + getNumberOfPoints() + ".";
    }
    
    //<editor-fold defaultstate="collapsed" desc="Some comments">
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
}
