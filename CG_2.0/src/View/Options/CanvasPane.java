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
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;
import m.poligonos.Ancora;
import m.poligonos.OldPoligono;
import resource.description.Ferramentas;
import resource.description.CriacaoPrevolucao;
import resource.description.Transformacoes;

/**
 *
 * @author JFPS
 */
public class CanvasPane extends Group{
    private MainController main;
    private List<OldPoligono> lista;
    
    private OldPoligono selectedPolygon;
    private ObservableList<Ancora> selectedPolygonAnchors;
    private ObservableValue<Point2D> firstPoint;
    
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
            } else if (selection == MainController.REVOLUCAO_SEL){
               CriacaoPrevolucao pols = main.getCurrentTipoDePoligono();
               switch(pols){
                    case porPontos:
                       
                        break;
                    case porLinha:
                        
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
