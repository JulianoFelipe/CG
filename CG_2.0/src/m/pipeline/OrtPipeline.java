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
import utils.math.MMath;

/**
 *
 * @author JFPS
 */
public class OrtPipeline extends CGPipeline{
    private final VistaOrtografica vista;
    
    private float[][] matrizVistaOrt;
    private boolean changed = true;
    
    public OrtPipeline(VistaOrtografica vista, Camera cam, Window win, Viewport view) {
        super(cam, win, view);
        this.vista = vista;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(vista);
    }

    @Override
    public List<CGObject> convert2D(List<CGObject> lista, VistaNEW vista) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void update(Observable o, Object arg) {
        //if observable is window ou viewport, update matrix jp
        
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public float[][] get3DPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        //Concatena as budega
        
        if (changed == false){
            return matrizVistaOrt;
        } else {
            ///Matriz vista = Msru,src * Mvista    --- (Mjp é separada, já que é 2D)
            //Concatena por multiplicar ao contrário
            matrizVistaOrt = MMath.multiplicar(MatrizOrtografica.getMatrizOrt(vista), super.getMatrizSRUsrc());
            changed = false;
            return matrizVistaOrt;
        }
    }
    
    public float[][] get2DPipelineMatrix(){
        return getMatrixJP();
    }
    
    public float[][] getMatrixProj(){
        return MatrizOrtografica.getMatrizOrt(vista);
    }
    
    public static enum VistaOrtografica{
        Frontal, Topo, Lateral;
    };
    
    protected static final class MatrizOrtografica{
        protected static final float[][] MAT_TOPO = {
            { 1, 0, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 1, 0},
            { 0, 0, 0, 1}
        };
        
        protected static final float[][] MAT_LATERAL = {
            { 0, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 1, 0},
            { 0, 0, 0, 1}
        };
        
        protected static final float[][] MAT_FRENTE = {
            { 1, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 1}
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
