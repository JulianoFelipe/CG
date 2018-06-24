/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.config;

import m.CGViewport;
import m.CGWindow;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class StandardConfigWinView {
    public static final CGWindow   STD_WINDOW   = new CGWindow(544, 374);
    public static final CGViewport STD_VIEWPORT = new CGViewport(new Vertice(0, 0), new Vertice(544, 374));
    
    public static CGWindow getStandardWindow(){
        return STD_WINDOW;
    }
    
    public static CGViewport getStandardViewport(){
        return STD_VIEWPORT;
    }
}
