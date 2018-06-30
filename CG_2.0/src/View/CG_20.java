/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.List;
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
import m.poligonos.Aresta;
import m.poligonos.Face;
import m.poligonos.Poligono;
import m.poligonos.Vertice;
import utils.config.StandardConfigCam;
import utils.config.StandardConfigWinView;
import utils.math.PMath;

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
        
        Aresta x_axis = new Aresta(new Vertice(0,0,0), new Vertice(5000,0,0));
        Aresta y_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,5000,0));
        Aresta z_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,5000));
        
        Aresta mx_axis = new Aresta(new Vertice(0,0,0), new Vertice(-5000,0,0));
        Aresta my_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,-5000,0));
        Aresta mz_axis = new Aresta(new Vertice(0,0,0), new Vertice(0,0,-5000));
        
        Poligono pol = new Poligono(pol_mat);
        List<Face> lista = PMath.attemptBuildingFromPlanes(pol);

        mundo.addObject(lista);
        mundo.addObject(x_axis, y_axis, z_axis, mx_axis, my_axis, mz_axis);
        
        //PaintController paint = new PaintController();
        //RegularPolygonController regular = new RegularPolygonController();
        
        FXMLLoader mainLoader = new FXMLLoader(getClass().getResource("Main.fxml"));
        mainLoader.setController(main);
        
        Parent root = mainLoader.load();
        
        Scene scene = new Scene(root);
        
        stage.setTitle("Modelador JSA (CG)");
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
