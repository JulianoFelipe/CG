/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.poligonosEsp;

import Model.Poligono;
import Model.Vertice;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class Nregular extends Poligono {
    private int noLados;
    private int radius;
    private Vertice center;

    private List<Vertice> buildNsided(){
        List<Vertice> lista = new ArrayList();
        
        double x,y;
        double angulo = 0.;
        double increment = 2*Math.PI/ noLados;
        for (int i=0; i<noLados; i++, angulo += increment){
            x = center.getX() + ( radius * Math.cos(angulo));
            y = center.getY() + ( radius * Math.sin(angulo));
            
            lista.add(new Vertice((float) x, 
                                  (float) y));
        }
        
        return lista;
    }
    
    public Nregular(int noLados, int radius, Vertice center) {
        this.radius = radius;
        this.noLados = noLados;
        this.center = center;
        
        super.addAllVertices(buildNsided());
    }
    
    @Override
    public String toString() {
        return "Polígono Regular {" + super.getVertices() + '}';
    }
}
