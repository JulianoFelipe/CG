/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.poligonosEsp;

import Model.Poligono;
import Model.Vertice;

/**
 *
 * @author JFPS
 */
public class Triangulo extends Poligono{
    private Vertice a,b,c;

    public Triangulo(Vertice a, Vertice b, Vertice c) {
        this.a = a;
        this.b = b;
        this.c = c;
        super.addVertice(a);
        super.addVertice(b);
        super.addVertice(c);
    }

    public Vertice getA() {
        return a;
    }

    public void setA(Vertice a) {
        this.a = a;
    }

    public Vertice getB() {
        return b;
    }

    public void setB(Vertice b) {
        this.b = b;
    }

    public Vertice getC() {
        return c;
    }

    public void setC(Vertice c) {
        this.c = c;
    }
    
    @Override
    public String toString() {
        return "Triângulo {" + a + "; " + b + " ;" + c + '}';
    }
}
