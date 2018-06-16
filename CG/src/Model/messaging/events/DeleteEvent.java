/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.Poligono;
import Model.messaging.listeners.DeletePoligonoListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class DeleteEvent implements CGEvent<DeletePoligonoListener>{

    private final ProjectionPlane plane;
    private final Poligono toDelete;

    public DeleteEvent(final Poligono toDelete, final ProjectionPlane plane) {
        this.plane = plane;
        this.toDelete = toDelete;
    }
    
    @Override
    public void notify(DeletePoligonoListener listener) {
        listener.deletePoligono(toDelete, plane);
    }
    
}
