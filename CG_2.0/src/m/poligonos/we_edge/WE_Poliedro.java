package m.poligonos.we_edge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import m.poligonos.CGObject;
import m.poligonos.Face;
import m.poligonos.Vertice;

/**
 *
 * @author
 */
public class WE_Poliedro extends CGObject {

    private List<WE_Aresta>  listaDeArestas;
    private List<WE_Vertice> listaDeVertices;
    private List<WE_Face>    listaDeFaces;
    private final boolean[] visibilidade_faces;

    public WE_Poliedro(float[][] pointMatrix, List<IndexList> faces) {
        super();

        visibilidade_faces = new boolean[faces.size()];
        listaDeFaces = new ArrayList(faces.size());
        listaDeArestas = new ArrayList();
        listaDeVertices = new ArrayList(pointMatrix[0].length + 3);

        for (int i = 0; i < pointMatrix[0].length; i++) {
            listaDeVertices.add(new WE_Vertice(pointMatrix[0][i], pointMatrix[1][i], pointMatrix[2][i]));
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
        
        listaDeFaces = new ArrayList(listaDeFaces.size());
        p.listaDeFaces.forEach((face) -> {
            listaDeFaces.add(new WE_Face(face));
        });
        
        listaDeArestas = new ArrayList(listaDeArestas.size());
        p.listaDeArestas.forEach((aresta) -> {
            listaDeArestas.add(new WE_Aresta(aresta));
        });
        
        listaDeVertices = new ArrayList(listaDeVertices.size()); 
        for (WE_Vertice v : p.listaDeVertices){
            listaDeVertices.add(new WE_Vertice(v));
        }
    }
    
    @Override
    public String toString() {
        return "WE_Poliedro: ID=" + ID + "; Points=" + listaDeVertices.size() + "; Faces=" + listaDeFaces.size() + ".";
    }

    /*public void updateVisibility(Camera cam) {
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
    }*/
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
    }

    public static void main(String... args) {
        //Index2 a = new Index2(0, 1);
        //Index2 b = new Index2(1, 0);
        //System.out.println(a.hashCode());
        //System.out.println(b.hashCode());
        //System.out.println(a.equals(b));
        //System.out.println(a.equalsRev(b));

        float[][] pol_mat = {
            {  0,  10, 10,  5}, //X
            {  0,   0,  0, 10}, //Y
            {  0,  10,  0,  5}, //Z
            {  1,   1,  1,  1}
            // A    B   C   D
        };
        
        int[] face0 = {0,1,3};
        int[] face1 = {1,2,3};
        int[] face2 = {2,0,3};
        int[] face3 = {0,2,1};
        /*float[][] pol_mat = {
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
        int[] face4 = {3, 0, 4};*/

        List<IndexList> faces = new ArrayList();
        faces.add(new IndexList(face0));
        faces.add(new IndexList(face1));
        faces.add(new IndexList(face2));
        faces.add(new IndexList(face3));
        /*faces.add(new IndexList(face4));*/

        /*Vertice ViewUp = new Vertice(0, 1, 0);
        Vertice VRP = new Vertice(50, 15, 30);
        Vertice P = new Vertice(20, 6, 15);
        Camera cam = new Camera(ViewUp, VRP, P);*/
        WE_Poliedro p = new WE_Poliedro(pol_mat, faces);

        /*p.updateVisibility(cam);
        System.out.println(p.getVisibleFaces());

        System.out.println(p.vertices_das_faces.get(2));*/
    }

    @Override
    public void updateInternals(CGObject updatedObj) {
        if (!(updatedObj instanceof WE_Poliedro)) throw new IllegalArgumentException("Não é uma instância de WE_Poliedro."); //Is this Right?

        WE_Poliedro updated = (WE_Poliedro) updatedObj;
        
        for (int i=0; i<listaDeVertices.size(); i++)
           listaDeVertices.get(i).copyAttributes(updated.get(i));
    }
    
    
}
