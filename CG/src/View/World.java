/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Model.Aresta;
import Model.CGObject;
import Model.Poligono;
import Model.Vertice;
import Model.Nregular;
import Model.messaging.events.AnchorEvent;
import Model.messaging.events.CancelTemporaryEvent;
import Model.messaging.events.DeleteEvent;
import Model.messaging.events.InputEvent;
import Model.messaging.events.LastTempPointMovedEvent;
import Model.messaging.events.SelectedEvent;
import Model.messaging.events.TemporaryPointEvent;
import Model.messaging.events.TemporaryRegularEvent;
import Model.messaging.events.TransformEvent;
import Model.messaging.listeners.AnchorListener;
import Model.messaging.listeners.CancelTemporaryListener;
import Model.messaging.listeners.DeletePoligonoListener;
import Model.messaging.listeners.InputListener;
import Model.messaging.listeners.LastTempPointMovedListener;
import Model.messaging.listeners.SelectedChangedListener;
import Model.messaging.listeners.TemporaryPointListener;
import Model.messaging.listeners.TemporaryRegularListener;
import Model.messaging.listeners.TransformListener;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
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

    private List<Aresta> tempoLines = new ArrayList<>();
    private Aresta movable = null;
    private Nregular tempRegular = null;

    private Poligono selectedPolygon = null;
    private Vertice centerAnchor = null;
    
    public World(List<Poligono> objetos) {
        //this.objetos = FXCollections.observableArrayList(objetos);
        this.objetos = new ArrayList(objetos);
        
        CancelTemporaryListener cancelTemp = (ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTclear();
            });
        };
        
        InputListener inputListen = (InputListener) (CGObject object, ProjectionPlane planeOfOrigin) -> {
            projPlanes.stream().filter((pla) -> (!planeOfOrigin.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTaddPoligono((Poligono) object);
            });
        };
        
        LastTempPointMovedListener lastTempPoint = (Vertice newLocation, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTchangeLastPoint(newLocation);
            });
        };
        
        TemporaryPointListener tempPoint = (Vertice newPoint, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTaddTempPoint(newPoint);
            });
        };
        
        TemporaryRegularListener tempRegularListen = (Nregular newTempRegular, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTsetTempRegular(newTempRegular);
            });
        };
        
        TransformListener transformListen = (TransformListener) (CGObject object, ProjectionPlane planeOfOrigin) -> {
            projPlanes.stream().filter((pla) -> (!planeOfOrigin.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTchangePoligono((Poligono) object);
            });
        };
        
        SelectedChangedListener selectedPolygonListen = (Poligono newSelected, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTsetSelectedPolygon(newSelected);
            });
        };
        
        DeletePoligonoListener deleteListen = (Poligono p, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTremove(p);
            });
        };
        
        AnchorListener anchor = (Vertice anchor1, ProjectionPlane plane) -> {
            projPlanes.stream().filter((pla) -> (!plane.equals(pla))).forEachOrdered((pla) -> {
                pla.NOUPDTsetAnchor(anchor1);
            });
        };
        
        StaticConfig.EVENTS.listen(CancelTemporaryEvent.class, cancelTemp);
        StaticConfig.EVENTS.listen(InputEvent.class, inputListen);
        StaticConfig.EVENTS.listen(LastTempPointMovedEvent.class, lastTempPoint);
        StaticConfig.EVENTS.listen(TemporaryPointEvent.class, tempPoint);
        StaticConfig.EVENTS.listen(TemporaryRegularEvent.class, tempRegularListen);
        StaticConfig.EVENTS.listen(TransformEvent.class, transformListen);
        StaticConfig.EVENTS.listen(SelectedEvent.class, selectedPolygonListen);
        StaticConfig.EVENTS.listen(DeleteEvent.class, deleteListen);
        StaticConfig.EVENTS.listen(AnchorEvent.class, anchor);
    }
    
    public World(){ this(new ArrayList());}

    public void setProjectionPlaneList(ProjectionPlane...planes){
        projPlanes = new ArrayList();
        projPlanes.addAll(Arrays.asList(planes));
    }
    
    public List<Poligono> getListaPoligonos(){
        return new ArrayList<>(objetos);
    }
    
    public void addPoligono(Poligono p, ProjectionPlane plane){
        LOG.info("Adicionado: " + p);
        objetos.add(p);
        
        projPlanes.stream().filter((pla) -> (!pla.equals(plane))).forEachOrdered((pla) -> {
            pla.NOUPDTaddPoligono(new Poligono(p));
        });
    }
    
    public void addAllPoligonos(List<Poligono> lista, ProjectionPlane plane){
        lista.forEach((p) -> {
            addPoligono(p, plane);
        }); 
    }
    
    /*public Poligono getPoligono(int index){
        return objetos.get(index);
    }
    
    public void removePoligono(int index, ProjectionPlane plane){        
        projPlanes.stream().filter((pla) -> (!pla.equals(plane))).forEachOrdered((pla) -> {
            pla.NOUPDTremove(objetos.get(index));
        });
        
        objetos.remove(index);
    }

    public void setMovable(Aresta movable) {
        this.movable = movable;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTsetMovable(new Aresta(movable));
        });
        
        projPlanes.stream().filter((pla) -> (!pla.equals(plane))).forEachOrdered((pla) -> {
            pla.NOUPDTremove(objetos.get(index));
        });
    }
    
    public void setTempRegular(Nregular regularPolygon, ProjectionPlane plane){
        tempRegular = regularPolygon;
        projPlanes.forEach((pla) -> {
            pla.NOUPDTsetTempRegular(new Nregular(regularPolygon));
        });
    }
    
    public void clearTemporary(){
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
        //cleanTempRegular();
        //cleanTempoLines();
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
            pla.NOUPDTremove(p);
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
    }*/
}

/*
Change poligono
add Poligono
setAnchor
remove
set selected
set temp regular
addVertice
setMovable
clear
*/