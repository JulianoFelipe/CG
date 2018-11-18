/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import m.poligonos.Aresta;
import m.poligonos.ArestaEixo;
import m.poligonos.CGObject;
import m.poligonos.Face;
import m.poligonos.Nregular;
import m.poligonos.PointObject;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public final class World {
    private static final Logger LOG = Logger.getLogger("CG_2.0");    
    private final ArrayList<CGObject> objetos;
    private final List<Vertice> tempPoints;
    private List<Vista> vistas;
    
    private final List<CGObject> axis = new ArrayList<>();
    private boolean addedAxisFlag = false;
    
    //https://coderanch.com/t/666722/java/Notify-ObservableList-Listeners-Change-Elements
    
    private World() {       
        objetos    = new ArrayList();
        tempPoints = new ArrayList();
        
        ArestaEixo x_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(5000,0,0), Color.GREEN);
        ArestaEixo y_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(0,5000,0), Color.BLUE);
        ArestaEixo z_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(0,0,5000), Color.RED);
        
        ArestaEixo mx_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(-5000,0,0), Color.GREEN);
        ArestaEixo my_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(0,-5000,0), Color.BLUE);
        ArestaEixo mz_axis = new ArestaEixo(new Vertice(0,0,0), new Vertice(0,0,-5000), Color.RED);

        axis.add( x_axis);
        axis.add( y_axis);
        axis.add( z_axis);
        axis.add(mx_axis);
        axis.add(my_axis);
        axis.add(mz_axis);
        
        //<editor-fold defaultstate="collapsed" desc="Observable List Test">
        /*objetos = FXCollections.observableArrayList((CGObject param) -> {
            return new Observable[]{ param.changedProperty()};
        });
        /*tempPoints = FXCollections.observableArrayList((Vertice param) -> {
            return new Observable[]{ param.changedProperty()};
        });*/
        
        /*objetos.addListener((Change<? extends CGObject> c) -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        throw new UnsupportedOperationException("Permutação de elementos não implementada.");
                        //System.out.println("Permuted: " + i + " " + objetos.get(i));
                    }
                } else if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        System.out.println("UP: " + objetos.get(i).changedProperty());
                        objetos.get(i).changedProperty().set(false);
                        //System.out.println("UP: " + objetos.get(i).changedProperty());
                        //System.out.println("Updated: " + i + " " + objetos.get(i));
                        for (Vista v : vistas){
                            v.setObject(i, objetos.get(i));
                        }
                    }
                } else {
                    if ( !(tempPoints.isEmpty() && c.getRemovedSize()>0) ){ //if (!(clearEvent)), proceed
                        c.getRemoved().forEach((removedItem) -> {
                            System.out.println("Removed: " + removedItem);
                            vistas.forEach((v) -> {
                                v.remove(removedItem);
                            });
                        });
                        c.getAddedSubList().forEach((addedItem) -> {
                            System.out.println("Added: " + addedItem + " Points: " + addedItem.getPoints());
                            vistas.forEach((v) -> {
                                v.addObject(deepCopy( addedItem ));
                            });
                        });
                    }
                }
            }
        });

        tempPoints.addListener((Change<? extends Vertice> c) -> {
            while (c.next()) {
                if (c.wasPermutated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        throw new UnsupportedOperationException("Permutação de elementos não implementada.");
                        //System.out.println("TEMP Permuted: " + i + " " + tempPoints.get(i));
                    }
                } else if (c.wasUpdated()) {
                    for (int i = c.getFrom(); i < c.getTo(); ++i) {
                        System.out.println("TEMP Updated: " + i + " " + tempPoints.get(i));
                        for (Vista v : vistas){
                            v.setTempPoint(i, new Vertice(tempPoints.get(i)));
                        }
                    }
                } else {
                    if ( !(tempPoints.isEmpty() && c.getRemovedSize()>0) ){
                        c.getRemoved().forEach((removedItem) -> {
                            System.out.println("TEMP Removed: " + removedItem);
                        });
                        c.getAddedSubList().forEach((addedItem) -> {
                            System.out.println("TEMP Added: " + addedItem);
                            vistas.forEach((v) -> {
                                v.addTempPoint(new Vertice(addedItem));
                            });
                        });
                    }
                }
            }
        });*/
        //</editor-fold>
    }
    
    private World(List<CGObject> lista){
        this();
        
        this.addObject(lista);
    }
    
    public void setPlanes(Vista...planes){
        this.vistas = new ArrayList<>();
        this.vistas.addAll(Arrays.asList(planes));
    }
    
    public void addObject(CGObject p){
        objetos.add(p);
        vistas.forEach((vista) -> {
            vista.addObject(deepCopy(p)); //Very ugly, but..
        });
    }
    
    public void addObject(CGObject...p){
        for (CGObject obj : p){
            addObject(obj);
        }
    }
    
    public void addObject(Collection<? extends CGObject> colecao){
        colecao.forEach((obj) -> {
            addObject(obj);
        });
    }
    
    public void addTempPoint(Vertice p){
        tempPoints.add(p);
        vistas.forEach((vista) -> {
            vista.addTempPoint(new Vertice(p));
        });
        //System.out.println("TEMPO POINT: " + tempPoints);
    }
    
    public void addTempPoint(Vertice...p){
        for (Vertice obj : p){
            addTempPoint(obj);
        }
    }
    
    public void clearTemp(){
        tempPoints.clear();
        vistas.forEach((vista) -> {
            vista.clearTempPoints();
        });
    }

    public List<Vista> getVistas() {
        return vistas;
    }
    
    private static World INSTANCE;
    
    public static World getInstance(){
        if (INSTANCE == null){
            INSTANCE = new World();
        }
        return INSTANCE;
    }
    
    public List<CGObject> getObjects(){
        /*List<CGObject> newList = new ArrayList<>(objetos.size());
        objetos.forEach((v) -> {
            newList.add(deepCopy(v));
        });
        
        return newList;*/
        return objetos;
    }
    
    public List<Vertice> getTempPointsCopy(){
        List<Vertice> newList = new ArrayList<>(tempPoints.size());
        tempPoints.forEach((v) -> {
            newList.add(new Vertice(v));
        });
        
        return newList;
    }
    
    private CGObject deepCopy(CGObject obj){
        CGObject deepCopied = null;
        
        if        (obj instanceof ArestaEixo){
            deepCopied = new ArestaEixo( (ArestaEixo) obj);
        } else if (obj instanceof Aresta){
            deepCopied = new Aresta( (Aresta) obj);
        } else if (obj instanceof Face){
            deepCopied = new Face( (Face) obj);
        } else if (obj instanceof HE_Poliedro){
            deepCopied = new HE_Poliedro( (HE_Poliedro) obj);
        } else if (obj instanceof Nregular){
            deepCopied = new Nregular( (Nregular) obj);
        } else if (obj instanceof PointObject){
            deepCopied = new PointObject( (PointObject) obj);
        } else {
            throw new IllegalArgumentException("Sub-tipo de CGObject não previsto para cópia.");
        }
        
        return deepCopied;
    }
    
    public void clearAll(){
        tempPoints.clear();
        objetos.clear();
        vistas.forEach((vista) -> {
            vista.clearAll();
        });
        
        if (addedAxisFlag){
            addedAxisFlag = false;
            addAxis();
        }
    }    
    
    public void addAxis(){
        //System.out.println("ADDED AXIS");
        if (addedAxisFlag) return;
        
        addObject(axis);
        addedAxisFlag = true;
    }
    
    public void removeAxis(){
        axis.forEach((CGObject axisObj) -> {
            removeObject(axisObj.getID());
            vistas.forEach((vista) -> {
                vista.remove(axisObj.getID());
            });
        });
        
        addedAxisFlag = false;
    }
    
    public void removeObject(long id){
        for (CGObject obj : objetos){
            if (obj.getID() == id){
                objetos.remove(obj);
                break;
            }
        }
        
        vistas.forEach((vista) -> {
            vista.remove(id);
        });
    }
    
    public void updateAll(){
        for (int i=0; i<objetos.size(); i++){
            for (Vista v : vistas){
                v.setObject(i, objetos.get(i));
            }
        }
        
        for (int i=0; i<tempPoints.size(); i++){
            for (Vista v : vistas){
                v.setTempPoint(i, tempPoints.get(i));
            }
        }
    }
    
    public void updateThis(Vista vista){
        Vista localV = null;
        for (Vista v : vistas){
            if (v == vista){
                localV = v;
                break;
            }
        }
        if (localV == null) throw new IllegalArgumentException("Vista não contida no mundo.");
        
        for (int i=0; i<objetos.size(); i++){
            localV.setObject(i, objetos.get(i));
        }
        
        for (int i=0; i<tempPoints.size(); i++){
            localV.setTempPoint(i, tempPoints.get(i));
        }
    }
    
    public CGObject getObject(CGObject obj){
        int index = objetos.indexOf(obj);
        if (index != -1) return objetos.get(index);
        else return null;
    }
    
    public void update(CGObject obj, Vista sender){
        int index = objetos.indexOf(obj);
        objetos.get(index).updateInternals(obj);
        //A vista que é o "sender" do update tem que ser o último
        //Pois o objeto foi retirado/modificado dela
        //Assim, quando ela re-converter, o ponto passado para as outras
        //vistas será diferente
        //Portanto, sender tem que ser último, o fazer uma cópia dos pontos
        //Nesse método, aí as mudanças não são refletidas dessa maneira
        vistas.forEach((v) -> {
            if (v != sender)
                v.setObject(index, obj);
        });
        sender.setObject(index, obj); //Dual conversão, mas...
        //Se não for dual conversão, tem que ter cópia em algum lugar.
        //Ora aqui ou no update da vista
    }
    
    public void updateSelectedColors(CGObject obj){
        int index = objetos.indexOf(obj);
        if (index==-1) return;
                
        objetos.get(index).setAllK(obj.getKa(), obj.getKd(), obj.getKs());
        objetos.get(index).setIsChromatic(obj.isChromatic());
        
        vistas.forEach((v) -> {
            v.updateColors(obj);
        });
    }
}
