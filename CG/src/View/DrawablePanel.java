/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Poligono;
import java.awt.Graphics;
import java.util.List;
import javax.swing.JPanel;

/**
 *
 * @author JFPS
 */
public class DrawablePanel extends JPanel {
    private final List<Poligono> objetos;
    //private final Graphics2D graphics;

    public DrawablePanel(List<Poligono> objetos) {
        this.objetos = objetos;
    }
    
    @Override
    protected void paintComponent(Graphics g){
        int xs[], ys[];
        int len;
        for (int i=0; i<objetos.size(); i++){
            xs = objetos.get(i).getXpoints();
            ys = objetos.get(i).getYpoints();
            len = xs.length;
            g.drawPolygon(xs, ys, len);
        }
    }
    
    /*public void paintPolygons(){
        paintComponent(graphics);
    }*/
    
}
