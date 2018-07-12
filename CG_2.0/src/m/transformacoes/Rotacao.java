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
    private boolean lockChange = false;

    public Rotacao() {
    }
    
    public Rotacao(boolean lockChange) {
        this.lockChange = lockChange;
    }
    
    public void rotacaoZ(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            
            p.get(i).setAll( 
                (float) ((x*cos)-(y*sin)), 
                (float) ((x*sin)+(y*cos)),
                (float) (z)
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void rotacaoX(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);
            
            p.get(i).setAll( 
                (float) (x), 
                (float) ((y*cos)-(z*sin)),
                (float) ((y*sin)+(z*cos))
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void rotacaoY(double graus, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            double x = copy.getX(), y = copy.getY(), z = copy.getZ();
            double sin = Math.sin(graus);
            double cos = Math.cos(graus);

            p.get(i).setAll(
                (float) ((x*cos)+(z*sin)), 
                (float) (y),
                (float) (-(x*sin)+(z*cos))
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void rotacaoZ(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            rotacaoZ(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        rotacaoZ(graus, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void rotacaoX(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            rotacaoX(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        rotacaoX(graus, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void rotacaoY(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            rotacaoY(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        rotacaoY(graus, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);        
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }

    public boolean isLockChange() {
        return lockChange;
    }

    public void setLockChange(boolean lockChange) {
        this.lockChange = lockChange;
    }
    
    
}
