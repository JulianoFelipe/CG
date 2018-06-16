/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.Poligono;
import Model.messaging.listeners.SelectedChangedListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class SelectedEvent implements CGEvent<SelectedChangedListener>{

    private final ProjectionPlane plane;
    private final Poligono newSelected;

    public SelectedEvent(final Poligono newSelected, final ProjectionPlane plane) {
        this.plane = plane;
        this.newSelected = newSelected;
    }
    
    @Override
    public void notify(SelectedChangedListener listener) {
        listener.selectedPolygonChanged(newSelected, plane);
    }
    
}
