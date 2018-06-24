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
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import m.Camera;
import m.Visao;
import m.poligonos.Vertice;
import utils.config.StandardConfigCam;

/**
 *
 * @author JFPS
 */
public class ManualCamController implements Initializable {
    //https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    //https://stackoverflow.com/questions/35219394/javafx-wrap-an-existing-object-with-simple-properties
    
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
    
    @FXML private Button okVRP;
    @FXML private Button okP;
    @FXML private Button okViewUp;
    
    private final ObjectProperty<Camera> camProperty;
    private final Visao visao;
    private boolean changeVRP;
    private boolean changeP;
    private boolean changeViewUp;
    
    public ObjectProperty<Camera> camProperty() {
        return camProperty;
    }

    public ManualCamController(Camera cam, Visao vis) {
        camProperty = new SimpleObjectProperty<>(cam);
        visao = vis;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // VRP LISTENERS
        vrpX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeVRP = true;
        });
        vrpY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeVRP = true;
        });
        vrpZ.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeVRP = true;
        });
        resetVRP.setOnAction((ActionEvent event) -> {
            Vertice stdVRP = StandardConfigCam.getStandardCamera(visao).getVRP();
            setTextFields(vrpX, vrpY, vrpZ, stdVRP);
        });
        okVRP.setOnAction((ActionEvent event) -> {
            if (changeVRP) propagateVRP();
        });
        
        //P LISTENERS
        pX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeP = true;
        });
        pY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeP = true;
        });
        pZ.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeP = true;
        });
        resetP.setOnAction((ActionEvent event) -> {
            Vertice stdP = StandardConfigCam.getStandardCamera(visao).getP();
            setTextFields(pX, pY, pZ, stdP);
        });
        okP.setOnAction((ActionEvent event) -> {
            if (changeP) propagateP();
        });
        
        // VIEW UP LISTENERS
        viewupX.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeViewUp = true;
        });
        viewupY.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeViewUp = true;
        });
        viewupZ.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changeViewUp = true;
        });
        resetViewUp.setOnAction((ActionEvent event) -> {
            Vertice stdViewUp = StandardConfigCam.getStandardCamera(visao).getViewUp();
            setTextFields(viewupX, viewupY, viewupZ, stdViewUp);
        });
        okViewUp.setOnAction((ActionEvent event) -> {
            if (changeViewUp) propagateViewUp();
        });
        
        Camera cam = camProperty.get();
        setTextFields(   vrpX,    vrpY,    vrpZ, cam.getVRP());
        setTextFields(     pX,      pY,      pZ, cam.getP());
        setTextFields(viewupX, viewupY, viewupZ, cam.getViewUp());
    }
    
    private void propagateVRP(){
        Camera localCopy = camProperty.get();
        
        Vertice newVRP = new Vertice(
            Float.parseFloat( vrpX.textProperty().get() ),
            Float.parseFloat( vrpY.textProperty().get() ),
            Float.parseFloat( vrpZ.textProperty().get() )
        );
        
        camProperty.set( new Camera(localCopy.getViewUp(), newVRP, localCopy.getP()));
        changeVRP = false;
    }
    
    private void propagateP(){
        Camera localCopy = camProperty.get();
        
        Vertice newP = new Vertice(
            Float.parseFloat( pX.textProperty().get() ),
            Float.parseFloat( pY.textProperty().get() ),
            Float.parseFloat( pZ.textProperty().get() )
        );
        
        camProperty.set( new Camera(localCopy.getViewUp(), localCopy.getVRP(), newP));
        changeP = false;
    }
    
    private void propagateViewUp(){
        Camera localCopy = camProperty.get();
        
        Vertice newViewUp = new Vertice(
            Float.parseFloat( viewupX.textProperty().get() ),
            Float.parseFloat( viewupY.textProperty().get() ),
            Float.parseFloat( viewupZ.textProperty().get() )
        );
        
        camProperty.set( new Camera(newViewUp, localCopy.getVRP(), localCopy.getP()));
        changeViewUp = false;
    }
    
    private void setTextFields(TextField x, TextField y, TextField z, Vertice newValue){
        x.textProperty().set(String.valueOf(newValue.getX()));
        y.textProperty().set(String.valueOf(newValue.getY()));
        z.textProperty().set(String.valueOf(newValue.getZ()));
    }
}
