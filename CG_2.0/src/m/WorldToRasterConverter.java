/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import m.poligonos.OldPoligono;

/**
 *
 * @author JFPS
 */
public interface WorldToRasterConverter {
    
    public void convertToRaster(OldPoligono p);
    public void convertFromRaster(OldPoligono p);
}
