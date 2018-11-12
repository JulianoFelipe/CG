/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.shader.scans;

/**
 *
 * @author JFPS
 */
public class ExtremityScan {
    //No need for color information, as only the flat method will use this class
    //As such, color information is stored on a face level basis
    
    //Put here so no ID's are wasted
    private float xIni, xFin,
                yIni, yFin;

    public ExtremityScan(float xIni, float xFin, float yIni, float yFin) {
        this.xIni = xIni;
        this.xFin = xFin;
        this.yIni = yIni;
        this.yFin = yFin;
    }

    public int getxIni() { return Math.round(xIni); }
    public int getxFin() { return Math.round(xFin); }
    public int getyIni() { return Math.round(yIni); }
    public int getyFin() { return Math.round(yFin); }

    public float getXinicial(){ return xIni;}
    public float getXfinal  (){ return xFin;}
    public float getYinicial(){ return yIni;}
    public float getYfinal  (){ return yFin;}
    
    @Override
    public String toString() {
        return "ExtremityScan{" + "xIni=" + xIni + ", yIni=" + yIni + ", xFin=" + xFin + ", yFin=" + yFin + '}';
    }
    
    
}
