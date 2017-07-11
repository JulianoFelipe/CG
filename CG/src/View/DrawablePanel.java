/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Aresta;
import Model.poligonosEsp.Circunferencia;
import Model.Poligono;
import Model.Vertice;
import Model.poligonosEsp.Nregular;
import Model.poligonosEsp.QuadrilateroRegular;
import java.awt.Graphics;
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

    private List<Aresta> tempoLines = new ArrayList<>();
    private Aresta movable = null;
    private Circunferencia tempCirc = null;
    private Nregular tempRegular = null;
    
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
    
    public Poligono getPoligono(int index){
        return objetos.get(index);
    }
    
    public void removePoligono(int index){
        objetos.remove(index);
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
        
        for (Aresta a : tempoLines){
            Vertice um = a.getvInicial();
            Vertice dois = a.getvFinal();
            g.drawLine((int)um.getX(), (int)um.getY(), (int)dois.getX(), (int)dois.getY());
        }
        
        if (movable != null){
            Vertice um = movable.getvInicial();
            Vertice dois = movable.getvFinal();
            g.drawLine((int)um.getX(), (int)um.getY(), (int)dois.getX(), (int)dois.getY());
        }
        
        if (tempCirc != null){
            g.drawOval((int) tempCirc.getCentro().getX() - tempCirc.getRadius(),
                       (int) tempCirc.getCentro().getY() - tempCirc.getRadius(), 
                       tempCirc.getRadius()*2, tempCirc.getRadius()*2);
        }
        
        if (tempRegular != null){
            xs = tempRegular.getXpoints();
            ys = tempRegular.getYpoints();
            len = xs.length;

            g.drawPolygon(xs, ys, len);
        }
    }
    
    public void paintPolygons(){
        paintComponent(graphics);
    }
    
    @Override
    public void repaint(){
        if (graphics != null)
            super.paintComponent(graphics);
        paintComponent(graphics);
    }

    public void addTempoLine(Aresta aresta) {
       tempoLines.add(aresta);
    }

    public void setMovable(Aresta movable) {
        this.movable = movable;
    }
    
    public void setTempRegular(int lados, int radius, Vertice center, double pos){
        //super.paintComponent(graphics);
        tempRegular = new Nregular(lados, radius, center, pos);
    }
    
    public void cleanTempoLines(){
        tempoLines = new ArrayList<>();
        movable = null;
    }
    
    public void setTempCirc(Vertice center, int radius){
        super.paintComponent(graphics);
        tempCirc = new Circunferencia(center, radius);
    }
    
    public void cleanTempCirc(){
        //super.paintComponent(graphics);
        tempCirc = null;
    }
    
    public void cleanTempRegular(){
        //super.paintComponent(graphics);  
        tempRegular = null;
    }
    
    public void nullTemps(){
        cleanTempCirc();
        cleanTempRegular();
        cleanTempoLines();
    }
}
