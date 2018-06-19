/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

import m.anderson.Vertice;

/**
 * Mix de MMath e VMath
 * @author Juliano
 */
public class MVMath {
    /**
     * Multiplica matriz e Vertice, retornando
     * o resultado no mesmo vertice.
     * @param v
     * @param m
     */
    public static void multiplicar(Vertice v, float[][] m) {
        float x = (m[0][0] * v.getX()) + (m[0][1] * v.getY()) + (m[0][2] * v.getW());
        float y = (m[1][0] * v.getX()) + (m[1][1] * v.getY()) + (m[1][2] * v.getW());
        float z = (m[2][0] * v.getX()) + (m[2][1] * v.getY()) + (m[2][2] * v.getW());
        float w = (m[3][0] * v.getX()) + (m[3][1] * v.getY()) + (m[3][2] * v.getW());
        
        v.setAll(x, y, z,w);
    }
    
    /**
     * Multiplica matriz e Vertice, retornando
     * o resultado no mesmo vertice.
     * @param v
     * @param m
     */
    public static void multiplicar(Vertice v, double[][] m) {
        double x = (m[0][0] * v.getX()) + (m[0][1] * v.getY()) + (m[0][2] * v.getW());
        double y = (m[1][0] * v.getX()) + (m[1][1] * v.getY()) + (m[1][2] * v.getW());
        double z = (m[2][0] * v.getX()) + (m[2][1] * v.getY()) + (m[2][2] * v.getW());
        double w = (m[3][0] * v.getX()) + (m[3][1] * v.getY()) + (m[3][2] * v.getW());
        
        v.setAll((float)x,(float) y,(float) z, (float) w); /* MUCH PRECISÃO. VERY CASTING. WOW*/
    }
    
    /**
     * Multiplica matriz e Vertice, retornando
     * o resultado em um outro vertice.
     * @param v
     * @param m
     * @return 
     */
    public static Vertice multiplicarR(Vertice v, float[][] m) {
        float x = (m[0][0] * v.getX()) + (m[0][1] * v.getY()) + (m[0][2] * v.getW());
        float y = (m[1][0] * v.getX()) + (m[1][1] * v.getY()) + (m[1][2] * v.getW());
        float z = (m[2][0] * v.getX()) + (m[2][1] * v.getY()) + (m[2][2] * v.getW());
        float w = (m[3][0] * v.getX()) + (m[3][1] * v.getY()) + (m[3][2] * v.getW());
        
        Vertice vertice = new Vertice(x, y, z);
        if (w != 1)
            vertice.setW(w);
        return vertice;
    }
}
