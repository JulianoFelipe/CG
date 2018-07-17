package m.poligonos.we_edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import m.Camera;
import m.Eixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import utils.math.VMath;

/**
 * Winged Edge com Edges duplicadas
 * @author
 */
public class HE_Poliedro extends CGObject {
    //https://people.cs.clemson.edu/~dhouse/courses/405/papers/winged-edge.pdf
    //Referente ao link acima (PG: 11 de 21):
    /*
    Em "WE_Poliedro", mantém-se somente uma cópia de cada aresta, onde está é ordenada de maneira anti-horária.
    Assim, algumas faces podem não ter nenhuma referência em "Faces à esquerda" na lista de arestas, e essa propriedade
    estava sendo explorada para a obténção dos vértices da face para utilizá-los em cálculos de normal, por exemplo.
    
    (Essa classe foi copiada e adaptada de "WE_Poliedro"
    */
    
    private final List<WE_Aresta>  listaDeArestas;
    private final List<WE_Vertice> listaDeVertices;
    private final List<WE_Face>    listaDeFaces;
    private final boolean[] visibilidade_faces;

    private Camera lastCamCopy;
    
    private float min_x, max_x,
                  min_y, max_y,
                  min_z, max_z;
    
    public HE_Poliedro(float[][] pointMatrix, List<IndexList> faces) {
        super();

        visibilidade_faces = new boolean[faces.size()];
        Arrays.fill(visibilidade_faces, true);
        
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
        
        HashMap<HE_Key,Integer> map = new HashMap();
        
        int i, previous, current = 0, faceCounter=0;
        for (IndexList face : faces) {
            if (face.size() < 3) {
                throw new IllegalArgumentException("Face com menos de 3 pontos: " + face.size());
            }

            for (i = 1; i < face.size(); i++) {
                previous = face.get(i - 1);
                current = face.get(i);

                WE_Aresta we_arestaTemp = new WE_Aresta(listaDeVertices.get(previous), listaDeVertices.get(current));
                we_arestaTemp.setFaceEsquerda(listaDeFaces.get(faceCounter));
                listaDeArestas.add(we_arestaTemp);
                
                map.put(new HE_Key(we_arestaTemp), faceCounter);
                
                if (listaDeVertices.get(current).getArestaIncidente() == null) {
                    listaDeVertices.get(current).setArestaIncidente(listaDeArestas.get(listaDeArestas.size() - 1));
                }
                
                if (listaDeFaces.get(faceCounter).getArestaDaFace() == null){
                    listaDeFaces.get(faceCounter).setArestaDaFace(we_arestaTemp);
                }
            }

            //Aresta que vai do último ponto até o primeiro
            previous = current;
            current = face.get(0);

            WE_Aresta we_arestaTemp = new WE_Aresta(listaDeVertices.get(previous), listaDeVertices.get(current));
            we_arestaTemp.setFaceEsquerda(listaDeFaces.get(faceCounter));
            listaDeArestas.add(we_arestaTemp);
            
            map.put(new HE_Key(we_arestaTemp), faceCounter);
            
            if (listaDeVertices.get(current).getArestaIncidente() == null) {
                listaDeVertices.get(current).setArestaIncidente(listaDeArestas.get(listaDeArestas.size() - 1));
            }
            
            if (listaDeFaces.get(faceCounter).getArestaDaFace() == null){
                listaDeFaces.get(faceCounter).setArestaDaFace(we_arestaTemp);
            }
            
            faceCounter++;
        }
        //System.out.println("Lista de Vertices: " + listaDeVertices);
        //System.out.println("Lista de Arestas: " + listaDeArestas);
        //System.out.println("Lista de Faces: " + listaDeFaces);
       
        /* Ao término do laço deveria-se ter:
        - "ListaDeVertices" com os índices das arestas incidentes nos respectivos pontos
        - "ListaDeArestas" com as arestas pertencentes as faces, mas com predecessoras/sucessoras e faces nulas
        */

        for (i=0; i<listaDeArestas.size(); i++){
            WE_Aresta local = listaDeArestas.get(i);
            local.setFaceDireita(
                listaDeFaces.get(
                    map.get(
                        new HE_Key(local.getvFinal(), local.getvInicial())
                    ) 
                ) 
            );
            
            //System.out.println("Local aresta: " + local);
            map.forEach((HE_Key key, Integer faceIndex) -> {
                Vertice iniKey = key.getIni();
                Vertice finKey = key.getFin();

                //System.out.println("Face Index: " + faceIndex + " Aresta: " + key.getAresta().idString());
                
                if(listaDeFaces.get(faceIndex).ID == local.getFaceEsquerda().ID){ //Mesma face
                    //System.out.println("Same Face");
                    if (iniKey.equals(local.getvFinal())){ //Sucessora ESQ
                        //System.out.println("SUC ESQ");
                        local.setEsquerdaSucessora(key.getAresta());
                    } else if (finKey.equals(local.getvInicial())){ //Predecessora ESQ
                        //System.out.println("PRE ESQ");
                        local.setEsquerdaPredecessora(key.getAresta());
                    }
                } else if (listaDeFaces.get(faceIndex).ID == local.getFaceDireita().ID){
                    //System.out.println("Face dir");
                    if (!local.oppositeEdges(key.getAresta())){
                        //System.out.println("DIR LC: " + local.vertexIndicatorString());
                        //System.out.println("DIR AR: " + key.getAresta().vertexIndicatorString());
                        if (iniKey.equals(local.getvInicial())){ //Predecessora DIR
                            //System.out.println("SUC DIR");
                            local.setDireitaSucessora(key.getAresta());
                        } else if (finKey.equals(local.getvFinal())){ //Sucessora DIR
                            //System.out.println("PRE DIR");
                            local.setDireitaPredecessora(key.getAresta());
                        }
                    }                   
                }
            });
            //System.out.println("\n");
        }
        
        //System.out.println("Lista de Arestas: " + listaDeArestas);
    }

    public HE_Poliedro(HE_Poliedro p) {
        super(p);
        
        visibilidade_faces = new boolean[p.visibilidade_faces.length];
        System.arraycopy(p.visibilidade_faces, 0, visibilidade_faces, 0, p.visibilidade_faces.length);
        
        listaDeVertices = new ArrayList(p.listaDeVertices.size()); 
        HashMap<Long,Vertice> ref_ver = new HashMap(p.listaDeVertices.size()+3);
        p.listaDeVertices.forEach((v) -> {
            WE_Vertice vert = new WE_Vertice(v);
            listaDeVertices.add(vert);
            ref_ver.put(v.getID(), vert);
        });
        //System.out.println("P VERT: " + p.listaDeVertices.get(0));
        //System.out.println("SET THIS VERT");
        //p.listaDeVertices.get(0).setAll(666, 666, 666);
        //System.out.println("P VERT: " + p.listaDeVertices.get(0));
        //System.out.println("THIS VERT: " + listaDeVertices.get(0));
        
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
            listaDeFaces.add(fac);
            ref_fac.put(face.ID, fac);
        });
        
        listaDeArestas = new ArrayList(p.listaDeArestas.size()+3);
        HashMap<Long,WE_Aresta> ref_set = new HashMap(p.listaDeArestas.size()+3);
        p.listaDeArestas.forEach((aresta) -> {
            WE_Aresta ar = new WE_Aresta(aresta, ref_ver.get(aresta.getvInicial().getID()), ref_ver.get(aresta.getvFinal().getID()));
            listaDeArestas.add(ar);
            ref_set.put(aresta.getID(), ar);
        });
        //System.out.println("ARR: " + listaDeArestas.get(0));
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
        return "HE_Poliedro: ID=" + ID + "; Points=" + listaDeVertices.size() + "; Faces=" + listaDeFaces.size() + ".";
    }
    
    public void updateVisibility(Camera cam) {
        //Precisa de 3 pontos para normal.
        int i = 0;

        for(WE_Face face : listaDeFaces){
            WE_Aresta arestaFace = face.getArestaDaFace();
            //System.out.println("Face ID: " + face.ID + " Aresta ID: " + arestaFace.getID());
            
            Vertice third = arestaFace.getEsquerdaSucessora().getvFinal();
            //System.out.println("One: " + arestaFace.getvFinal());
            //System.out.println("Two: " + arestaFace.getvInicial());
            //System.out.println("Thr: " + third);
            
            Vertice normal = VMath.obterNormal(arestaFace.getvFinal(), arestaFace.getvInicial(), third);
            VMath.normalizar(normal);
            double mult = VMath.produtoEscalar(cam.getVetorN(), normal);
            //System.out.println("Normal: " + normal + " Mult: " + mult);
            visibilidade_faces[i] = mult <= 0; //Se mult>0, face[i] é visível
            i++;
        }
        
        lastCamCopy = cam;
    }

    public List<List<WE_Aresta>> getVisibleFaces() {
        List<List<WE_Aresta>> lista = new ArrayList();

        for (int i = 0; i < listaDeFaces.size(); i++) {
            if (visibilidade_faces[i] == true) {
                List<WE_Aresta> arestas_face = new ArrayList();
                WE_Face face = listaDeFaces.get(i);
                WE_Aresta ini = face.getArestaDaFace();
                arestas_face.add(ini);
                //System.out.println("Aresta add: " + ini);
                WE_Aresta local=ini.getEsquerdaSucessora();
                while (local != ini){
                    arestas_face.add(local);
                    //System.out.println("Aresta add: " + local);
                    local = local.getEsquerdaSucessora();
                }
                
                lista.add(arestas_face);
                //System.out.println("\n\n");
            }
        }

        return lista;
    }
    
    private List<List<Vertice>> getVisiblePoints() {
        List<List<Vertice>> lista = new ArrayList();

        for (int i = 0; i < listaDeFaces.size(); i++) {
            if (visibilidade_faces[i] == true) {
                List<Vertice> vertices_face = new ArrayList();
                WE_Face face = listaDeFaces.get(i);
                WE_Aresta ini = face.getArestaDaFace();
                vertices_face.add(ini.getvInicial());
                //System.out.println("Aresta add: " + ini);
                WE_Aresta local=ini.getEsquerdaSucessora();
                while (local != ini){
                    vertices_face.add(local.getvInicial());
                    //System.out.println("Aresta add: " + local);
                    local = local.getEsquerdaSucessora();
                }
                
                lista.add(vertices_face);
                //System.out.println("\n\n");
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

    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof HE_Poliedro)) throw new IllegalArgumentException("Não é uma instância de WE_Poliedro."); //Is this Right?

        HE_Poliedro updated = (HE_Poliedro) updatedObj;
        
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
        
        List<List<Vertice>> lista = getVisiblePoints();
        
        for (List<Vertice> face : lista){
            inside = false;
            for (int i=0, j=face.size()-1 ; i<face.size() ; j=i++){
                Vertice ith = face.get(i);
                Vertice jth = face.get(j);

                if ( (ith.getY()>y) != (jth.getY()>y) && x < (jth.getX()-ith.getX()) * (y-ith.getY()) / (jth.getY()-ith.getY()) + ith.getX() ) {
                    inside = !inside;
                }
            }

            if (inside) return inside;
        }
        
        

        return inside;
    }
    
    /*@Override //NÃO CONSIDERA DIFERENÇA NENHUMA EM FACES. TODOS OS PONTOS TERIAM UMA FACE SÓ
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
    }*/

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
        Vertice VRP    = new Vertice(50, 15, 30);
        Vertice P      = new Vertice(20, 6, 15);
        Camera cam     = new Camera(ViewUp, VRP, P);
        HE_Poliedro p  = new HE_Poliedro(pol_mat, faces);

        p.updateVisibility(cam);
        //System.out.println(p.getVisibleFaces());
        System.out.println(Arrays.toString(p.visibilidade_faces));
        
        /*System.out.println("Ponto 0: " + p.get(0));
        System.out.println("FACES: " + p.getVisibleFaces().get(0));
        HE_Poliedro new_p = new HE_Poliedro(p);
        new_p.set(0, new Vertice(0, 0, 666));
        System.out.println("New   0: " + new_p.listaDeArestas.get(0));
        System.out.println("New   0: " + p.listaDeArestas.get(0));
        System.out.println("FACES: " + new_p.getVisibleFaces().get(0));
        System.out.println("FACES: " + p.getVisibleFaces().get(0));*/
    }

    @Override
    public Vertice getCentroide() {
        double avgX=0, avgY=0, avgZ=0;
        
        for (Vertice v : listaDeVertices){
            avgX += v.getX();
            avgY += v.getY();
            avgZ += v.getZ();
        }
        
        int count = listaDeVertices.size();
        
        return new Vertice(
            (float) (avgX/count),
            (float) (avgY/count),
            (float) (avgZ/count)
        );
    }
    
}
