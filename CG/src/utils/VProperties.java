/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Aresta;
import Model.Poligono;
import Model.Vertice;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class VProperties {
    public static Vertice getMaxVertex(List<Poligono> lista){
        float maxX = Float.MIN_VALUE;
        float maxY = Float.MIN_VALUE;
        
        for (Poligono pol : lista){
            for (Vertice ver : pol.getVertices()){
                if (ver.getX() > maxX)
                    maxX = ver.getX();
                
                if(ver.getY() > maxY)
                    maxY = ver.getY();
            }
        }
        
        return new Vertice((float) maxX, (float) maxY);
    }
    
    public static List<Aresta> calculateScanLines(Poligono p){
        List<Aresta> edgeList = buildEdges(p);
        List<Aresta> paintLines = new ArrayList();
        
        float[] minMax = findMinMaxOfPolygon(p);
        
        int min = (int) minMax[0];
        int max = (int) minMax[1];
        
        for (int i=min; i<max; i++){
            
            //for() todas arestas em Edge list (Scan atual está entre max e min de cada aresta)
            //Verifica os pontos de intersecção
            //Faz arestas para pintura.
            //http://www.geeksforgeeks.org/scan-line-polygon-filling-using-opengl-c/
        }
    }
    
    private static float[] findMinMaxOfPolygon(Poligono p){
        float min = Float.MIN_VALUE;
        float max = Float.MAX_VALUE;
        
        for (Vertice v : p.getVertices()){
            float y = v.getY();
            if (y < min)
                min = y;
            if(y > max)
                max = y;
        }
        
        float[] ret = {min,max};
        
        return ret;
    }
    
    private static List<Aresta> buildEdges(Poligono p){
        List<Vertice> listaVertices = p.getVertices();
        List<Aresta> lista = new ArrayList();
        
        int i;
        for (i=0; i<listaVertices.size()-1; i++){
            Aresta temp = new Aresta(listaVertices.get(i), listaVertices.get(i+1));
            if (!VMath.isLineExactlyHorizontal(temp))
                lista.add(temp);
        }
        
        lista.add(new Aresta(listaVertices.get(i), listaVertices.get(0)));
        return lista;
    }
}
