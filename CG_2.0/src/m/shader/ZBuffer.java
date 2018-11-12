/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader;

import View.Fatores;
import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class ZBuffer {
    private final int width;
    private final int height;
    private final Color[][] imageBuffer;
    private final float[][] zBuffer;

    public ZBuffer(int width, int height) {
        this.width = width;
        this.height = height;
        
        imageBuffer = new Color[width][height];
        for (int i=0; i<width; i++){
            for (int j=0; j<height; j++){
                imageBuffer[i][j] = Fatores.BACKGROUND_COLOR;
            }    
        }
        
        zBuffer     = new float[width][height];
    }
    
    
}
