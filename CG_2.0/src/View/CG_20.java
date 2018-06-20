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
import m.Camera;
import m.Viewport;
import m.Visao;
import m.Vista;
import m.Window;
import m.World;
import m.poligonos.Vertice;
import m.pipeline.OrtPipeline;
import m.pipeline.PersPipeline;
import m.poligonos.Poligono;

/**
 *
 * @author JFPS
 */
public class CG_20 extends Application {
    
    public static final MainController main = new MainController();
    
    ///                                          ViewUp              VRP                                                 p
    private static Camera cam1 = new Camera( new Vertice(0,1,0), new Vertice((float) 0, (float) 0, (float) 0), new Vertice(0, 0, 0) );
    private static Window   win1  = new Window(544, -374); //?????
    private static Viewport view1 = new Viewport( new Vertice(0, 0), new Vertice(544, 374) );
    private static Camera cam2 = new Camera( new Vertice(0,1,0), new Vertice((float) 10, (float) 0, (float) 0), new Vertice(0, 0, 0) );
    private static Camera cam3 = new Camera( new Vertice(0,0,1), new Vertice((float) 0, (float) 10, (float) 0), new Vertice(0, 0, 0) );
    private static Camera cam4 = new Camera( new Vertice(0,1,0), new Vertice((float) 50, (float) 15, (float) 30), new Vertice(0, 0, 0) );
    private static float DP = 500;
    
    @Override
    public void start(Stage stage) throws Exception {
        World mundo = World.getInstance();
        mundo.setPlanes(
            new Vista(new OrtPipeline(Visao.Frontal, cam1, win1, view1)),
            new Vista(new OrtPipeline(Visao.Lateral, cam2, win1, view1)),
            new Vista(new OrtPipeline(Visao.Topo,    cam3, win1, view1)),
            new Vista(new PersPipeline(DP,           cam4, win1, view1))
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
