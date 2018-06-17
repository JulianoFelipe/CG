/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.List;
import java.util.Observable;
import m.Camera;
import m.Vista;
import m.anderson.CGObject;

/**
 *
 * @author JFPS
 */
public class OrtPipeline extends CGPipeline{
    private final VistaOrtografica vista;
    private float[][] matrizVistaOrt;

    private boolean changed = false;
    
    public OrtPipeline(VistaOrtografica vista, Camera cam) {
        super(cam);
        this.vista = vista;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(vista);
    }

    @Override
    public List<CGObject> convert2D(List<CGObject> lista, Vista vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected float[][] getPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        //Concatena as budega
    }
    
    public static enum VistaOrtografica{
        Frontal, Topo, Lateral;
    };
    
    protected static final class MatrizOrtografica{
        protected static final float[][] MAT_TOPO = {
            { 0, 0, 0, 0},
            { 0, 0, 0, 0},  ESSAS 3 AQUI ANDER
            { 0, 0, 0, 0},
            { 0, 0, 0, 0}
        };
        
        protected static final float[][] MAT_LATERAL = {
            { 0, 0, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 0}
        };
        
        protected static final float[][] MAT_FRENTE = {
            { 0, 0, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 0}
        };
        
        protected static float[][] getMatrizOrt(VistaOrtografica vista){
            switch(vista){
                case Frontal:
                    return MAT_FRENTE;
                case Lateral:
                    return MAT_LATERAL;
                case Topo:
                    return MAT_TOPO;
                default:
                    throw new IllegalArgumentException("Vista ortográfica não tem matriz correspondente.");
            }
        }
    };

}
