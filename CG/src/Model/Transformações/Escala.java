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
public class Escala {
    public Poligono escala(Eixo axis, double fator, Poligono p){
        switch (axis) {
            case Eixo_X:
                return escalaX(fator, p);
            case Eixo_Y:
                return escalaY(fator, p);
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public Poligono escala(Eixo axis, double fator, Poligono p, final Vertice pontoFixo){
        switch (axis) {
            case Eixo_X:
                return escalaX(fator, p, pontoFixo);
            case Eixo_Y:
                return escalaY(fator, p, pontoFixo);
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public Poligono escalaX(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setX((float) (copy.getX() * fator));
        }
        return p;
    }
    
    public Poligono escalaY(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setY((float) (copy.getY() * fator));
        }
        return p;
    }
    
    public Poligono escalaX(double fator, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaX(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), 0, p);
        p = escalaX(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), 0, p);
        return p;
    }
    
    public Poligono escalaY(double fator, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaY(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), 0, p);
        p = escalaY(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), 0, p);
        return p;
    }
}
