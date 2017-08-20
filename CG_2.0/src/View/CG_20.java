/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import View.Options.PaintController;
import View.Options.RegularPolygonController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author JFPS
 */
public class CG_20 extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        PaintController paint = new PaintController();
        RegularPolygonController regular = new RegularPolygonController();
        
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainLoader.setController(new MainController());
        
        Parent root = mainLoader.load();
        
        Scene scene = new Scene(root);
        
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}

/*
https://www.youtube.com/watch?v=EBKHdV-_rIc
https://gist.github.com/jewelsea/1475424
https://gist.github.com/jewelsea/5375786



*/
