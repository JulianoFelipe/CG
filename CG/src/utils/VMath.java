/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Aresta;
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
    
    public static double distancia(Vertice v1, Vertice v2){
        double firstT = Math.pow((v1.getX() - v2.getX()),2);
        double secndT = Math.pow((v1.getY() - v2.getY()),2);
        
        return Math.sqrt(firstT + secndT);
    }
    
    //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
    public static double shortestDistance(Aresta a, Vertice p) {
        float px=a.getvFinal().getX() - a.getvInicial().getX();
        float py=a.getvFinal().getY() - a.getvInicial().getY();
        float temp=(px*px)+(py*py);
        float u=((p.getX() - a.getvInicial().getX()) * px + (p.getY() - a.getvInicial().getY()) * py) / (temp);
        if(u>1){
            u=1;
        }
        else if(u<0){
            u=0;
        }
        float x = a.getvInicial().getX() + u * px;
        float y = a.getvInicial().getY() + u * py;

        float dx = x - p.getX();
        float dy = y - p.getY();
        double dist = Math.sqrt(dx*dx + dy*dy);
        return dist;
    }
    
    public static double shortestDistance(Vertice lineA, Vertice lineB, Vertice point){
        return shortestDistance(new Aresta(lineA, lineB), point);
    }
    
    public static double lineSlope(Aresta line){
        return (line.getvFinal().getY() - line.getvInicial().getY()) / 
               (line.getvFinal().getX() - line.getvInicial().getX());
    }
    
    public static boolean isLineHorizontal(Aresta line){
        Double m = lineSlope(line);
        //return m==0.0; //Exactly Horizontal
        return m>-1.0 && m<1.0;
    }
    
    public static boolean isLineHorizontal(Vertice a, Vertice b){
        return isLineHorizontal(new Aresta(a, b));
    }
    
    public static boolean isLineVertical(Aresta line){
        //return !isLineVertical(line);
        Double m = lineSlope(line);
        //return m==Double.POSITIVE_INFINITY || m==Double.NEGATIVE_INFINITY; //Exaclty Vertical
        return m<-1.0 || m>1.0;
    }
    
    public static boolean isLineVertical(Vertice a, Vertice b){
        return isLineVertical(new Aresta(a, b));
    }
    
    public static void main(String...args){
        Vertice a = new Vertice((float) 1., (float)1.);
        Vertice b = new Vertice((float) 2.0, (float)1);
        
        System.out.println("SLOPE: " + lineSlope(new Aresta(a, b)));
        System.out.println("Is Hor: " + isLineHorizontal(a, b));
        System.out.println("Is Ver: " + isLineVertical(a, b));
    }
}
