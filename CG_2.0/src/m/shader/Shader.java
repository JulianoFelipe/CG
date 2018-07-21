/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import m.poligonos.CGObject;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public interface Shader {
    public void shade(List<CGObject> objetosSRT, GraphicsContext graphs, long selectedID);
    
    public void paintTemporaryPoints(List<? extends Vertice> temporaryPoints, GraphicsContext graphs);
}
