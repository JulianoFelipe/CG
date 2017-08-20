/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View.Options;

import View.MainController;
import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import m.Ponto2D;
import m.poligonos.Ancora;
import m.poligonos.Poligono;
import resource.description.Ferramentas;
import resource.description.Poligonos;
import resource.description.Transformacoes;

/**
 *
 * @author JFPS
 */
public class CanvasPane extends Group{
    private MainController main;
    private List<Poligono> lista;
    
    private Poligono selectedPolygon;
    private ObservableList<Ancora> selectedPolygonAnchors;
    private ObservableValue<Ponto2D> firstPoint;
    
    public CanvasPane(MainController main) {
        this.main = main;
        addHandlers();
    }
    
    private void addHandlers(){
        this.setOnMouseClicked((MouseEvent event) -> {
            byte selection = main.getCurrentSelection();
            if (selection == MainController.FERRAMENTA_SEL){
                Ferramentas ferr = main.getCurrentFerramenta();
                switch(ferr){
                    case Select:
                        
                        break;
                    case Delete:
                        
                        break;
                }
            } else if (selection == MainController.POLIGONO_SEL){
               Poligonos pols = main.getCurrentTipoDePoligono();
               switch(pols){
                    case Regular:
                       
                        break;
                    case Irregular:
                        
                        break;
               } 
               
            } else if (selection == MainController.TRANSFORMACAO_SEL){
                Transformacoes tra = main.getCurrentTrasformacao();
            }
            
        });
        
        /*this.setOnMouseDragged((MouseEvent event) -> {
            throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        });*/
    }
    
    private void temporaryHandlers(){
        
    }
}
