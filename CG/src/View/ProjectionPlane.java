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
    private final List<Aresta> projectionTempLines;
    private Poligono selectedPolygon;
    private Vertice selectedPolygonCenter;
    private Aresta movable;

    /* Problemas notados:
    - São 3 planos "clicáveis", se alterar um objeto em um, ele manda as alterações para o "mundo"
      que então propaga as alterações para as projeções que "escutam" o mundo (as 4), então o que manda
      processa os objetos duas vezes quando há alteração.
    
    */
    
    private final World world;
    private final Graphics graphics;
    private final Projection projectionMethod;
    private final short ID;
    
    private boolean useJavaFill = true;
    //https://stackoverflow.com/questions/937302/simple-java-message-dispatching-system
            
    public ProjectionPlane(Graphics g, Projection projectionMethod, World world){
        this.graphics = g;
        //objetos = FXCollections.observableArrayList();
        projectionList = new ArrayList();
        projectionTempLines = new ArrayList();
        this.projectionMethod = projectionMethod;
        this.world =  world;
        ID = INSTANCES++;
    }

    public boolean isUseJavaFill() {
        return useJavaFill;
    }

    public void setUseJavaFill(boolean useJavaFill) {
        this.useJavaFill = useJavaFill;
    }
        
    public List<Poligono> getListaPoligonos(){
        return projectionList;
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
        //System.out.println("PAINTING");
        //graphics.drawRect(0, 0, 100, 100);
        //this.revalidate();
        //this.getGraphics().fillRect(0, 0, 300, 300);
        //if (projectionList == null) return;        

        projectionList.forEach((p) -> {
            paintPolygon(p);
        });

        projectionTempLines.forEach((a) -> {
            Vertice um = a.getvInicial();
            Vertice dois = a.getvFinal();
            g.drawLine((int)um.getX(), (int)um.getY(), (int)dois.getX(), (int)dois.getY());
        });
        
        if (movable != null){
            Vertice um = movable.getvInicial();
            Vertice dois = movable.getvFinal();
            g.drawLine((int)um.getX(), (int)um.getY(), (int)dois.getX(), (int)dois.getY());
        }

        if (projectionTempRegular != null){
            paintPolygon(projectionTempRegular);
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
        world.clear();
    }
    
    public void setMovable(Aresta movable){
        world.setMovable(projectionMethod.transformBack(movable));
    }
    
    public void addTempoLine(Aresta aresta) {
        //projectionTempLines.add(aresta);
        world.addTempoLine(projectionMethod.transformBack(movable));
    }
    
    public void setTempRegular(Nregular regularPolygon){
        //projectionTempRegular = regularPolygon;
        world.setTempRegular(projectionMethod.transformBack(regularPolygon));
    }
    
    public void cleanTempoLines(){
        //projectionTempLines.clear();
        world.cleanTempoLines();
    }
    
    public void cleanTempRegular(){
        //projectionTempRegular = null;
        world.cleanTempRegular();
    }
    
    public void nullTemps(){
        //cleanTempRegular();
        //cleanTempoLines();
        world.nullTemps();
    }
    
    public void setSelectedPolygon(Poligono selectedPolygon) {
        //this.selectedPolygon = selectedPolygon;
        world.setSelectedPolygon(projectionMethod.transformBack(selectedPolygon));
    }
    
    public void remove(Poligono p){
        //projectionList.remove(p);
        world.remove(p);
    }
    
    public void setAnchor(Vertice v){
        //selectedPolygonCenter = v;
        world.setAnchor(projectionMethod.transformBack(v));
    }
    
    public void addPoligono(Poligono p){
        world.addPoligono(projectionMethod.transformBack(p));
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="No Updt">    
    public void NOUPDTclear(){
        throw new IllegalAccessError("IDUNNO");
    }
    
    public void NOUPDTsetMovable(Aresta movable){
        if (movable != null)
            this.movable = projectionMethod.project(movable);
        else
            this.movable = null;
    }
    
    public void NOUPDTaddTempoLine(Aresta aresta) {
        Aresta toAdd;
        if (aresta != null){
            toAdd = projectionMethod.project(aresta);
            projectionTempLines.add(toAdd);
        }
    }
    
    public void NOUPDTsetTempRegular(Nregular regularPolygon){
        if (regularPolygon != null)
            projectionTempRegular = projectionMethod.project(regularPolygon);
        else
            projectionTempRegular = null;
    }
    
    public void NOUPDTcleanTempoLines(){
        projectionTempLines.clear();
    }
    
    public void NOUPDTcleanTempRegular(){
        projectionTempRegular = null;
    }
    
    public void NOUPDTnullTemps(){
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
    
    public void remLines(){
        projectionTempLines.clear();
    }
    
    public void remNregular(){
        projectionTempRegular = null;
    }
    
    public void remPolygon(Poligono p){
        projectionList.remove(p);
    }
    
    /*... BUGS AND DOODADS*/
    
//</editor-fold>
}
