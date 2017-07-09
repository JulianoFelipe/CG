/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

/**
 *
 * @author Juliano
 */
public class Viewport {
    private final int Xmax;
    private final int Xmin;
    private final int Ymax;
    private final int Ymin;
    private final float dx; //Dx = Xmax - Xmin
    private final float dy; //Dy = Ymax - Ymin
    private final float dz; //Dz = (f) - (n)

    public Viewport(int Xmax, int Xmin, int Ymax, int Ymin, float far, float near) {
        this.Xmax = Xmax;
        this.Xmin = Xmin;
        this.Ymax = Ymax;
        this.Ymin = Ymin;
        
        dx = Xmax - Xmin;
        dy = Ymax - Ymin;
        dz = far - near;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public int getXmax() {
        return Xmax;
    }
    
    public int getXmin() {
        return Xmin;
    }
    
    public int getYmax() {
        return Ymax;
    }
    
    public int getYmin() {
        return Ymin;
    }
    
    public float getDx() {
        return dx;
    }
    
    public float getDy() {
        return dy;
    }
    
    public float getDz() {
        return dz;
    }
//</editor-fold>
    
    /*public void resetRate(){
        dx = Xmax - Xmin;
        dy = Ymax - Ymin;
    }*/
}
