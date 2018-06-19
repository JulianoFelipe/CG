/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import View.ANDERSON_TEST_SUITE;
import java.util.ArrayList;
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
public class PersPipeline extends CGPipeline{
    protected float DP;
    
    private float[][] matrizVistaPers;
    private boolean changed = true;

    public PersPipeline(float DP, Camera cam, Window win, Viewport view) {
        super(cam, win, view);
        this.DP = DP;
    }
    
    public float[][] getMatrixProj(){
        return new float[][] {
            { 1, 0,     0, 0},
            { 0, 1,     0, 0},
            { 0, 0,     1, 0},
            { 0, 0, -1/DP, 0}
        };
    }
    
    public float[][] get3DPipelineMatrix(){
        //Retorna a matriz final do pipeline
        //Se receber update de camera, alterar changed e calc tudo de novo
        //Concatena as budega
        
        if (changed == false){
            return matrizVistaPers;
        } else {
            ///Matriz vista = Msru,src * Mvista    --- (Mjp é separada, já que é 2D)
            //Concatena por multiplicar ao contrário
            matrizVistaPers = MMath.multiplicar(getMatrixProj(), super.getMatrizSRUsrc());
            changed = false;
            return matrizVistaPers;
        }
    }
    
    public float[][] get2DPipelineMatrix(){
        return getMatrixJP();
    }

    @Override
    public void convert2D(List<CGObject> lista) {
        lista.forEach((object) -> {
            float[][] retPoints = multiply3D(get3DPipelineMatrix(), object.getPointMatrix());
            retPoints = persMatrixSwitcheroo(retPoints);
            retPoints = multiply2D(get2DPipelineMatrix(), retPoints);
            object.setPointMatrix(retPoints);
        });
    }
    
    @Override
    public void convert2D(CGObject object) {
        float[][] retPoints = multiply3D(get3DPipelineMatrix(), object.getPointMatrix());
        System.out.println("\n MATRIZ DEPOIS DE PIPE 3D");
        ANDERSON_TEST_SUITE.printMatrix(retPoints);
        retPoints = persMatrixSwitcheroo(retPoints);
        System.out.println("\n MATRIZ DEPOIS DE DIVISÃO PERSPECTIVA");
        ANDERSON_TEST_SUITE.printMatrix(retPoints);
        retPoints = multiply2D(get2DPipelineMatrix(), retPoints);
        System.out.println("\n MATRIZ DEPOIS DE PIPE 2D (Window e Viewport)");
        ANDERSON_TEST_SUITE.printMatrix(retPoints);
        object.setPointMatrix(retPoints);
    }

    @Override
    public void update(Observable o, Object arg) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
    protected float[][] persMatrixSwitcheroo(float[][] pointMatrix){
        int noPoints = pointMatrix[0].length;
        
        float[][] result = new float[4][noPoints];
        for (int i = 0; i < noPoints; i++) {
            float fator = pointMatrix[3][i];
            result[0][i]   = pointMatrix[0][i] / fator;
            result[1][i]   = pointMatrix[1][i] / fator;
            result[2][i]   = pointMatrix[2][i] / fator;
        }

        return result;
    }
}
