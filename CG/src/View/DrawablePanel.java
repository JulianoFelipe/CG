/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Aresta;
import Model.Poligono;
import Model.Vertice;
import Model.Nregular;
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
    private Nregular tempRegular = null;
    
    private List<Vertice> selectedPolygonPoints;
    
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
        //System.out.println("PAINT");
        int xs[], ys[];
        int len;

        for (int i=0; i<objetos.size(); i++){
            xs = objetos.get(i).getXpoints();
            ys = objetos.get(i).getYpoints();
            len = xs.length;

            g.drawPolygon(xs, ys, len);
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
    
    public void cleanTempRegular(){
        //super.paintComponent(graphics);  
        tempRegular = null;
    }
    
    public void nullTemps(){
        cleanTempRegular();
        cleanTempoLines();
    }
    
    public void clear(){
        objetos.clear();
    }
}
