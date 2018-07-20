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
import utils.math.VMath;

/**
 *
 * @author JFPS
 */
public class Grid extends ImageView{
    private static final int X_MARGIN = 5;
    private static final int Y_MARGIN = 5;
    
    private final Image AXIS;
    private final Image AUTO;
    private Canvas drawableGrid;
    
    private int cellSizePX;
    private Color gridColor = Color.LIGHTGRAY;
    private int gridThickness = 2;
    
    private boolean showGrid = true;
    private boolean showAxisIcon = true;
    private boolean showAutoHotkeys = false;
    
    public Grid(Canvas canvas, Image axis, int cellSizePX, Image auto) {
        this.AXIS = axis;
        this.AUTO = auto;
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
    
    public Grid(Canvas canvas, Visao axis, int cellSizePX){ this(canvas, axis.getImage(), cellSizePX, Visao.getAutoHotkeyImage(axis)); }

    public Grid(Canvas canvas, Visao axis){ this(canvas, axis, 30);}
    
    private double calcLowerY(Image imgToDraw, Canvas canvas, int pxLowerMargin){
        double max = canvas.getHeight(); 
        return max - pxLowerMargin - imgToDraw.getHeight();
    }
    
    private double calcLowerX(Image imgToDraw, Canvas canvas, int pxLowerMargin){
        double max = canvas.getWidth(); 
        return max - pxLowerMargin - imgToDraw.getWidth();
    }
    
    private void redraw(){
        int gridWidth  = (int) drawableGrid.getWidth(),
            gridHeight = (int) drawableGrid.getHeight();
        
        GraphicsContext graph = drawableGrid.getGraphicsContext2D();
        graph.clearRect(0, 0, gridWidth, gridHeight);
        
        //Grid por baixo de tudo
        if (showGrid){
            graph.setStroke(gridColor); //Cor da linha
            graph.setLineWidth(gridThickness); //Espessura da linha
            
            //HERE
            int currentHeight=cellSizePX;
            while (currentHeight < gridHeight){
                graph.strokeLine(0, currentHeight, gridWidth, currentHeight);
                currentHeight += cellSizePX;
            }
            
            int currentWidth=cellSizePX;
            while (currentWidth < gridWidth){
                graph.strokeLine(currentWidth, 0, currentWidth, gridHeight);
                currentWidth += cellSizePX;
            }

            //END
        }
        
        if (showAxisIcon)
            graph.drawImage(AXIS, X_MARGIN, calcLowerY(AXIS, drawableGrid, Y_MARGIN));
        
        if (showAutoHotkeys)
            graph.drawImage(AUTO, calcLowerX(AUTO, drawableGrid, X_MARGIN), calcLowerY(AUTO, drawableGrid, Y_MARGIN));
        
        SnapshotParameters parameters = new SnapshotParameters();
        parameters.setFill(Color.TRANSPARENT);
        this.setImage(drawableGrid.snapshot(parameters, new WritableImage((int) gridWidth, (int) gridHeight)));
    }

    public void setCellSizePX(int cellSizePX) {
        this.cellSizePX = cellSizePX;
        redraw();
    }
    
    public void setGridColor(Color color){
        if (color != null){
            this.gridColor = color;
            redraw();
        }
    }
    
    public void setGridThickness(int thick){
        if (thick > 0){
            this.gridThickness = thick;
            redraw();
        }
    }
        
    public void showGrid(boolean show){
        if (showGrid != show){
            this.showGrid = show;
            redraw();
        }        
    }
    
    public void showAxisIcon(boolean show){
        if (showAxisIcon != show){
            this.showAxisIcon = show;
            redraw();
        }  
    }
    
    public void showHotkeys(boolean show){
        if (showAutoHotkeys != show){
            this.showAutoHotkeys = show;
            redraw();
        }  
    }
    
    public float[] closestIntercept(float x, float y, int threshold){
        int x_div = (int) (x/cellSizePX);
        int y_div = (int) (y/cellSizePX);
        
        // Left Top
        int x_cell = x_div*cellSizePX;
        int y_cell = y_div*cellSizePX;
        double dist = VMath.distancia(x_cell, y_cell, x, y);
        if (dist < threshold){
            return new float[]{ x_cell, y_cell};
        }
        
        // Right Top
        x_cell = (x_div+1)*cellSizePX;
        dist = VMath.distancia(x_cell, y_cell, x, y);
        if (dist < threshold){
            return new float[]{ x_cell, y_cell};
        }
        
        // Left Bottom
        x_cell = x_div*cellSizePX;
        y_cell = (y_div+1)*cellSizePX;
        dist = VMath.distancia(x_cell, y_cell, x, y);
        if (dist < threshold){
            return new float[]{ x_cell, y_cell};
        }
        
        // Right Bottom
        x_cell = (x_div+1)*cellSizePX;
        dist = VMath.distancia(x_cell, y_cell, x, y);
        if (dist < threshold){
            return new float[]{ x_cell, y_cell};
        }
        
        return null;
    }
}
