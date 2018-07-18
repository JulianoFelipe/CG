/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import View.Fatores;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import m.Eixo;

/**
 *
 * @author JFPS
 */
public class EscalaController implements Initializable {
    //https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    //https://stackoverflow.com/questions/35219394/javafx-wrap-an-existing-object-with-simple-properties
    
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField factorField;
    @FXML private Button    factorReset;
    @FXML private Button    factorOk;
       
    @FXML private RadioButton justX;
    @FXML private RadioButton justY;
    @FXML private RadioButton both;
   
    private final ObjectProperty<Eixo> axisOfOperationProperty;

    public EscalaController(ObjectProperty<Eixo> axisOfOperationProperty) {
        this.axisOfOperationProperty = axisOfOperationProperty;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        factorField.setText(Float.toString((Fatores.getFatorEscalaPlus()*100)-100));
        
        Eixo axis = axisOfOperationProperty.get();
             if (axis == Eixo.Eixo_X)  justX.selectedProperty().set(true); 
        else if (axis == Eixo.Eixo_Y)  justY.selectedProperty().set(true);
        else if (axis == Eixo.Eixo_XY) both .selectedProperty().set(true); 
        else throw new IllegalArgumentException("Eixo de operação não esperado: " + axis);
        
        factorReset.setOnAction((ActionEvent event) -> {
            Fatores.setFatorEscala(Fatores.DEFAULT_ESCALA);
            factorField.setText(Float.toString((Fatores.getFatorEscalaPlus()*100)-100));
        });
        factorOk.setOnAction((ActionEvent event) -> {
            Fatores.setFatorEscala(Float.parseFloat(factorField.getText()));
        });

        justX.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (justX.selectedProperty().get()){
                axisOfOperationProperty.set(Eixo.Eixo_X);
            }
        });
        
        justY.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (justY.selectedProperty().get()){
                axisOfOperationProperty.set(Eixo.Eixo_Y);
            }
        });
        
        both.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (both.selectedProperty().get()){
                axisOfOperationProperty.set(Eixo.Eixo_XY);
            }
        });
    }

}
