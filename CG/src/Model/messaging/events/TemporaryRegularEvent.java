/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.Nregular;
import Model.messaging.listeners.TemporaryRegularListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class TemporaryRegularEvent implements CGEvent<TemporaryRegularListener>{
    private final Nregular nregular;
    private final ProjectionPlane plane;

    public TemporaryRegularEvent(final Nregular nregular, final ProjectionPlane plane) {
        this.nregular = nregular;
        this.plane = plane;
    }
    @Override
    public void notify(TemporaryRegularListener listener) {
        listener.temporaryRegularChanged(nregular, plane);
    }
    
}
