/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

//import Model.WE_Poliedro;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import m.poligonos.CGObject;
import m.poligonos.Face;
import m.poligonos.PointObject;
import m.poligonos.we_edge.WE_Poliedro;
import m.poligonos.Vertice;

//import Model.Vertice;

/**
 *
 * @author JFPS
 */
public class PMath {
    public static final double CLOSE_THRESHOLD = 5.9;
    
    /**
     * Se a distância de qualquer vertice do polígono p for "próximo"
     * ao ponto v, retorna verdadeiro. Caso contrário, falso.
     * 
     * "Próximo" é verdadeiro se a distância do ponto v e um vértice
     * do polígono é menor que {@link #CLOSE_THRESHOLD}.
     * 
     * @param p Polígono
     * @param v Vertices
     * @return Verdadeiro se v próximo à qualquer vértice de p.
     */
    @Deprecated
    public static boolean proximoDeQualquerVerticeDoPoligono(CGObject p, Vertice v){
        for (int i=0; i<p.getNumberOfPoints(); i++){
            Vertice ve = p.get(i);
            double dist = VMath.distancia(ve, v);
            if (dist < CLOSE_THRESHOLD) return true;
        }
        
        return false;
        
        //return p.getPointList().stream().map((ver) -> VMath.distancia(ver, v)).anyMatch((d) -> (d < CLOSE_THRESHOLD));
    }
    
    /**
     * Se a distância de qualquer vertice do polígono p for "próximo"
     * ao ponto v, retorna o vertice próximo. Caso contrário, null.
     * 
     * "Próximo" é verdadeiro se a distância do ponto v e um vértice
     * do polígono é menor que {@link #CLOSE_THRESHOLD}.
     * 
     * @param p Polígono
     * @param v Vertices
     * @return Vértice próximo de v, ou null, caso não exista.
     */
    @Deprecated
    public static Vertice verticeProximoDeQualquerVerticeDoPoligono(WE_Poliedro p, Vertice v){
        for (Vertice ver : p.getPoints()){
            if (VMath.distancia(ver, v) < CLOSE_THRESHOLD){
                return ver;
            }
        }
        return null;
    }
    
    //Extrapolei uma ideia que uso no TCC. :3
    private static List<List<Vertice>> combinationsOf3(float[][] pointMatrix){
        int elements = pointMatrix[0].length;
        
        if (elements < 3) return null;
        
        int i,j,k;
        int count = 0; // nCr, onde r=3  (Combinação de n elementos tomados 3 a 3)
        
        List<Vertice> um = new ArrayList<>();
        List<Vertice> dois = new ArrayList<>();
        List<Vertice> tres = new ArrayList<>();
        
        for (i=0; i<elements; i++){
            Vertice umV = new Vertice(pointMatrix[0][i], pointMatrix[1][i], pointMatrix[2][i]);
            for(j=i+1; j<elements; j++){
                Vertice doisV = new Vertice(pointMatrix[0][j], pointMatrix[1][j], pointMatrix[2][j]);
                for (k=j+1; k<elements; k++){
                    Vertice tresV = new Vertice(pointMatrix[0][k], pointMatrix[1][k], pointMatrix[2][k]);
                    count++;
                    
                    um.add(umV);
                    dois.add(doisV);
                    tres.add(tresV);
                }
            }
        }
        
        List<List<Vertice>> ret = new ArrayList();
        ret.add(um);
        ret.add(dois);
        ret.add(tres);
        return ret;
    }
    
    private static List<List<Vertice>> combinationsOf3(List<? extends Vertice> pointMatrix){
        int elements = pointMatrix.size();
        
        if (elements < 3) return null;
        
        int i,j,k;
        int count = 0; // nCr, onde r=3  (Combinação de n elementos tomados 3 a 3)
        
        List<Vertice> um   = new ArrayList<>();
        List<Vertice> dois = new ArrayList<>();
        List<Vertice> tres = new ArrayList<>();
        
        for (i=0; i<elements; i++){
            Vertice umV = pointMatrix.get(i);
            for(j=i+1; j<elements; j++){
                Vertice doisV = pointMatrix.get(j);
                for (k=j+1; k<elements; k++){
                    Vertice tresV = pointMatrix.get(k);
                    count++;
                    
                    um.add(umV);
                    dois.add(doisV);
                    tres.add(tresV);
                }
            }
        }
        
        List<List<Vertice>> ret = new ArrayList();
        ret.add(um);
        ret.add(dois);
        ret.add(tres);
        return ret;
    }
    
    private static Vertice buildPlane(Vertice p, Vertice q, Vertice r){        
        Vertice pq = new Vertice(q.getX()-p.getX(), q.getY()-p.getY(), q.getZ()-p.getZ());
        Vertice pr = new Vertice(r.getX()-p.getX(), r.getY()-p.getY(), r.getZ()-p.getZ());
        
        Vertice f = VMath.produtoVetorial(pq, pr);        
        
        float fatorD = (f.getX()*(-p.getX())) + (f.getY()*(-p.getY())) + (f.getZ()*(-p.getZ()));

        //Normalizar plano
        //https://github.com/nasa/World-Wind-Java/blob/master/WorldWind/src/gov/nasa/worldwind/geom/Plane.java
        double norma = Math.sqrt((f.getX()*f.getX()) + (f.getY()*f.getY()) + (f.getZ()*f.getZ()));

        Vertice ret = new Vertice((float) (f.getX()/norma),
                                  (float) (f.getY()/norma),
                                  (float) (f.getZ()/norma),
                                  (float) (  fatorD/norma)
        );
        
        return ret;
    }
    
    private static boolean planesAreEqual(float[] plane1, float[] plane2){
        Vertice v1 = new Vertice(plane1[0], plane1[1], plane1[2]);
        v1.setW(plane1[3]);
        VMath.normalizar(v1);
        
        Vertice v2 = new Vertice(plane2[0], plane2[1], plane2[2]);
        v2.setW(plane2[3]);
        VMath.normalizar(v2);
        
        return (v1.getW()==v2.getW()) //Comparar pelo fator e com o
            && (v1.getX()==v2.getX()) //uso de curto circuito
            && (v1.getY()==v2.getY()) //torna mais eficiente que começar
            && (v1.getZ()==v2.getZ()); //com x; já que planos paralelos teria tudo menos W igual.
    }
    
    private static boolean pointBelongsToPlane(Vertice plane, Vertice point){
        double res = (plane.getX()*point.getX()) // a * point.getX()
                   + (plane.getY()*point.getY()) // b * point.getY()
                   + (plane.getZ()*point.getZ()) // c * point.getZ()
                   + (plane.getW());             //+ (-d)
        
        return Math.abs(res)<=(0.00001); //Problemas com precisão de pontos flutuantes?
    }
    
    public static List<Face> attemptBuildingFromPlanes(CGObject object){
        if (object.getNumberOfPoints() < 3) return null;
        
        List<List<Vertice>> combinacoes = combinationsOf3(object.getPoints());
        
        List<Vertice> um   = combinacoes.get(0);
        List<Vertice> dois = combinacoes.get(1);
        List<Vertice> tres = combinacoes.get(2);
        
        int numberOfCombs  = um.size(),
            numberOfPoints = object.getNumberOfPoints();
        
        Vertice plane1;
        //System.out.println("COMS: " + numberOfCombs);
        HashSet<Integer> existingPlanes = new HashSet<>();
        List<Vertice> currentPlane;
        List<Face> listaDeFaces = new ArrayList<>();
        for (int i=0; i<numberOfCombs; i++){
            plane1 = buildPlane(um.get(i), dois.get(i), tres.get(i));

            if (existingPlanes.size()>0 && existingPlanes.contains(plane1.hashCode())){ 
                //Essa combinação é referente à um plano já testado? Se sim, pula
                continue;
            } 
            //Coloquei só Hash mesmo podendo dar falsos positivos (em relação à conter
            //planos), pois é uma alteração fácil que altera várias duplicadas de planos
            //orientados em sentidos diferentes (e.g. o "mesmo" plano só que um para cima
            //e outro para baixo)
            existingPlanes.add(plane1.hashCode());
            
            //Por que não adicionar os pontos do plane1 na face aqui?
            //...
            currentPlane = new ArrayList();
            
            for (int j=0; j<numberOfPoints; j++){
                Vertice local = object.get(j);

                if (pointBelongsToPlane(plane1, local)){
                    currentPlane.add(local);
                }
            }
            
            if (!currentPlane.isEmpty())
                listaDeFaces.add(new Face(currentPlane));
        }
        
        return listaDeFaces;
    }
    
    public static List<Face> attemptBuildingFromPlanes(List<Vertice> lista){
        if (lista.size() < 3) return null;

        PointObject pol = PointObject.build(lista);

        return attemptBuildingFromPlanes(pol);
    }

    /*public static void main(String...args){
        /*float[][] pol_mat = {
            {  30,  35,  25,  20,  30},
            {   2,   4,   3,   1,  10},
            {  25,  20,  18,  23,  (float) 22.5},
            {   1,   1,   1,   1,   1}
            //  A    B    C    D    E
        };*/
        
        /*float[][] pol_mat = {
            {  0,  00,   0,  00},
            {  0,  10,  00,  10},
            {  0,   0,  10,  10},
            {  1,   1,   1,   1}
            //  A    B    C    D    E
        };*/
        
        /*List<List<Vertice>> lista = combinationsOf3(pol_mat);
        
        // BEWARE:    5C3 = 120, então são 120 linhas de combinações!
        lista.forEach((listaInterna) -> {
            System.out.println(listaInterna);
        });*/
        
        /*Vertice p = new Vertice( 1,-2, 0);
        Vertice q = new Vertice( 3, 1, 4);
        Vertice r = new Vertice( 0,-1, 2);
        
        System.out.println(buildPlane(p, q, r));*/
        
        /*PointObject pol = new PointObject(pol_mat);
        List<Face> listaFaces = attemptBuildingFromPlanes(pol);
        
        for (Face f : listaFaces){
            System.out.println(f);
            MMath.printMatrix(f.getPoints());
        }*/
    //}
}
