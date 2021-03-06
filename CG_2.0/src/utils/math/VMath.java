/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils.math;

import m.Eixo;
import m.poligonos.Aresta;
import m.poligonos.Movimento;
import m.poligonos.Vertice;

/**
 * Vector/Vertice math
 * @author Juliano
 */
public class VMath {
    
    public static Vertice obterNormal(Vertice a, Vertice b, Vertice c){
        Vertice ab = new Vertice(b.getX()-a.getX(), b.getY()-a.getY(), b.getZ()-a.getZ());
        Vertice ac = new Vertice(c.getX()-a.getX(), c.getY()-a.getY(), c.getZ()-a.getZ());
        
        return VMath.produtoVetorial(ab, ac);
    }
    
    public static void normalizar(Vertice v) {
        double norma;
        norma = Math.sqrt((v.getX() * v.getX()) + 
                          (v.getY() * v.getY()) +
                          (v.getZ() * v.getZ()));

        if (norma != 0) {
            v.setX((float) (v.getX() / norma));
            v.setY((float) (v.getY() / norma));
            v.setZ((float) (v.getZ() / norma));
        }
    }
    
    public static void soma(Vertice a, Vertice b){
        a.setAll(
            a.getX() + b.getX(),
            a.getY() + b.getY(),
            a.getZ() + b.getZ()
        );
    }
    
    public static void dividirEscalar(Vertice a, float escalar){
        a.setAll(
            a.getX() / escalar,
            a.getY() / escalar,
            a.getZ() / escalar
        );
    }
    
    /**
     * Produto vetorial de vertices
     *
     * @param v1
     * @param v2
     * @return
     */
    public static Vertice produtoVetorial(Vertice v1, Vertice v2) {
        // i = v1.y * v2.z - (v2.y * v1.z)
        double i = v1.getY() * v2.getZ() - v2.getY() * v1.getZ();
        // j = v1.z * v2.x - (v2.z * v1.x)
        double j = v1.getZ() * v2.getX() - v2.getZ() * v1.getX();
        // k = v1.x * v2.y - (v2.x * v1.y)
        double k = v1.getX() * v2.getY() - v2.getX() * v1.getY();
        
        return new Vertice ((float) i, (float) j, (float) k);
    }
    
    /**
     * Produto escalar de vertices
     *
     * @param v1
     * @param escalar
     * @return
     */
    public static Vertice produto(Vertice v1, double escalar) {
        double x = v1.getX() * escalar;
        double y = v1.getY() * escalar;
        double z = v1.getZ() * escalar;

        return new Vertice((float) x, (float) y, (float) z);
    }
    
    public static double produtoEscalar (Vertice v1, Vertice v2){
        return (v1.getX() * v2.getX()) +
               (v1.getY() * v2.getY()) +
               (v1.getZ() * v2.getZ());
    }
    
    public static double distancia(Vertice v1, Vertice v2){
        double firstT = Math.pow((v1.getX() - v2.getX()),2);
        double secndT = Math.pow((v1.getY() - v2.getY()),2);
        double thirdT = Math.pow((v1.getZ() - v2.getZ()),2);
        
        return Math.sqrt(firstT + secndT + thirdT);
    }
    
    public static double distancia(float x1, float y1, float x2, float y2){
        double firstT = Math.pow((x1 - x2),2);
        double secndT = Math.pow((y1 - y2),2);
        
        return Math.sqrt(firstT + secndT);
    }
    
    //https://stackoverflow.com/questions/849211/shortest-distance-between-a-point-and-a-line-segment
    public static double shortestDistance2D(Aresta a, Vertice p) {
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
    
    public static double shortestDistance2D(Vertice lineA, Vertice lineB, Vertice point){
        return shortestDistance2D(new Aresta(lineA, lineB), point);
    }
    
    public static double[] slopeInterceptForm(Aresta line){
        double slope = (line.getvFinal().getY() - line.getvInicial().getY()) / 
                       (line.getvFinal().getX() - line.getvInicial().getX());
        
        double b = line.getvInicial().getY() + 
                  (line.getvInicial().getX()*slope);
        
        double[] ret = {slope, b};
        return ret;
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
        /*double m = lineSlope(line);
        return (m==0.0) || (m==-0.0);*/
        return line.getvInicial().getY() == line.getvFinal().getY();
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
    
    //public static void main(String...args){
        //Vertice a = new Vertice((float) 2, (float)3);
        //Vertice b = new Vertice((float) 8, (float)1);
        /*Vertice c = new Vertice( 2, 2);
        Vertice d = new Vertice( 3, 4);
        
        List<Vertice> lista = new ArrayList();
        lista.add(b); lista.add(d); lista.add(c); lista.add(a);
        
        System.out.println(lista);
        
        Collections.sort(lista, new Comparator<Vertice>() {
            @Override
            public int compare(Vertice o1, Vertice o2) {
                return Float.compare(o1.getX(), o2.getX());
            }
        });
        
        System.out.println(lista);*/
        
        //System.out.println("SLOPE INTER: " + Arrays.toString(slopeInterceptForm(new Aresta(a, b))));
        //System.out.println("SLOPE: " + lineSlope(new Aresta(a, b)));
        //System.out.println("Is Hor: " + isLineHorizontal(a, b));
        //System.out.println("Is Ver: " + isLineVertical(a, b));
        
        //System.out.println(movimentoSegundoParaPrimeiro(a, b));
    //}
    
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
    
    public static Movimento movimento(float primeiro, float segundo, Eixo eixo){
        if (eixo == Eixo.Eixo_X){
                 if (segundo < primeiro) return Movimento.Esquerda;
            else if (segundo > primeiro) return Movimento.Direita;
            else return Movimento.Estatico;
        } else if (eixo == Eixo.Eixo_Y){
                 if (segundo < primeiro) return Movimento.Baixo;
            else if (segundo > primeiro) return Movimento.Cima;
            else return Movimento.Estatico;
        } else {
            throw new IllegalArgumentException("Movimento não implementado para: " + eixo);
        }
    }
    
    public static Movimento movimentoVertical(Vertice primeiro, Vertice segundo){
             if (segundo.getY() < primeiro.getY()) return Movimento.Baixo;
        else if (segundo.getY() > primeiro.getY()) return Movimento.Cima;
        else return Movimento.Estatico;
    }
    
    public static Movimento invertHorizontal(Movimento m){
        if (m == Movimento.Direita) return Movimento.Esquerda;
        else if (m == Movimento.Esquerda) return Movimento.Direita;
        else return m;
    }
    
    public static Movimento invertVertical(Movimento m){
        if (m == Movimento.Baixo) return Movimento.Cima;
        else if (m == Movimento.Cima) return Movimento.Baixo;
        else return m;
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
