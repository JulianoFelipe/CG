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
public class PersPipeline extends CGPipeline{
    protected float DP;
    private float[][] matrizVistaPers;

    //<editor-fold defaultstate="collapsed" desc="Construtores">
    public PersPipeline(float DP, Camera cam, CGWindow win, CGViewport view) {
        super(cam, win, view);
        this.DP = DP;
    }
    
    public PersPipeline(){
        super(StandardConfigCam.getStandardCamera(Visao.Perspectiva),
                StandardConfigWinView.STD_WINDOW, StandardConfigWinView.STD_VIEWPORT);
        this.DP = StandardConfigCam.PERS_DP;
    }
//</editor-fold>
       
    @Override
    public void convert2D(CGObject object) {
        MMath.printMatrix(object.getPointMatrix());
        
        float[][] retPoints = MMath.multiplicar(get3DPipelineMatrix(), object.getPointMatrix());
        retPoints = persMatrixSwitcheroo(retPoints); 
        retPoints = MMath.removeFactor(retPoints);
        retPoints = MMath.multiplicar(getMatrixJP(), retPoints);

        object.setAll(retPoints);
        
        System.out.println("AFTER CHANGE");
        MMath.printMatrix(object.getPointMatrix());
    }

    @Override
    public void reverseConversion(CGObject object) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
        
    public float[][] get3DPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        
        if (sruSRCchanged == false){
            return matrizVistaPers;
        } else {
            ///Matriz vista = Msru,src * Mvista    --- (Mjp é separada, já que é 2D)
            //Concatena por multiplicar ao contrário
            matrizVistaPers = MMath.multiplicar(getMatrixProj(), super.getMatrizSRUsrc());
            sruSRCchanged = false;
            return matrizVistaPers;
        }
    }
    
    public float[][] getMatrixProj(){
        return new float[][] {
            { 1, 0,     0, 0},
            { 0, 1,     0, 0},
            { 0, 0,     1, 0},
            { 0, 0, -1/DP, 0}
        };
    }
    
    protected float[][] persMatrixSwitcheroo(float[][] pointMatrix){
        int noPoints = pointMatrix[0].length;
        
        float[][] result = new float[4][noPoints];
        for (int i = 0; i < noPoints; i++) {
            float fator = pointMatrix[3][i];
            result[0][i]   = pointMatrix[0][i] / fator;
            result[1][i]   = pointMatrix[1][i] / fator;
            //result[2][i]   = pointMatrix[2][i] / fator;
            //result[3][i]   = pointMatrix[3][i] / fator;
            result[2][i]   = 1;
            //result[3][i]   = 1;
        }

        return result;
    }

    @Override
    public Visao getVisao() {
        return Visao.Perspectiva;
    }
}
