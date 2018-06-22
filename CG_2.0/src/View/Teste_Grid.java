/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;


import java.io.IOException;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.image.ImageView;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.SnapshotParameters;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;


public class Teste_Grid extends Application {
    
    class Teste_Controller {
        @FXML private Canvas canvas;
        @FXML private ImageView imageView;

        private Canvas tempCanvas;
        
        @FXML
        public void initialize() {
            canvas.setOnMouseClicked((MouseEvent event) -> {
                //System.out.println("CLICK ON CANVAS");
                GraphicsContext graphs = canvas.getGraphicsContext2D();
                graphs.setFill(Color.BLACK);
                graphs.setStroke(Color.BLACK);
                graphs.setLineWidth(1);
                graphs.fillOval(event.getX(), event.getY(), 5, 5);
            });
            
            Image axis = new Image("/resource/images/Lateral.png");            
            tempCanvas = new Canvas(600, 400);
            
            // Adiciona a imagem do eixo lateral no canto inferior
            tempCanvas.getGraphicsContext2D().drawImage(axis, 5, calcLowerY(axis, tempCanvas, 5));
                       
            imageView.setImage(desenharGrid(tempCanvas));
        }
        
        private WritableImage desenharGrid(Canvas canvas){
            GraphicsContext graph = canvas.getGraphicsContext2D();
            graph.setStroke(Color.RED); //Cor da linha
            graph.setLineWidth(5); //Espessura da linha
            
            ///Desenha aqui
            graph.strokeLine(0, 0, canvas.getWidth(), canvas.getHeight());
            
            
            
            SnapshotParameters parameters = new SnapshotParameters();
            parameters.setFill(Color.TRANSPARENT);
            return canvas.snapshot(parameters, new WritableImage(600, 400));
        }
        
        private double calcLowerY(Image imgToDraw, Canvas canvas, int pxLowerMargin){
            double max = canvas.getHeight(); 
            return max - pxLowerMargin - imgToDraw.getHeight();
        }
    }
    
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader loader = new FXMLLoader(
            getClass().getResource(
                "Teste_Grid.fxml"
            )
        );
        loader.setController(new Teste_Controller());

        Pane batman = loader.load();

        stage.setTitle("Where's Batman?");
        stage.setScene(new Scene(batman));
        stage.show();
        stage.setResizable(false);
    }

    public static void main(String[] args) {
        launch(args);
    }
}