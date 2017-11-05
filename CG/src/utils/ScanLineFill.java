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
public class ScanLineFill {
    private final Poligono p;
    private final List<Aresta> activeEdge;
    private List<Aresta> nonProcessedEdges;

    public ScanLineFill(Poligono poligono) {
        activeEdge = new ArrayList<>();
        this.p = poligono;
    }
    
    private void initialize(int firstScanLine){
        for(int i=0; i<nonProcessedEdges.size(); i++){
            Aresta a = nonProcessedEdges.get(i);
            if (firstScanLine >= a.getMinY() &&
                firstScanLine <= a.getMaxY()){
                activeEdge.add(a);
                nonProcessedEdges.remove(i);
            }
        }
    }
    
    /**
     * Coloca arestas na lista de arestas ativas,
     * retira da mesma lista, e retira da lista de
     * não processadas conforme scan line.
     * 
     * @param scanline 
     */
    private void updateFromScan(int scanline){
        for(int i=0; i<activeEdge.size(); i++){
            Aresta a = activeEdge.get(i);
            if (scanline <  a.getMinY() ||
                scanline >  a.getMaxY()){
                activeEdge.remove(i);
            }
        }
        
        for(int i=0; i<nonProcessedEdges.size(); i++){
            Aresta a = nonProcessedEdges.get(i);
            if (scanline >  a.getMinY() &&
                scanline <= a.getMaxY()){
                activeEdge.add(a);
                nonProcessedEdges.remove(i);
            }
        }
    }
    
    public List<Aresta> calculateScans(){
        nonProcessedEdges = buildEdges();
        
        List<Aresta> paintLines = new ArrayList();
        
        float[] minMax = VProperties.findMinMaxOfPolygon(p);
        
        int min = (int) minMax[0];
        int max = (int) minMax[1];
        initialize(min);
        
        for (int yScan=min; yScan<=max; yScan++){        
            //http://www.geeksforgeeks.org/scan-line-polygon-filling-using-opengl-c/
            //https://stackoverflow.com/questions/13807343/pixel-overlap-with-polygon-efficient-scanline-type-algorithm?rq=1
            
            HashSet<Vertice> points = new HashSet();           
            
            byte parity = 0;
            for (int ps = 0; ps <activeEdge.size(); ps++) {
                                
                Aresta current = activeEdge.get(ps);
                double x = current.getInterceptX();
                int roundedX;

                
                          
                roundedX = (int) Math.round(x);                
                double y1=current.getvInicial().getY(), y2=current.getvFinal().getY();
                if ((y1 <= yScan && y2 > yScan) || (y2 <= yScan && y1 > yScan)) {
                    points.add(new Vertice(roundedX, yScan));
                }
                
                double inverseSlope = Math.abs(1/current.getSlope());
                if (Double.isFinite(inverseSlope)){
                    x += inverseSlope;
                    current.setInterceptX(x);
                }
                parity = 1;
            }
            
            List<Vertice> copyPoints = new ArrayList(points);

            //you have to sort the intersection points of every scanline from the lowest x value to thr highest
            Collections.sort(copyPoints, (Vertice o1, Vertice o2) -> {
                return Float.compare(o1.getX(), o2.getX());
            });
            
            for (int i=0; i<copyPoints.size()-1; i+=2)
                paintLines.add(new Aresta(copyPoints.get(i), copyPoints.get(i+1)));
            
            updateFromScan(yScan+1);
        }
        return paintLines;
    }
    
    /**
     * Monta todas as arestas que não são horizontais do poligono
     * 
     * @param p
     * @return 
     */
    private List<Aresta> buildEdges(){
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
