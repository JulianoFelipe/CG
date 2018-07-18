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
public class Escala {
    private boolean lockChange = false;

    public Escala() {
    }
    
    public Escala(boolean lockChange) {
        this.lockChange = lockChange;
    }

    public void escala(Eixo axis, double fator, CGObject p){
        switch (axis) {
            case Eixo_X:
                escalaX(fator, p);
                break;
            case Eixo_Y:
                escalaY(fator, p);
                break;
            case Eixo_Z:
                escalaZ(fator, p);
                break;
            case Eixo_XYZ:
                escalaUniforme(fator, p);
                break;
            case Eixo_XY:
                escala(fator, fator, 1, p);
                break;
            case Eixo_YZ:
                escala(1, fator, fator, p);
                break;
            case Eixo_XZ:
                escala(fator, 1, fator, p);
                break;
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
        }
    }
    
    public void escala(Eixo axis, double fator, CGObject p, final Vertice pontoFixo){

        switch (axis) {
            case Eixo_X:
                escalaX(fator, p, pontoFixo);
                break;
            case Eixo_Y:
                escalaY(fator, p, pontoFixo);
                break;
            case Eixo_Z:
                escalaZ(fator, p, pontoFixo);
                break;
            case Eixo_XYZ:
                escalaUniforme(fator, p, pontoFixo);
                break;
            case Eixo_XY:
                escala(fator, fator, 1, p, pontoFixo);
                break;
            case Eixo_YZ:
                escala(1, fator, fator, p, pontoFixo);
                break;
            case Eixo_XZ:
                escala(fator, 1, fator, p, pontoFixo);
                break;
            default:
                throw new UnsupportedOperationException("NÃ£o implementado");
        }
    }
    
    public void escalaX(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll(  
                (float) (copy.getX() * fator), 
                (float) (copy.getY()),
                (float) (copy.getZ())
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaY(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll( 
                (float) (copy.getX()), 
                (float) (copy.getY() * fator),
                (float) (copy.getZ())
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaZ(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll( 
                (float) (copy.getX()), 
                (float) (copy.getY()),
                (float) (copy.getZ() * fator)
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escala(double fatorX, double fatorY, double fatorZ, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll(  
                (float) (copy.getX() * fatorX), 
                (float) (copy.getY() * fatorY),
                (float) (copy.getZ() * fatorZ)
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaUniforme(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll(  
                (float) (copy.getX() * fator), 
                (float) (copy.getY() * fator),
                (float) (copy.getZ() * fator)
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaX(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            escalaX(fator, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        escalaX(fator, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaY(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            escalaY(fator, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        escalaY(fator, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaZ(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            escalaZ(fator, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        escalaZ(fator, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escalaUniforme(double fator, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            escalaUniforme(fator, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        escalaUniforme(fator, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void escala(double fatorX, double fatorY, double fatorZ, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            escala(fatorX, fatorY, fatorZ, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        escala(fatorX, fatorY, fatorZ, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
}
