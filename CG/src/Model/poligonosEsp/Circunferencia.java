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
public class Circunferencia extends Poligono{
    private Vertice centro;
    private int radius;

    public Circunferencia(Vertice centro, int radius) {
        this.centro = centro;
        this.radius = radius;
    }

    public Vertice getCentro() {
        return centro;
    }

    public void setCentro(Vertice centro) {
        this.centro = centro;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    @Override
    public String toString() {
        return "Circunferência {Centro: " + centro + "; Raio: " + radius + '}';
    }
    
    
}
