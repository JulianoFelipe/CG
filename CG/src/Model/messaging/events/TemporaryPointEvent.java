/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.Vertice;
import Model.messaging.listeners.TemporaryPointListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class TemporaryPointEvent implements CGEvent<TemporaryPointListener>{

    private final Vertice vertice;
    private final ProjectionPlane plane;

    public TemporaryPointEvent(final Vertice vertice, final ProjectionPlane plane) {
        this.vertice = vertice;
        this.plane = plane;
    }
    
    @Override
    public void notify(TemporaryPointListener listener) {
        listener.temporaryPointAdded(vertice, plane);
    }
    
}
