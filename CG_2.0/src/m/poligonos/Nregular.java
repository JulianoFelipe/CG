/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

/**
 *
 * @author JFPS
 */
public class Nregular extends PointObject {
    private int noLados;
    private int radius;
    private Vertice center;

    //<editor-fold defaultstate="collapsed" desc="Constrututores">
    private float[][] buildNsided(double pos){
        float[][] lista = new float[4][noLados];
        
        double x,y;
        double angulo = pos;
        double increment = 2*Math.PI/ noLados;
        for (int i=0; i<noLados; i++, angulo += increment){
            x = center.getX() + ( radius * Math.cos(angulo));
            y = center.getY() + ( radius * Math.sin(angulo));
            
            setPoint(i, (float) x, (float) y, 0);
        }
        
        return lista;
    }
    
    public Nregular(int noLados, int radius, Vertice center) {
        this(noLados, radius, center, 0.);
    }
    
    public Nregular(int noLados, int radius, Vertice center, double pos){
        super(noLados); //Valido sempre?
        this.radius = radius;
        this.noLados = noLados;
        this.center = center;
        
        buildNsided(pos);
    }
    
    public Nregular(Nregular nregular){
        super(nregular.ID, nregular.noLados); //Valido sempre?
        this.radius  = nregular.radius;
        this.noLados = nregular.noLados;
        this.center  = nregular.center;
        
        buildNsided(0.);
    }
//</editor-fold>
    
    public int getNoLados() {
        return noLados;
    }

    public int getRadius() {
        return radius;
    }

    public Vertice getCenter() {
        return center;
    }
    
    @Override
    public String toString() {
        return "Polígono Regular: ID=" + ID + "; Lados=" + noLados + "; Centro=" + center + "; Raio=" + radius + '.';
    }

    /*@Override
    public Vertice getCentroideDaMedia() {
        return center;
    }*/
    
    
}
