/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.messaging.listeners.CancelTemporaryListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class CancelTemporaryEvent implements CGEvent<CancelTemporaryListener>{
    private final ProjectionPlane plane;

    public CancelTemporaryEvent(final ProjectionPlane plane) {
        this.plane = plane;
    }
    
    @Override
    public void notify(CancelTemporaryListener listener) {
        listener.cancelTemps(plane);
    }
    
}
