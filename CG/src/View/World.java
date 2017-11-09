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
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javax.swing.JPanel;
import utils.ColorMath;
import utils.NaiveScanLineFill;

/**
 *
 * @author JFPS
 */
public class World extends JPanel {
    private static final Logger LOG = Logger.getLogger("CG");
    public static final int SELECTED_RADIUS = 5;
    public static final Color SELECTED_COLOR  = Color.RED;
    public static final Color SELECTED_COLOR2 = Color.BLUE;
    
    private List<ProjectionPlane> projPlanes = new ArrayList();
    private final List<Poligono> objetos;
    private final Graphics graphics;

    private boolean useJavaFill = true;
    private List<Aresta> tempoLines = new ArrayList<>();
    private Aresta movable = null;
    private Nregular tempRegular = null;

    private Poligono selectedPolygon = null;
    private Vertice centerAnchor = null;
    
    public World(List<Poligono> objetos) {
        //this.objetos = FXCollections.observableArrayList(objetos);
        this.objetos = new ArrayList(objetos);
        this.graphics = null;
    }
    
    /*public World(){
        this(new ArrayList<>());
    }*/
    
    public World(Graphics g){
        this.graphics = g;
        this.objetos = new ArrayList();
    }

    public void setProjectionPlaneList(ProjectionPlane...planes){
        projPlanes = new ArrayList();
        projPlanes.addAll(Arrays.asList(planes));
    }
    
    public void setUseJavaFill(boolean useJavaFill) {
        this.useJavaFill = useJavaFill;
    }
    
    public void addPoligono(Poligono p){
        LOG.info("Adicionado: " + p);
        objetos.add(p);
        
        projPlanes.forEach((pla) -> {
            pla.NOUPDTaddPoligono(new Poligono(p));
        });
    }
    
    public void addAllPoligonos(List<Poligono> lista){
        lista.forEach((p) -> {
            addPoligono(p);
        }); 
        //objetos.addAll(lista);
        //throw new IllegalArgumentException("SOMETHING HERE");
    }
    
    public Poligono getPoligono(int index){
        return objetos.get(index);
    }
    
    public void removePoligono(int index){
        projPlanes.forEach((pla) -> {
            pla.remPolygon(objetos.get(index));
        });
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
                //ManualPaint m = new ManualPaint(1280,720);
                //m.floodFill(graphics, p);
                //System.out.println("NOT JAVA FILL");
                
                NaiveScanLineFill scn = new NaiveScanLineFill(graphics);
                scn.scanLineFill(p);
            }
        }
        
        graphics.setColor(previousColor);
    }
    
    public void paintSelectedPolygon(){
        if (selectedPolygon == null) return;
        Color prev = graphics.getColor();
        
        if (ColorMath.isThisColorCloseToThatOne(SELECTED_COLOR, selectedPolygon.getCorFundo())) 
            graphics.setColor(SELECTED_COLOR2);
        else 
            graphics.setColor(SELECTED_COLOR);
        //graphics.setColor(SELECTED_COLOR);
        
        selectedPolygon.getVertices().forEach((ponto) -> {
            graphics.drawOval((int) ponto.getX() - SELECTED_RADIUS,
                              (int) ponto.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
            graphics.fillOval((int) ponto.getX() - SELECTED_RADIUS,
                              (int) ponto.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
        });
        
        
        if (centerAnchor != null){
            /*if (ColorMath.isThisColorCloseToThatOne(SELECTED_COLOR2, selectedPolygon.getCorFundo())) 
                graphics.setColor(ANCHOR_COLOR2);
            else 
                graphics.setColor(SELECTED_COLOR2);*/
            
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
        projPlanes.forEach((pla) -> {
            pla.NOUPDTaddTempoLine(new Aresta(aresta));
        });
    }

    public void setMovable(Aresta movable) {
        this.movable = movable;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTsetMovable(new Aresta(movable));
        });
    }
    
    public void setTempRegular(Nregular regularPolygon){
        tempRegular = regularPolygon;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTsetTempRegular(new Nregular(regularPolygon));
        });
    }
    
    public void cleanTempoLines(){
        tempoLines = new ArrayList<>();
        movable = null;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTcleanTempoLines();
        });
    }
    
    public void cleanTempRegular(){
        //super.paintComponent(graphics);  
        tempRegular = null;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTcleanTempRegular();
        });
    }
    
    public void nullTemps(){
        cleanTempRegular();
        cleanTempoLines();
        projPlanes.forEach((pla) -> {
            pla.NOUPDTnullTemps();
        });
    }
    
    public void clear(){
        objetos.clear();
        nullTemps();
        selectedPolygon = null;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTclear();
        });
    }

    public void setSelectedPolygon(Poligono selectedPolygon) {
        this.selectedPolygon = selectedPolygon;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTsetSelectedPolygon(new Poligono(selectedPolygon));
        });
    }
    
    public void remove(Poligono p){
        objetos.remove(p);
        projPlanes.forEach((pla) -> {
            pla.remPolygon(p);
        });
    }
    
    public void setAnchor(Vertice v){
        centerAnchor = v;
        projPlanes.forEach((pla) -> {
            if (v != null)
                pla.NOUPDTsetAnchor(new Vertice(v));
            else
                pla.NOUPDTsetAnchor(null);
        });
    }
}
