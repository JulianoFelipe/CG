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
public class Cisalhamento {
    public Poligono cisalhamento(Eixo axis, double fator, Poligono p){
        switch (axis) {
            case Eixo_X:
                return cisalhamentoX(fator, p);
            case Eixo_Y:
                return cisalhamentoY(fator, p);
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
}
