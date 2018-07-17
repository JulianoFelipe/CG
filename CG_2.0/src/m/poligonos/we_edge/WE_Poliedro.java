package m.poligonos.we_edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import m.Camera;
import m.Eixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import utils.math.VMath;

/**
 *
 * @author
 */
class WE_Poliedro extends CGObject {

    private final List<WE_Aresta>  listaDeArestas;
    private final List<WE_Vertice> listaDeVertices;
    private final List<WE_Face>    listaDeFaces;
    private final boolean[] visibilidade_faces;

    private float min_x, max_x,
                  min_y, max_y,
                  min_z, max_z;
    
    public WE_Poliedro(float[][] pointMatrix, List<IndexList> faces) {
        super();

        visibilidade_faces = new boolean[faces.size()];
        listaDeFaces = new ArrayList(faces.size());
        listaDeArestas = new ArrayList();
        listaDeVertices = new ArrayList(pointMatrix[0].length + 3);

        listaDeVertices.add(new WE_Vertice(pointMatrix[0][0], pointMatrix[1][0], pointMatrix[2][0]));
        min_x = max_x = pointMatrix[0][0];
        min_y = max_y = pointMatrix[1][0];
        min_z = max_z = pointMatrix[2][0];
        
        for (int i = 1; i < pointMatrix[0].length; i++) {
            listaDeVertices.add(new WE_Vertice(pointMatrix[0][i], pointMatrix[1][i], pointMatrix[2][i]));
            
            min_x = Math.min(pointMatrix[0][i], min_x);
            max_x = Math.max(pointMatrix[0][i], max_x);
            min_y = Math.min(pointMatrix[0][i], min_y);
            max_y = Math.max(pointMatrix[0][i], max_y);
            min_z = Math.min(pointMatrix[0][i], min_z);
            max_z = Math.max(pointMatrix[0][i], max_z);
        }

        faces.forEach((_item) -> {
            listaDeFaces.add(new WE_Face());
        });
        
        List<Index2> listaDeArestasAdicionadas = new ArrayList();
        //Percorre montando as arestas
        int i, previous, current = 0, faceCounter = 0;
        Index2 arestaTemp;
        HashMap<String, Integer> facePositionMap = new HashMap();
        for (IndexList face : faces) {
            if (face.size() < 3) {
                throw new IllegalArgumentException("Face com menos de 3 pontos: " + face.size());
            }

            for (i = 1; i < face.size(); i++) {
                previous = face.get(i - 1);
                current = face.get(i);
                arestaTemp = new Index2(previous, current); //Aresta vai de previous até current
                //System.out.println("Aresta: " + arestaTemp + " Face: " + face + " Counter: " + faceCounter);
                facePositionMap.put(arestaTemp.toString(), faceCounter);

                int indexOf = listaDeArestasAdicionadas.indexOf(arestaTemp); //Verifica se a aresta já existe na lista
                //System.out.println("Inddex: " + indexOf);
                if (indexOf == -1) { //Não existe, adiciona e pega o índice
                    indexOf = listaDeArestasAdicionadas.size();
                    listaDeArestasAdicionadas.add(arestaTemp);

                    WE_Aresta we_arestaTemp = new WE_Aresta(listaDeVertices.get(arestaTemp.getInd1()), listaDeVertices.get(arestaTemp.getInd2()));
                    listaDeArestas.add(we_arestaTemp);
                    //se existe, o índice já é de uma aresta
                }
                //System.out.println("Current: " + current + " Index: " + indexOf);

                if (listaDeVertices.get(current).getArestaIncidente() == null) {
                    listaDeVertices.get(current).setArestaIncidente(listaDeArestas.get(listaDeArestas.size() - 1));
                    listaDeFaces.get(current).setArestaDaFace(listaDeArestas.get(listaDeArestas.size() - 1));
                    //Aresta incidente no vertice "current" é a que está na posição "indexOf" do array de arestas (lAresta_vertices)
                }
            }

            //Aresta que vai do último ponto até o primeiro
            previous = current;
            current = face.get(0);
            arestaTemp = new Index2(previous, current);
            //System.out.println("Aresta: " + arestaTemp + " Face: " + face + " Counter: " + faceCounter);
            facePositionMap.put(arestaTemp.toString(), faceCounter);

            int indexOf = listaDeArestasAdicionadas.indexOf(arestaTemp); //Verifica se a aresta já existe na lista
            //System.out.println("Inddex: " + indexOf);
            if (indexOf == -1) { //Não existe, adiciona e pega o índice
                indexOf = listaDeArestasAdicionadas.size();
                listaDeArestasAdicionadas.add(arestaTemp);

                WE_Aresta we_arestaTemp = new WE_Aresta(listaDeVertices.get(arestaTemp.getInd1()), listaDeVertices.get(arestaTemp.getInd2()));
                listaDeArestas.add(we_arestaTemp);
                //se existe, o índice já é de uma aresta
            }
            //System.out.println("Current: " + current + " Index: " + indexOf);

            if (listaDeVertices.get(current).getArestaIncidente() == null) {
                listaDeVertices.get(current).setArestaIncidente(listaDeArestas.get(listaDeArestas.size() - 1));
                listaDeFaces.get(current).setArestaDaFace(listaDeArestas.get(listaDeArestas.size() - 1));
                //Aresta incidente no vertice "current" é a que está na posição "indexOf" do array de arestas (lAresta_vertices)
            }
            faceCounter++;
        }
        //System.out.println("Lista de Vertices: " + listaDeVertices);
        //System.out.println("Lista de Arestas: " + listaDeArestas);
        //System.out.println("Lista de Faces: " + listaDeFaces);
       
        /* Ao término do laço deveria-se ter:
        - "ListaDeVertices" com os índices das arestas incidentes nos respectivos pontos
        - "ListaDeArestas" com as arestas pertencentes as faces, mas com predecessoras/sucessoras e faces nulas
        - "ListaDeFaces" contém a primeira aresta de suas respectivas faces
        - "facePositionMap" contém a relação de arestas para suas faces.
        */

        //System.out.println(facePositionMap);
        //Index2 faceEsqDir;
        i = 0;
        for (Index2 aresta : listaDeArestasAdicionadas) { //Para todas as arestas definidas, procuram-se as faces esquerda/direita
            String arestaString = aresta.toString();
            String reverseAresta = new StringBuilder(aresta.toString()).reverse().toString();
            //System.out.println("Aresta: " + arestaString);
            //System.out.println("Aresta rev: " + reverseAresta);

            int ind1 = facePositionMap.get(arestaString);
            int ind2 = facePositionMap.get(reverseAresta);

            //System.out.println("IND: " + ind1 + ", "+ ind2);
            //lAresta_faces.add(new Index2(ind1, ind2));
            listaDeArestas.get(i).setFaceEsquerda(listaDeFaces.get(ind1));
            listaDeArestas.get(i).setFaceDireita(listaDeFaces.get(ind2));

            AtomicInteger faceEsqPREaresta = new AtomicInteger(-1),
                    faceEsqSUCaresta = new AtomicInteger(-1),
                    faceDirPREaresta = new AtomicInteger(-1),
                    faceDirSUCaresta = new AtomicInteger(-1);
            //https://stackoverflow.com/questions/30026824/modifying-local-variable-from-inside-lambda

            facePositionMap.forEach((key, value) -> {
                //key = aresta
                //value = face
                if (!key.equals(arestaString)) {
                    if (ind1 == value) { //Face da esquerda
                        //System.out.println("Face Esquerda: " + key);
                        if (key.charAt(1) == arestaString.charAt(0)) { //Fim da aresta key = começo da aresta atual  --> Predecessora
                            Index2 arestaKey = Index2.of(key);
                            int indexOf = listaDeArestasAdicionadas.indexOf(arestaKey);
                            //System.out.println("ESQ PRE: " + indexOf);
                            faceEsqPREaresta.set(indexOf);
                        } else if (key.charAt(0) == arestaString.charAt(1)) {  //Começo da aresta key = fim da aresta atual  --> Sucessora
                            Index2 arestaKey = Index2.of(key);
                            int indexOf = listaDeArestasAdicionadas.indexOf(arestaKey);
                            //System.out.println("ESQ SUC: " + indexOf);
                            faceEsqSUCaresta.set(indexOf);
                        }
                    }
                }

                if (!key.equals(reverseAresta)) {
                    if (ind2 == value) { //Face da direita
                        //System.out.println("Face Direita: " + key);
                        if (key.charAt(1) == reverseAresta.charAt(0)) { //Fim da aresta key = começo da aresta atual  --> Predecessora
                            Index2 arestaKey = Index2.of(key);
                            int indexOf = listaDeArestasAdicionadas.indexOf(arestaKey);
                            //System.out.println("DIR PRE: " + indexOf);
                            faceDirPREaresta.set(indexOf);
                        } else if (key.charAt(0) == reverseAresta.charAt(1)) {  //Começo da aresta key = fim da aresta atual  --> Sucessora
                            Index2 arestaKey = Index2.of(key);
                            int indexOf = listaDeArestasAdicionadas.indexOf(arestaKey);
                            //System.out.println("DIR SUC: " + indexOf);
                            faceDirSUCaresta.set(indexOf);
                        }
                    }
                }
            });
            //System.out.println(arestaString);
            //System.out.println("ESQ PRE ind: " + listaDeArestas.get(faceEsqPREaresta.get()).ID);
            //System.out.println("ESQ SUC ind: " + listaDeArestas.get(faceEsqSUCaresta.get()).ID);
            //System.out.println("DIR PRE ind: " + listaDeArestas.get(faceDirPREaresta.get()).ID);
            //System.out.println("DIR SUC ind: " + listaDeArestas.get(faceDirSUCaresta.get()).ID);
            listaDeArestas.get(i).setEsquerdaPredecessora(listaDeArestas.get(faceEsqPREaresta.get()));
            listaDeArestas.get(i).setEsquerdaSucessora(listaDeArestas.get(faceEsqSUCaresta.get()));
            //lAresta_faceEsq.add(new Index2(faceEsqPREaresta.get(), faceEsqSUCaresta.get()));
            listaDeArestas.get(i).setDireitaPredecessora(listaDeArestas.get(faceDirPREaresta.get()));
            listaDeArestas.get(i).setDireitaSucessora(listaDeArestas.get(faceDirSUCaresta.get()));
            //lAresta_faceDir.add(new Index2(faceDirPREaresta.get(), faceDirSUCaresta.get()));

            i++;
        }
        //System.out.println("Lista de Arestas: " + listaDeArestas);
    }

    public WE_Poliedro(WE_Poliedro p) {
        super(p);
        
        visibilidade_faces = new boolean[p.visibilidade_faces.length];
        System.arraycopy(p.visibilidade_faces, 0, visibilidade_faces, 0, p.visibilidade_faces.length);
        
        listaDeVertices = new ArrayList(p.listaDeVertices.size()); 
        p.listaDeVertices.forEach((v) -> {
            listaDeVertices.add(new WE_Vertice(v));
        });
        
        min_x = p.min_x;
        max_x = p.max_x;
        min_y = p.min_y;
        max_y = p.max_y;
        min_z = p.min_z;
        max_z = p.max_z;
        
        listaDeFaces = new ArrayList(p.listaDeFaces.size()+3);
        HashMap<Long,WE_Face> ref_fac = new HashMap(p.listaDeFaces.size()+3);
        p.listaDeFaces.forEach((face) -> {
            WE_Face fac = new WE_Face(face);
            listaDeFaces.add(face);
            ref_fac.put(face.ID, fac);
        });
        
        listaDeArestas = new ArrayList(p.listaDeArestas.size()+3);
        HashMap<Long,WE_Aresta> ref_set = new HashMap(p.listaDeArestas.size()+3);
        p.listaDeArestas.forEach((aresta) -> {
            WE_Aresta ar = new WE_Aresta(aresta, listaDeVertices.get(0), listaDeVertices.get(0));
            if (ar != null)
                throw new UnsupportedOperationException("HERE. Arrumar cópia de aresta com referência a vertices copiados");
            listaDeArestas.add(ar);
            ref_set.put(aresta.getID(), ar);
        });
            
        int i;
        for(i=0; i<p.listaDeArestas.size(); i++){
            WE_Aresta toCopy = p.listaDeArestas.get(i);
            
            WE_Aresta toSet = listaDeArestas.get(i);
            toSet.setEsquerdaPredecessora(ref_set.get(toCopy.getEsquerdaPredecessora().getID()));
            toSet.setEsquerdaSucessora   (ref_set.get(toCopy.getEsquerdaSucessora()   .getID()));
            toSet.setDireitaPredecessora (ref_set.get(toCopy.getDireitaPredecessora() .getID()));
            toSet.setDireitaSucessora    (ref_set.get(toCopy.getDireitaSucessora()    .getID()));
            
            toSet.setFaceEsquerda(ref_fac.get(toCopy.getFaceEsquerda().ID));
            toSet.setFaceDireita (ref_fac.get(toCopy.getFaceDireita().ID));
        }
        
        for(i=0; i<p.listaDeVertices.size(); i++){
            WE_Vertice toCopy = p.listaDeVertices.get(i);
            
            WE_Vertice toSet = listaDeVertices.get(i);
            toSet.setArestaIncidente(ref_set.get(toCopy.getArestaIncidente().getID()));
        }
        
        for(i=0; i<p.listaDeFaces.size(); i++){
            WE_Face toCopy = p.listaDeFaces.get(i);
            
            WE_Face toSet = listaDeFaces.get(i);
            toSet.setArestaDaFace(ref_set.get(toCopy.getArestaDaFace().getID()));
        }
        
        //throw new IllegalArgumentException("Terminar de arrumar as referências aqui.");
    }
    
    @Override
    public String toString() {
        return "WE_Poliedro: ID=" + ID + "; Points=" + listaDeVertices.size() + "; Faces=" + listaDeFaces.size() + ".";
    }

    public void updateVisibility(Camera cam) {
        //Precisa de 3 pontos para normal.
        int i = 0;
        
        for(WE_Face face : listaDeFaces){
            WE_Aresta arestaFace = face.getArestaDaFace();
            
            Vertice third = null;
            searchForAnyArestaParaEsquerda:
            for (WE_Aresta aresta : listaDeArestas){
                if (!aresta.equals(arestaFace)){
                    if (face.ID == aresta.getFaceEsquerda().ID){
                        third = aresta.getvFinal();
                        break searchForAnyArestaParaEsquerda;
                    } else if (face.ID == aresta.getFaceDireita().ID){
                        third = aresta.getvInicial();
                        break searchForAnyArestaParaEsquerda;
                    }
                }
            }
            
            Vertice normal = VMath.obterNormal(arestaFace.getvFinal(), arestaFace.getvInicial(), third);
            VMath.normalizar(normal);
            double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
            System.out.println("Normal: " + normal + " Mult: " + mult);
            visibilidade_faces[i] = mult > 0; //Se mult>0, face[i] é visível
            i++;
        }
        throw new UnsupportedOperationException("This doesn't work and programmer should feel bad. :(");
        //<editor-fold defaultstate="collapsed" desc="Testes que não funcionaram">
        /*System.out.println("Lista vertices: " + listaDeVertices);
        System.out.println("Lista arestas: " + listaDeArestas);
        System.out.println("Lista faces: " + listaDeFaces);

        for (WE_Face face : listaDeFaces){
        WE_Aresta arestaFace = face.getArestaDaFace();
        System.out.println("ARESTA FACE: " + arestaFace);
        WE_Aresta next;
        Vertice third;
        if (arestaFace.getFaceEsquerda().ID == face.ID){
        next = arestaFace.getEsquerdaPredecessora();
        } else {
        next = arestaFace.getDireitaSucessora();
        }

        if (next.getvInicial().equals(arestaFace.getvInicial())
        ||next.getvInicial().equals(arestaFace.getvFinal  ()))
        third = next.getvFinal();
        else
        third = next.getvInicial();

        System.out.println("POINTS (" + face.ID + "): ");
        System.out.println("One: " + arestaFace.getvFinal());
        System.out.println("Two: " + arestaFace.getvInicial());
        System.out.println("Thr: " + third);

        Vertice normal = VMath.obterNormal(arestaFace.getvFinal(), arestaFace.getvInicial(), third);
        VMath.normalizar(normal);
        double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
        System.out.println("Normal: " + normal + " Mult: " + mult);
        visibilidade_faces[i] = mult > 0; //Se mult>0, face[i] é visível

        i++;
        }*/

        /*for (WE_Face face : listaDeFaces){
        WE_Aresta arestaFace = null;

        searchForAnyArestaParaEsquerda:
        for (WE_Aresta aresta : listaDeArestas){
        if (face.ID == aresta.getFaceEsquerda().ID){
        arestaFace = aresta;
        break searchForAnyArestaParaEsquerda;
        }
        }

        Vertice one = arestaFace.getvInicial();
        Vertice two = arestaFace.getvFinal();
        Vertice three;

        arestaFace = arestaFace.getEsquerdaSucessora();
        if (arestaFace.getvInicial().equals(two)){
        three = arestaFace.getvFinal();
        } else {
        three = arestaFace.getvInicial();
        }

        Vertice normal = VMath.obterNormal(two, one, three);
        VMath.normalizar(normal);
        double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
        System.out.println("Normal: " + normal + " Mult: " + mult);
        visibilidade_faces[i] = mult > 0; //Se mult>0, face[i] é visível
        }*/
        //</editor-fold>
    }

    public List<Integer> getVisibleFaces() {
        List<Integer> lista = new ArrayList();

        for (int i = 0; i < listaDeFaces.size(); i++) {
            if (visibilidade_faces[i] == true) {
                lista.add(i);
            }
        }

        return lista;
    }
    
    @Override
    public List<WE_Vertice> getPoints() {
        return listaDeVertices;
    }

    @Override
    public int getNumberOfPoints() {
        return listaDeVertices.size();
    }

    @Override
    public Vertice get(int i) {
        return listaDeVertices.get(i);
    }

    @Override
    public void set(int i, Vertice point) {
        WE_Vertice vert = listaDeVertices.get(i);
        vert.copyAttributes(point);
        
        min_x = Math.min(point.getX(), min_x);
        max_x = Math.max(point.getX(), max_x);
        min_y = Math.min(point.getY(), min_y);
        max_y = Math.max(point.getY(), max_y);
        min_z = Math.min(point.getZ(), min_z);
        max_z = Math.max(point.getZ(), max_z);
    }

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
        int[] face1 = {0, 1, 4}; //This
        int[] face2 = {1, 2, 4};
        int[] face3 = {2, 3, 4};
        int[] face4 = {3, 0, 4}; //THis

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
        WE_Poliedro p = new WE_Poliedro(pol_mat, faces);

        p.updateVisibility(cam);
        //System.out.println(p.getVisibleFaces());

        System.out.println(Arrays.toString(p.visibilidade_faces));
    }

    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof WE_Poliedro)) throw new IllegalArgumentException("Não é uma instância de WE_Poliedro."); //Is this Right?

        WE_Poliedro updated = (WE_Poliedro) updatedObj;
        
        for (int i=0; i<listaDeVertices.size(); i++){
            listaDeVertices.get(i).copyAttributes(updated.get(i));
            
            Vertice point = updated.get(i);
            min_x = Math.min(point.getX(), min_x);
            max_x = Math.max(point.getX(), max_x);
            min_y = Math.min(point.getY(), min_y);
            max_y = Math.max(point.getY(), max_y);
            min_z = Math.min(point.getZ(), min_z);
            max_z = Math.max(point.getZ(), max_z);
        }
    }

    @Override
    public boolean contains(float x, float y, Eixo axis) {
        //https://stackoverflow.com/questions/217578/how-can-i-determine-whether-a-2d-point-is-within-a-polygon
        
        if (!insideBoundingBox(min_x, min_y, axis)) return false;
        
        // http://www.ecse.rpi.edu/Homepages/wrf/Research/Short_Notes/pnpoly.html
        boolean inside = false;
        for (int i=0, j=getNumberOfPoints()-1 ; i<getNumberOfPoints() ; j=i++){
            Vertice ith = get(i);
            Vertice jth = get(j);
            
            if ( (ith.getY()>y) != (jth.getY()>y) && x < (jth.getX()-ith.getX()) * (y-ith.getY()) / (jth.getY()-ith.getY()) + ith.getX() ) {
                inside = !inside;
            }
        }

        return inside;
    }

    @Override
    public boolean insideBoundingBox(float x, float y, Eixo axis) {
        float minX, maxX, minY, maxY;
        switch (axis){
            case Eixo_XY:
                minX = min_x;
                maxX = max_x;
                minY = min_y;
                maxY = max_y;
                break;
            case Eixo_XZ:
                minX = min_x;
                maxX = max_x;
                minY = min_z;
                maxY = max_z;
                break;
            case Eixo_YZ:
                minX = min_y;
                maxX = max_y;
                minY = min_z;
                maxY = max_z;
                break;
            default :
                throw new IllegalArgumentException("Eixo " + axis + " não é 2D.");
        }
        
        return !(x < minX || x > maxX || y < minY || y > maxY); //Se menor que min ou maior que max, false
    }

    @Override
    public Vertice getCentroide() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
