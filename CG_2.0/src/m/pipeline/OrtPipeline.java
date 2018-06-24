/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.List;
import m.Camera;
import m.CGViewport;
import m.Visao;
import m.CGWindow;
import m.poligonos.CGObject;
import utils.config.StandardConfigCam;
import utils.config.StandardConfigWinView;
import utils.math.MMath;

/**
 *
 * @author JFPS
 */
public class OrtPipeline extends CGPipeline{
    private final Visao vista;
    private float[][] matrizVistaOrt;
    
    //<editor-fold defaultstate="collapsed" desc="Construtores">
    public OrtPipeline(Visao visao, Camera cam, CGWindow win, CGViewport view) {
        super(cam, win, view);
        this.vista = visao;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(visao);
    }
    
    public OrtPipeline(Visao visao, CGViewport view){
        super(StandardConfigCam.getStandardCamera(visao), StandardConfigWinView.STD_WINDOW, view);
        this.vista = visao;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(visao);
    }
    
    public OrtPipeline(Visao visao){
        super(StandardConfigCam.getStandardCamera(visao), StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT);
        this.vista = visao;
        matrizVistaOrt = MatrizOrtografica.getMatrizOrt(visao);
    }
//</editor-fold>
   
    @Override
    public void convert2D(CGObject object) {
        //MMath.printMatrix(object.getPointMatrix());
        
        float[][] retPoints = MMath.multiplicar(get3DPipelineMatrix(), object.getPointMatrix());
        retPoints = MMath.removeFactor(retPoints);
        retPoints = MMath.multiplicar(getMatrixJP(), retPoints);

        object.setAll(retPoints);
    }
    
    @Override
    public void reverseConversion(CGObject object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    public float[][] get3DPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        
        if (sruSRCchanged == false){
            return matrizVistaOrt;
        } else {
            ///Matriz vista = Msru,src * Mvista    --- (Mjp é separada, já que é 2D)
            //Concatena por multiplicar ao contrário
            matrizVistaOrt = MMath.multiplicar(MatrizOrtografica.getMatrizOrt(vista), super.getMatrizSRUsrc());
            sruSRCchanged = false;
            return matrizVistaOrt;
        }
    }
    
    public float[][] getMatrixProj(){
        return MatrizOrtografica.getMatrizOrt(vista);
    }

    @Override
    public Visao getVisao() {
        return vista;
    }

    //<editor-fold defaultstate="collapsed" desc="Matrizes Ortogonais">
    protected static final class MatrizOrtografica{
        protected static final float[][] MAT_TOPO = {
            { 1, 0,  0, 0},
            { 0, 0,  0, 0},
            { 0, 0,  1, 0},
            { 0, 0,  0, 1}
        };
        
        protected static final float[][] MAT_LATERAL = {
            { 0, 0,  0, 0},
            { 0, 1,  0, 0},
            { 0, 0, -1, 0},
            { 0, 0,  0, 1}
        };
        
        protected static final float[][] MAT_FRENTE = {
            { 1, 0, 0, 0},
            { 0, 1, 0, 0},
            { 0, 0, 0, 0},
            { 0, 0, 0, 1}
        };
        
        protected static float[][] getMatrizOrt(Visao vista){
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
//</editor-fold>

}
