/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

/**
 *
 * @author JFPS
 */
public class Fatores {
    /////////////////////////////////////////////////////////////
    // DEFAULT UNCHANGEABLE
    public static final float DEFAULT_OPACITY  = (float) 0.35;
    public static final int POINT_RADIUS = 5;
    
    /////////////////////////////////////////////////////////////
    // DEFAULT FATORES NUMERICOS
    public static final float DEFAULT_MOVIMENTO_ORT  = (float) 1;
    public static final float DEFAULT_MOVIMENTO_PERS = (float) 0.1;
    
    public static final float DEFAULT_ZOOM = (float) 0.05;
    
    public static final int   DEFAULT_THRESHOLD = 20;
    
    public static final float DEFAULT_ROTACAO      = (float) 0.05;
    public static final float DEFAULT_ESCALA       = 1;
    public static final float DEFAULT_CISALHAMENTO = 1;
    
    /////////////////////////////////////////////////////////////
    // FATORES NUMERICOS ALTERÁVEIS
    public static float fator_movimento_ort  = DEFAULT_MOVIMENTO_ORT;
    public static float fator_movimento_pers = DEFAULT_MOVIMENTO_PERS;
    
    public static float fator_zoom = DEFAULT_ZOOM;

    public static int   fator_threshold = DEFAULT_THRESHOLD;
    
    public static float fator_rotacao      = DEFAULT_ROTACAO;
        
    private static float fator_escala_plus  = 1+(DEFAULT_ESCALA/100);
    private static float fator_escala_minus = (100-DEFAULT_ESCALA)/100;
    private static float fator_cisalhamento_plus  = +((DEFAULT_CISALHAMENTO)/100);
    private static float fator_cisalhamento_minus = -((DEFAULT_CISALHAMENTO)/100);
    
    public static float getFatorEscalaPlus() {
        return fator_escala_plus;
    }

    public static float getFatorEscalaMinus() {
        return fator_escala_minus;
    }
    
    public static void setFatorEscala(float fator_escala) {
        Fatores.fator_escala_plus  = 1+(Math.abs(fator_escala)/100);
        Fatores.fator_escala_minus = (100-Math.abs(fator_escala))/100;
    } 
    
    public static float getFatorCisalhamentoPlus() {
        return fator_cisalhamento_plus;
    }

    public static float getFatorCisalhamentoMinus() {
        return fator_cisalhamento_minus;
    }
    
    public static void setFatorCisalhamento(float fator_cisalhamento) {
        Fatores.fator_cisalhamento_plus  = +((Math.abs(fator_cisalhamento))/100);
        Fatores.fator_cisalhamento_minus = -((Math.abs(fator_cisalhamento))/100);
    } 
    
}
