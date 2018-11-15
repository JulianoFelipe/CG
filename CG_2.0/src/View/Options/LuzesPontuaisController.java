/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import View.MainController;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import static javafx.collections.FXCollections.copy;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import m.poligonos.Vertice;
import m.shader.PointLight;

/**
 *
 * @author JFPS
 */
public class LuzesPontuaisController implements Initializable {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
        
    @FXML private TextField intensidadeField;
    @FXML private Label     intensidadeLabel;
    @FXML private ColorPicker corPicker;
    @FXML private Label       corLabel;
    @FXML private CheckBox chromaticCheck;
    @FXML private Button   okButton;
    @FXML private Button   removeButton;
    @FXML private TextField xField;
    @FXML private TextField yField;
    @FXML private TextField zField;
    @FXML private Tab tabLuzes;
    @FXML private Tab tabEdit;
    @FXML private VBox vBox;
    @FXML private TextField descField;
       
    private final TreeView<String> tree;
    
    private final MainController mainController;
    private final List<PointLight> lights;

    private int index;
    private final ObjectProperty<PointLight> copyProperty;
    
    public LuzesPontuaisController(MainController mainController, List<PointLight> lights) {
        this.mainController = mainController;
        this.lights = lights;
        
        TreeItem<String> root = new TreeItem<>("Root");
        tree = new TreeView(root); tree.setShowRoot(false);
        
        copyProperty = new SimpleObjectProperty<>(null);
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        vBox.getChildren().add(tree);
        vBox.setFillWidth(true);
        
        showTree();
        
        tree.setOnMouseClicked((MouseEvent event) -> {
            Node node = event.getPickResult().getIntersectedNode();
            if (node instanceof Text || (node instanceof TreeCell && ((TreeCell) node).getText() != null)){
                String name = (String) ((TreeItem)tree.getSelectionModel().getSelectedItem()).getValue();
                String[] strs = name.split(":");
                if (strs.length < 2) return;
                index = Integer.parseInt(strs[0]);
                if (index < 0) return;
                copyProperty.set(lights.get(index));
                
            } else {
                copyProperty.set(null);
                tree.selectionModelProperty().get().clearSelection();
            }
        });
        
        okButton.setOnAction((ActionEvent event) -> {
            okButton.setDisable(true);
            
            PointLight li = (copyProperty.get()!=null ? copyProperty.get() : new PointLight(new Vertice(0, 0), 0));
            if (chromaticCheck.selectedProperty().get()){
                li.setColor(corPicker.getValue());
            } else {
                li.setIntensidade(defaultFloatParser(intensidadeField.getText())); 
            }
            
            float x = defaultFloatParser(xField.textProperty().get());
            float y = defaultFloatParser(yField.textProperty().get());
            float z = defaultFloatParser(zField.textProperty().get());
            li.setPosition(x, y, z);
            
            if (copyProperty.get() != null){
                mainController.setPointLight(index, li);
            } else {
                mainController.addPointLight(li);
                index = lights.size()-1;
                tree.getRoot().getChildren().clear();
                showTree();
                tree.getSelectionModel().select(index);
                setFields(li);
                okButton.setDisable(true);
            }  
            copyProperty.set(li);
        });
        
        removeButton.setOnAction((ActionEvent event) -> {
            if (copyProperty.get() != null){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Deseja excluir a luz pontual \"" + copyProperty.get().descriptionString() + "\" ?", ButtonType.YES, ButtonType.NO);
                alert.showAndWait();

                if (alert.getResult() == ButtonType.YES) {
                    copyProperty.set(null);
                    mainController.removePointLight(index);
                    index = -1;
                    tree.getRoot().getChildren().clear();
                    showTree();
                    tree.getSelectionModel().clearSelection();
                    setFields(null);
                }
            }
        });
        
        xField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(false);
        });
        yField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(false);
        });
        zField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(false);
        });
        
        chromaticCheck.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            okButton.setDisable(false);
            if (newValue){
                intensidadeLabel.setDisable(true);
                intensidadeField.setDisable(true);
                corLabel .setDisable(false);
                corPicker.setDisable(false);
            } else {
                corLabel .setDisable(true);
                corPicker.setDisable(true);
                intensidadeLabel.setDisable(false);
                intensidadeField.setDisable(false);
            }
        });
               
        intensidadeField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            okButton.setDisable(false);
        });
        
        corPicker.valueProperty().addListener((ObservableValue<? extends Color> observable, Color oldValue, Color newValue) -> {
            okButton.setDisable(false);
        });
        
        copyProperty.addListener((ObservableValue<? extends PointLight> observable, PointLight oldValue, PointLight newValue) -> {
            if (newValue == null){
                tabEdit.setText("Adicionar nova luz");
            } else {
                tabEdit.setText("Editar luz selecionada");
            }
        });
        
        tabEdit.setOnSelectionChanged((Event event) -> {
            if(tabEdit.isSelected()){
                setFields(copyProperty.get());
            }
        });
    }
    
    private void setFields(PointLight light){
        if (light == null){
            okButton.setDisable(false);
            
            if (chromaticCheck.selectedProperty().get()){
                intensidadeLabel.setDisable(true);
                intensidadeField.setDisable(true);
                corLabel .setDisable(false);
                corPicker.setDisable(false);
            } else {
                corLabel .setDisable(true);
                corPicker.setDisable(true);
                intensidadeLabel.setDisable(false);
                intensidadeField.setDisable(false);
            }
            
            descField.textProperty().set("");
            xField.textProperty().set("");
            yField.textProperty().set("");
            zField.textProperty().set("");
            intensidadeField.textProperty().set("");
            corPicker.valueProperty().set(Color.WHITE);
            
            return;
        }
        
        if (light.isChromatic()){
            intensidadeLabel.setDisable(true);
            intensidadeField.setDisable(true);
            corLabel .setDisable(false);
            corPicker.setDisable(false);
            chromaticCheck.selectedProperty().set(true);
            corPicker.valueProperty().set(light.getColor());
        } else {
            chromaticCheck.selectedProperty().set(false);
            intensidadeField.textProperty().set(Double.toString(light.getIntensidade()));
            corLabel .setDisable(true);
            corPicker.setDisable(true);
            intensidadeLabel.setDisable(false);
            intensidadeField.setDisable(false);
        }
        okButton.setDisable(true);
        
        Vertice pos = light.getPosicao();
        xField.textProperty().set(Float.toString(pos.getX()));
        yField.textProperty().set(Float.toString(pos.getY()));
        zField.textProperty().set(Float.toString(pos.getZ()));
        descField.textProperty().set(index + ": " + light.descriptionString());
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
      
    private void showTree(){
        TreeItem<String> root = tree.getRoot();
        
        for (int i=0; i<lights.size(); i++){
            root.getChildren().add(new TreeItem(i+": " + lights.get(i).descriptionString()));
        }
    }
    
}
