/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader.scans;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.paint.Color;

/**
 *
 * @author JFPS
 */
public class FullScan {
    
    private final List<Integer> xCoords;
    private final int scanlineY;
    private final List<Color> colors;
    
    public FullScan(int scanline){
        scanlineY = scanline;
        xCoords = new ArrayList();
        colors = new ArrayList();
    }
    
    public void addEntry(int x, Color color){
        xCoords.add(x);
        colors.add(color);
    }
    
    public int getY(){
        return scanlineY;
    }
    
    public int getX(int index){
        return xCoords.get(index);
    }
    
    public Color getColor(int index){
        return colors.get(index);
    }
    
    public int getEntries(){
        return xCoords.size();
    }
}
