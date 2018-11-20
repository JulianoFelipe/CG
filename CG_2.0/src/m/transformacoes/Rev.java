/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import java.util.ArrayList;
import java.util.List;
import m.Eixo;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.IndexList;

/**
 *
 * @author JFPS
 */
public class Rev {

    private List<Vertice> listaDePontos;
    private int secoes;
    private double angulo;
    private Eixo eixo;

    public Rev(List<Vertice> listaDePontos, int secoes, double angulo, Eixo eixo) {
        this.listaDePontos = listaDePontos;
        this.secoes = secoes;
        this.angulo = angulo;
        this.eixo = eixo;
    }

    public HE_Poliedro getPoli() {
        int noPoints = listaDePontos.size()*secoes;
        double teta = (angulo * (Math.PI / 180)) / (secoes-1);
        float[][] newPoints = new float[4][noPoints];
        for (int i=0; i<listaDePontos.size(); i++){
            Vertice point = listaDePontos.get(i);
            newPoints[0][i] = point.getX();
            newPoints[1][i] = point.getY();
            newPoints[2][i] = point.getZ();
            newPoints[3][i] = 1;
        }
        
        for (int i=1; i<secoes; i++){
            float[] toRotate = new float[]{
                newPoints[0][i-1], newPoints[1][i-1], newPoints[2][i-1] 
            }; //Pontos para serem rotacionados
            
            switch (eixo) {
                case Eixo_X: destructiveRotateX(toRotate, teta); break;
                case Eixo_Y: destructiveRotateY(toRotate, teta); break;
                case Eixo_Z: destructiveRotateZ(toRotate, teta); break;
                default: throw new IllegalArgumentException("Eixo não esperado para revolução.");
            }
            
            newPoints[0][i] = toRotate[0];
            newPoints[1][i] = toRotate[1];
            newPoints[2][i] = toRotate[2];
        }
        
        //Pontos todos devem estar rotacionados, agora deve-se construir a lista de faces
        
        /*int[] sface0 = {0, 3, 2, 1};
        int[] sface1 = {0, 1, 4};
        int[] sface2 = {1, 2, 4};
        int[] sface3 = {2, 3, 4};
        int[] sface4 = {3, 0, 4};
        List<IndexList> faces2 = new ArrayList();
        faces2.add(new IndexList(sface0));*/

        return null;
    }

    public void destructiveRotateX(float[] points, double teta){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);

        points[0] = x;
        points[1] = (float) ((y*cos)-(z*sin));
        points[2] = (float) ((y*sin)+(z*cos));
    }
    
    public void destructiveRotateY(float[] points, double teta){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);
        
        points[0] = (float) ((x*cos)+(z*sin));
        points[1] = y;
        points[2] = (float) (-(x*sin)+(z*cos));
    }
    
    public void destructiveRotateZ(float[] points, double teta){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);

        points[0] = (float) ((x*cos)-(y*sin));
        points[1] = (float) ((x*sin)+(y*cos));
        points[2] = (z);
    }
}

/*

float[][] pol_mat2 = {
    {  40,  45,  35,  30,  40},
    {   3,   5,   4,   2,  20},
    {  35,  30,  28,  33,  (float) 32.5},
    {   1,   1,   1,   1,   1}
    //  A    B    C    D    E
};

int[] sface0 = {0, 3, 2, 1};
int[] sface1 = {0, 1, 4};
int[] sface2 = {1, 2, 4};
int[] sface3 = {2, 3, 4};
int[] sface4 = {3, 0, 4};
List<IndexList> faces2 = new ArrayList();
faces2.add(new IndexList(sface0));
faces2.add(new IndexList(sface1));
faces2.add(new IndexList(sface2));
faces2.add(new IndexList(sface3));
faces2.add(new IndexList(sface4));

HE_Poliedro poli2 = new HE_Poliedro(pol_mat2, faces2);
mundo.addObject(poli2);

*/
