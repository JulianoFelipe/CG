/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;
import m.Ponto2D;

/**
 * Âncora para arastar um polígono selecionado
 * 
 * 
 * 
 * @author JFPS
 */
public class Ancora extends Circle{
    private static final Color DEFAULT_COLOR = Color.RED;
    private static final int ANCHOR_RADIUS = 10;
    
    private final DoubleProperty x, y;

    public Ancora(Color color, DoubleProperty x, DoubleProperty y) {
        super(x.get(), y.get(), ANCHOR_RADIUS);
        setFill(color.deriveColor(1, 1, 1, 0.5));
        setStroke(color);
        setStrokeWidth(2);
        setStrokeType(StrokeType.OUTSIDE);

        this.x = x;
        this.y = y;

        x.bind(centerXProperty());
        y.bind(centerYProperty());
        enableDrag();
    }

    private void enableDrag() {
        final Delta dragDelta = new Delta();
        setOnMousePressed((MouseEvent mouseEvent) -> {
            dragDelta.x = getCenterX() - mouseEvent.getX();
            dragDelta.y = getCenterY() - mouseEvent.getY();
            getScene().setCursor(Cursor.MOVE);
        });

        setOnMouseReleased((MouseEvent mouseEvent) -> {
            getScene().setCursor(Cursor.HAND);
        });

        setOnMouseDragged((MouseEvent mouseEvent) -> {
            double newX = mouseEvent.getX() + dragDelta.x;
            if (newX > 0 && newX < getScene().getWidth()) {
                setCenterX(newX);
            }
            double newY = mouseEvent.getY() + dragDelta.y;
            if (newY > 0 && newY < getScene().getHeight()) {
                setCenterY(newY);
            }
        });

        setOnMouseEntered((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.HAND);
            }
        });
        setOnMouseExited((MouseEvent mouseEvent) -> {
            if (!mouseEvent.isPrimaryButtonDown()) {
                getScene().setCursor(Cursor.DEFAULT);
            }
        });
    }
    
    public static ObservableList<Ancora> criarAncorasSelecionaveis(final ObservableList<Ponto2D> points) {
        ObservableList<Ancora> anchors = FXCollections.observableArrayList();

        for (int i = 0; i <points.size(); i++) {
            final int idx = i;
            Ponto2D idxPt = points.get(i);

            DoubleProperty xProperty = new SimpleDoubleProperty(idxPt.getX());
            DoubleProperty yProperty = new SimpleDoubleProperty(idxPt.getY());

            ///Weird behavior because coordinates are separated?
            xProperty.addListener((ObservableValue<? extends Number> ov, Number oldX, Number x1) -> {
                Ponto2D toChange = points.get(idx);
                points.set(idx, new Ponto2D((int)x1, toChange.getY()));
            });

            yProperty.addListener((ObservableValue<? extends Number> ov, Number oldY, Number y1) -> {
                Ponto2D toChange = points.get(idx);
                points.set(idx, new Ponto2D(toChange.getX(), (int)y1));
            });

            anchors.add(new Ancora(Color.GOLD, xProperty, yProperty));
        }

        return anchors;
    }
    
    private class Delta { double x, y; } //https://gist.github.com/jewelsea/5375786
}
