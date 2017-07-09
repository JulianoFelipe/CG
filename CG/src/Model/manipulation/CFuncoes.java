/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.manipulation;

import Model.Aresta;
import Model.Face;
import Model.LinhaPreenchimento;
import Model.Poligono;
import Model.Vertice;
import java.util.ArrayList;
import java.util.Iterator;
import utils.MVMath;
import utils.VMath;

/**
 *
 * @author Anderson
 */
public class CFuncoes {
    private static Model.visualizacao.Camera cam = Model.visualizacao.Camera.getCamera();
    
    /**
     * Calcula o vetor entre dois pontos
     *
     * @param vf
     * @param vi
     * @return
     */
    public static Vertice calculaVetor(Vertice vf, Vertice vi) {
        float x = vf.getX() - vi.getX();
        float y = vf.getY() - vi.getY();
        float z = vf.getZ() - vi.getZ();

        return new Vertice(x, y, z);
    }

    public static Vertice transformaCoordenadasHomogeneas(Vertice v) {
        Vertice vertice = new Vertice();

        vertice.setX(v.getX() / v.getW());
        vertice.setY(v.getY() / v.getW());
        vertice.setZ(v.getZ() / v.getW());
        vertice.setW(v.getW() / v.getW());

        return vertice;
    }

    /**
     * Retora o Poligono Selecionado.
     *
     * @param v ponto do usuario
     * @return Retora o Poligono Selecionado
     */
    public static Poligono getPoligonoSelecionadoVistaFrontal(Vertice v) {
        float distanciaMim = Float.MAX_VALUE;
        Poligono poligonoSelecionado = null;

        for (Poligono poligono : Controle.getCena().getListaPoligonos()){
            for (Aresta aresta : poligono.getArestas()){
                Vertice vi = MVMath.multiplicarR(
                        aresta.getvInicial(),
                        cam.getProjecao().getmOrtograficaVistaFrontal()
                );
                Vertice vf = MVMath.multiplicarR(
                        aresta.getvFinal(),
                        cam.getProjecao().getmOrtograficaVistaFrontal()
                );

                //Aresta reta = Controle.getCena().getPoligonos().get(i).getArestas().get(j);
                Aresta reta = new Aresta(vi, vf);
                // calcula a distancia do ponto até a Reta 
                float d = calcularDistanciaPontoReta(v, reta);
                if (d < distanciaMim) {
                    // atualiza a distancia minima
                    distanciaMim = d;
                    // armazena qual o poligono possui a menor distancia
                    poligonoSelecionado = poligono;
                }
            }
        }
        return poligonoSelecionado;
    }

    public static Poligono getPoligonoSelecionadoVistaLateral(Vertice v) {
        float distanciaMim = Float.MAX_VALUE;
        Poligono poligonoSelecionado = null;

        for (Poligono poligono : Controle.getCena().getListaPoligonos()){
            for (Aresta aresta : poligono.getArestas()){
                Vertice vi = MVMath.multiplicarR(
                        aresta.getvInicial(),
                        cam.getProjecao().getmOrtograficaVistaLateral()
                );
                Vertice vf = MVMath.multiplicarR(
                        aresta.getvFinal(),
                        cam.getProjecao().getmOrtograficaVistaLateral()
                );

                //Aresta reta = Controle.getCena().getPoligonos().get(i).getArestas().get(j);
                Aresta reta = new Aresta(vi, vf);
                // calcula a distancia do ponto até a Reta 
                float d = calcularDistanciaPontoReta(v, reta);
                if (d < distanciaMim) {
                    // atualiza a distancia minima
                    distanciaMim = d;
                    // armazena qual o poligono possui a menor distancia
                    poligonoSelecionado = poligono;
                }
            }
        }
        return poligonoSelecionado;
    }

    public static Poligono getPoligonoSelecionadoVistaTopo(Vertice v) {
        float distanciaMim = Float.MAX_VALUE;
        Poligono poligonoSelecionado = null;

        for (Poligono poligono : Controle.getCena().getListaPoligonos()){
            for (Aresta aresta : poligono.getArestas()){
                Vertice vi = MVMath.multiplicarR(
                        aresta.getvInicial(),
                        cam.getProjecao().getmOrtograficaVistaTopo()
                );
                Vertice vf = MVMath.multiplicarR(
                        aresta.getvFinal(),
                        cam.getProjecao().getmOrtograficaVistaTopo()
                );

                //Aresta reta = Controle.getCena().getPoligonos().get(i).getArestas().get(j);
                Aresta reta = new Aresta(vi, vf);
                // calcula a distancia do ponto até a Reta 
                float d = calcularDistanciaPontoReta(v, reta);
                if (d < distanciaMim) {
                    // atualiza a distancia minima
                    distanciaMim = d;
                    // armazena qual o poligono possui a menor distancia
                    poligonoSelecionado = poligono;
                }
            }
        }
        return poligonoSelecionado;
    }

    /**
     * Calcula a distância entre um ponto a uma Reta em um mesmo Plano.
     * Considerando o fato da Reta ser um Seguimento Infinito, onde para cada
     * distancia calculada obtém o seu respectivo ponto de interseção e
     * verifica-se se o mesmo é interno ao segmento de reta. Caso seja retonar o
     * valor da distância calculada. Caso contrario retorna o maior valor
     * possível em float
     *
     * @param v ponto
     * @param reta reta
     * @return Retorna a distância entre um ponto a uma Reta.
     */
    public static float calcularDistanciaPontoReta(Vertice v, Aresta reta) {
        // fazendo a equação da reta do poligono
        double a = reta.getvFinal().getY() - reta.getvInicial().getY();//r
        double b = (reta.getvFinal().getX() - reta.getvInicial().getX()) * -1;//s
        double c = (reta.getvFinal().getX() * reta.getvInicial().getY()) - (reta.getvInicial().getX() * reta.getvFinal().getY());// t 

        // fazendo a equação da reta perpendicular atraves da formula: − sx + ry + (sx0 − ry0)=0
        double r2 = b * -1;
        double s2 = a;
        double t2 = (((r2 * -1) * v.getX()) - (s2 * v.getY()));

        //Calculando a distância do ponto a reta atraves da formula: d = |ax + by + c|/ sqrt(a*a + b*b)
        double aux = Math.abs((a * v.getX()) + (b * v.getY()) + c); //d = |ax + by + c|
        aux = (aux / (Math.sqrt((a * a) + (b * b))));  // sqrt (a*a + b*b)
        float d = (float) aux;

        //verifica se está dentro da distacia limite 
        if (d < 8) {
            //encontra o ponto de interseção
            Vertice pontoIntersecao = new Vertice();
            pontoIntersecao.setX((float) (((b * t2) - (s2 * c)) / ((s2 * a) - (b * r2))));
            pontoIntersecao.setY((float) (((c * r2) - (t2 * a)) / ((s2 * a) - (b * r2))));

            // verifica se o ponto é esta dentro do segmento de reta
            if (pontoInternoSeguimentoReta(reta, pontoIntersecao)) {
                return d;
            } else {
                // Caso Contrário verifica se o Ponto esta dentro da distancia limite 
                //entre algum dos extremos da reta.
                // calcula a distancia do ponto ao Vertice inicial da aresta
                d = distanciaPontoPonto(v, reta.getvInicial());
                if (d < 8) {
                    return d;
                }
                // calcula a distancia do ponto ao Vertice inicial da aresta
                d = distanciaPontoPonto(v, reta.getvFinal());
                if (d < 8) {
                    return d;
                }
            }
        }
        return Float.MAX_VALUE;
    }

    public static boolean pontoDentroLimitesX_daReta(Aresta r, Vertice v) {
        float pX = v.getX();
        float rxI = r.getvInicial().getX();
        float rxF = r.getvFinal().getX();
        if (pX + 1 >= rxI && pX - 1 <= rxF) {
            return true;
        }
        if (pX + 1 >= rxF && pX - 1 <= rxI) {
            return true;
        }

        return false;
    }

    public static boolean pontoDentroLimitesY_daReta(Aresta r, Vertice v) {
        float pY = v.getY();
        float ryI = r.getvInicial().getY();
        float ryF = r.getvFinal().getY();
        if (pY + 1 >= ryI && pY - 1 <= ryF) {
            return true;
        }
        if (pY + 1 >= ryF && pY - 1 <= ryI) {
            return true;
        }

        return false;
    }

    /**
     * Verifica se um determinado Ponto é Interno (está dentro dos limites) a um
     * Seguimento de Reta
     *
     * @param r Seguimento de Reta
     * @param v Ponto a ser examinado.
     * @return Verdadeiro se o Ponto v é interno. Caso contrário retorna falso.
     */
    public static boolean pontoInternoSeguimentoReta(Aresta r, Vertice v) {
        return pontoDentroLimitesX_daReta(r, v) && pontoDentroLimitesY_daReta(r, v);
    }

    public static boolean pontoInternoSeguimentoRetaZX(Aresta r, Vertice v) {
        if (v.getX() >= r.getvInicial().getX() && v.getX() <= r.getvFinal().getX()) {
            if (v.getZ() >= r.getvInicial().getZ() && v.getZ() <= r.getvFinal().getZ()) {
                return true;
            }
            if (v.getZ() >= r.getvFinal().getZ() && v.getZ() <= r.getvInicial().getZ()) {
                return true;
            }
        }
        if (v.getX() >= r.getvFinal().getX() && v.getX() <= r.getvInicial().getX()) {
            if (v.getZ() >= r.getvInicial().getZ() && v.getZ() <= r.getvFinal().getZ()) {
                return true;
            }
            if (v.getZ() >= r.getvFinal().getZ() && v.getZ() <= r.getvInicial().getZ()) {
                return true;
            }
        }
        return false;
    }

    public static boolean pontoInternoSeguimentoRetaZY(Aresta r, Vertice v) {
        if (v.getZ() >= r.getvInicial().getZ() && v.getZ() <= r.getvFinal().getZ()) {
            if (v.getY() >= r.getvInicial().getY() && v.getY() <= r.getvFinal().getY()) {
                return true;
            }
            if (v.getY() >= r.getvFinal().getY() && v.getY() <= r.getvInicial().getY()) {
                return true;
            }
        }
        if (v.getZ() >= r.getvFinal().getZ() && v.getZ() <= r.getvInicial().getZ()) {
            if (v.getY() >= r.getvInicial().getY() && v.getY() <= r.getvFinal().getY()) {
                return true;
            }
            if (v.getY() >= r.getvFinal().getY() && v.getY() <= r.getvInicial().getY()) {
                return true;
            }
        }
        return false;
    }

    /**
     * Retorna a distância entre dois Pontos em um mesmo Plano
     *
     * @param v1 Ponto Inicial
     * @param v2 Ponto Final
     * @return Retorna a distância entre dois Pontos
     */
    public static float distanciaPontoPonto(Vertice v1, Vertice v2) {
        // formula da distacia Raiz((x2-x1)² + (y2-y1)²)
        double dx = (float) Math.pow((v1.getX() - v2.getX()), 2); //(x2-x1)²
        double dy = (float) Math.pow((v1.getY() - v2.getY()), 2); //(y2-y1)²

        return (float) Math.sqrt(dx + dy); //Raiz((x2-x1)² + (y2-y1)²)
    }

    public static void translacao(Poligono p, double[][] matrizTransformacao){
        aplicaMatrizTransformacao(p, matrizTransformacao);
    }
    
    public static void escala(Poligono p, double[][] matrizTransformacao){
        aplicaMatrizTransformacao(p, matrizTransformacao);
    }
    
    public static void rotaciona(Poligono p, double[][] matrizTransformacao){
        aplicaMatrizTransformacao(p, matrizTransformacao);
    }
    
    public static void aplicaMatrizTransformacao(Poligono p, double[][] matrizTransformacao){
        p.getVertices().stream().forEach((vertice) -> {
            MVMath.multiplicar(vertice, matrizTransformacao);
        });
    }
    
    public static int getCoordenadaYmax() {
        int yMax = Integer.MIN_VALUE;

        for (Poligono poligono : Controle.getCena().getListaPoligonos()){
            for (Face face : poligono.getFaces()){
                for (Aresta aresta : face.getListaArestas()){
                    if (aresta.getvInicial().getY() > yMax) 
                        aresta.getvInicial().getY();
                    
                    if (aresta.getvFinal().getY() > yMax) 
                        yMax = (int) aresta.getvFinal().getY();
                }
            }
        }

        return yMax;
    }

    public static ArrayList<Vertice> ordenarListaPontos(ArrayList<Vertice> listaPontosIntersecao) {
        ArrayList<Vertice> listaPontosOrdenados = new ArrayList<>();

        while (!listaPontosIntersecao.isEmpty()) {
            Vertice vMin = new Vertice(Float.MAX_VALUE, Float.MAX_VALUE, 0);
            int index = 0;
            int x = listaPontosIntersecao.size();
            for (int i = 0; i < listaPontosIntersecao.size(); i++) {
                if (listaPontosIntersecao.get(i).getY() <= vMin.getY() && listaPontosIntersecao.get(i).getX() < vMin.getX()) {
                    vMin = listaPontosIntersecao.get(i);
                    index = i;
                }
            }
            listaPontosOrdenados.add(listaPontosIntersecao.remove(index));
        }

        return listaPontosOrdenados;
    }

    public static ArrayList<Vertice> ordenarListaPontosX(ArrayList<Vertice> listaPontosIntersecao) {
        ArrayList<Vertice> listaPontosOrdenados = new ArrayList<>();

        while (!listaPontosIntersecao.isEmpty()) {
            Vertice vMin = new Vertice(Float.MAX_VALUE, 0, Float.MAX_VALUE);
            int index = 0;
            int x = listaPontosIntersecao.size();
            for (int i = 0; i < listaPontosIntersecao.size(); i++) {
                //if (listaPontosIntersecao.get(i).getY() <= vMin.getY() && listaPontosIntersecao.get(i).getX() < vMin.getX()) {
                if (listaPontosIntersecao.get(i).getX() < vMin.getX()) {
                    vMin = listaPontosIntersecao.get(i);
                    index = i;
                }
            }
            listaPontosOrdenados.add(listaPontosIntersecao.remove(index));
        }

        return listaPontosOrdenados;
    }

    public static ArrayList<Vertice> ordenarListaPontosZ(ArrayList<Vertice> listaPontosIntersecao) {
        ArrayList<Vertice> listaPontosOrdenados = new ArrayList<>();

        while (!listaPontosIntersecao.isEmpty()) {
            Vertice vMin = new Vertice(Float.MAX_VALUE, 0, Float.MAX_VALUE);
            int index = 0;
            int x = listaPontosIntersecao.size();
            for (int i = 0; i < listaPontosIntersecao.size(); i++) {
                //if (listaPontosIntersecao.get(i).getY() <= vMin.getY() && listaPontosIntersecao.get(i).getX() < vMin.getX()) {
                if (listaPontosIntersecao.get(i).getZ() < vMin.getZ()) {
                    vMin = listaPontosIntersecao.get(i);
                    index = i;
                }
            }
            listaPontosOrdenados.add(listaPontosIntersecao.remove(index));
        }

        return listaPontosOrdenados;
    }

    public static ArrayList<Aresta> getListaArestaCoordenadaOrtograficaVistaFrontal(ArrayList<Aresta> listaArestas) {
        ArrayList<Aresta> arestas = new ArrayList<>();
        Vertice vi;
        Vertice vf;
        for (Aresta listaAresta : listaArestas) {
            vi = MVMath.multiplicarR(listaAresta.getvInicial(), cam.getProjecao().getmOrtograficaVistaFrontal());
            vf = MVMath.multiplicarR(listaAresta.getvFinal(), cam.getProjecao().getmOrtograficaVistaFrontal());
            Aresta a = new Aresta(vi, vf);
            arestas.add(a);
        }

        return arestas;
    }

    public static ArrayList<Aresta> getListaArestaCoordenadaOrtograficaVistaLateral(ArrayList<Aresta> listaArestas) {
        ArrayList<Aresta> arestas = new ArrayList<>();
        Vertice vi;
        Vertice vf;
        for (Aresta listaAresta : listaArestas) {
            vi = MVMath.multiplicarR(listaAresta.getvInicial(), cam.getProjecao().getmOrtograficaVistaLateral());
            vf = MVMath.multiplicarR(listaAresta.getvFinal(), cam.getProjecao().getmOrtograficaVistaLateral());
            Aresta a = new Aresta(vi, vf);
            arestas.add(a);
        }

        return arestas;
    }

    public static ArrayList<Aresta> getListaArestaCoordenadaOrtograficaVistaTopo(ArrayList<Aresta> listaArestas) {
        ArrayList<Aresta> arestas = new ArrayList<>();
        Vertice vi;
        Vertice vf;
        for (Aresta listaAresta : listaArestas) {
            vi = MVMath.multiplicarR(listaAresta.getvInicial(), cam.getProjecao().getmOrtograficaVistaTopo());
            vf = MVMath.multiplicarR(listaAresta.getvFinal(), cam.getProjecao().getmOrtograficaVistaTopo());
            Aresta a = new Aresta(vi, vf);
            arestas.add(a);
        }

        return arestas;
    }

    public static float getCoordenadaYmax(ArrayList<Aresta> listaArestas) {
        float yMax = Float.MIN_VALUE;

        for (Aresta listaAresta : listaArestas) {
            if (listaAresta.getVerticeMaxY().getY() > yMax) {
                yMax = listaAresta.getVerticeMaxY().getY();
            }
        }

        return yMax;
    }

    public static float getCoordenadaYmin(ArrayList<Aresta> listaArestas) {
        float yMin = Float.MAX_VALUE;

        for (Aresta listaAresta : listaArestas) {
            if (listaAresta.getVerticeMinY().getY() < yMin) {
                yMin = listaAresta.getVerticeMinY().getY();
            }
        }

        return yMin;
    }
}
