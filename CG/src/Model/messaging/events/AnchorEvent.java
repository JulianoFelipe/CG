/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.Poligono;
import Model.Vertice;
import Model.messaging.listeners.AnchorListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class AnchorEvent implements CGEvent<AnchorListener>{
    private final ProjectionPlane plane;
    private final Vertice anchor;

    public AnchorEvent(final Vertice anchor, final ProjectionPlane plane) {
        this.plane = plane;
        this.anchor = anchor;
    }
    

    @Override
    public void notify(AnchorListener listener) {
        listener.anchorChanged(anchor, plane);
    }
    
}
