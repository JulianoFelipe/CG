/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

import java.util.ArrayList;
import java.util.List;
import m.Eixo;
import m.poligonos.Vertice;
import m.poligonos.we_edge.HE_Poliedro;
import m.poligonos.we_edge.IndexList;

/**
 *
 * @author JFPS
 */
public class Revolucao {

    private List<Vertice> listaDePontos;
    private int secoes;
    private double angulo;
    private Eixo eixo;

    public Revolucao(List<Vertice> listaDePontos, int secoes, double angulo, Eixo eixo) {
        this.listaDePontos = listaDePontos;
        this.secoes = secoes;
        this.angulo = angulo;
        this.eixo = eixo;
    }

    public HE_Poliedro getPoli() {

        //Anderson, se tu quiser acessar a enum de eixos, é assim:
        switch (eixo) {
            case Eixo_X:
                //Faz algo com eixo X
                RevEixoX();
                break;
            case Eixo_Y:
                //Faz algo com eixo Y
                RevEixoY();
                break;
            case Eixo_Z:
                //Faz algo com eixo X
                RevEixoZ();
                break;
            default:
                throw new IllegalArgumentException("Eixo não esperado para revolução.");
        }

        float[][] pol_mat = {
            {30, 35, 25, 20, 30},
            {2, 4, 3, 1, 10},
            {25, 20, 18, 23, (float) 22.5},
            {1, 1, 1, 1, 1}
        //  A    B    C    D    E
        };

        int[] face0 = {0, 3, 2, 1};
        int[] face1 = {0, 1, 4};
        int[] face2 = {1, 2, 4};
        int[] face3 = {2, 3, 4};
        int[] face4 = {3, 0, 4};
        List<IndexList> faces = new ArrayList();
        faces.add(new IndexList(face0));
        faces.add(new IndexList(face1));
        faces.add(new IndexList(face2));
        faces.add(new IndexList(face3));
        faces.add(new IndexList(face4));

        HE_Poliedro poli = new HE_Poliedro(pol_mat, faces);

        return poli;
    }

    public void RevEixoX() {
        double teta = (angulo * (Math.PI / 180)) / secoes; //Calcula o angulo de distancia para cada passo da revolucao
        double ccos = Math.cos(teta); //Cosseno do angulo
        double csin = Math.sin(teta); //Seno do angulo

    }

    public void RevEixoY() {

        double teta = (angulo * (Math.PI / 180)) / secoes; //Calcula o angulo de distancia para cada passo da revolucao
        double ccos = Math.cos(teta); //Cosseno do angulo
        double csin = Math.sin(teta); //Seno do angulo

        int[] BPosi = new int[listaDePontos.size()];
        double xmin = listaDePontos.get(0).getY();
        double zmax = listaDePontos.get(0).getY();

        for (int i = 0; i < listaDePontos.size(); i++) { //Interacao inicial com os pontos do perfil
            Vertice copy = listaDePontos.get(i);
            listaDePontos.get(i).setAll(
                    (copy.getX()),
                    (copy.getY()),
                    (copy.getZ())
            );
            if (listaDePontos.get(i).getY() < xmin) {
                xmin = listaDePontos.get(i).getY(); //xold carrega y minimo
            }
            if (listaDePontos.get(i).getY() > zmax) {
                zmax = listaDePontos.get(i).getY(); //zold carrega y maximo
            }
        }

        //Ainda não entendi como fazer, mas é necessário*********************************************************
        /*
         //Constroi as arestas iniciais (primeiro perfil)
         obj.ConstroiArestasMaisFaces(fechado);
         int[] FPosi = new int[obj.arrFace.size()]; //Fposi carrega os indices das faces que estao sendo criadas
         int UPP = -1, PPP = -1;
         for (int i = 0; i < FPosi.length; i++) {
         FPosi[i] = i;
         }
         if (listaDePontos.get(0).getX() != 0) {
         PPP = 0; //Se o primeiro ponto e do eixo
         }
         if (listaDePontos.get(listaDePontos.size() - 1).getX() != 0) {
         UPP = 0; //Se o ultimo ponto e do eixo
         }
         //FAZER Definir se objeto e fechado ou nao (dupla face ou unica face)
         obj.Fechado = false; //Assim esta sempre aberto
         */
        //*************************************
        if (angulo == 360.0) { //Rotacao completa
            for (int i = 0; i < secoes - 1; i++) { //Todas as revolucoes menos uma (ja que junta no fim)
                int ij = 0; //Indice para vetor de indices de face sendo construida
                for (int y = 0; y < listaDePontos.size(); y++) {
                    plinha = listaDePontos.get(y); //plinha recebe cada um dos pontos do perfil
                    if (plinha.x != 0 || plinha.z != 0) { //Ponto fora do eixo
                        xmin = plinha.x;
                        plinha.x = (plinha.x * ccos) + (plinha.z * csin); //Rotaciona
                        plinha.z = (xmin * (-csin)) + (plinha.z * ccos);
                        obj.listaDePontos.add(new Ponto(plinha)); //copia plinha para o objeto
                        obj.arrAresta.add(new Aresta(BPosi[y], obj.listaDePontos.size() - 1)); //Aresta entre o ponto rotacionado e o seu irmão anterior
                        BPosi[y] = obj.listaDePontos.size() - 1; //Agora o irmao anterior passa a ser ele
                        obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1); //Adiciona aresta na face
                        obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij]; //Adiciona a aresta esta face como a esquerda
                        if ((FPosi.length - 1) > (ij + 1)) { //Se nao for a ultima face liga com a proxima tambem
                            obj.arrFace.get(FPosi[ij + 1]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = FPosi[ij + 1];
                        }
                        if (y == 0) {
                            PPP = obj.arrAresta.size() - 1; //Pode estar errado!
                        } else if (y > 0) { //Qualquer outro ponto que nao o primeiro (liga com o irmao de cima)
                            if (listaDePontos.get(y - 1).getX() != 0 || listaDePontos.get(y - 1).getZ() != 0) { //Ponto anterior tambem fora do eixo
                                obj.arrAresta.add(new Aresta(obj.listaDePontos.size() - 2, obj.listaDePontos.size() - 1));
                            } else { //Ponto anterior no eixo
                                obj.arrAresta.add(new Aresta(y - 1, obj.listaDePontos.size() - 1));
                            }
                            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrFace.add(new Face(obj.arrAresta.size() - 1));
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = obj.arrFace.size() - 1;
                            obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij];
                            FPosi[ij] = obj.arrFace.size() - 1;
                            ij++;
                        }
                    } else { //Ponto no eixo
                        if (y > 0) { //Se nao for o primeiro ponto (ja que so liga com o irmao de cima)
                            if (listaDePontos.get(y - 1).getX() != 0 || listaDePontos.get(y - 1).getZ() != 0) { //Ponto anterior tambem fora do eixo
                                obj.arrAresta.add(new Aresta(y, obj.listaDePontos.size() - 1));
                            } else {
                                ErroPadrao();
                            }
                            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrFace.add(new Face(obj.arrAresta.size() - 1));
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = obj.arrFace.size() - 1;
                            obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij];
                            FPosi[ij] = obj.arrFace.size() - 1;
                            ij++; //Para a proxima face
                        }
                    }
                }
            }
        } else { //Rotacao nao completa
            for (int i = 0; i < secoes; i++) { //Todas as revolucoes (vai ate o angulo)
                int ij = 0;
                for (int y = 0; y < listaDePontos.size(); y++) {
                    plinha = listaDePontos.get(y); //plinha recebe cada um dos pontos do perfil
                    if (plinha.x != 0 || plinha.z != 0) { //Ponto fora do eixo
                        xmin = plinha.x;
                        plinha.x = (plinha.x * ccos) + (plinha.z * csin); //Rotaciona
                        plinha.z = (xmin * (-csin)) + (plinha.z * ccos);
                        obj.listaDePontos.add(new Ponto(plinha)); //copia plinha
                        obj.arrAresta.add(new Aresta(BPosi[y], obj.listaDePontos.size() - 1));
                        BPosi[y] = obj.listaDePontos.size() - 1;
                        obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                        obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij];
                        if ((FPosi.length - 1) > (ij + 1)) {
                            obj.arrFace.get(FPosi[ij + 1]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = FPosi[ij + 1];
                        }
                        if (y == 0) {
                            PPP = obj.arrAresta.size() - 1;
                        } else if (y > 0) {
                            if (listaDePontos.get(y - 1).getX() != 0 || listaDePontos.get(y - 1).getZ() != 0) { //Ponto anterior tambem fora do eixo
                                obj.arrAresta.add(new Aresta(obj.listaDePontos.size() - 2, obj.listaDePontos.size() - 1));
                            } else {
                                obj.arrAresta.add(new Aresta(y - 1, obj.listaDePontos.size() - 1));
                            }
                            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrFace.add(new Face(obj.arrAresta.size() - 1));
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = obj.arrFace.size() - 1;
                            obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij];
                            FPosi[ij] = obj.arrFace.size() - 1;
                            ij++;
                        }
                    } else { //Ponto no eixo
                        if (y > 0) {
                            if (listaDePontos.get(y - 1).getX() != 0 || listaDePontos.get(y - 1).getX() != 0) { //Ponto anterior tambem fora do eixo
                                obj.arrAresta.add(new Aresta(y, obj.listaDePontos.size() - 1));
                            } else {
                                ErroPadrao();
                            }
                            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                            obj.arrFace.add(new Face(obj.arrAresta.size() - 1));
                            obj.arrAresta.get(obj.arrAresta.size() - 1).d = obj.arrFace.size() - 1;
                            obj.arrAresta.get(obj.arrAresta.size() - 1).e = FPosi[ij];
                            FPosi[ij] = obj.arrFace.size() - 1;
                            ij++;
                        }
                    }
                }
                if (fechado) {
                    obj.arrAresta.add(new Aresta(BPosi[BPosi.length - 1], BPosi[0]));
                    obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 1);
                    if (PPP != -1) {
                        obj.arrFace.get(FPosi[ij]).fAresta.add(PPP);
                        obj.arrAresta.get(PPP).e = FPosi[ij];
                    }
                    if (UPP == 0) { //Duvidas sobre esse trecho
                        obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size() - 3);
                        obj.arrAresta.get(obj.arrAresta.size() - 3).e = FPosi[ij];
                    }
                }
            }
        }
        obj.CalculaCentro(); //Calcula centrodo objeto para operacoes posteriores
    

}

public void RevEixoZ() {
        double teta = (angulo * (Math.PI / 180)) / secoes; //Calcula o angulo de distancia para cada passo da revolucao
        double ccos = Math.cos(teta); //Cosseno do angulo
        double csin = Math.sin(teta); //Seno do angulo

    }
}
