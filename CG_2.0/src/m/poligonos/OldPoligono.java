/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.ListProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.geometry.Point3D;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Shape;

/**
 *
 * @author JFPS
 */
public class OldPoligono implements Serializable{
    //https://stackoverflow.com/questions/15457814/using-listproperty-in-javafx
    private ListProperty<Point3D> vertices;

    protected static long INSTANCES;
    protected long ID;
    
    private boolean changedState = false;
    private Shape toDraw;
        
    public OldPoligono() {
        ObservableList<Point3D> obsPontos = FXCollections.observableArrayList();
        this.vertices = new SimpleListProperty(obsPontos);
        
        ID = INSTANCES++;
    }

    public OldPoligono(List<Point3D> vertices) {
        ObservableList<Point3D> obsPontos = FXCollections.observableArrayList(vertices);
        this.vertices = new SimpleListProperty(obsPontos);
        
        ID = INSTANCES++;
    }

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }
    
    public void addPonto(Point3D point){
        vertices.add(point);
        changedState = true;
    }

    public void addAllPontos(Collection<Point3D> colectionOfPoints){
        vertices.addAll(colectionOfPoints);
    }
    
    public ListProperty<Point3D> pontosProperty() {
        return vertices;
    }
    
    public void addPoligonoListener(ListChangeListener listener){
        vertices.addListener(listener);
    }
    
    protected void buildShape(){
        if(!changedState) return;
        
        vertices = null;
        changedState = false;
    }
    
    private ObjectProperty<Shape> shape;
    private ObjectProperty<Paint> fill;
    private StringProperty id;
    
    protected final ChangeListener updateListener = (v, o, n) -> {
        changedState = true;
        buildShape();
    };
    
    protected void setShape(Shape shape) {
        shapeProperty().set(shape);
    }

    public ObjectProperty<Shape> shapeProperty() {
        if (shape == null) {
            shape = new SimpleObjectProperty<>(this, "shape");
            shape.addListener((observable, oldShape, newShape) -> {
                if (newShape != null) {
                    // forwards property values directly
                    newShape.setFill(getFill());
                    //newShape.setId(getId());
                }
            });
        }
        return shape;
    }

    public ObjectProperty<Paint> fillProperty() {
        if (fill == null) {
            fill = new SimpleObjectProperty<>(this, "fill", Color.BLACK);
            //fill.addListener((v, o, n) -> forwardShapeProperty(s -> s.setFill(n)));
        }
        return fill;
    }

    public StringProperty idProperty() {
        if (id == null) {
            id = new SimpleStringProperty(this, "id");
            //id.addListener((v, o, n) -> forwardShapeProperty(s -> s.setId(n)));
        }
        return id;
    }
    
    Shape getShape() {
        return shapeProperty().get();
    }

    public Paint getFill() {
        return fillProperty().get();
    }

    public void setFill(Paint fill) {
        fillProperty().set(fill);
    }
    
    
}
