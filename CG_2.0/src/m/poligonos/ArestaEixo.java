/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class ArestaEixo extends Aresta{
    
    private final Color axisColor;
    
    public ArestaEixo(Vertice I, Vertice F, Color color) {
        super(I, F);
        this.axisColor = color;
    }
    
    public ArestaEixo(ArestaEixo A) {
        super(A);
        this.axisColor = A.axisColor;
    }

    public Color getAxisColor() {
        return axisColor;
    }
    
    
}
