/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

//import Model.Poligono;

import m.anderson.Poligono;
import m.anderson.Vertice;

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
    public static boolean proximoDeQualquerVerticeDoPoligono(Poligono p, Vertice v){
        return p.getPointList().stream().map((ver) -> VMath.distancia(ver, v)).anyMatch((d) -> (d < CLOSE_THRESHOLD));
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
    public static Vertice verticeProximoDeQualquerVerticeDoPoligono(Poligono p, Vertice v){
        for (Vertice ver : p.getPointList()){
            if (VMath.distancia(ver, v) < CLOSE_THRESHOLD){
                return ver;
            }
        }
        return null;
    }
}
