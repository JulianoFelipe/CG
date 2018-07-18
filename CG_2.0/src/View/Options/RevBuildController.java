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
            CG_20.main.finalizeTempPoints();
        });
        
        cancelButton.setOnAction((ActionEvent event) -> {
            CG_20.main.cancelTempPoints();
        });
    }
}
