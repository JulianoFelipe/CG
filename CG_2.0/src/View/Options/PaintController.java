/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class PaintController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    private static final String PINTAR_FUNDO = "Pintar fundo";
    private static final String PINTAR_BORDA = "Pintar borda";
    private static final byte PINTAR_FUNDO_CODE = 0;
    private static final byte PINTER_BORDA_CODE = 1;
    
    @FXML
    private ColorPicker colorPicker;
    private Color currentColor;
    
    @FXML
    private ChoiceBox choicePaint;
    private byte currentPaint;
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        choicePaint.getItems().addAll(PINTAR_FUNDO, PINTAR_BORDA);
        choicePaint.setValue(choicePaint.getItems().get(1));
    }
    
    @FXML
    private void onMouseClickedUpdateColor(MouseEvent e){
        currentColor = colorPicker.getValue();
        System.out.println(currentColor);
    } 
    
    @FXML
    private void onMouseClickedUpdatePaint(MouseEvent e){
        String choice = (String) choicePaint.getValue();
        
        if(choice.equals(PINTAR_FUNDO))
            currentPaint = PINTAR_FUNDO_CODE;
        else
            currentPaint = PINTER_BORDA_CODE;
    } 
}
