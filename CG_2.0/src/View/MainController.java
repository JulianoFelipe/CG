/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;

/**
 *
 * @author JFPS
 */
public class MainController implements Initializable {
    //https://www.youtube.com/watch?v=RY_Rb2UVQKQ
    
    @FXML
    TreeView<String> tools;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        TreeItem<String> root = new TreeItem<>("Root");
             
        TreeItem<String> ferramentas    = new TreeItem<>("Ferramentas");
        TreeItem<String> poligonos      = new TreeItem<>("Polígonos");
        TreeItem<String> transformacoes = new TreeItem<>("Transformações");

        ferramentas.getChildren().addAll(
            new TreeItem<>("Seleção"),
            new TreeItem<>("Exclusão"),
            new TreeItem<>("Pintura")
        );
        
        poligonos.getChildren().addAll(
            new TreeItem<>("Regular"),
            new TreeItem<>("Irregular"),
            new TreeItem<>("Circulo")
        );
        
        transformacoes.getChildren().addAll(
            new TreeItem<>("Rotação"),
            new TreeItem<>("Translação"),
            new TreeItem<>("Cisalhamento")
        );
        
        root.getChildren().addAll(ferramentas, poligonos, transformacoes);
        
        tools.setRoot(root);
        tools.setShowRoot(false);
    }
    
}
