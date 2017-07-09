/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.manipulation;

import Model.Aresta;
import Model.Poligono;
import Model.visualizacao.Camera;
import Model.Vertice;
import java.awt.Color;
import java.awt.Graphics;
import utils.MVMath;

/**
 *
 * @author Anderson
 */
public class CPintura {
    private static Camera cam = Camera.getCamera();
    
    private static final Color MASTERBORDER = Color.BLACK;
    
    private static Graphics g1;
    private static Graphics g2;
    private static Graphics g3;
    private static Graphics g4;

    //<editor-fold defaultstate="collapsed" desc="Getters & Setters">
    public static Graphics getG1() {
        return g1;
    }
    
    public static void setG1(Graphics g1) {
        CPintura.g1 = g1;
    }
    
    public static Graphics getG2() {
        return g2;
    }
    
    public static void setG2(Graphics g2) {
        CPintura.g2 = g2;
    }
    
    public static Graphics getG3() {
        return g3;
    }
    
    public static void setG3(Graphics g3) {
        CPintura.g3 = g3;
    }
    
    public static Graphics getG4() {
        return g4;
    }
    
    public static void setG4(Graphics g4) {
        CPintura.g4 = g4;
    }
//</editor-fold>
    
    public static void destacarVertice(Vertice v, boolean destacar) {
        // Arredondando as coordenadas dos vertices da Aresta
        int x;
        int y;

        Vertice vertice;

        if (destacar) {
            g1.setColor(Color.BLACK);
            g2.setColor(Color.BLACK);
            g3.setColor(Color.BLACK);
        } else {
            g1.setColor(Color.WHITE);
            g2.setColor(Color.WHITE);
            g3.setColor(Color.WHITE);
        }

        
        vertice = MVMath.multiplicarR(v, cam.getProjecao().getmOrtograficaVistaFrontal());
        x = Math.round(vertice.getX());
        y = Math.round(vertice.getY());
        // pintar vertice na Vista Frontal
        g1.drawLine(x, y, x, y);
        //pintar as coordenadas envolta do vertice
        g1.drawLine(x, y + 1, x, y + 1);
        g1.drawLine(x + 1, y, x + 1, y);
        g1.drawLine(x + 1, y + 1, x + 1, y + 1);
        g1.drawLine(x, y - 1, x, y - 1);
        g1.drawLine(x + 1, y - 1, x + 1, y - 1);
        g1.drawLine(x - 1, y, x - 1, y);
        g1.drawLine(x - 1, y - 1, x - 1, y - 1);
        g1.drawLine(x - 1, y + 1, x - 1, y + 1);


        // pintar vertice na Vista Lateral
        vertice = MVMath.multiplicarR(v, cam.getProjecao().getmOrtograficaVistaLateral());
        x = Math.round(vertice.getX());
        y = Math.round(vertice.getY());
        g2.drawLine(x, y, x, y);
        //pintar as coordenadas envolta do vertice
        g2.drawLine(x, y + 1, x, y + 1);
        g2.drawLine(x + 1, y, x + 1, y);
        g2.drawLine(x + 1, y + 1, x + 1, y + 1);
        g2.drawLine(x, y - 1, x, y - 1);
        g2.drawLine(x + 1, y - 1, x + 1, y - 1);
        g2.drawLine(x - 1, y, x - 1, y);
        g2.drawLine(x - 1, y - 1, x - 1, y - 1);
        g2.drawLine(x - 1, y + 1, x - 1, y + 1);

        // pintar vertice na Vista de Topo
        vertice = MVMath.multiplicarR(v, cam.getProjecao().getmOrtograficaVistaTopo());
        x = Math.round(vertice.getX());
        y = Math.round(vertice.getY());
        g3.drawLine(x, y, x, y);
        //pintar as coordenadas envolta do vertice
        g3.drawLine(x, y + 1, x, y + 1);
        g3.drawLine(x + 1, y, x + 1, y);
        g3.drawLine(x + 1, y + 1, x + 1, y + 1);
        g3.drawLine(x, y - 1, x, y - 1);
        g3.drawLine(x + 1, y - 1, x + 1, y - 1);
        g3.drawLine(x - 1, y, x - 1, y);
        g3.drawLine(x - 1, y - 1, x - 1, y - 1);
        g3.drawLine(x - 1, y + 1, x - 1, y + 1);

    }

    public static void PintarBordasPoligono(Poligono p) {
        g1.setColor(MASTERBORDER);
        g2.setColor(MASTERBORDER);
        g3.setColor(MASTERBORDER);
        g4.setColor(MASTERBORDER);

        p.getFaces().stream().forEach((face) -> {
            face.getListaArestas().stream().forEach((aresta) -> {
                pintarArestas(aresta, face);
            });
        });
    }
    
    public static void pintarArestaFrontal(Aresta a, Face f) {
        Vertice vI;
        Vertice vF;
        int xi;
        int yi;
        int xf;
        int yf;

        if (f.isVisivelFrontal()) {
            vI = MVMath.multiplicarR(a.getvInicial(), cam.getProjecao().getmOrtograficaVistaFrontal());
            vF = MVMath.multiplicarR(a.getvFinal(), cam.getProjecao().getmOrtograficaVistaFrontal());
            xi = Math.round(vI.getX());
            yi = Math.round(vI.getY());
            xf = Math.round(vF.getX());
            yf = Math.round(vF.getY());
            g1.drawLine(xi, yi, xf, yf);
        }
    }

    public static void pintarArestaLateral(Aresta a, Face f) {
        Vertice vI;
        Vertice vF;
        int xi;
        int yi;
        int xf;
        int yf;

        if (f.isVisivelLateral()) {
            vI = MVMath.multiplicarR(a.getvInicial(), cam.getProjecao().getmOrtograficaVistaLateral());
            vF = MVMath.multiplicarR(a.getvFinal(), cam.getProjecao().getmOrtograficaVistaLateral());
            xi = Math.round(vI.getX());
            yi = Math.round(vI.getY());
            xf = Math.round(vF.getX());
            yf = Math.round(vF.getY());
            g2.drawLine(xi, yi, xf, yf);
        }

    }

    public static void pintarArestaTopo(Aresta a, Face f) {
        Vertice vI;
        Vertice vF;
        int xi;
        int yi;
        int xf;
        int yf;

        if (f.isVisivelTopo()) {
            vI = MVMath.multiplicarR(a.getvInicial(), cam.getProjecao().getmOrtograficaVistaTopo());
            vF = MVMath.multiplicarR(a.getvFinal(), cam.getProjecao().getmOrtograficaVistaTopo());
            xi = Math.round(vI.getX());
            yi = Math.round(vI.getY());
            xf = Math.round(vF.getX());
            yf = Math.round(vF.getY());
            g3.drawLine(xi, yi, xf, yf);
        }
    }

    public static void pintarArestaPerspectiva(Aresta a, Face f) {
        Vertice vI;
        Vertice vF;
        int xi;
        int yi;
        int xf;
        int yf;

        if (f.isVisivelPerspectiva()) {
            vI = MVMath.multiplicarR(a.getvInicial(), cam.getProjecao().getmPers());
            vF = MVMath.multiplicarR(a.getvFinal(), cam.getProjecao().getmPers());
            if (vI.getW() != 1 || vF.getW() != 1) {
                vI = CFuncoes.transformaCoordenadasHomogeneas(vI);
                vF = CFuncoes.transformaCoordenadasHomogeneas(vF);
            }
            xi = Math.round(vI.getX());
            yi = Math.round(vI.getY());
            xf = Math.round(vF.getX());
            yf = Math.round(vF.getY());
            g4.drawLine(xi, yi, xf, yf);
        }
    }

    public static void pintarArestas(Aresta a, Face f) {
        pintarArestaFrontal(a, f);
        pintarArestaLateral(a, f);
        pintarArestaTopo(a, f);
        pintarArestaPerspectiva(a, f);
    }
    
    public static void PintarPoligono(Poligono p) {
        PintarBordasPoligono(p);
    }
}
