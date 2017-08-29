/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Aresta;
import Model.Poligono;
import Model.Vertice;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class ScanLineFill {
    private Graphics g;
    private ArrayList<Vertice> intersectsList;
    
    public ScanLineFill(Graphics g) {
        intersectsList = new ArrayList<>();
        this.g = g;
    }

    public void setG(Graphics g) {
        this.g = g;
    }
    
    /*public void scanLineFill(Poligono p){
        intersectsList.clear();
        
        buildIntersectionList(p);
        
        for(int i=0; i<intersectsList.size(); i+=2)
            paintLine(intersectsList.get(i), intersectsList.get(i+1));
    }*/
    
    public void scanLineFill(Poligono p){
        List<Aresta> paint = p.getPaintLines();
        
        paint.forEach((a) -> {
            paintLine(a.getvInicial(), a.getvFinal());
        });
    }
    
    private void paintLine(Vertice begin, Vertice end){
        g.drawLine((int)begin.getX(), (int)begin.getY(),
                   (int)  end.getX(), (int)  end.getY());
    }
    
    private void buildIntersectionList(Poligono p){
        List<Aresta> edgeList = buildEdges(p);
    }
    
    public float[] findMaxMinY(Poligono p){
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
    
    public List<Aresta> buildEdges(Poligono p){
        List<Vertice> listaVertices = p.getVertices();
        List<Aresta> lista = new ArrayList();
        
        int i;
        for (i=0; i<listaVertices.size()-1; i++)
            lista.add(new Aresta(listaVertices.get(i), listaVertices.get(i+1)));
        
        lista.add(new Aresta(listaVertices.get(i), listaVertices.get(0)));
        return lista;
    }
}
