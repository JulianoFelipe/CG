/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Aresta;
import Model.Nregular;
import Model.Poligono;
import Model.Projections.Projection;
import Model.Vertice;
import Model.messaging.events.AnchorEvent;
import Model.messaging.events.CancelTemporaryEvent;
import Model.messaging.events.DeleteEvent;
import Model.messaging.events.InputEvent;
import Model.messaging.events.LastTempPointMovedEvent;
import Model.messaging.events.SelectedEvent;
import Model.messaging.events.TemporaryPointEvent;
import Model.messaging.events.TemporaryRegularEvent;
import Model.messaging.events.TransformEvent;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javax.swing.JPanel;
import utils.ColorMath;
import utils.NaiveScanLineFill;

/**
 *
 * @author JFPS
 */
public class ProjectionPlane extends JPanel{
    private static short INSTANCES;
    private static final Logger LOG = Logger.getLogger("CG");
    public static final int SELECTED_RADIUS = 5;
    public static final Color SELECTED_COLOR  = Color.RED;
    public static final Color SELECTED_COLOR2 = Color.BLUE;
    
    //Objects to draw
    private final List<Poligono> projectionList;
    private Nregular projectionTempRegular;
    private final List<Vertice> projectionTemp;
    private Poligono selectedPolygon;
    private Vertice selectedPolygonCenter;
    private Vertice movable;

    //private short changedVertices = 0;
    //private boolean lastChanged = false;
    private final ArrayList<Integer> coordsX = new ArrayList();
    private final ArrayList<Integer> coordsY = new ArrayList();
    
    /* Problemas notados:
    - São 3 planos "clicáveis", se alterar um objeto em um, ele manda as alterações para o "mundo"
      que então propaga as alterações para as projeções que "escutam" o mundo (as 4), então o que manda
      processa os objetos duas vezes quando há alteração.
    
    */
    
    private Graphics graphics;
    private final Projection projectionMethod;
    private final short ID;
    
    //private boolean useJavaFill = true;
    //https://stackoverflow.com/questions/937302/simple-java-message-dispatching-system
            
    public ProjectionPlane(Projection projectionMethod){
        //objetos = FXCollections.observableArrayList();
        projectionList = new ArrayList();
        projectionTemp = new ArrayList();
        this.projectionMethod = projectionMethod;
        ID = INSTANCES++;
    }

    /*public boolean isUseJavaFill() {
        return useJavaFill;
    }

    public void setUseJavaFill(boolean useJavaFill) {
        this.useJavaFill = useJavaFill;
    }*/
        
    public List<Poligono> getListaPoligonos(){
        return projectionList;
    }

    public Poligono getSelectedPoligon(){
        return selectedPolygon;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + this.ID;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProjectionPlane other = (ProjectionPlane) obj;
        if (this.ID != other.ID) {
            return false;
        }
        return true;
    }
    
    @Override
    protected void paintComponent(Graphics g){      
        if (g == null){
            System.out.println(" Null");
            return;
        }
        graphics = g;
        
        //System.out.println("PAINTING");
        g.drawRect(0, 0, 100, 100);
        //this.revalidate();
        //this.getGraphics().fillRect(0, 0, 300, 300);
        //if (projectionList == null) return;        

        projectionList.forEach((p) -> {
            paintPolygon(p);
        });

        paintTemporaryPoints();

        if (projectionTempRegular != null){
            paintPolygon(projectionTempRegular);
        }
        
        paintSelectedPolygon();
    }
    
    private void paintTemporaryPoints(){
        /*if (changedVertices > 0){
            lastChanged = false;
        }
        
        if (lastChanged){
            
        }*/
        
        if (projectionTemp.isEmpty())
            return;
        
        coordsX.add((int)movable.x);
        coordsY.add((int)movable.y);
        
        //https://stackoverflow.com/questions/718554/how-to-convert-an-arraylist-containing-integers-to-primitive-int-array
        int[] xCoords = coordsX.stream().mapToInt(i -> i).toArray();
        int[] yCoords = coordsY.stream().mapToInt(i -> i).toArray();
        
        graphics.drawPolyline(xCoords, yCoords, yCoords.length);
        
        coordsX.remove(coordsX.size()-1);
        coordsY.remove(coordsY.size()-1);
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
            //if(useJavaFill)
                graphics.fillPolygon(xs, ys, len);
            /*else{                
                NaiveScanLineFill scn = new NaiveScanLineFill(graphics);
                scn.scanLineFill(p);
            }*/
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
        
        
        if (selectedPolygonCenter != null){
            /*if (ColorMath.isThisColorCloseToThatOne(SELECTED_COLOR2, selectedPolygon.getCorFundo())) 
                graphics.setColor(ANCHOR_COLOR2);
            else 
                graphics.setColor(SELECTED_COLOR2);*/
            
            graphics.fillOval((int) selectedPolygonCenter.getX() - SELECTED_RADIUS,
                              (int) selectedPolygonCenter.getY() - SELECTED_RADIUS,
                              SELECTED_RADIUS*2, SELECTED_RADIUS*2);
        }
        
        graphics.setColor(prev);
    }
    
    @Override
    public void repaint(){
        if (graphics != null)
            super.paintComponent(graphics);
        paintComponent(graphics);
    }
    
    //<editor-fold defaultstate="collapsed" desc="From Clicks">
    public void clear(){
        NOUPDTclear();
        StaticConfig.EVENTS.notify(new CancelTemporaryEvent(this));
    }
    
    public void setMovable(Vertice movable){
        this.movable = movable;
        StaticConfig.EVENTS.notify(
            new LastTempPointMovedEvent(movable, this)
        );
    }
    
    public void addVertice(Vertice vertice) {
        if(vertice != null){
            if (movable != null){
                projectionTemp.add(movable);
                coordsX.add((int)movable.x);
                coordsY.add((int)movable.y);
            }

            movable = vertice;
            StaticConfig.EVENTS.notify(
                new TemporaryPointEvent(vertice, this)
            );
        }
    }
    
    public void setTempRegular(Nregular regularPolygon){
        projectionTempRegular = regularPolygon;
        StaticConfig.EVENTS.notify(
            new TemporaryRegularEvent(regularPolygon, this)
        );
    }
        
    public void setSelectedPolygon(Poligono selectedPolygon) {
        this.selectedPolygon = selectedPolygon;
        StaticConfig.EVENTS.notify(
            new SelectedEvent(selectedPolygon, this)
        );
    }
    
    public void remove(Poligono p){
        projectionList.remove(p);
        StaticConfig.EVENTS.notify(
            new DeleteEvent(p, this)
        );
    }
    
    public void setAnchor(Vertice v){
        this.selectedPolygonCenter = v;
        StaticConfig.EVENTS.notify(
            new AnchorEvent(v, this)
        );
    }
    
    public void addPoligono(Poligono p){
        if (p != null){
            projectionList.add(p);
        }
        StaticConfig.EVENTS.notify(
            new InputEvent(p, this)
        );
    }
    
    public void changePoligono(Poligono p){
        if (p != null){
            for (int i=0; i<projectionList.size(); i++){
                Poligono proje = projectionList.get(i);
                if (proje.equals(p)){
                    proje.setID(p.getID());
                    projectionList.set(i, proje); //Weird, but equals compares IDs, so it kinda makes sense
                    break;
                }
            }
        }
        
        StaticConfig.EVENTS.notify(
            new TransformEvent(p, this)
        );
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="No Updt">    
    public void NOUPDTclear(){
        NOUPDTnullTemps();
        coordsX.clear();
        coordsY.clear();
        //changedVertices = 0;
    }
    
    public void NOUPDTchangeLastPoint(Vertice movable){
        if (movable != null){
            //lastChanged = true;
            this.movable = projectionMethod.project(movable);
        } else
            this.movable = null;
    }
    
    public void NOUPDTaddTempPoint(Vertice point) {
        Vertice toAdd;
        if (point != null){
            toAdd = projectionMethod.project(point);
            if(movable != null){
                projectionTemp.add(movable);
                coordsX.add((int)movable.x);
                coordsY.add((int)movable.y);
            }
            movable = toAdd;
            //changedVertices ++;
        }
    }
    
    public void NOUPDTsetTempRegular(Nregular regularPolygon){
        if (regularPolygon != null)
            projectionTempRegular = projectionMethod.project(regularPolygon);
        else
            projectionTempRegular = null;
    }
    
    private void NOUPDTcleanTempoLines(){
        projectionTemp.clear();
    }
    
    private void NOUPDTcleanTempRegular(){
        projectionTempRegular = null;
    }
    
    private void NOUPDTnullTemps(){
        NOUPDTcleanTempRegular();
        NOUPDTcleanTempoLines();
    }
    
    public void NOUPDTsetSelectedPolygon(Poligono selectedPolygon) {
        if (selectedPolygon != null)
            this.selectedPolygon = projectionMethod.project(selectedPolygon);
        else
            this.selectedPolygon = null;
    }
    
    public void NOUPDTremove(Poligono p){
        projectionList.remove(p);
    }
    
    public void NOUPDTchangePoligono(Poligono p){
        if (p != null){
            for (int i=0; i<projectionList.size(); i++){
                if (projectionList.get(i).equals(p)){
                    Poligono proje = projectionMethod.project(p);
                    proje.setID(p.getID());
                    projectionList.set(i, proje); //Weird, but equals compares IDs, so it kinda makes sense
                    break;
                }
            }
        }
    }
    
    public void NOUPDTsetAnchor(Vertice v){
        selectedPolygonCenter = projectionMethod.project(v);
    }
    
    public void NOUPDTaddPoligono(Poligono p){
        Poligono newUpdated = projectionMethod.project(p);
        newUpdated.setID(p.getID());
        projectionList.add(newUpdated);
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Doodad">
    /* UGLY "PSEUDO LISTENTER" THINGAMAJIGS FULL OF  ... */
    
    /*public void remLines(){
        projectionTempLines.clear();
    }
    
    public void remNregular(){
        projectionTempRegular = null;
    }
    
    public void remPolygon(Poligono p){
        projectionList.remove(p);
    }*/
    
    /*... BUGS AND DOODADS*/
    
//</editor-fold>
}
