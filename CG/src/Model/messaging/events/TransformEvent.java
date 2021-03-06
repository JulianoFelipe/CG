/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

import Model.CGObject;
import Model.messaging.listeners.TransformListener;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public class TransformEvent implements CGEvent<TransformListener>{
    private final CGObject object;
    private final ProjectionPlane plane;

    public TransformEvent(final CGObject object, final ProjectionPlane plane) {
        this.object = object;
        this.plane = plane;
    }
    
    @Override
    public void notify(TransformListener listener) {
        listener.objectTransformed(object, plane);
    }
    
}
