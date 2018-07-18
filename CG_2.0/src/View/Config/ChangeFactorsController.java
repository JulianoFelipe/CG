/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Config;

import View.Fatores;
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
public class ChangeFactorsController implements Initializable {
    //https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    //https://stackoverflow.com/questions/35219394/javafx-wrap-an-existing-object-with-simple-properties
    
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField mov_ort;
    @FXML private Button    mov_ort_reset;
    @FXML private Button    mov_ort_ok;
    
    @FXML private TextField mov_pers;
    @FXML private Button    mov_pers_reset;
    @FXML private Button    mov_pers_ok;
    
    @FXML private TextField zoom;
    @FXML private Button    zoom_reset;
    @FXML private Button    zoom_ok;
    
    @FXML private TextField threshold;
    @FXML private Button    threshold_reset;
    @FXML private Button    threshold_ok;
    
    @FXML private TextField rotacao;
    @FXML private Button    rotacao_reset;
    @FXML private Button    rotacao_ok;
    
    @FXML private TextField escala;
    @FXML private Button    escala_reset;
    @FXML private Button    escala_ok;
    
    @FXML private TextField cisalhamento;
    @FXML private Button    cisalhamento_reset;
    @FXML private Button    cisalhamento_ok;    
   
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        setFields();
        ///////////////////
        mov_ort_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_movimento_ort = Fatores.DEFAULT_MOVIMENTO_ORT;
            mov_ort .setText(Float.toString(Fatores.fator_movimento_ort));
        });
        mov_ort_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_movimento_ort = Float.parseFloat(mov_ort.getText());
        });
        ///////////////////
        mov_pers_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_movimento_pers = Fatores.DEFAULT_MOVIMENTO_PERS;
            mov_pers .setText(Float.toString(Fatores.fator_movimento_pers));
        });
        mov_pers_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_movimento_pers = Float.parseFloat(mov_pers.getText());
        });
        ///////////////////
        zoom_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_zoom = Fatores.DEFAULT_ZOOM;
            zoom .setText(Float.toString(Fatores.fator_zoom));
        });
        zoom_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_zoom = Float.parseFloat(zoom.getText());
        });
        ///////////////////
        threshold_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_threshold = Fatores.DEFAULT_THRESHOLD;
            threshold .setText(Integer.toString(Fatores.fator_threshold));
        });
        threshold_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_threshold = Integer.parseInt(threshold.getText());
        });
        ///////////////////
        rotacao_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_rotacao = Fatores.DEFAULT_ROTACAO;
            rotacao .setText(Float.toString(Fatores.fator_rotacao));
        });
        rotacao_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_rotacao = Float.parseFloat(rotacao.getText());
        });
        ///////////////////
        escala_reset.setOnAction((ActionEvent event) -> {
            Fatores.setFatorEscala(Fatores.DEFAULT_ESCALA);
            escala .setText(Float.toString((Fatores.getFatorEscalaPlus()*100)-100));
        });
        escala_ok.setOnAction((ActionEvent event) -> {
            Fatores.setFatorEscala(Float.parseFloat(escala.getText()));
        });
        ///////////////////
        cisalhamento_reset.setOnAction((ActionEvent event) -> {
            Fatores.fator_cisalhamento = Fatores.DEFAULT_CISALHAMENTO;
            cisalhamento .setText(Float.toString(Fatores.fator_cisalhamento));
        });
        cisalhamento_ok.setOnAction((ActionEvent event) -> {
            Fatores.fator_cisalhamento = Float.parseFloat(cisalhamento.getText());
        });
        ///////////////////
    }
    
    private void setFields(){
        mov_ort .setText(Float.toString(Fatores.fator_movimento_ort));
        mov_pers.setText(Float.toString(Fatores.fator_movimento_pers));
        zoom.setText(Float.toString(Fatores.fator_zoom));
        threshold.setText(Float.toString(Fatores.fator_threshold));
        rotacao.setText(Float.toString(Fatores.fator_rotacao));
        escala.setText(Float.toString((Fatores.getFatorEscalaPlus()*100)-100));
        cisalhamento.setText(Float.toString(Fatores.fator_cisalhamento));
    }
}
