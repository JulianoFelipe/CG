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
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import m.World;
import m.poligonos.CGObject;

/**
 *
 * @author JFPS
 */
public class PolySelectController implements Initializable {    
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField kaR;
    @FXML private TextField kaG;
    @FXML private TextField kaB;
    @FXML private TextField kdR;
    @FXML private TextField kdG;
    @FXML private TextField kdB;
    @FXML private TextField ksR;
    @FXML private TextField ksG;
    @FXML private TextField ksB;
    @FXML private TextField ksN;
    
    @FXML private TextField objType;
    @FXML private TextField objID;
    @FXML private TextField objPoints;
    
    @FXML private Button   okButton;
    @FXML private Button   deleteThis;
    @FXML private CheckBox isChromatic;
    
    @FXML private Label vermelhoLabel;
    @FXML private Label verdeLabel;
    @FXML private Label azulLabel;
    
    private final MainController mainController;
    private final ObjectProperty<CGObject> objProperty;
    private boolean changeKA;
    private boolean changeKD;
    private boolean changeKS;
    private boolean changeColorGamut;
    
    public ObjectProperty<CGObject> objectProperty() {
        return objProperty;
    }

    public PolySelectController(ObjectProperty<CGObject> objProperty, MainController controller) {
        //objProperty = new SimpleObjectProperty<>(obj);
        this.mainController = controller;
        this.objProperty = objProperty;
        objProperty.addListener((ObservableValue<? extends CGObject> observable, CGObject oldValue, CGObject newValue) -> {
            if (newValue != null)
                setFields(newValue);
            else
                clearFields();
            
            changeKA = changeKD = changeKS = changeColorGamut = false;
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // KA LISTENERS
        kaR.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKA = true;
        });
        kaG.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKA = true;
        });
        kaB.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKA = true;
        });
        
        //KD LISTENERS
        kdR.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKD = true;
        });
        kdG.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKD = true;
        });
        kdB.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKD = true;
        });
        
        // KS LISTENERS
        ksR.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKS = true;
        });
        ksG.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKS = true;
        });
        ksB.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKS = true;
        });
        ksN.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeKS = true;
        });

        isChromatic.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            changeColorGamut = true;
            if (newValue){
                kaG.setDisable(false); kaB.setDisable(false);
                kdG.setDisable(false); kdB.setDisable(false);
                ksG.setDisable(false); ksB.setDisable(false);
                vermelhoLabel.setText("Vermelho");
                verdeLabel.setText("Verde");
                azulLabel.setText("Azul");
            } else {
                kaG.setDisable(true); kaB.setDisable(true);
                kdG.setDisable(true); kdB.setDisable(true);
                ksG.setDisable(true); ksB.setDisable(true);
                vermelhoLabel.setText("Intensidade");
                verdeLabel.setText("");
                azulLabel.setText("");
            }
        });
        
        okButton.setOnAction((ActionEvent event) -> {
            if (objProperty.get() != null)
                propagate();
        });
        
        deleteThis.setOnAction((ActionEvent event) -> {
            if (objProperty.get() != null){
                Alert alert = new Alert(AlertType.CONFIRMATION, "Deseja excluir o objeto \"" + objProperty.get() + "\" ?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    World.getInstance().removeObject(objProperty.get().getID());
                    objProperty.set(null);
                    mainController.paint();
                }
            }
        });
        
        CGObject obj = objProperty.get();
        if (obj == null) return;
        setFields(obj);
    }
    
    private void setFields(CGObject obj){
        if (obj.isKset()){
            setTextFields(kaR, kaG, kaB, obj.getKa());
            setTextFields(kdR, kdG, kdB, obj.getKd());
            setTextFields(ksR, ksG, ksB, obj.getKs());
            ksN.setText(String.valueOf(obj.getKs()[3]));
        }
        
        objType.  setText(obj.getClass().getSimpleName());
        objID.    setText(String.valueOf(obj.getID()));
        objPoints.setText(String.valueOf(obj.getNumberOfPoints()));
        isChromatic.selectedProperty().set(obj.isChromatic());
        
    }
       
    private void propagate(){
        if (changeColorGamut==false
                 && changeKA==false
                 && changeKD==false
                 && changeKS==false)
            return;
                
        float[] newKa,
                newKd,
                newKs;
        
        if (changeKA || changeKD || changeKS){
            newKa = new float[]{
                capCheck(defaultFloatParser( kaR.textProperty().get()),0,1),
                capCheck(defaultFloatParser( kaG.textProperty().get()),0,1),
                capCheck(defaultFloatParser( kaB.textProperty().get()),0,1)
            };

            newKd = new float[]{
                capCheck(defaultFloatParser( kdR.textProperty().get()),0,1),
                capCheck(defaultFloatParser( kdG.textProperty().get()),0,1),
                capCheck(defaultFloatParser( kdB.textProperty().get()),0,1)
            };

            newKs = new float[]{
                capCheck(defaultFloatParser(ksR.textProperty().get()),0,1),
                capCheck(defaultFloatParser(ksG.textProperty().get()),0,1),
                capCheck(defaultFloatParser(ksB.textProperty().get()),0,1),
                defaultFloatParser( ksN.textProperty().get() )
            };

            objProperty.get().setAllK(newKa, newKd, newKs);
            changeKA = changeKD = changeKS = false;
        }
                
        if (changeColorGamut){
            objProperty.get().setIsChromatic(isChromatic.selectedProperty().get());
            changeColorGamut = false;
        }
        
        //objProperty.set( objProperty.get() ); //Era pra tentar forçar a alteração para que os CGCanvases copiassem os KAs, KDs...
        mainController.forceSelectedObjUpdate();
        setFields(objProperty.get());
    }
    
    private void setTextFields(TextField x, TextField y, TextField z, float[] newValue){
        x.textProperty().set(String.valueOf(newValue[0]));
        y.textProperty().set(String.valueOf(newValue[1]));
        z.textProperty().set(String.valueOf(newValue[2]));
    }
    
    private void setTextFields(TextField x, TextField y, TextField z, String newValue){
        x.textProperty().set(String.valueOf(newValue));
        y.textProperty().set(String.valueOf(newValue));
        z.textProperty().set(String.valueOf(newValue));
    }
    
    private float defaultFloatParser(String text){
        if (text==null || text.isEmpty()) return 0;
        else {
            float test;
            try{
                test = Float.parseFloat(text);               
            } catch(NumberFormatException e){
                test = 0;
            }
            return test;
        }
    }
    
    private float capCheck(float value, float min, float max){
        float r = Math.min(value, max);
        r = Math.max(r, min);
        return r;
    }
    
    private void clearFields(){
        setTextFields(kaR, kaG, kaB, "");
        setTextFields(kdR, kdG, kdB, "");
        setTextFields(ksR, ksG, ksB, "");

        objType.  setText(null);
        objID.    setText(null);
        objPoints.setText(null);
        ksN.      setText(null);
    }
}
