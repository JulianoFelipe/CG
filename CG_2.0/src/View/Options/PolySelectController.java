/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;
import m.Camera;
import m.Visao;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
import utils.config.StandardConfigCam;

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
    @FXML private CheckBox isChromatic;
    
    private final ObjectProperty<CGObject> objProperty;
    private boolean changeKA;
    private boolean changeKD;
    private boolean changeKS;
    private boolean changeColorGamut;
    
    public ObjectProperty<CGObject> objectProperty() {
        return objProperty;
    }

    public PolySelectController(CGObject obj) {
        objProperty = new SimpleObjectProperty<>(obj);
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

        okButton.setOnAction((ActionEvent event) -> {
            if (objProperty.get() != null)
                propagate();
        });
        
        CGObject obj = objProperty.get();
        if (obj == null) return;
        setFields(obj);
    }
    
    private void setFields(CGObject obj){
        setTextFields(kaR, kaG, kaB, obj.getKa());
        setTextFields(kdR, kdG, kdB, obj.getKd());
        setTextFields(ksR, ksG, ksB, obj.getKs());

        objType.  setText(obj.getClass().getSimpleName());
        objID.    setText(String.valueOf(obj.getID()));
        objPoints.setText(String.valueOf(obj.getNumberOfPoints()));
        ksN.      setText(Float.toString(obj.getKs().getW()));
        isChromatic.selectedProperty().set(obj.isChromatic());
    }
       
    private void propagate(){
        if (changeColorGamut==false
                 && changeKA==false
                 && changeKD==false
                 &&changeKS==false)
            return;
        
        CGObject localCopy = objProperty.get();
        
        Vertice newKa = localCopy.getKa(),
                newKd = localCopy.getKd(),
                newKs = localCopy.getKs();
        
        if (changeKA){
            newKa = new Vertice(
                Float.parseFloat( kaR.textProperty().get() ),
                Float.parseFloat( kaG.textProperty().get() ),
                Float.parseFloat( kaB.textProperty().get() )
            );
            changeKA = false;
        }
        
        if (changeKD){
            newKd = new Vertice(
                Float.parseFloat( kdR.textProperty().get() ),
                Float.parseFloat( kdG.textProperty().get() ),
                Float.parseFloat( kdB.textProperty().get() )
            );
            changeKD = false;
        }
        
        if (changeKS){
            newKs = new Vertice(
                Float.parseFloat( ksR.textProperty().get() ),
                Float.parseFloat( ksG.textProperty().get() ),
                Float.parseFloat( ksB.textProperty().get() )
            );
            newKs.setW(Float.parseFloat(ksN.textProperty().get()));
            changeKA = false;
        }
        
        localCopy.setKa(newKa);
        localCopy.setKd(newKd);
        localCopy.setKs(newKs);
        
        if (changeColorGamut){
            localCopy.setIsChromatic(isChromatic.selectedProperty().get());
            changeColorGamut = false;
        }
        
        objProperty.set( localCopy );
    }
    
    private void setTextFields(TextField x, TextField y, TextField z, Vertice newValue){
        x.textProperty().set(String.valueOf(newValue.getX()));
        y.textProperty().set(String.valueOf(newValue.getY()));
        z.textProperty().set(String.valueOf(newValue.getZ()));
    }
    
    private void setTextFields(TextField x, TextField y, TextField z, String newValue){
        x.textProperty().set(String.valueOf(newValue));
        y.textProperty().set(String.valueOf(newValue));
        z.textProperty().set(String.valueOf(newValue));
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
