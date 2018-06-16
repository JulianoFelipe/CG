/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import javafx.scene.PerspectiveCamera;
import m.poligonos.OldPoligono;

/**
 *
 * @author JFPS
 */
public class PerspectiveConverter implements WorldToRasterConverter{

    private final PerspectiveCamera camera;

    public PerspectiveConverter(PerspectiveCamera camera) {
        this.camera = camera;
    }
    
    @Override
    public void convertToRaster(OldPoligono p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void convertFromRaster(OldPoligono p) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
