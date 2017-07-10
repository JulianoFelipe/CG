/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.poligonosEsp.Circunferencia;
import Model.Poligono;
import Model.poligonosEsp.QuadrilateroRegular;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 *
 * @author JFPS
 */
public class DrawablePanel extends JPanel {
    private static final Logger LOG = Logger.getLogger("CG");
    private final List<Poligono> objetos;
    private final Graphics graphics;

    public DrawablePanel(List<Poligono> objetos) {
        this.objetos = objetos;
        this.graphics = null;
    }
    
    public DrawablePanel(){
        this(new ArrayList<>());
    }
    
    public DrawablePanel(Graphics g){
        this.graphics = g;
        objetos = new ArrayList();
    }
    
    public void addPoligono(Poligono p){
        LOG.info("Adicionado: " + p);
        objetos.add(p);
    }
    
    public void addAllPoligonos(List<Poligono> lista){
        objetos.addAll(lista);
    }
    
    public Poligono getPoligono(int i){
        return objetos.get(i);
    }
    
    public List<Poligono> getListaPoligonos(){
        return new ArrayList<>(objetos);
    }
    
    @Override
    protected void paintComponent(Graphics g){      
        if (objetos == null) return;
        
        int xs[], ys[];
        int len;
        
        Poligono temp;
        for (int i=0; i<objetos.size(); i++){
            temp = objetos.get(i);
            if (temp instanceof QuadrilateroRegular){
                QuadrilateroRegular quad = ((QuadrilateroRegular) temp);
                g.drawRect((int)quad.getMinX(), (int)quad.getMinY(), (int)quad.getWidth(), (int)quad.getHeight());
            } else if (temp instanceof Circunferencia){ 
                Circunferencia circ = ((Circunferencia) temp);
                g.drawOval((int) circ.getCentro().getX() - circ.getRadius(),
                           (int) circ.getCentro().getY() - circ.getRadius(), 
                           circ.getRadius()*2, circ.getRadius()*2);
            } else {
            
                xs = objetos.get(i).getXpoints();
                ys = objetos.get(i).getYpoints();
                len = xs.length;

                g.drawPolygon(xs, ys, len);
            }
        }
    }
    
    public void paintPolygons(){
        paintComponent(graphics);
    }
    
    @Override
    public void repaint(){
        paintComponent(graphics);
    }
    
}
