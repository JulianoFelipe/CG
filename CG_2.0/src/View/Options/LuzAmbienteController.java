/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import View.MainController;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import m.shader.AmbientLight;

/**
 *
 * @author JFPS
 */
public class LuzAmbienteController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField intensidadeField;
    @FXML private Label     intensidadeLabel;
    @FXML private ColorPicker corPicker;
    @FXML private Label       corLabel;
    @FXML private CheckBox chromaticCheck;
    @FXML private Button   okButton;
    
    private boolean chromaticChangeFlag;
    private boolean colorChangeFlag;
    private boolean intensidadeChangeFlag;
    
    private final MainController mainController;
    private final AmbientLight light;

    public LuzAmbienteController(MainController controller) {
        this.mainController = controller;
        chromaticChangeFlag = colorChangeFlag = intensidadeChangeFlag = false;
        light = controller.getAmbientLight();
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {   
        if (light != null) setFields(light);
        
        okButton.setOnAction((ActionEvent event) -> {
            okButton.setDisable(true);
            
            if (chromaticChangeFlag){
                if (intensidadeChangeFlag){
                    light.setColor(corPicker.getValue());
                } else if (colorChangeFlag){
                    light.setIntensidade(Float.parseFloat(intensidadeField.getText()));
                }
                colorChangeFlag = intensidadeChangeFlag = false;
            }
                        
            if (colorChangeFlag){
                light.setColor(corPicker.getValue());
            }
            
            if (intensidadeChangeFlag){
                light.setIntensidade(Float.parseFloat(intensidadeField.getText()));
            }
            
            chromaticChangeFlag = colorChangeFlag = intensidadeChangeFlag = false;
            
            mainController.setAmbientLight(light);
        });
        
        chromaticCheck.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            chromaticChangeFlag = true;
            okButton.setDisable(false);
            if (newValue){
                intensidadeLabel.setDisable(true);
                intensidadeField.setDisable(true);
                corLabel .setDisable(false);
                corPicker.setDisable(false);
            } else {
                corLabel .setDisable(true);
                corPicker.setDisable(true);
                intensidadeLabel.setDisable(false);
                intensidadeField.setDisable(false);
            }
        });
               
        intensidadeField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(false);
            intensidadeChangeFlag = true;
        });
        
        corPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            okButton.setDisable(false);
            colorChangeFlag = true;
        });
    }
    
    private void setFields(AmbientLight newValue){       
        if (newValue.isChromatic()){
            chromaticCheck.selectedProperty().set(true);
            corPicker.valueProperty().set(newValue.getColor());
        } else {
            chromaticCheck.selectedProperty().set(false);
            intensidadeField.textProperty().set(Double.toString(newValue.getIntensidade()));
        }
    }
}
