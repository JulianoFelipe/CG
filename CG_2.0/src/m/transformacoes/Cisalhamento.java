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
    private boolean lockChange = false;

    public Cisalhamento() {
    }
    
    public Cisalhamento(boolean lockChange) {
        this.lockChange = lockChange;
    }
    
    public void cisalhamento(Eixo axis, double fator, CGObject p){
        switch (axis) {
            case Eixo_X:
                cisalhamentoX(fator, p);
                break;
            case Eixo_Y:
                cisalhamentoY(fator, p);
                break;
            case Eixo_Z:
                cisalhamentoZ(fator, p);
                break;
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public void cisalhamento(Eixo axis, double fator, CGObject p, final Vertice pontoFixo){
        switch (axis) {
            case Eixo_X:
                cisalhamentoX(fator, p, pontoFixo);
                break;
            case Eixo_Y:
                cisalhamentoY(fator, p, pontoFixo);
                break;
            case Eixo_Z:
                cisalhamentoZ(fator, p, pontoFixo);
                break;
            default:
                throw new UnsupportedOperationException("Não implementado");
        }
    }
    
    public void cisalhamentoX(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll( 
                (float) (copy.getX() + (fator*copy.getY())), 
                (float) (copy.getY()),
                (float) (copy.getZ())
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void cisalhamentoY(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll( 
                (float) (copy.getX()), 
                (float) (copy.getY() + (fator*copy.getX())),
                (float) (copy.getZ())
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void cisalhamentoZ(double fator, CGObject p){
        int vertices = p.getNumberOfPoints();
        for (int i=0; i<vertices; i++){
            Vertice copy = p.get(i);
            
            p.get(i).setAll(  
                (float) (copy.getX()), 
                (float) (copy.getY()),
                (float) (copy.getY() + (fator*copy.getX()))
            );
        }
        
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void cisalhamentoX(double graus, CGObject p, final Vertice pontoFixo){    
        if (pontoFixo == null){
            cisalhamentoX(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        cisalhamentoX(graus, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
        
    public void cisalhamentoY(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            cisalhamentoY(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        cisalhamentoY(graus, p);
        t.transladar((int)pontoFixo.getX(), (int)pontoFixo.getY(), (int)pontoFixo.getZ(), p);
        
        lockChange = previousLock;
        if (!lockChange)
            p.changedProperty().set(true);
    }
    
    public void cisalhamentoZ(double graus, CGObject p, final Vertice pontoFixo){
        if (pontoFixo == null){
            cisalhamentoZ(graus, p);
            return;
        }
        
        boolean previousLock = lockChange;
        Translacao t = new Translacao(true); //Lock change
        lockChange = true;
        
        t.transladar(-(int)pontoFixo.getX(), -(int)pontoFixo.getY(), -(int)pontoFixo.getZ(), p);
        cisalhamentoY(graus, p);
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
