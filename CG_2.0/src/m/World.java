/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import javafx.beans.Observable;
import javafx.beans.property.ListProperty;
import javafx.beans.property.SimpleListProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.util.Callback;
import m.anderson.Vertice;
import m.poligonos.OldPoligono;

/**
 *
 * @author JFPS
 */
public class World {
    private static final Logger LOG = Logger.getLogger("CG_2.0");
    private final ListProperty<OldPoligono> objetos;
    private ListProperty<Vertice> verticesTemporarios;
    private List<Vista> planes;
    
    public World() {       
        ObservableList<OldPoligono> oobjetos = FXCollections.observableArrayList(new Callback<OldPoligono, Observable[]>() {
                @Override
                public Observable[] call(final OldPoligono param) {
                    return new Observable[]{
                        param.pontosProperty()
                    };
                }
        });
        
        objetos = new SimpleListProperty(oobjetos);
    }
    
    public World(List<OldPoligono> lista){
        ObservableList<OldPoligono> oobjetos = 
                FXCollections.observableArrayList((final OldPoligono param) 
                        -> new Observable[]{
            param.pontosProperty()
        });
        
        objetos = new SimpleListProperty(oobjetos);
        objetos.addAll(lista);
    }
    
    public void setPlanes(Vista...planes){
        this.planes = new ArrayList<>();
        for (Vista p : planes){
            this.planes.add(p);
            //<editor-fold defaultstate="collapsed" desc="Not to be added">
            p.addPoligonoListener((ListChangeListener) new ListChangeListener<OldPoligono>() {
                @Override
                public void onChanged(ListChangeListener.Change<? extends OldPoligono> c) {
                    while (c.next()) {
                        if (c.wasPermutated()) {
                            for (int i = c.getFrom(); i < c.getTo(); ++i) {
                                LOG.severe("Permutation not implemented");
                            }
                        } else if (c.wasUpdated()) {
                            for (int i = c.getFrom(); i < c.getTo(); ++i) {
                                p.objetosProperty().get(i);
                            }
                        } else {
                            c.getRemoved().forEach((removedItem) -> {
                                objetos.remove(removedItem);
                            });
                            c.getAddedSubList().forEach((addedItem) -> {
                                //p.getConverter().convertFromRaster(addedItem);
                                objetos.add(addedItem);
                            });
                        }
                    }
                }
            });
//</editor-fold>
        }
    }
    
    public void addPoligonoListener(ListChangeListener listener){
        objetos.addListener(listener);
    }

    public ListProperty<OldPoligono> objetosProperty() {
        return objetos;
    }
    
    public void addTempPointsListener(ListChangeListener listener){
        verticesTemporarios.addListener(listener);
    }
    
}
