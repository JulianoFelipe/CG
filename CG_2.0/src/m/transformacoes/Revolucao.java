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
public class Revolucao {

    private final List<Vertice> listaDePontos;
    private final int secoes;
    private final double angulo;
    private final Eixo eixo;

    public Revolucao(List<Vertice> listaDePontos, int secoes, double angulo, Eixo eixo) {
        this.listaDePontos = listaDePontos;
        this.secoes = secoes;
        this.angulo = angulo;
        this.eixo = eixo;
    }

    public HE_Poliedro getPoli() {
        Vertice center = getCenter();
        //System.out.println("Lista: " + listaDePontos);
        //System.out.println("LISTA DE PONTOS: " + listaDePontos.size());
        int pontosPorPerfil = listaDePontos.size(); 
        int noPoints = pontosPorPerfil*secoes;
        double teta = (angulo * (Math.PI / 180)) / (secoes-1);
        float[][] newPoints = new float[4][noPoints];
        for (int i=0; i<listaDePontos.size(); i++){
            Vertice point = listaDePontos.get(i);
            newPoints[0][i] = point.getX();
            newPoints[1][i] = point.getY();
            newPoints[2][i] = point.getZ();
            newPoints[3][i] = 1;
        }
        
        for (int i=pontosPorPerfil; i<noPoints; i++){
            float[] toRotate = new float[]{
                newPoints[0][i-pontosPorPerfil], newPoints[1][i-pontosPorPerfil], newPoints[2][i-pontosPorPerfil] 
            }; //Pontos para serem rotacionados
            
            switch (eixo) {
                case Eixo_X: destructiveRotateX(toRotate, teta, center); break;
                case Eixo_Y: destructiveRotateY(toRotate, teta, center); break;
                case Eixo_Z: destructiveRotateZ(toRotate, teta, center); break;
                default: throw new IllegalArgumentException("Eixo não esperado para revolução.");
            }
            
            newPoints[0][i] = toRotate[0];
            newPoints[1][i] = toRotate[1];
            newPoints[2][i] = toRotate[2];
            newPoints[3][i] = 1;
        } //Pontos todos devem estar rotacionados, agora deve-se construir a lista de faces
        
        //Todas as faces quadradas no "meio", ou seja, sem contar as que "voltam para o começo".
        List<IndexList> faces = new ArrayList();
        int i,j, facesQdrNormais = (secoes-1)*(pontosPorPerfil-1);
        for (i=0, j=0; j<facesQdrNormais; i++, j++){
            //System.out.print("J: " + j + " JMod: " + ( j%(pontosPorPerfil-1)));
            if ((j>0) && j%(pontosPorPerfil-1) == 0) ++i;
            int[] face = new int[]{i, i+pontosPorPerfil, i+pontosPorPerfil+1, i+1};
            //System.out.println("   Face (" + j + ")" + Arrays.toString(face));
            faces.add(new IndexList(face));
        }
        
        //Faces que "voltam" para o começo, para evitar várias divisões de móodulo nos laços anteriores
        int offset = (secoes-1)*pontosPorPerfil;
        for (j=0; j<pontosPorPerfil-1; j++){
            int[] face = new int[]{offset+j, j, j+1, offset+j+1};
            //System.out.println("Face Offs (" + j + ")" + Arrays.toString(face));
            faces.add(new IndexList(face));   
        }
        
        //Faces de cima e de baixo para "fechar" o objeto
        int[] faceAboveOrBelow = new int[secoes];
        for (i=0, j=secoes-1; j>=0; j--, i+=pontosPorPerfil){ //Face de baixo é sentido horário
            faceAboveOrBelow[j] = i;
        } faces.add(new IndexList(faceAboveOrBelow));
        //System.out.println("Face Below" + Arrays.toString(faceAboveOrBelow));
        for (i=pontosPorPerfil-1, j=0; j<secoes; j++, i+=pontosPorPerfil){ //Face de baixo é sentido horário
            faceAboveOrBelow[j] = i;
        } faces.add(new IndexList(faceAboveOrBelow));
        //System.out.println("Face Above" + Arrays.toString(faceAboveOrBelow));
        
        /*for (float[] newPoint : newPoints) {
            System.out.println(Arrays.toString(newPoint));
        }*/
        //System.out.println("\n\n\nBUILD REV");
        return new HE_Poliedro(newPoints, faces);
    }

    public void destructiveRotateX(float[] points, double teta, Vertice center){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);

        //y -= center.getY();
        //z -= center.getZ();
        
        points[0] = x;
        points[1] = (float) ((y*cos)-(z*sin));
        points[2] = (float) ((y*sin)+(z*cos));
        
        //y += center.getY();
        //z += center.getZ();
    }
    
    public void destructiveRotateY(float[] points, double teta, Vertice center){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);
        
        //x -= center.getX();
        //z -= center.getZ();
        
        points[0] = (float) ((x*cos)+(z*sin));
        points[1] = y;
        points[2] = (float) (-(x*sin)+(z*cos));
        
        //x += center.getX();
        //z += center.getZ();
    }
    
    public void destructiveRotateZ(float[] points, double teta, Vertice center){
        float x = points[0], y = points[1], z = points[2];
        double sin = Math.sin(teta);
        double cos = Math.cos(teta);

        //x -= center.getX();
        //y -= center.getY();
        
        points[0] = (float) ((x*cos)-(y*sin));
        points[1] = (float) ((x*sin)+(y*cos));
        points[2] = (z);
        
        //x += center.getX();
        //y += center.getY();
    }
    
    public Vertice getCenter(){
        float x=0, y=0, z=0;
        int counter = 0;
        for (Vertice v : listaDePontos){
            x += v.getX();
            y += v.getY();
            z += v.getZ();
            ++counter;
        }
        
        return new Vertice(x/counter, y/counter, z/counter);
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
