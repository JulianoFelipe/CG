/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.config;

import m.Camera;
import m.Visao;
import m.poligonos.Vertice;

/**
 *
 * @author JFPS
 */
public class StandardConfigCam {
    //                                                   |        ViewUP      |            VRP         |              P       |
    private static final Camera FRONTAL_CAM = new Camera( new Vertice( 0, 1, 0), new Vertice( 0,  0, 10), new Vertice(0, 0, 0) );
    private static final Camera LATERAL_CAM = new Camera( new Vertice( 0, 1, 0), new Vertice(10,  0,  0), new Vertice(0, 0, 0) );
    private static final Camera TOPO_CAM    = new Camera( new Vertice( 0, 0,-1), new Vertice( 0, 10,  0), new Vertice(0, 0, 0) );
    private static final Camera PERS_CAM    = new Camera( new Vertice( 0, 1, 0), new Vertice(50, 15, 30), new Vertice(0, 0, 0) );
    //                                                                 X  Y  Z               X    Y   Z               X  Y  Z 
    public static final float  PERS_DP = 100;
    
    public static Camera getStandardCamera(Visao vis){
        switch (vis){
            case Frontal:
                return new Camera(FRONTAL_CAM);
            case Lateral:
                return new Camera(LATERAL_CAM);
            case Topo:
                return new Camera(TOPO_CAM);
            case Perspectiva:
                return new Camera(PERS_CAM);
            default:
                throw new IllegalArgumentException("Visão sem câmera padrão determinada.");
                         
        }
    }
}
