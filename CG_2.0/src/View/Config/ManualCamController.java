/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Config;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

/**
 *
 * @author JFPS
 */
public class ManualCamController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField vrpX;
    @FXML private TextField vrpY;
    @FXML private TextField vrpZ;
    @FXML private TextField pX;
    @FXML private TextField pY;
    @FXML private TextField pZ;
    @FXML private TextField viewupX;
    @FXML private TextField viewupY;
    @FXML private TextField viewupZ;
    
    @FXML private Button resetVRP;
    @FXML private Button resetP;
    @FXML private Button resetViewUp;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
        /*okButton.setOnAction((ActionEvent event) -> {
            CG_20.main.finalizeTempPoints();
        });
        
        cancelButton.setOnAction((ActionEvent event) -> {
            CG_20.main.cancelTempPoints();
        });*/
    }
}
