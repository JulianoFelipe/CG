/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Config;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class BackgroundColorsController implements Initializable {
    
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private ColorPicker frentePicker;
    @FXML private Button      frenteSemCor;

    @FXML private ColorPicker lateralPicker;
    @FXML private Button      lateralSemCor;
    
    @FXML private ColorPicker topoPicker;
    @FXML private Button      topoSemCor;
    
    @FXML private ColorPicker persPicker;
    @FXML private Button      persSemCor;
    
    private final ObjectProperty<Color> frenteColor;
    private final ObjectProperty<Color> lateralColor;
    private final ObjectProperty<Color> topoColor;
    private final ObjectProperty<Color> persColor;
    
    public BackgroundColorsController(ObjectProperty<Color> frenteColor, ObjectProperty<Color> lateralColor, ObjectProperty<Color> topoColor, ObjectProperty<Color> persColor) {
        this.frenteColor = frenteColor;
        this.lateralColor = lateralColor;
        this.topoColor = topoColor;
        this.persColor = persColor;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frentePicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            frenteColor.setValue(newValue);
        });
        frenteSemCor.setOnAction((ActionEvent event) -> {
            frenteColor.setValue(null);
        });
        
        lateralPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            lateralColor.setValue(newValue);
        });
        lateralSemCor.setOnAction((ActionEvent event) -> {
            lateralColor.setValue(null);
        });
        
        topoPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            topoColor.setValue(newValue);
        });
        topoSemCor.setOnAction((ActionEvent event) -> {
            topoColor.setValue(null);
        });
        
        persPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            persColor.setValue(newValue);
        });
        persSemCor.setOnAction((ActionEvent event) -> {
            persColor.setValue(null);
        });
    }
}
