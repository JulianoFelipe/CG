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
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.scene.text.TextFlow;
import m.Eixo;
import resource.description.Transformacoes;

/**
 *
 * @author JFPS
 */
public class TransformacaoController implements Initializable {
    //https://stackoverflow.com/questions/16549296/how-perform-task-on-javafx-textfield-at-onfocus-and-outfocus
    //https://stackoverflow.com/questions/35219394/javafx-wrap-an-existing-object-with-simple-properties
    
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    
    @FXML private TextField factorField;
    @FXML private Button    factorReset;
    @FXML private Button    factorOk;
       
    @FXML private RadioButton justX;
    @FXML private RadioButton justY;
    @FXML private RadioButton both;
    @FXML private RadioButton allRadio;
   
    @FXML private Label factorLabel;
    @FXML private TextFlow textFlow;
    
    private final ObjectProperty<Eixo> axisOfOperationProperty;
    private final Transformacoes tra;
    
    public TransformacaoController(ObjectProperty<Eixo> axisOfOperationProperty, Transformacoes t) {
        this.axisOfOperationProperty = axisOfOperationProperty;
        
        this.tra = t;
    }
    
    @Override
    public void initialize(URL location, ResourceBundle resources) {        
        factorField.setText(Float.toString(getFactor()));
        
        Text text;
        switch (tra){
            case Escala:
                text = new Text("Fator de escala em porcentagem. Funciona de modo que se, por exemplo,"
                + " o fator for 1%, a cada operação (determinada pelo movimento de arraste do mouse) o"
                + " objeto tem sua escala ampliada/reduzida em 1%. Se a opção \"Somente largura\" estiver"
                + " selecionada, apenas os movimentos horizontais serão considerados e só a largura será afetada. Idem para os restantes."
                + " A opção para as 3 dimensões simultâneas o faz proporcionalmente nas 3."); 
                break;
            case Cisalhamento:
                text = new Text("Fator de cisalhamento em porcentagem. Funciona de modo que se, por exemplo,"
                + " o fator for 1%, a cada operação (determinada pelo movimento de arraste do mouse) o objeto tem 1% do valor das"
                + " coordenadas horizontais acrescentado nas verticais, e/ou vice versa, dependendo da opção selecionada."
                + " Se a opção \"Somente largura\" estiver selecionada, apenas os movimentos horizontais serão considerados e só a largura"
                + " será afetada. Idem para os restantes.");  
                allRadio.setDisable(true); 
                break;
            case Rotacao:
                factorLabel.textProperty().set("Fator (°)");
                text = new Text("Fator de rotação em graus. Funciona de modo que se, por exemplo,"
                + " o fator for 1°, a cada operação (determinada pelo movimento de arraste do mouse)"
                + " o objeto é rotacionado em 1° em sentido horário ou anti-horário dependendo do movimento do mouse."
                + " As opções são desabilitadas pois o eixo de rotação é determinado apenas"
                + " por qual porta de visão inicia-se a transformação.");
                justX.setDisable(true); justY.setDisable(true); both.setDisable(true); allRadio.setDisable(true);
                break;
            default:
                text = new Text(""); 
                break;
        }
        
        textFlow.getChildren().add(text);
        
        if (axisOfOperationProperty.get()==Eixo.Eixo_XYZ && tra==Transformacoes.Cisalhamento) axisOfOperationProperty.set(Eixo.Eixo_XY);
        
        Eixo axis = axisOfOperationProperty.get();
             if (axis == Eixo.Eixo_X)   justX.selectedProperty().set(true); 
        else if (axis == Eixo.Eixo_Y)   justY.selectedProperty().set(true);
        else if (axis == Eixo.Eixo_XY)  both .selectedProperty().set(true); 
        else if (axis == Eixo.Eixo_XYZ) allRadio.selectedProperty().set(true);
        else throw new IllegalArgumentException("Eixo de operação não esperado: " + axis);
        
        factorReset.setOnAction((ActionEvent event) -> {
            setDefault();
            factorField.setText(Float.toString(getFactor()));
        });
        factorOk.setOnAction((ActionEvent event) -> {
            setFactor(Float.parseFloat(factorField.getText()));
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
        
        allRadio.selectedProperty().addListener((ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) -> {
            if (allRadio.selectedProperty().get()){
                axisOfOperationProperty.set(Eixo.Eixo_XYZ);
            }
        });
    }
    
    private float getFactor(){
        switch (tra){
            case Escala:       return ((Fatores.getFatorEscalaPlus()*100)-100);
            case Cisalhamento: return (Fatores.getFatorCisalhamentoPlus()*100);
            case Rotacao:      return Fatores.fator_rotacao;
            case Translacao:   throw new IllegalArgumentException("Translação não possui parâmetros para Controller.");
            default: throw new IllegalArgumentException("Transformação não implementada.");
        }
    }
    
    private void setFactor(float factor){
        switch (tra){
            case Escala:       Fatores.setFatorEscala(factor); return;
            case Cisalhamento: Fatores.setFatorCisalhamento(factor); return;
            case Rotacao:      Fatores.fator_rotacao = factor; return;
            case Translacao:   throw new IllegalArgumentException("Translação não possui parâmetros para Controller.");
            default: throw new IllegalArgumentException("Transformação não implementada.");
        }
    }

    private void setDefault(){
        switch (tra){
            case Escala:       setFactor(Fatores.DEFAULT_ESCALA); return;
            case Cisalhamento: setFactor(Fatores.DEFAULT_CISALHAMENTO); return;
            case Rotacao:      setFactor(Fatores.DEFAULT_ROTACAO); return;
            case Translacao:   throw new IllegalArgumentException("Translação não possui parâmetros para Controller.");
            default: throw new IllegalArgumentException("Transformação não implementada.");
        }
    }    
}
