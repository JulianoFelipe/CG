package m.poligonos;

import m.poligonos.we_edge.IndexList;
import m.poligonos.we_edge.Index2;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import m.Camera;
import utils.math.VMath;


/**
 *
 * @author 
 */
public class PoligonoOld_WingedEdge extends CGObject{
    //http://www.dtic.mil/dtic/tr/fulltext/u2/755141.pdf
    //https://www.sciencedirect.com/science/article/pii/S0097849307001653
    //http://www.inf.unioeste.br/~adair/CG/Notas%20Aula/Slides%2003%20-%20Modelagem%20de%20Solidos%20Curvas%20e%20Superficies.pdf
    //https://stackoverflow.com/questions/23768682/implementation-of-winged-edge-in-lua
    
    //LISTA DE ARESTAS
    private final List<Index2> lAresta_vertices;//Vertices inicial/final
    private final List<Index2> lAresta_faces;   //Faces, esquerda/direita
    private final List<Index2> lAresta_faceEsq; //Aresta predecessora/sucessora da face esquerda
    private final List<Index2> lAresta_faceDir; //Aresta predecessora/sucessora da face direita
    
    //LISTA DE FACES
    private final int[] lFaces; //Uma aresta da face
    //lFace redundante? Tem que ser uma aresta diferente da que está em "lVertices" na mesma posição?
    
    //LISTA DE VERTICES (Só o ponteiro "Aresta Incidente" da Winged Edge)
    private final int[] lVertices;

    private final boolean[] visibilidade_faces;
        
    //<editor-fold defaultstate="collapsed" desc="Construtores">     
    public PoligonoOld_WingedEdge(float[][] pointMatrix, List<IndexList> faces){
        //super(pointMatrix);
        
        int points = pointMatrix[0].length;
        
        lFaces = new int[faces.size()];
        visibilidade_faces = new boolean[faces.size()];
        lVertices = new int[points];
        Arrays.fill(lVertices, -1);
        
        lAresta_vertices = new ArrayList();
        lAresta_faces    = new ArrayList();
        lAresta_faceEsq  = new ArrayList();
        lAresta_faceDir  = new ArrayList();
        
        //Percorre montando as arestas
        int i, previous, current=0, faceCounter=0;
        Index2 arestaTemp;
        HashMap<String,Integer> facePositionMap = new HashMap();
        for (IndexList face : faces){
            
            if (face.size() < 3) throw new IllegalArgumentException("Face com menos de 3 pontos: " + face.size());
            
            for (i=1; i<face.size(); i++){
                previous = face.get(i-1);
                current  = face.get(i);
                arestaTemp = new Index2(previous, current); //Aresta vai de previous até current
                //System.out.println("Aresta: " + arestaTemp + " Face: " + face + " Counter: " + faceCounter);
                facePositionMap.put(arestaTemp.toString(), faceCounter);
                
                int indexOf = lAresta_vertices.indexOf(arestaTemp); //Verifica se a aresta já existe na lista
                //System.out.println("Inddex: " + indexOf);
                if (indexOf == -1){ //Não existe, adiciona e pega o índice
                    indexOf = lAresta_vertices.size();
                    lAresta_vertices.add(arestaTemp);
                    //se existe, o índice já é de uma aresta
                }
                //System.out.println("Current: " + current + " Index: " + indexOf);
                
                if (lVertices[current] == -1){
                    lVertices[current] = indexOf; //Aresta incidente no vertice "current" é a que está na posição "indexOf" do array de arestas (lAresta_vertices)
                    lFaces[current] = indexOf;
                }
            }
            
            //Aresta que vai do último ponto até o primeiro
            previous = current;
            current = face.get(0);
            arestaTemp = new Index2(previous, current);
            //System.out.println("Aresta: " + arestaTemp + " Face: " + face + " Counter: " + faceCounter);
            facePositionMap.put(arestaTemp.toString(), faceCounter);
            
            int indexOf = lAresta_vertices.indexOf(arestaTemp);
            //System.out.println("Inddex: " + indexOf);
            if (indexOf == -1){
                indexOf = lAresta_vertices.size();
                lAresta_vertices.add(arestaTemp);
            }
            //System.out.println("Current: " + current + " Index: " + indexOf);
            if (lVertices[current] == -1){
                lVertices[current] = indexOf;
                lFaces[current] = indexOf;
            }
            faceCounter++;
        }
        System.out.println("LVERTICES: " + Arrays.toString(lVertices));
        System.out.println("LARESTA_VERTICES: " + lAresta_vertices);
        System.out.println("ARESTA FACE: " + Arrays.toString(lFaces));
        /* Ao término do laço deveria-se ter:
        - "lVertices" com os índices das arestas incidentes nos respectivos pontos
        - "lAresta_vertices" com as arestas pertencentes as faces, SEM REPETIÇÕES
        - "lFaces" contém a primeira areta de suas respectivas faces
        - "facePositionMap" contém a relação de arestas para suas faces.
        */
        
        System.out.println(facePositionMap);
        //Index2 faceEsqDir;
        for (Index2 aresta : lAresta_vertices){ //Para todas as arestas definidas, procuram-se as faces esquerda/direita
            String arestaString  = aresta.toString();
            String reverseAresta = new StringBuilder(aresta.toString()).reverse().toString();
            //System.out.println("Aresta: " + arestaString);
            //System.out.println("Aresta rev: " + reverseAresta);
            
            int ind1 = facePositionMap.get(arestaString);
            int ind2 = facePositionMap.get(reverseAresta);
            
            //System.out.println("IND: " + ind1 + ", "+ ind2);
            
            lAresta_faces.add(new Index2(ind1, ind2));
                        
            AtomicInteger faceEsqPREaresta = new AtomicInteger(-1),
                          faceEsqSUCaresta = new AtomicInteger(-1),
                          faceDirPREaresta = new AtomicInteger(-1),
                          faceDirSUCaresta = new AtomicInteger(-1);
            //https://stackoverflow.com/questions/30026824/modifying-local-variable-from-inside-lambda
            
            facePositionMap.forEach((key, value) -> {
                //key = aresta
                //value = face
                if (!key.equals(arestaString)){
                
                if (ind1==value){ //Face da esquerda
                    //System.out.println("Face Esquerda: " + key);
                    if (key.charAt(1)==arestaString.charAt(0)){ //Fim da aresta key = começo da aresta atual  --> Predecessora
                        Index2 arestaKey = Index2.of(key);
                        int indexOf = lAresta_vertices.indexOf(arestaKey);
                        //System.out.println("ESQ PRE: " + indexOf);
                        faceEsqPREaresta.set(indexOf);
                    } else if (key.charAt(0)==arestaString.charAt(1)) {  //Começo da aresta key = fim da aresta atual  --> Sucessora
                        Index2 arestaKey = Index2.of(key);
                        int indexOf = lAresta_vertices.indexOf(arestaKey);
                        //System.out.println("ESQ SUC: " + indexOf);
                        faceEsqSUCaresta.set(indexOf);
                    }
                }
                }
                
                if (!key.equals(reverseAresta)){
                if (ind2==value){ //Face da direita
                    //System.out.println("Face Direita: " + key);
                    if (key.charAt(1)==reverseAresta.charAt(0)){ //Fim da aresta key = começo da aresta atual  --> Predecessora
                        Index2 arestaKey = Index2.of(key);
                        int indexOf = lAresta_vertices.indexOf(arestaKey);
                        //System.out.println("DIR PRE: " + indexOf);
                        faceDirPREaresta.set(indexOf);
                    } else if (key.charAt(0)==reverseAresta.charAt(1)) {  //Começo da aresta key = fim da aresta atual  --> Sucessora
                        Index2 arestaKey = Index2.of(key);
                        int indexOf = lAresta_vertices.indexOf(arestaKey);
                        //System.out.println("DIR SUC: " + indexOf);
                        faceDirSUCaresta.set(indexOf);
                    }
                }
                
                }
            });
            
            lAresta_faceEsq.add(new Index2(faceEsqPREaresta.get(), faceEsqSUCaresta.get()));
            lAresta_faceDir.add(new Index2(faceDirPREaresta.get(), faceDirSUCaresta.get()));
        }
        System.out.println("LAresta_Faces: " + lAresta_faces);
        System.out.println("lAresta_faceEsq: " + lAresta_faceEsq);
        System.out.println("lAresta_faceDir: " + lAresta_faceDir);
    }
    
    public PoligonoOld_WingedEdge(PoligonoOld_WingedEdge p){
        super(p); //super(p.getPoints(), p.getID());
        
        lAresta_vertices = new ArrayList(p.lAresta_vertices);
        lAresta_faces    = new ArrayList(p.lAresta_faces);
        lAresta_faceEsq  = new ArrayList(p.lAresta_faceEsq);
        lAresta_faceDir  = new ArrayList(p.lAresta_faceDir);
        
        lFaces = new int[p.lFaces.length];
        System.arraycopy(p.lFaces, 0, lFaces, 0, p.lFaces.length);
        
        lVertices = new int[p.lVertices.length];
        System.arraycopy(p.lVertices, 0, lVertices, 0, p.lVertices.length);
        
        visibilidade_faces = new boolean[p.visibilidade_faces.length];
        System.arraycopy(p.visibilidade_faces, 0, visibilidade_faces, 0, p.visibilidade_faces.length);
    }

//</editor-fold>
    
    @Override
    public String toString(){
        return "Polígono: ID=" + ID + "; Points=" + getNumberOfPoints() + "; Faces=" + lFaces.length + ".";
    }
    
    //Não funcionou. Repetia alguns pontos daí a normal fica zer0
    /*public void updateVisibility(Camera cam){
        //Precisa de 3 pontos para normal.
        //Dois pontos obtém-se da aresta da lista das faces
        //Outro do inicio da aresta predecessora da face esquerda na lista de arestas da mesma aresta pega no item anterior.
        
        int indexA, index1, index2, index3;        
        for (int i=0; i<lFaces.length; i++){
            indexA = lFaces[i];
            Index2 arestaFace = lAresta_vertices.get(indexA);
            System.out.println("ARESTA: " + arestaFace);
            index1 = arestaFace.getInd1(); //Ind1 = A, Ind2 = B, Ind3 = C;
            index2 = arestaFace.getInd2(); //Normal = BA x BC
            
            /*Index2 temp = lAresta_faceEsq.get(indexA);
            if (temp.getInd1() == index2){
                index3 = temp.getInd2();
            } else {
                index3 = temp.getInd1();
            }*/
            /*index3 = lAresta_faceEsq.get(indexA).getInd1();
            System.out.println("PRE ARESTA: " + lAresta_faceEsq.get(indexA));
            System.out.println("IND3: " + index3 + " PRE-END: " + lAresta_faceEsq.get(indexA).getInd2());
            System.out.println("P1: " + getPoint(index1));
            System.out.println("P2: " + getPoint(index2));
            System.out.println("P3: " + getPoint(index3));
            Vertice normal = VMath.obterNormal(getPoint(index1), getPoint(index2), getPoint(index3));
            System.out.println(normal);
            VMath.normalizar(normal);
            System.out.println(normal);
            double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
            System.out.println(mult);
            visibilidade_faces[i] = mult > 0; //Se mult>0, face[i] é visível
        }
    }*/

    public void updateVisibility(Camera cam){
        //Precisa de 3 pontos para normal.
        //Dois pontos obtém-se da aresta da lista das faces
        //Outro do inicio da aresta predecessora da face esquerda na lista de arestas da mesma aresta pega no item anterior.
        
        int indexA, index1, index2, index3;        
        for (int i=0; i<lFaces.length; i++){
            indexA = lFaces[i];
            Index2 arestaFace = lAresta_vertices.get(indexA);
            System.out.println("ARESTA: " + arestaFace);
            index1 = arestaFace.getInd1(); //Ind1 = A, Ind2 = B, Ind3 = C;
            index2 = arestaFace.getInd2(); //Normal = BA x BC
            
            Index2 temp = lAresta_faceEsq.get(indexA); //Cada indice é uma aresta, ind1=pre e ind2=suc
            temp = lAresta_vertices.get(temp.getInd1()); //Pega a aresta predecessora
            index3=temp.getInd2();
            
            System.out.println("PRE ARESTA: " + lAresta_faceEsq.get(indexA));
            System.out.println("IND3: " + index3 + " PRE-END: " + lAresta_faceEsq.get(indexA).getInd2());
            System.out.println("P1: " + get(index1));
            System.out.println("P2: " + get(index2));
            System.out.println("P3: " + get(index3));
            Vertice normal = VMath.obterNormal(get(index1), get(index2), get(index3));
            System.out.println(normal);
            VMath.normalizar(normal);
            System.out.println(normal);
            double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
            System.out.println(mult);
            visibilidade_faces[i] = mult > 0; //Se mult>0, face[i] é visível
        }
    }
    
    public List<Integer> getVisibleFaces(){
        List<Integer> lista = new ArrayList();
        
        for (int i=0; i<lFaces.length; i++){
            if (visibilidade_faces[i] == true) lista.add(i);
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
    
    public static void main(String...args){
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
            {  30,  35,  25,  20,  30},
            {   2,   4,   3,   1,  10},
            {  25,  20,  18,  23,  (float) 22.5},
            {   1,   1,   1,   1,   1}
            //  A    B    C    D    E
        };
        
        int[] face0 = {0,3,2,1};
        int[] face1 = {0,1,4};
        int[] face2 = {1,2,4};
        int[] face3 = {2,3,4};
        int[] face4 = {3,0,4};
        
        List<IndexList> faces = new ArrayList();
        faces.add(new IndexList(face0));
        faces.add(new IndexList(face1));
        faces.add(new IndexList(face2));
        faces.add(new IndexList(face3));
        faces.add(new IndexList(face4));
        
        Vertice ViewUp = new Vertice(0,1,0);
        Vertice VRP = new Vertice(50,15,30);
        Vertice P = new Vertice(20,6,15);
        Camera cam = new Camera(ViewUp, VRP, P);
        
        PoligonoOld_WingedEdge p = new PoligonoOld_WingedEdge(pol_mat, faces);
        System.out.println(p.getVisibleFaces());
        p.updateVisibility(cam);
        System.out.println(p.getVisibleFaces());
    }

    @Override
    public List<? extends Vertice> getPoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int getNumberOfPoints() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Vertice get(int i) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void set(int i, Vertice point) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateInternals(CGObject updatedObj) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
