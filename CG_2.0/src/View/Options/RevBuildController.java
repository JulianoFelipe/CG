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
import javafx.scene.control.TextField;

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
    
    @FXML
    private TextField sectionsField;
    @FXML
    private TextField angleField;
    @FXML
    private Button okButton;
    @FXML
    private Button cancelButton;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        //sidesField.setText("3");
        
        /*sidesSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sidesField.textProperty().setValue( String.valueOf((int) sidesSlider.getValue()));
            }
        });*/

        /*sidesField.textProperty().addListener((observable, oldValue, newValue) -> {
            numberOfSides = Integer.parseInt(sidesField.getText());
        });*/
        
        okButton.setOnAction((ActionEvent event) -> {
            int sections = defaultIntParser(sectionsField.textProperty().get(), MIN_SEC, MAX_SEC);
            int graus    = defaultIntParser(angleField   .textProperty().get(), MIN_GRD, MAX_GRD);
            
            fillFields(sections, graus);
            
            CG_20.main.finalizeTempPoints(sections, graus);
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
