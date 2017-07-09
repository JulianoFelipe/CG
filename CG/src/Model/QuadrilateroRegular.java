/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model;

/**
 *
 * @author JFPS
 */
public class QuadrilateroRegular extends Poligono{
    private Vertice a,b;

    public QuadrilateroRegular(Vertice a, Vertice b) {
        this.a = a;
        this.b = b;
        super.addVertice(a);
        super.addVertice(b);
        super.addVertice(new Vertice(a.getX(), b.getY()));
        super.addVertice(new Vertice(b.getX(), a.getY()));
    }
    
    public float getMinX() {
        if (a.getX()< b.getX())
            return a.getX();
        else
            return b.getX();
    }
    
    public float getMinY() {
        if (a.getY()< b.getY())
            return a.getY();
        else
            return b.getY();
    }
    
    public float getMaxX(){
        if (a.getX()> b.getX())
            return a.getX();
        else
            return b.getX();
    }
    
    public float getMaxY() {
        if (a.getY()> b.getY())
            return a.getY();
        else
            return b.getY();
    }
    
    public float getWidth(){
        return getMaxX() - getMinX();
    }
    
    public float getHeight(){
        return getMaxY() - getMinY();
    }
    
    
    
}
