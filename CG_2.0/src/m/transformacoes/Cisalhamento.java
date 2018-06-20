/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import m.Eixo;
import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class Cisalhamento {
    public CGObject cisalhamento(Eixo axis, double fator, CGObject p){
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
    
    public CGObject cisalhamento(Eixo axis, double fator, CGObject p, final Vertice pontoFixo){
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
    
    public CGObject cisalhamentoX(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX() + (fator*copy.getY())), 
                (float) (copy.getY()),
                (float) (copy.getZ())
            );
        }
        return p;
    }
    
    public CGObject cisalhamentoY(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX()), 
                (float) (copy.getY() + (fator*copy.getX())),
                (float) (copy.getZ())
            );
        }
        return p;
    }
    
    public CGObject cisalhamentoZ(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX()), 
                (float) (copy.getY()),
                (float) (copy.getY() + (fator*copy.getX()))
            );
        }
        return p;
    }
    
    public CGObject cisalhamentoX(double graus, CGObject p, final Vertice pontoFixo){    
        if (pontoFixo == null) return cisalhamentoX(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoX(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
        
    public CGObject cisalhamentoY(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return cisalhamentoY(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public CGObject cisalhamentoZ(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return cisalhamentoZ(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = cisalhamentoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
        
    
}
