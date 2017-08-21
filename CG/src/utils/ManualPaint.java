/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Poligono;
import java.awt.Graphics;

/**
 *
 * @author JFPS
 */
public class ManualPaint {
    private static void paintPixel(Graphics g, int x, int y){
        g.fillRect(x, y, 1, 1);
    }
    
    //https://softwareengineering.stackexchange.com/questions/105580/what-is-the-best-bucket-fill-algorithm
    //https://stackoverflow.com/questions/23983465/is-there-a-fill-function-for-arbitrary-shapes-in-javafx
    //https://www.tutorialspoint.com/computer_graphics/polygon_filling_algorithm.htm
    public static void floodFill(Graphics g, Poligono p){
        
    }
}
