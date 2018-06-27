/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

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
    
    //<editor-fold defaultstate="collapsed" desc="Métodos para conversão de Tela para Mundo">
    @Override
    public void reverseConversion(CGObject object) {
        if (!isCameraStraight()){
            //Se a câmera NÃO está "olhando reto para o ponto",
            //faça a conversão com a função abaixo
            standardReverseConversion(object);
            return;
        }
        
        //Se a câmera está "olhando reto para o ponto"
        //Faça as conversões abaixo
        switch(vista){
            case Frontal:
                reverseFrontalConversion(object);
                break;
            case Lateral:
                reverseLateralConversion(object);
                break;
            case Topo:
                reverseTopConversion(object);
                break;
            default:
                throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
    }
    
    private void standardReverseConversion(CGObject object){
        float[][] retPoints = MMath.removeFactor(object.getPointMatrix());
        
        /*System.out.println("OBJ POINTS: ");
        MMath.printMatrix(retPoints);
        System.out.println("JP POINTS: ");
        MMath.printMatrix(getMatrixJP());*/
        
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());
        
        //System.out.println("INV JP: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        /*System.out.println("POINTS AFTER INV JP: ");
        MMath.printMatrix(retPoints);
        System.out.println("MATRIX PROJ: ");
        MMath.printMatrix(getMatrixProj());*/
        
        retPoints = MMath.addFactor(retPoints);
        invJP = MMath.invert4x4Matrix(getMatrixProj());
        
        //System.out.println("INV MATRIX PROJ: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        System.out.println("POINTS AFTER INV JP: ");
        MMath.printMatrix(retPoints);
        System.out.println("MATRIX SRU SRC: ");
        MMath.printMatrix(getMatrizSRUsrc());
        
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        //System.out.println("INV MATRIX SRU SRC: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        //System.out.println("POINTS AFTER INV SRUSRC: ");
        //MMath.printMatrix(retPoints);
        
        object.setAll(retPoints);
    }
    
    private void reverseFrontalConversion(CGObject object){
        float[][] retPoints = MMath.removeFactor(object.getPointMatrix());
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());
        
        //É pra dar erro se não tiver inversa mesmo
        retPoints = MMath.multiplicar(invJP, retPoints);
        retPoints = MMath.addFactor(retPoints);
        
        //Etapa diferente confome visão (Frente, Topo...)
        float minusVRPz = -cam.getVRP().getZ();
        for (int i=0; i<retPoints[0].length; i++){
            retPoints[1][i] = -retPoints[1][i]; //Inverter sinal de Y
            retPoints[2][i] += minusVRPz; //-VRP (Coordenada Z)  
        }
        
        //Fim de etapa diferenciada
        
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        object.setAll(retPoints);
    }
    
    private void reverseLateralConversion(CGObject object){
        float[][] retPoints = MMath.removeFactor(object.getPointMatrix());
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());
        
        //É pra dar erro se não tiver inversa mesmo
        retPoints = MMath.multiplicar(invJP, retPoints);
        retPoints = MMath.addFactor(retPoints);
        
        //Etapa diferente confome visão (Frente, Topo...)
        for (int i=0; i<retPoints[0].length; i++){
            retPoints[2][0] = retPoints[0][i]; //Z recebe X
            retPoints[0][i] = 0; //Zera X    
        }
        
        //Fim de etapa diferenciada
        
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        object.setAll(retPoints);
    }
    
    private void reverseTopConversion(CGObject object){
        float[][] retPoints = MMath.removeFactor(object.getPointMatrix());
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());
        
        //É pra dar erro se não tiver inversa mesmo
        retPoints = MMath.multiplicar(invJP, retPoints);
        retPoints = MMath.addFactor(retPoints);
        
        //Etapa diferente confome visão (Frente, Topo...)
        float minusVRPz = -cam.getVRP().getZ();
        for (int i=0; i<retPoints[0].length; i++){
            retPoints[2][i] = minusVRPz; //-VRP (Coordenada Z)  
        }
        //Fim de etapa diferenciada
        
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        if (invJP != null)
            retPoints = MMath.multiplicar(invJP, retPoints);
        
        object.setAll(retPoints);
    }
    
    private boolean isCameraStraight(){
        //if (cam!=null) return false; //Dum-e debug
        Vertice camN = cam.getVetorN();
        
        switch(vista){
            case Frontal:
                //return false;
                if (camN.getZ() == 1.0)
                    return true;
                break;
            case Lateral:
                return false;
                /*if (camN.getX() == 1.0)
                    return true;
                break;*/
            case Topo:
                if (camN.getY() == 1.0)
                    return true;
                break;
            default:
                throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
        return false;
    }
//</editor-fold>
    
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
        protected static final float[][] MAT_FRENTE = {
            { 1,  0, 0, 0},
            { 0, -1, 0, 0},
            { 0,  0, 0, 0},
            { 0,  0, 0, 1}
        };
               
        protected static final float[][] MAT_LATERAL = {
            { -1,  0,  0, 0},
            {  0, -1,  0, 0},
            {  0,  0,  1, 0},
            {  0,  0,  0, 1}
        };
        
        protected static final float[][] MAT_TOPO = {
            { 1,  0,  0, 0},
            { 0,  1,  0, 0},
            { 0,  0,  0, 0},
            { 0,  0,  0, 1}
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
