/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.awt.Cursor;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;

/**
 *
 * @author JFPS
 */
public class CustomCursorLoader {
    //https://stackoverflow.com/questions/24818948/custom-cursor-in-java
    
    public static Cursor loadHorizontalResize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CustomCursorLoader.class.getResource("/Images/horizontal-resize.png"));
        Point hotspot = new Point(0,0);
        return toolkit.createCustomCursor(image, hotspot, "HorizontalResize");
    }
    
    public static Cursor loadVerticalResize(){
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Image image = toolkit.getImage(CustomCursorLoader.class.getResource("/Images/vertical-resize.png"));
        Point hotspot = new Point(0,0);
        return toolkit.createCustomCursor(image, hotspot, "VerticalResize");
    }
}
