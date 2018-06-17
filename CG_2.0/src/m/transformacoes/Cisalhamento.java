/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import m.Eixo;
import m.anderson.Poligono;
import m.anderson.Vertice;

/**
 *
 * @author JFPS
 */
public class Cisalhamento {
    public Poligono cisalhamento(Eixo axis, double fator, Poligono p){
        switch (axis) {
            case Eixo_X:
                return cisalhamentoX(fator, p);
            case Eixo_Y:
                return cisalhamentoY(fator, p);
            case Eixo_Z:
                return cisalhamentoZ(fator, p);
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public Poligono cisalhamento(Eixo axis, double fator, Poligono p, final Vertice pontoFixo){
        switch (axis) {
            case Eixo_X:
                return cisalhamentoX(fator, p, pontoFixo);
            case Eixo_Y:
                return cisalhamentoY(fator, p, pontoFixo);
            case Eixo_Z:
                return cisalhamentoZ(fator, p, pontoFixo);
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public Poligono cisalhamentoX(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setX((float) (copy.getX() + (fator*copy.getY())));
        }
        return p;
    }
    
    public Poligono cisalhamentoY(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setY((float) (copy.getY() + (fator*copy.getX())));
        }
        return p;
    }
    
    public Poligono cisalhamentoZ(double fator, Poligono p){
        int vertices = p.getVertices().size();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getVertices().get(i);
            p.getVertices().get(i).setZ((float) (copy.getY() + (fator*copy.getX())));
        }
        return p;
    }
    
    public Poligono cisalhamentoX(double graus, Poligono p, final Vertice pontoFixo){    
        if (pontoFixo == null) return cisalhamentoX(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoX(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
        
    public Poligono cisalhamentoY(double graus, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return cisalhamentoY(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public Poligono cisalhamentoZ(double graus, Poligono p, final Vertice pontoFixo){
        if (pontoFixo == null) return cisalhamentoZ(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
        
    
}
