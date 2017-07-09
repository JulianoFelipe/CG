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
        super.addVertice(new Vertice(a.getY(), b.getX()));
    }
    
}
