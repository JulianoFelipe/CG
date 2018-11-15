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
public class FatorAtenuacaoController implements Initializable {
    
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
    
    public FatorAtenuacaoController(ObjectProperty<Color> frenteColor, ObjectProperty<Color> lateralColor, ObjectProperty<Color> topoColor, ObjectProperty<Color> persColor) {
        this.frenteColor = frenteColor;
        this.lateralColor = lateralColor;
        this.topoColor = topoColor;
        this.persColor = persColor;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        frentePicker.valueProperty().set(frenteColor.get());
        lateralPicker.valueProperty().set(lateralColor.get());
        topoPicker.valueProperty().set(topoColor.get());
        persPicker.valueProperty().set(persColor.get());
        
        frentePicker.setOnAction((ActionEvent event) -> {
            if (frentePicker.getValue()==Color.TRANSPARENT || frentePicker.getValue().getOpacity()==0.0)
                frenteColor.setValue(null);
            else
                frenteColor.setValue(frentePicker.getValue());
        });
        frenteSemCor.setOnAction((ActionEvent event) -> {
            frenteColor.setValue(null);
        });
        
        lateralPicker.setOnAction((ActionEvent event) -> {
            if (lateralPicker.getValue()==Color.TRANSPARENT || lateralPicker.getValue().getOpacity()==0.0)
                lateralColor.setValue(null);
            else
                lateralColor.setValue(lateralPicker.getValue());
        });
        lateralSemCor.setOnAction((ActionEvent event) -> {
            lateralColor.setValue(null);
        });
        
        topoPicker.setOnAction((ActionEvent event) -> {
            if (topoPicker.getValue()==Color.TRANSPARENT || topoPicker.getValue().getOpacity()==0.0)
                topoColor.setValue(null);
            else
                topoColor.setValue(topoPicker.getValue());
        });
        topoSemCor.setOnAction((ActionEvent event) -> {
            topoColor.setValue(null);
        });
        
        persPicker.setOnAction((ActionEvent event) -> {
            if (persPicker.getValue()==Color.TRANSPARENT || persPicker.getValue().getOpacity()==0.0)
                persColor.setValue(null);
            else
                persColor.setValue(persPicker.getValue());
        });
        persSemCor.setOnAction((ActionEvent event) -> {
            persColor.setValue(null);
        });
    }
}
