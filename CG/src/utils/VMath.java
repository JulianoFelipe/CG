/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Vertice;

/**
 * Vector/Vertice math
 * @author Juliano
 */
public class VMath {
    public static void normalizar(Vertice v) {
        double norma;
        norma = Math.sqrt((v.getX() * v.getX()) + (v.getY() * v.getY()));

        if (norma != 0) {
            v.setX((float) (v.getX() / norma));
            v.setY((float) (v.getY() / norma));
        }
    }
    
    /**
     * Produto vetorial de vertices
     *
     * @param v1
     * @param v2
     * @return
     */
    public static double produtoVetorial(Vertice v1, Vertice v2) {
        // i = v1.y * v2.z - (v2.y * v1.z)
        double i = v1.getY() * 0 - v2.getY() * 0;
        // j = v1.z * v2.x - (v2.z * v1.x)
        double j = 0 * v2.getX() - 0 * v1.getX();
        // k = v1.x * v2.y - (v2.x * v1.y)
        double k = v1.getX() * v2.getY() - v2.getX() * v1.getY();

        return k;
    }
    
    /**
     * Produto escalar de vertices
     *
     * @param v1
     * @param escalar
     * @return
     */
    public static Vertice produtoEscalar(Vertice v1, float escalar) {
        double x = v1.getX() * escalar;
        double y = v1.getY() * escalar;

        return new Vertice((float) x, (float) y);
    }
}
