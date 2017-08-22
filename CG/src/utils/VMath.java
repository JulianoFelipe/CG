/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import Model.Aresta;
import Model.Movimento;
import Model.Transformações.Translacao;
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
    
    public static boolean isLineExactlyHorizontal(Aresta line){
        double m = lineSlope(line);
        return (m==0.0) || (m==-0.0);
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
    
    public static boolean isLineExactlyVertical(Aresta line){
        double m = lineSlope(line);
        return Double.isInfinite(m);
    }
    
    public static void main(String...args){
        Vertice a = new Vertice((float) 1, (float)1);
        Vertice b = new Vertice((float) 1, (float)2);
        
        System.out.println("SLOPE: " + lineSlope(new Aresta(a, b)));
        System.out.println("Is Hor: " + isLineHorizontal(a, b));
        System.out.println("Is Ver: " + isLineVertical(a, b));
        
        System.out.println(movimentoSegundoParaPrimeiro(a, b));
    }
    
    /**
     * Movimento do segundo vértice em relação ao primeiro.
     * Se o segundo está "Acima" do primeiro, é retornado
     * "Movimento.Cima"; e assim sucessivamente.
     * 
     * @param primeiro vértice.
     * @param segundo vértice.
     * @return Movimento do segundo em relação ao primeiro.
     */
    public static Movimento movimentoSegundoParaPrimeiro(Vertice primeiro, Vertice segundo){
        Movimento hori = movimentoHorizontal(primeiro, segundo);
        Movimento vert = movimentoVertical(primeiro, segundo);
        
        return Movimento.compor(vert, hori);
    }
    
    public static Movimento movimentoHorizontal(Vertice primeiro, Vertice segundo){
        if (segundo.getX() < primeiro.getX()) return Movimento.Esquerda;
        else if (segundo.getX() > primeiro.getX()) return Movimento.Direita;
        else return Movimento.Estatico;
    }
    
    public static Movimento movimentoVertical(Vertice primeiro, Vertice segundo){
        if (segundo.getY() < primeiro.getY()) return Movimento.Baixo;
        else if (segundo.getY() > primeiro.getY()) return Movimento.Cima;
        else return Movimento.Estatico;
    }
}

///Rascunho
//        if (primeiro.equals(segundo)) return Movimento.Estatico;
//        
//        /*
//        Transladam-se ambos os pontos e
//        retorna o movimento em relação
//        à posição do segundo ponto
//        nos quadrantes do plano XY.
//        
//        Ex.:
//             |   
//           2 | 
//         ----1----
//             | 
//             |  
//        
//        Dois está à esquerda cima
//        */
//        float x = primeiro.getX(), y = primeiro.getY();
//        //primeiro.setX(0); primeiro.setY(0);
//        segundo.setX(segundo.getX() - x);
//        segundo.setY(segundo.getY() - y);
//        
//        /* (Desconsidere. Anotação "mental")
//        Ex. octante:
//          \  |   /
//          2\ | /
//         ----1----
//           / | \
//         /   |  \
//        */
//        
//        int right = (segundo.getX() >  0.0 ? 1 : 0); //Direita se x é positivo
//        int up    = (segundo.getY() >  0.0 ? 1 : 0); //Cima se y é positivo
//        int ex_up = (segundo.getY() == 0.0 ? 1 : 0);
//        int ex_rg = (segundo.getX() == 0.0 ? 1 : 0); 
//        
//        Movimento E = null;
//        
//        //1D - Right
//        //2D - Up
//        //3D - Exactly in Y
//        //4D - Exactly in X
//        Movimento lookup[][][][] = new Movimento[2][2][2][2];
//        lookup[1][1][0][0] = Movimento.Cima_Direita;
//        lookup[0][1][0][0] = Movimento.Cima_Esquerda;
//        lookup[1][0][0][0] = Movimento.Baixo_Direita;
//        lookup[1][1][0][0] = Movimento.Baixo_Esquerda;
//        lookup[0][0][1][0] = Movimento.Cima;
//        lookup[0][0][0][0] = Movimento.Baixo;
//        lookup[0][0][0][1] = Movimento.Direita;
//        lookup[0][0][0][0] = Movimento.Esquerda;
//        
//        int[][][] threeDArray = 
//    {  { {1,   2,  3}, { 4,  5,  6}, { 7,  8,  9} },
//       { {10, 11, 12}, {13, 14, 15}, {16, 17, 18} },
//       { {19, 20, 21}, {22, 23, 24}, {25, 26, 27} } };
//        
//             if ( up &&  right && !ex_up && !ex_rg) return Movimento.Cima_Direita;
//        else if ( up && !right && !ex_up && !ex_rg) return Movimento.Cima_Esquerda;
//        else if (!up &&  right && !ex_up && !ex_rg) return Movimento.Baixo_Direita;
//        else if (!up && !right && !ex_up && !ex_rg) return Movimento.Cima;
//        else if ( up &&  right &&  ex_up &&  ex_rg) return Movimento.Cima;
//        else if ( up && !right &&  ex_up &&  ex_rg) return Movimento.Cima;
//        else if (!up &&  right &&  ex_up &&  ex_rg) return Movimento.Cima;
//        else if (!up && !right &&  ex_up &&  ex_rg) return Movimento.Cima;
//        
//        return Movimento.Baixo;
//    }
