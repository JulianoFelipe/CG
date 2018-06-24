/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import m.Visao;
import m.Vista;
import m.World;
import m.pipeline.OrtPipeline;
import m.pipeline.PersPipeline;
import m.poligonos.Poligono;
import utils.config.StandardConfigCam;
import utils.config.StandardConfigWinView;

/**
 *
 * @author JFPS
 */
public class CG_20 extends Application {
    public static final MainController main = new MainController();
    
    
    private static float DP = StandardConfigCam.PERS_DP;
    
    @Override
    public void start(Stage stage) throws Exception {
        World mundo = World.getInstance();
        mundo.setPlanes(
            new Vista(new OrtPipeline(Visao.Frontal, StandardConfigCam.getStandardCamera(Visao.Frontal),     StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT)),
            new Vista(new OrtPipeline(Visao.Lateral, StandardConfigCam.getStandardCamera(Visao.Lateral),     StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT)),
            new Vista(new OrtPipeline(Visao.Topo,    StandardConfigCam.getStandardCamera(Visao.Topo),        StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT)),
            new Vista(new PersPipeline(DP,           StandardConfigCam.getStandardCamera(Visao.Perspectiva), StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT))
        );

        float[][] pol_mat = {
            {  30,  35,  25,  20,  30},
            {   2,   4,   3,   1,  10},
            {  25,  20,  18,  23,  (float) 22.5},
            {   1,   1,   1,   1,   1}
            //  A    B    C    D    E
        };
        
        Poligono pol = new Poligono(pol_mat);
        mundo.addObject(pol);
        
        //PaintController paint = new PaintController();
        //RegularPolygonController regular = new RegularPolygonController();
        
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainLoader.setController(main);
        
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
