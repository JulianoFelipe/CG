/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.listeners;

import Model.Poligono;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 */
public interface SelectedChangedListener {
    public void selectedPolygonChanged(Poligono newSelected, ProjectionPlane plane);
}
