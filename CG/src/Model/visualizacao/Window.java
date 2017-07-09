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
public class Window {
    private final float cu; //(u,v) = Centróide
    private final float cv;
    private final float su; //su = 1/2 half width
    private final float sv; //sv = 1/2 half height

    public Window(float cu, float cv, float su, float sv) {
        this.cu = cu;
        this.cv = cv;
        this.su = su;
        this.sv = sv;
    }

    //<editor-fold defaultstate="collapsed" desc="Getters">
    public float getCu() {
        return cu;
    }
    
    public float getCv() {
        return cv;
    }
    
    public float getSu() {
        return su;
    }
    
    public float getSv() {
        return sv;
    }
//</editor-fold>  
}
