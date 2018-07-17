/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.ArrayList;
import java.util.List;
import m.Camera;
import m.CGViewport;
import m.Visao;
import m.CGWindow;
import m.poligonos.CGObject;
import m.poligonos.Vertice;
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
    public void convert2D(Vertice v) {
        List<Vertice> lista = new ArrayList(); lista.add(v);
        MMath.multiplicar(get3DPipelineMatrix(), lista);
        persMatrixSwitcheroo(v); 
        MMath.multiplicar(getMatrixJP(), lista);
    }

    @Override
    public void convert2D(CGObject obj) {
        //Obsevação: Verificar se há problemas relacionados com as conversões "STD" do OrtPipeline, como descrito
        //no comentário do método equivalente na outra classe
        
        //MMath.printMatrix(object.getPoints());
        //float[][] retPoints = MMath.multiplicar(get3DPipelineMatrix(), object.getPoints());
        //retPoints = persMatrixSwitcheroo(retPoints); 
        //retPoints = MMath.removeFactor(retPoints);
        //retPoints = MMath.multiplicar(getMatrixJP(), retPoints);
        //object.setAll(retPoints);
        //System.out.println("AFTER CHANGE");
        //MMath.printMatrix(object.getPoints());
        
        MMath.multiplicar(get3DPipelineMatrix(), obj.getPoints());
        persMatrixSwitcheroo(obj); 
        //MMath.removeFactor(retPoints);
        MMath.multiplicar(getMatrixJP(), obj.getPoints());
    }
    
    @Override
    public void reverseConversion(Vertice v) {
        //float[][] retPoints = MMath.multiplicar(getMatrizSRCsru(), object.getPoints());
        //object.setAll(retPoints);
        
        throw new IllegalArgumentException("Reversão para PersPipeline não implementada.");
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
    
    protected void persMatrixSwitcheroo(Vertice v){
        float fator = v.getW();
        
        v.setAll(
            v.getX() / fator,
            v.getY() / fator,
            1 //v.getZ() / fator
        );
    }
    
    protected void persMatrixSwitcheroo(CGObject obj){
        int noPoints = obj.getNumberOfPoints();
        
        for (int i = 0; i < noPoints; i++) {
            Vertice copy = obj.get(i);
            float fator = copy.getW();
            
            obj.get(i).setAll(
                copy.getX() / fator,
                copy.getY() / fator,
                1 //copy.getZ() / fator
            );
            
            //result[0][i]   = pointMatrix[0][i] / fator;
            //result[1][i]   = pointMatrix[1][i] / fator;
            ////result[2][i]   = pointMatrix[2][i] / fator;
            ////result[3][i]   = pointMatrix[3][i] / fator;
            //result[2][i]   = 1;
            ////result[3][i]   = 1;
        }
    }

    @Override
    public Visao getVisao() {
        return Visao.Perspectiva;
    }

    @Override
    public float getDP() {
        return DP;
    }
}
