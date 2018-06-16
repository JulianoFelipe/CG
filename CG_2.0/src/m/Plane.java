/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import m.poligonos.OldPoligono;

/**
 *
 * @author JFPS
 */
public class Plane {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    //https://coderanch.com/t/666722/java/Notify-ObservableList-Listeners-Change-Elements
    private final WorldToRasterConverter converter;
    private final ListProperty<OldPoligono> objetos;
    private final World world;

    private boolean changeLock = false;
    
    public Plane(WorldToRasterConverter converter, World world) {
        this.converter = converter;
        
        ObservableList<OldPoligono> oobjetos = 
                FXCollections.observableArrayList((final OldPoligono param) 
                        -> new Observable[]{
            param.pontosProperty()
        });
        
        objetos = new SimpleListProperty(oobjetos);
        
        world.addPoligonoListener((ListChangeListener) new ListChangeListener<OldPoligono>() {
            @Override
            public void onChanged(ListChangeListener.Change<? extends OldPoligono> c) {
                if(changeLock){
                    System.out.println("LOCK");
                    changeLock=false;
                    return;
                }
                
                while (c.next()) {                   
                    if (c.wasPermutated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            LOG.severe("Permutation not implemented");
                        }
                    } else if (c.wasUpdated()) {
                        for (int i = c.getFrom(); i < c.getTo(); ++i) {
                            OldPoligono p = world.objetosProperty().get(i);
                            converter.convertToRaster(p);
                            FORLOOP: for(int t=0; t<objetos.getSize(); t++){
                                if (objetosProperty().get(t).getID() == p.getID()){
                                    objetosProperty().set(t, p);
                                    break FORLOOP;
                                }
                            }
                        }
                    } else {
                        c.getRemoved().forEach((removedItem) -> {
                            objetos.remove(removedItem);
                        });
                        c.getAddedSubList().forEach((addedItem) -> {
                            converter.convertToRaster(addedItem);
                            objetos.add(addedItem);
                        });
                    }
                }
            }
        });
        
        this.world = world;
    }
    
    public void addPoligonoListener(ListChangeListener listener){
        objetos.addListener(listener);
    }

    public WorldToRasterConverter getConverter() {
        return converter;
    }

    public ListProperty<OldPoligono> objetosProperty() {
        return objetos;
    }
    
    public void addPoligono(OldPoligono p){
        changeLock = true; //Bloqueia atualização do plano atual
        converter.convertFromRaster(p); //Converte para coordenadas do mundo
        world.objetosProperty().add(p); //Adiciona ao mundo
    }
    
}
