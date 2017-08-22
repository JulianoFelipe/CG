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
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPanel;
import utils.ManualPaint;

/**
 *
 * @author JFPS
 */
public class DrawablePanel extends JPanel {
    private static final Logger LOG = Logger.getLogger("CG");
    public static final int SELECTED_RADIUS = 5;
    public static final Color SELECTED_COLOR = Color.RED;
    public static final Color ANCHOR_COLOR1 = Color.BLUE;
    public static final Color ANCHOR_COLOR2 = Color.MAGENTA;
    
    private final List<Poligono> objetos;
    private final Graphics graphics;

    private boolean useJavaFill = true;
    private List<Aresta> tempoLines = new ArrayList<>();
    private Aresta movable = null;
    private Nregular tempRegular = null;

    private Poligono selectedPolygon = null;
    private Vertice centerAnchor = null;
    
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

    public void setUseJavaFill(boolean useJavaFill) {
        this.useJavaFill = useJavaFill;
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

        objetos.forEach((p) -> {
            paintPolygon(p);
        });

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
            paintPolygon(tempRegular);
        }
        
        paintSelectedPolygon();
    }
    
    public void paintPolygon(Poligono p){
        Color previousColor = graphics.getColor();
        
        int xs[], ys[];
        int len;
        
        xs = p.getXpoints();
        ys = p.getYpoints();
        len = xs.length;
  
        graphics.setColor(p.getCorBorda());
        graphics.drawPolygon(xs, ys, len);
        
        if (p.getCorFundo() != null){
            graphics.setColor(p.getCorFundo());
            if(useJavaFill)
                graphics.fillPolygon(xs, ys, len);
            else{
                ManualPaint.floodFill(graphics, p);
                //System.out.println("NOT JAVA FILL");
            }
        }
        
        graphics.setColor(previousColor);
    }
    
    public void paintSelectedPolygon(){
        if (selectedPolygon == null) return;
        Color prev = graphics.getColor();
        graphics.setColor(SELECTED_COLOR);
        
        selectedPolygon.getVertices().forEach((ponto) -> {
            graphics.drawOval((int) ponto.getX() - SELECTED_RADIUS,
                              (int) ponto.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
            graphics.fillOval((int) ponto.getX() - SELECTED_RADIUS,
                              (int) ponto.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
        });
        
        
        if (centerAnchor != null){
            if (selectedPolygon.getCorFundo() == ANCHOR_COLOR1) 
                graphics.setColor(ANCHOR_COLOR2);
            else 
                graphics.setColor(ANCHOR_COLOR1);
            
            graphics.fillOval((int) centerAnchor.getX() - SELECTED_RADIUS,
                              (int) centerAnchor.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
        }
        
        graphics.setColor(prev);
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
    
    public void setTempRegular(Nregular regularPolygon){
        tempRegular = regularPolygon;
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
        nullTemps();
        selectedPolygon = null;
    }

    public void setSelectedPolygon(Poligono selectedPolygon) {
        this.selectedPolygon = selectedPolygon;
    }
    
    public void remove(Poligono p){
        objetos.remove(p);
    }
    
    public void setAnchor(Vertice v){
        centerAnchor = v;
    }
}
