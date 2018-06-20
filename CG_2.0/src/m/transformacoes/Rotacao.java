/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class Rotacao{
    public CGObject rotacaoZ(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            
            p.setPoint(i, 
                (float) ((x*cos)-(y*sin)), 
                (float) ((x*sin)+(y*cos)),
                (float) (z)
            );
        }
        return p;
    }
    
    public CGObject rotacaoX(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            
            p.setPoint(i, 
                (float) (x), 
                (float) ((y*cos)-(z*sin)),
                (float) ((y*sin)+(z*cos))
            );
        }
        return p;
    }
    
    public CGObject rotacaoY(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.getPoint(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);

            p.setPoint(i, 
                (float) ((x*cos)+(z*sin)), 
                (float) (y),
                (float) (-(x*sin)+(z*cos))
            );
        }
        return p;
    }
    
    public CGObject rotacaoZ(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoZ(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoZ(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
    
    public CGObject rotacaoX(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoX(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoX(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
    
    public CGObject rotacaoY(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null) return rotacaoY(graus, p);
        Translacao t = new Translacao();
        p = t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        p = rotacaoY(graus, p);
        p = t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        return p;
    }
}
