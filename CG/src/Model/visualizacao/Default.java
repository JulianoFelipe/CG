/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.visualizacao;

import Model.Vertice;

/**
 *
 * @author Juliano Felipe
 */
public final class Default {
    public static final float DIST_PLANO_PROJECAO = 100;
    private static final int DIST_P_OFFSET = 30; //Padrao de offset do NEAR_PLANE e FAR_PLANE do plano de proj.
    public static final float FAR_PLANE = DIST_PLANO_PROJECAO+DIST_P_OFFSET;
    public static final float NEAR_PLANE = DIST_PLANO_PROJECAO-DIST_P_OFFSET;
    
    public static final Viewport VIEWPORT = new Viewport(340, 0,
                                                255, 0,
                                                FAR_PLANE,
                                                NEAR_PLANE);
    
    public static final Window WINDOW = new Window(0,0,160,120);
    
    public static final Vertice VRP = new Vertice(5, 10, 80);
    
    public static final Vertice direcaoCamera = new Vertice(0, 0, 0);
    
    public static final Vertice Y = new Vertice(0, 1, 0); //No questions
    
    public static final Projecao projecao = new Projecao();
}
