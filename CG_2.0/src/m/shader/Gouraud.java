/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import java.util.List;
import javafx.scene.canvas.GraphicsContext;
import m.poligonos.CGObject;

/**
 *
 * @author JFPS
 */
public class Gouraud extends CGShader{

    public Gouraud(AmbientLight luzAmbiente) {
        super(luzAmbiente);
    }
    
    public Gouraud(AmbientLight luzAmbiente, List<PointLight> luzesPontuais) {
        super(luzAmbiente, luzesPontuais);
    }
    
    @Override
    public void shade(List<CGObject> objetosSRT, GraphicsContext graphs, long selectedID) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
