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
import java.util.Collections;
import java.util.HashSet;
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
        
        for (int yScan=min; yScan<=max; yScan++){        
            
            //for() todas arestas em Edge list (Scan atual está entre max e min de cada aresta)
            //Verifica os pontos de intersecção
            //Faz arestas para pintura.
            //http://www.geeksforgeeks.org/scan-line-polygon-filling-using-opengl-c/
            //https://stackoverflow.com/questions/24469459/scanline-algorithm-how-to-calculate-intersection-points
            
            HashSet<Vertice> points = new HashSet();
            float x1, x2, y1, y2;
            double deltax, deltay, x;
            
            for (int ps = 0; ps <edgeList.size() - 1; ps++) {
                Aresta a = edgeList.get(ps);
                x1 = a.getvInicial().getX();
                y1 = a.getvInicial().getY();
                x2 = a.getvFinal().getX();
                y2 = a.getvFinal().getY();

                deltax = x2 - x1;
                deltay = y2 - y1;

                int roundedX;
                x = x1 + deltax / deltay * (yScan - y1);
                roundedX = (int) Math.round(x);
                if ((y1 <= yScan && y2 > yScan) || (y2 <= yScan && y1 > yScan)) {
                    points.add(new Vertice(roundedX, yScan));
                }
            }
            
            List<Vertice> copyPoints = new ArrayList(points);
            //you have to sort the intersection points of every scanline from the lowest x value to thr highest
            Collections.sort(copyPoints, (Vertice o1, Vertice o2) -> {
                return Float.compare(o1.getX(), o2.getX());
            });
            
            for (int i=0; i<points.size()-1; i+=2)
                paintLines.add(new Aresta(copyPoints.get(i), copyPoints.get(i+1)));
        }
        
        return paintLines;
    }
    
    private static void addAndRemoveFromScan(int scanLine, List<Aresta> activeList, List<Aresta> nonProcessedList){
        for (int i=0; i<activeList.size(); i++){
            
        }
        
        for (int i=0; i<nonProcessedList.size(); i++){
            
        }
    }
    
    public static float[] findMinMaxOfPolygon(Poligono p){
        float min = Float.MAX_VALUE;
        float max = Float.MIN_VALUE;
        
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
