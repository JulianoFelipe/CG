/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.pipeline;

import java.util.ArrayList;
import java.util.Arrays;
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
   
    //<editor-fold defaultstate="collapsed" desc="Conversão Mundo -> Tela">
    @Override
    public void convert2D(Vertice vertice) {
        if (!isCameraStraight()){
            //Se a câmera NÃO está "olhando reto para o ponto",
            //faça a conversão com a função abaixo
            standardConversion(vertice);
            return;
        }

        //Se a câmera está "olhando reto para o ponto"
        //Faça as conversões abaixo
        switch(vista){
            case Frontal: frontalConversion(vertice); break;
            case Lateral: lateralConversion(vertice); break;
            case Topo:    topConversion(vertice);     break;
            default: throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }

    }

    @Override
    public void convert2D(CGObject object) {
        //Por que substituir esse método?
        //Caso a câmera não estiver reta, o método "standardConversion(Vertice)"
        //será chamado para cada ponto, e será criado uma lista para cada conversão, o que é desnecessário.
        //Uma alternativa seria fazer um método para "MMath.mult(matriz,vertice)", mas...
        if (!isCameraStraight()){
            standardConversion(object); return;
        }

        switch(vista){
            case Frontal: object.getPoints().forEach((v) -> { frontalConversion(v); }); break;
            case Lateral: object.getPoints().forEach((v) -> { lateralConversion(v); }); break;
            case Topo:    object.getPoints().forEach((v) -> { topConversion(v);     }); break;
            default: throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
    }
        
    private void standardConversion(CGObject object){
        //Using synthetic cam
        MMath.multiplicar(get3DPipelineMatrix(), object.getPoints());
        //retPoints = MMath.removeFactor(retPoints);
        MMath.multiplicar(getMatrixJP(), object.getPoints());
    }
    
    private void standardConversion(Vertice v){
        //Using synthetic cam
        List<Vertice> lista = new ArrayList();
        lista.add(v);
        
        MMath.multiplicar(get3DPipelineMatrix(), lista);
        
        //retPoints = MMath.removeFactor(retPoints); //No need
        //MMath.mult quando passa lista controla até onde multiplica com base nas linhas
        //da matriz A, então o fator não será multiplicado

        MMath.multiplicar(getMatrixJP(), lista); 
    }
    
    private void frontalConversion(Vertice v){
        v.setAll(
            (v.getX() - cam.getVRP().getX()) * jpProportions[0],
            (v.getY() - cam.getVRP().getY()) * jpProportions[1],
            0
        );
    }
    
    private void lateralConversion(Vertice v){ 
        v.setAll(
            ((v.getY() - cam.getVRP().getY()) * jpProportions[0]),
            ((v.getZ() - cam.getVRP().getZ()) * jpProportions[1]),
            (0)
        );
    }
    
    private void topConversion(Vertice v){  
        v.setAll(
            (v.getX() - cam.getVRP().getX()) * jpProportions[0],
            (v.getZ() - cam.getVRP().getZ()) * jpProportions[1],
            0
        );            
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Conversão Tela -> Mundo">
    @Override
    public void reverseConversion(Vertice vertice) {
        if (!isCameraStraight()){
            //Se a câmera NÃO está "olhando reto para o ponto",
            //faça a conversão com a função abaixo
            standardReverseConversion(vertice);
            return;
        }
        
        //Se a câmera está "olhando reto para o ponto"
        //Faça as conversões abaixo
        switch(vista){
            case Frontal: reverseFrontalConversion(vertice); break;
            case Lateral: reverseLateralConversion(vertice); break;
            case Topo:    reverseTopConversion(vertice);     break;
            default: throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
    }
    
    @Override
    public void reverseConversion(CGObject object) {
        if (!isCameraStraight()){
            standardReverseConversion(object); return;
        }

        switch(vista){
            case Frontal: object.getPoints().forEach((v) -> { reverseFrontalConversion(v); }); break;
            case Lateral: object.getPoints().forEach((v) -> { reverseLateralConversion(v); }); break;
            case Topo:    object.getPoints().forEach((v) -> { reverseTopConversion(v);     }); break;
            default: throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
    }
    
    private void standardReverseConversion(CGObject object){
        //float[][] retPoints = MMath.removeFactor(object.getPoints());
        
        /*System.out.println("OBJ POINTS: ");
        MMath.printMatrix(retPoints);
        System.out.println("JP POINTS: ");
        MMath.printMatrix(getMatrixJP());*/
        
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());
        
        //System.out.println("INV JP: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            MMath.multiplicar(invJP, object.getPoints());
        
        /*System.out.println("POINTS AFTER INV JP: ");
        MMath.printMatrix(retPoints);
        System.out.println("MATRIX PROJ: ");
        MMath.printMatrix(getMatrixProj());*/
        
        //retPoints = MMath.addFactor(retPoints);
        invJP = MMath.invert4x4Matrix(getMatrixProj());
        
        //System.out.println("INV MATRIX PROJ: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            MMath.multiplicar(invJP, object.getPoints());
        
        /*System.out.println("POINTS AFTER INV JP: ");
        MMath.printMatrix(retPoints);
        System.out.println("MATRIX SRU SRC: ");
        MMath.printMatrix(getMatrizSRUsrc());*/
        
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        //System.out.println("INV MATRIX SRU SRC: ");
        //MMath.printMatrix(invJP);
        
        if (invJP != null)
            MMath.multiplicar(invJP, object.getPoints());
        
        //System.out.println("POINTS AFTER INV SRUSRC: ");
        //MMath.printMatrix(retPoints);
        
        //object.setAll(retPoints);
    }
    
    private void standardReverseConversion(Vertice v){
        List<Vertice> lista = new ArrayList();
        lista.add(v);
        
        //float[][] retPoints = MMath.removeFactor(v.getPoints());
        //No need
        
        float[][] invJP = MMath.invert3x3Matrix(getMatrixJP());       
        if (invJP != null)
            MMath.multiplicar(invJP, lista);
        
        //retPoints = MMath.addFactor(retPoints); //No need either
        invJP = MMath.invert4x4Matrix(getMatrixProj());     
        if (invJP != null)
            MMath.multiplicar(invJP, lista);
                
        invJP = MMath.invert4x4Matrix(getMatrizSRUsrc());
        if (invJP != null)
            MMath.multiplicar(invJP, lista);
    }
    
    private void reverseFrontalConversion(Vertice v){
        v.setAll(
            (v.getX() / jpProportions[0]) + cam.getVRP().getX(),
            (v.getY() / jpProportions[1]) + cam.getVRP().getY(),
            0
        );
    }
    
    private void reverseLateralConversion(Vertice v){
        float copyY=v.getY(), copyX=v.getX();      
        v.setAll(
            0,
            (copyX / jpProportions[1]) + cam.getVRP().getY(),
            (copyY / jpProportions[0]) + cam.getVRP().getZ()
        );        
    }
    
    private void reverseTopConversion(Vertice v){        
        float copyY = v.getY();
        v.setAll(
            (v.getX() / jpProportions[0]) + cam.getVRP().getX(),
            0,
            (copyY    / jpProportions[1]) + cam.getVRP().getZ()
        );
    }
//</editor-fold>
    
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
                //return false;
                if (camN.getX() == 1.0)
                    return true;
                break;
            case Topo:
                if (camN.getY() == 1.0)
                    return true;
                break;
            default:
                throw new UnsupportedOperationException("Conversão reversa ortográfica não implementada para: " + vista);
        }
        return false;
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

    @Override
    public float getDP() {
        return (float) 0.;
    }

    //<editor-fold defaultstate="collapsed" desc="Matrizes Ortogonais">
    protected static final class MatrizOrtografica{
        protected static final float[][] MAT_FRENTE = {
            { 1,  0, 0, 0},
            { 0,  1, 0, 0},
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
