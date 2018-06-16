/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.anderson;

import java.awt.Color;
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

    private List<Vertice> buildNsided(double pos){
        List<Vertice> lista = new ArrayList();
        
        double x,y;
        double angulo = pos;
        double increment = 2*Math.PI/ noLados;
        for (int i=0; i<noLados; i++, angulo += increment){
            x = center.getX() + ( radius * Math.cos(angulo));
            y = center.getY() + ( radius * Math.sin(angulo));
            
            lista.add(new Vertice((float) x, 
                                  (float) y, 0));
        }
        
        return lista;
    }
    
    public Nregular(int noLados, int radius, Vertice center) {
        this(noLados, radius, center, 0.);
    }
    
    public Nregular(int noLados, int radius, Vertice center, double pos){
        super(); //Default color and stuff
        this.radius = radius;
        this.noLados = noLados;
        this.center = center;
        
        super.addAllVertices(buildNsided(pos));
    }

    public Nregular(int noLados, int radius, Vertice center, Color cor, boolean corBorda){
        this(noLados, radius, center, 0.);
        
        if (corBorda)
            super.setCorBorda(cor);
        else
            super.setCorFundo(cor);
    }
    
    public Nregular(int noLados, int radius, Vertice center, Color corBorda, Color corFundo){
        this(noLados, radius, center, 0.);
        super.setCorBorda(corBorda);
        super.setCorFundo(corFundo);
    }

    public Nregular(Nregular nregular){
        this(nregular.getNoLados(), nregular.getRadius(), nregular.getCenter(), nregular.getCorBorda(), nregular.getCorFundo()); 
        this.setID(nregular.getID()); //Ugly stuff
    }
    
    public int getNoLados() {
        return noLados;
    }

    public int getRadius() {
        return radius;
    }

    public Vertice getCenter() {
        return center;
    }
       
    
    
    @Override
    public String toString() {
        return "Polígono Regular {Lados=" + super.getVertices().size() + ", Centro=" + center + ", Raio=" + radius + '}';
    }

    /*@Override
    public Vertice getCentroideDaMedia() {
        return center;
    }*/
    
    
}
