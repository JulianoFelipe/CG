/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.List;
import java.util.Observable;
import m.Camera;
import m.Viewport;
import m.VistaNEW;
import m.Window;
import m.anderson.CGObject;

/**
 *
 * @author JFPS
 */
public class PersPipeline extends CGPipeline{
    protected float DP;

    public PersPipeline(float DP, Camera cam, Window win, Viewport view) {
        super(cam, win, view);
        this.DP = DP;
    }
    
    protected float[][] getPersMatrix(){
        return new float[][] {
            { 1, 0,     0, 0},
            { 0, 1,     0, 0},
            { 0, 0,     1, 0},
            { 0, 0, -1/DP, 0}
        };
    }

    @Override
    public List<CGObject> convert2D(List<CGObject> lista, VistaNEW vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
