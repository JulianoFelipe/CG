/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.manipulation;

import Model.Aresta;
import Model.Classificacao.Prismas;
import static Model.Classificacao.Prismas.*;
import Model.Classificacao.SolidosExtras;
import Model.Face;
import Model.Poligono;
import Model.Vertice;
import java.util.ArrayList;

/**
 * Classe com métodos
 * para criar prismas
 * e etc.
 * @author Juliano
 */
public class CriadorSolidos {
    /**
     * Coordenadas padrão para o
     * posicionar o primeiro vértice
     * de um prisma.
     */
    private static final int DEFAULTX=0;
    private static final int DEFAULTY=0;
    private static final int DEFAULTZ=0;
    private static final int DEFAULTARESTA=10;
    
    public static Poligono criarPrisma(Prismas tipoDePrisma){
        switch (tipoDePrisma){
            case Triangular:
                return prismaTriangular();
            case Quadrangular:
                return prismaQuadrangular();
            case Pentagonal:
                return criadorGenerico(tipoDePrisma.getCodigo());
            case Hexagonal:
                return criadorGenerico(tipoDePrisma.getCodigo());
            case Heptagonal:
                return criadorGenerico(tipoDePrisma.getCodigo());
            default:
                throw new UnsupportedOperationException("Criação de prisma não implementada.");
        }
    }
    
    public static Poligono criarSolido(SolidosExtras tipoDeSolido){
        switch (tipoDeSolido){
            /*case Esfera:
                return esfera();*/
            default:
                throw new UnsupportedOperationException("Criação de prisma não implementada.");
        }
    }
    
    //<editor-fold defaultstate="collapsed" desc="Triangular">
    public static Poligono prismaTriangular(){
        return prismaTriangular(DEFAULTX, DEFAULTY, DEFAULTZ, DEFAULTARESTA);
    }
    
    public static Poligono prismaTriangular(int x, int y, int z, int aresta){
        CriadorSolidos.PrismasGenericos p =
            new CriadorSolidos.PrismasGenericos(x, y, z, aresta, Triangular, aresta);
        return p.criadorGenerico();
    }
//</editor-fold>
    
    //<editor-fold defaultstate="collapsed" desc="Quadrangular">
    public static Poligono prismaQuadrangular(){
        return prismaQuadrangular(DEFAULTX, DEFAULTY, DEFAULTZ, DEFAULTARESTA);
    }
    
    public static Poligono prismaQuadrangular(int x, int y, int z, int aresta){
        Poligono p = new Poligono();
        
        Vertice v1 = new Vertice(x, y, z);
        Vertice v2 = new Vertice(x+aresta, y, z);
        Vertice v3 = new Vertice(x, y+aresta, z);
        Vertice v4 = new Vertice(x+aresta, y+aresta, z);
        Vertice v5 = new Vertice(x, y, z+aresta);
        Vertice v6 = new Vertice(x+aresta, y, z+aresta);
        Vertice v7 = new Vertice(x, y+aresta, z+aresta);
        Vertice v8 = new Vertice(x+aresta, y+aresta, z+aresta);
        
        Aresta a1 = new Aresta(v1, v2);
        Aresta a2 = new Aresta(v2, v3);
        Aresta a3 = new Aresta(v3, v4);
        Aresta a4 = new Aresta(v4, v1);
        Aresta a5 = new Aresta(v5, v6);
        Aresta a6 = new Aresta(v6, v7);
        Aresta a7 = new Aresta(v7, v8);
        Aresta a8 = new Aresta(v8, v5);
        Aresta a9 = new Aresta(v5, v1);
        Aresta a10 = new Aresta(v2, v6);
        Aresta a11 = new Aresta(v7, v3);
        Aresta a12 = new Aresta(v4, v8);
        
        //face FRONTAL
        ArrayList<Aresta> listaArestas = new ArrayList<>();
        listaArestas.add(a1);
        listaArestas.add(a2);
        listaArestas.add(a3);
        listaArestas.add(a4);
        Face f = new Face(listaArestas);
        f.setNome("Frontal");
        p.addFace(f);
        
        //face FUNDO
        listaArestas = new ArrayList<>();
        listaArestas.add(a5);
        listaArestas.add(a6);
        listaArestas.add(a7);
        listaArestas.add(a8);
        f = new Face(listaArestas);
        f.setNome("Fundo");
        p.addFace(f);
        
        //face ESQUERDA
        listaArestas = new ArrayList<>();
        listaArestas.add(a5);
        listaArestas.add(a10);
        listaArestas.add(a1);
        listaArestas.add(a9);
        f = new Face(listaArestas);
        f.setNome("Esquerda");
        p.addFace(f);
        
        //face DIREITA
        listaArestas = new ArrayList<>();
        listaArestas.add(a7);
        listaArestas.add(a12);
        listaArestas.add(a3);
        listaArestas.add(a11);
        f = new Face(listaArestas);
        f.setNome("Direita");
        p.addFace(f);
        
        //face TOPO
        listaArestas = new ArrayList<>();
        listaArestas.add(a9);
        listaArestas.add(a4);
        listaArestas.add(a12);
        listaArestas.add(a8);
        f = new Face(listaArestas);
        f.setNome("Topo");
        p.addFace(f);
        
        //face BAIXO
        listaArestas = new ArrayList<>();
        listaArestas.add(a10);
        listaArestas.add(a2);
        listaArestas.add(a11);
        listaArestas.add(a6);
        f = new Face(listaArestas);
        f.setNome("Baixo");
        p.addFace(f);
        
        p.updateArestas();
        
        return p;
    }
//</editor-fold>

    public static Poligono criadorGenerico(int lados) {
        return criadorGenerico(DEFAULTX, DEFAULTY, DEFAULTZ, DEFAULTARESTA, lados);
    }
    
    public static Poligono criadorGenerico(int x, int y, int z, int aresta, int lados) {
        CriadorSolidos.PrismasGenericos p =
            new CriadorSolidos.PrismasGenericos(x, y, z, aresta, lados, aresta);
        return p.criadorGenerico();
    }
    
    private static final class PrismasGenericos{
        private final double x;
        private final double y;
        private final double z;
        private final double aresta;
        private final int lados;
        private final double altura;

        public PrismasGenericos(double x, double y, double z, double aresta, int lados, double altura) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.aresta = aresta;
            this.lados = lados;
            this.altura = altura;
        }
        
        public PrismasGenericos(double x, double y, double z, double aresta, Prismas tipo, double altura) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.aresta = aresta;
            lados = tipo.getCodigo();
            this.altura = altura;
        }
        
        public Poligono criadorGenerico(){   
            ArrayList<Vertice> pontos = new ArrayList<>();
            Poligono prismaGenerico = new Poligono();
            ArrayList<Aresta> arestas = new ArrayList<>();

            int aresta = 75;
            double angulo = 0;
            double auxX;
            double auxY;

            int cont = 0;

            while (cont != lados) {
                auxX = ((Math.sin(Math.toRadians(angulo))) * aresta) + (1 / 2);
                auxX = Math.round((float) auxX);
                
                auxY = ((Math.cos(Math.toRadians(angulo))) * aresta) + (1 / 2);
                auxY = Math.round((float) auxY);

                Vertice pt = new Vertice(((float) auxX), ((float) auxY), (0));
                pontos.add(pt);

                angulo += (360 / lados);
                cont++;
            }

            int tam = pontos.size();
            ArrayList<Aresta> arestasFace = new ArrayList<>();

            //Face de uma das circunferências
            for (int i = 0; i < pontos.size() - 1; i++) {
                Aresta a = new Aresta(pontos.get(i), pontos.get(i + 1));
                arestasFace.add(a);
                if ((i + 1) == pontos.size() - 1) {
                    a = new Aresta(pontos.get(pontos.size() - 1), pontos.get(0));
                }
            }

            Face f = new Face(arestasFace);
            prismaGenerico.getFaces().add(f);

            arestasFace = new ArrayList<>();

            for (int i = 0; i < tam; i++) {
                pontos.add(new Vertice((pontos.get(i).getX()), (pontos.get(i).getY()), (pontos.get(i).getZ() + 200)));
                //fazendo a segunda circunferência com o z a 200 de distância
            }

            //Face da outra circunferência
            for (int i = lados; i < tam; i++) {
                Aresta a = new Aresta(pontos.get(i), pontos.get(i + 1));
                arestasFace.add(a);
                if ((i + 1) == pontos.size() - 1) {
                    a = new Aresta(pontos.get(pontos.size() - 1), pontos.get(0));
                }
            }
            f = new Face(arestasFace);
            prismaGenerico.getFaces().add(f);

            arestasFace = new ArrayList<>();

            //fazer faces/arestas

            tam = pontos.size();

            int j = lados;
            for (int i = 0; i < (tam - lados - 1); i++) {
                Aresta a1 = new Aresta(pontos.get(i), pontos.get(i + 1));
                Aresta a2 = new Aresta(pontos.get(i + 1), pontos.get(j + 1));
                Aresta a3 = new Aresta(pontos.get(j + 1), pontos.get(j));
                Aresta a4 = new Aresta(pontos.get(j), pontos.get(i));

                arestas.add(a1);
                arestas.add(a2);
                arestas.add(a3);
                arestas.add(a4);

                arestasFace.add(a1);
                arestasFace.add(a2);
                arestasFace.add(a3);
                arestasFace.add(a4);
                f = new Face(arestasFace);
                prismaGenerico.getFaces().add(f);

                arestasFace = new ArrayList<>();

                j++;
            }

            Aresta a1 = new Aresta(pontos.get(lados - 1), pontos.get(0));
            Aresta a2 = new Aresta(pontos.get(0), pontos.get(lados));
            Aresta a3 = new Aresta(pontos.get(lados), pontos.get(tam - 1));
            Aresta a4 = new Aresta(pontos.get(tam - 1), pontos.get(lados - 1));

            arestas.add(a1);
            arestas.add(a2);
            arestas.add(a3);
            arestas.add(a4);

            arestasFace.add(a1);
            arestasFace.add(a2);
            arestasFace.add(a3);
            arestasFace.add(a4);
            f = new Face(arestasFace);
            prismaGenerico.getFaces().add(f);

            arestasFace = new ArrayList<>();

            pontos.stream().map((v) -> {
                v.setAll((v.getX() + aresta + 1), (v.getY() + aresta + 1), v.getZ() + aresta + 1, 0);
                return v;
            }).forEach((v) -> {
                CPintura.destacarVertice(v, true);
            });

            prismaGenerico.setArestas(arestas);
            prismaGenerico.setVertices(pontos);
            return prismaGenerico;
        }
    }
}
