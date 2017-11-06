/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Transformações;

import Model.Poligono;
import Model.Vertice;

/**
 *
 * @author JFPS
 */
public class Rotacao{
    public Poligono rotacaoZ(double graus, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            p.getVertices().get(i).setX((float) ((x*cos)-(y*sin)));
            p.getVertices().get(i).setY((float) ((x*sin)+(y*cos)));
        }
        return p;
    }
    
    public Poligono rotacaoX(double graus, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            p.getVertices().get(i).setY((float) ((y*cos)-(z*sin)));
            p.getVertices().get(i).setZ((float) ((y*sin)+(z*cos)));
        }
        return p;
    }
    
    public Poligono rotacaoY(double graus, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            p.getVertices().get(i).setX((float) ((x*cos)+(z*sin)));
            p.getVertices().get(i).setZ((float) (-(x*sin)+(z*cos)));
        }
        return p;
    }
    
    public Poligono rotacaoZ(double graus, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoZ(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoZ(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
    
    public Poligono rotacaoX(double graus, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoX(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoX(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
    
    public Poligono rotacaoY(double graus, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoY(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
}
