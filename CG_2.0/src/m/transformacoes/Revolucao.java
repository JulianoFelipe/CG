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
    private List<Vertice> listaDePontos;
    private int    secoes;
    private double angulo;
    private Eixo   eixo;

    public Revolucao(List<Vertice> listaDePontos, int secoes, double angulo, Eixo eixo) {
        this.listaDePontos = listaDePontos;
        this.secoes = secoes;
        this.angulo = angulo;
        this.eixo = eixo;
    }
    
    public HE_Poliedro getPoli(){
        
        //Anderson, se tu quiser acessar a enum de eixos, é assim:
        switch(eixo){
            case Eixo_X:
                //Faz algo com eixo X
                break;
            case Eixo_Y:
                //Faz algo com eixo Y
                break;
            case Eixo_Z:
                //Faz algo com eixo X
                break;
            default: throw new IllegalArgumentException("Eixo não esperado para revolução.");
        }
        
        float[][] pol_mat = {
            {  30,  35,  25,  20,  30},
            {   2,   4,   3,   1,  10},
            {  25,  20,  18,  23,  (float) 22.5},
            {   1,   1,   1,   1,   1}
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
        
        HE_Poliedro poli = new HE_Poliedro(pol_mat, faces);
        
        return poli;
    }
}
