/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import m.Visao;

/**
 *
 * @author JFPS
 */
public class Grid extends ImageView{
    private static final int X_MARGIN = 5;
    private static final int Y_MARGIN = 5;
    
    private final Image AXIS;
    private Canvas drawableGrid;
    
    private int cellSizePX = 50;
    private Color gridColor = Color.RED;
    private int gridThickness = 2;
       
    public Grid(Canvas canvas, Image axis, int cellSizePX) {
        this.AXIS = axis;
        this.cellSizePX = cellSizePX;
        
        //this.fitHeightProperty().bind(canvas.heightProperty());
        //this.fitWidthProperty().bind(canvas.widthProperty());
        //https://stackoverflow.com/questions/38216268/how-to-listen-resize-event-of-stage-in-javafx
        
        ChangeListener<Number> canvasSizeListener = (ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            drawableGrid.heightProperty().set(canvas.getHeight());
            drawableGrid.widthProperty() .set(canvas.getWidth());
            redraw();
        };
        
        canvas.widthProperty() .addListener(canvasSizeListener);
        canvas.heightProperty().addListener(canvasSizeListener);
        
        drawableGrid = new Canvas(canvas.getWidth(), canvas.getHeight());
        
        redraw();
    }
    
    public Grid(Canvas canvas, Visao axis, int cellSizePX){ this(canvas, axis.getImage(), cellSizePX); }

    public Grid(Canvas canvas, Visao axis){ this(canvas, axis, 50);}
    
    private double calcLowerY(Image imgToDraw, Canvas canvas, int pxLowerMargin){
        double max = canvas.getHeight(); 
        return max - pxLowerMargin - imgToDraw.getHeight();
    }
    
    private void redraw(){
        GraphicsContext graph = drawableGrid.getGraphicsContext2D();
        graph.clearRect(0, 0, drawableGrid.getWidth(), drawableGrid.getHeight());
        
        graph.drawImage(AXIS, X_MARGIN, calcLowerY(AXIS, drawableGrid, Y_MARGIN));
        
        graph.setStroke(Color.RED); //Cor da linha
        graph.setLineWidth(5); //Espessura da linha

          //HERE
        graph.strokeLine(0, 0, drawableGrid.getWidth(), drawableGrid.getHeight());


          //END
        
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        this.setImage(drawableGrid.snapshot(parameters, new WritableImage((int) drawableGrid.getWidth(), (int) drawableGrid.getHeight())));
    }

    public void setCellSizePX(int cellSizePX) {
        this.cellSizePX = cellSizePX;
        redraw();
    }
    
    public void setGridColor(Color color){
        if (color != null)
            this.gridColor = color;
    }
    
    public void setGridThickness(int thick){
        if (thick > 0)
            this.gridThickness = thick;
    }
    
    public void setGridProperties(Color color, int thickness){
        setGridColor(color);
        setGridThickness(thickness);
    }
}
