/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.listeners;

import Model.CGObject;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 * @param <T>
 */
public interface InputListener<T extends CGObject> {
    public void objectInputted(T object, ProjectionPlane planeOfOrigin);
}
