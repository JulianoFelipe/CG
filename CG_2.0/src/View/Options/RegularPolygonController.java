/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;

/**
 *
 * @author JFPS
 */
public class RegularPolygonController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML
    private Slider sidesSlider;
    @FXML
    private TextField sidesField;
    private int numberOfSides;
    
    @FXML
    private Slider angleSlider;
    @FXML
    private TextField angleField;
    private int initAngle;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        sidesField.setText("3");
        
        sidesSlider.valueProperty().addListener(new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                sidesField.textProperty().setValue( String.valueOf((int) sidesSlider.getValue()));
            }
        });
        
        sidesField.textProperty().addListener((observable, oldValue, newValue) -> {
            numberOfSides = Integer.parseInt(sidesField.getText());
        });
    }

    public int getNumberOfSides() {
        return numberOfSides;
    }

    public int getInitAngle() {
        return initAngle;
    }
}
