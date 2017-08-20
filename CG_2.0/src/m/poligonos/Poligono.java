/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.beans.value.ChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Line;
import javafx.scene.shape.Shape;
import m.Ponto2D;

/**
 *
 * @author JFPS
 */
public class Poligono implements Serializable{
    private ObservableList<Integer> points;
    private ObservableList<Line> arestas;
    
    private List<Ponto2D> vertices;
    private boolean changedState = false;
    private Shape toDraw;
        
    public Poligono() { }

    public Poligono(List<Ponto2D> vertices) {
        this.vertices = vertices;
    }
    
    public void addPonto(Ponto2D point){
        if (vertices == null){
            vertices = new ArrayList<>();
        }
        vertices.add(point);
    }

    public void addAllPontos(Collection<Ponto2D> colectionOfPoints){
        if (vertices == null){
            vertices = new ArrayList<>();
        }
        vertices.addAll(colectionOfPoints);
    }
    
    public List<Ponto2D> getPontos() {
        return vertices;
    }
    
    protected Shape buildShape(){
        
        points = null;
        arestas = null;
        return null;
    }
    
    private ObjectProperty<Shape> shape;
    private ObjectProperty<Paint> fill;
    private StringProperty id;

    protected final ChangeListener updateListener = (v, o, n) -> {
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
