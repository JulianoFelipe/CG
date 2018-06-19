/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import m.Eixo;
import m.anderson.CGObject;
import m.anderson.Vertice;

/**
 *
 * @author JFPS
 */
public class Escala {
    public CGObject escala(Eixo axis, double fator, CGObject p){
        switch (axis) {
            case Eixo_X:
                return escalaX(fator, p);
            case Eixo_Y:
                return escalaY(fator, p);
            case Eixo_Z:
                return escalaZ(fator, p);
            case Eixo_XYZ:
                return escalaZ(fator, p);
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
        }
    }
    
    public CGObject escala(Eixo axis, double fator, CGObject p, final Vertice pontoFixo){
        switch (axis) {
            case Eixo_X:
                return escalaX(fator, p, pontoFixo);
            case Eixo_Y:
                return escalaY(fator, p, pontoFixo);
            case Eixo_Z:
                return escalaZ(fator, p, pontoFixo);
            case Eixo_XYZ:
                return escalaZ(fator, p, pontoFixo);
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
        }
    }
    
    public CGObject escalaX(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX() * fator), 
                (float) (copy.getY()),
                (float) (copy.getZ())
            );
        }
        return p;
    }
    
    public CGObject escalaY(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX()), 
                (float) (copy.getY() * fator),
                (float) (copy.getZ())
            );
        }
        return p;
    }
    
    public CGObject escalaZ(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX()), 
                (float) (copy.getY()),
                (float) (copy.getZ() * fator)
            );
        }
        return p;
    }
    
    public CGObject escalaUniforme(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            
            p.setPoint(i, 
                (float) (copy.getX() * fator), 
                (float) (copy.getY() * fator),
                (float) (copy.getZ() * fator)
            );
        }
        return p;
    }
    
    public CGObject escalaX(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaX(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaX(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public CGObject escalaY(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaY(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaY(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public CGObject escalaZ(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaZ(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaZ(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
    
    public CGObject escalaUniforme(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return escalaZ(fator, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = escalaUniforme(fator, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        return p;
    }
}
