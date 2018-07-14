/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.util.ArrayList;
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
import m.poligonos.we_edge.IndexList;
import m.poligonos.we_edge.WE_Poliedro;
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
                
        int[] face0 = {0, 3, 2, 1};
        int[] face1 = {0, 1, 4};
        int[] face2 = {1, 2, 4};
        int[] face3 = {2, 3, 4};
        int[] face4 = {3, 0, 4};
        List<IndexList> faces = new ArrayList();
        faces.add(new IndexList(face0));
        faces.add(new IndexList(face1));
        faces.add(new IndexList(face2));
        faces.add(new IndexList(face3));
        faces.add(new IndexList(face4));
        
        WE_Poliedro poli = new WE_Poliedro(pol_mat, faces);
        
        //PointObject pol = new PointObject(pol_mat);
        //List<Face> lista = PMath.attemptBuildingFromPlanes(pol);

        //mundo.addObject(lista);
        //mundo.addObject(new Aresta(new Vertice(0,0,0), new Vertice(10,10,10)));
        mundo.addObject(poli);
        
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
