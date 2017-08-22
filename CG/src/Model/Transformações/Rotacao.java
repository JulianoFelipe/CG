/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Transformações;

import Model.Eixo;
import Model.Poligono;
import Model.Vertice;

/**
 *
 * @author JFPS
 */
public class Rotacao{
    public Poligono rotacao(double graus, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            double x = copy.getX(), y = copy.getY();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            p.getVertices().get(i).setX((float) ((x*cos)-(y*sin)));
            p.getVertices().get(i).setY((float) ((x*sin)+(y*cos)));
        }
        return p;
    }
    
    public Poligono rotacao(double graus, Poligono p, Vertice pontoFixo){
        int vertices = p.getVertices().size();
        
        Translacao t = new Translacao();
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), 0, p);
        
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            double x = copy.getX(), y = copy.getY();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            p.getVertices().get(i).setX((float) ((x*cos)-(y*sin)));
            p.getVertices().get(i).setY((float) ((x*sin)+(y*cos)));
        }
        
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), 0, p);
        return p;
    }
}
