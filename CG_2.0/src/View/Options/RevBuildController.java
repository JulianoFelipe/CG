/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import View.CG_20;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import m.Eixo;

/**
 *
 * @author JFPS
 */
public class RevBuildController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    public static final int MIN_SEC = 2;
    public static final int MAX_SEC = 50;
    
    public static final int MIN_GRD = 0;
    public static final int MAX_GRD = 360;
    
    @FXML private TextField sectionsField;
    @FXML private TextField angleField;
    @FXML private ChoiceBox<String> choiceBox;
    @FXML private Button okButton;
    @FXML private Button cancelButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choiceBox.getItems().add("Eixo X");
        choiceBox.getItems().add("Eixo Y");
        choiceBox.getItems().add("Eixo Z");
        choiceBox.selectionModelProperty().get().select(0);
        
        okButton.setOnAction((ActionEvent event) -> {
            int sections = defaultIntParser(sectionsField.textProperty().get(), MIN_SEC, MAX_SEC);
            int graus    = defaultIntParser(angleField   .textProperty().get(), MIN_GRD, MAX_GRD);
            
            Eixo axis = Eixo.eixoFromSpaceString(choiceBox.getSelectionModel().getSelectedItem());
            fillFields(sections, graus);
            
            CG_20.main.finalizeTempPoints(sections, graus, axis);
        });
        
        cancelButton.setOnAction((ActionEvent event) -> {
            CG_20.main.cancelTempPoints();
        });
    }
    
    private int defaultIntParser(String text, int minCap, int maxCap){
        if (text==null || text.isEmpty()) return 0;
        else {
            int test;
            try{
               test = Integer.parseInt(text);
            } catch(NumberFormatException e){
                test = 0;
            }
            
            test = Math.min(test, maxCap);
            test = Math.max(test, minCap);
            
            return test;
        }
    }
    
    private void fillFields(int sec, int grd){
        sectionsField.textProperty().set(Integer.toString(sec));
        angleField   .textProperty().set(Integer.toString(grd));
    }
}
