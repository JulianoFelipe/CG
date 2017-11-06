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
            case Eixo_Z:
                return escalaZ(fator, p);
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
        }
    }
    
    public Poligono escala(Eixo axis, double fator, Poligono p, final Vertice pontoFixo){
        switch (axis) {
            case Eixo_X:
                return escalaX(fator, p, pontoFixo);
            case Eixo_Y:
                return escalaY(fator, p, pontoFixo);
            case Eixo_Z:
                return escalaZ(fator, p, pontoFixo);
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
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
    
    public Poligono escalaZ(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setZ((float) (copy.getZ() * fator));
        }
        return p;
    }
    
    public Poligono escalaX(double fator, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaX(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaX(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public Poligono escalaY(double fator, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaY(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaY(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public Poligono escalaZ(double fator, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaZ(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaZ(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
}
