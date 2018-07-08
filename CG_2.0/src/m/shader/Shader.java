/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import java.util.List;
import javafx.scene.canvas.Canvas;
import m.poligonos.CGObject;

/**
 *
 * @author JFPS
 */
public interface Shader {
    public void shade(float[][] matrix, List<CGObject> objetosSRT, Canvas canvas);
}
