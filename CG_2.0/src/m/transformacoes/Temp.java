/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.transformacoes;

/**
 *
 * @author Anderson
 */
public class Temp {
    public void CriaObjetoY(int Num, double Ang) {
    double teta = (Ang * (Math.PI / 180)) / Num; //Calcula o angulo de distancia para cada passo da revolucao
    double ccos = Math.cos(teta); //Cosseno do angulo
    double csin = Math.sin(teta); //Seno do angulo
    /*System.out.println("Teta2 = " + teta);
    System.out.println("ccos = " + ccos);
    System.out.println("csin = " + csin);*/
    double xold = arrPonto.get(0).y, zold = arrPonto.get(0).y;
    
    //**********************************************
    Ponto plinha = new Ponto();
    
    
    int[] BPosi = new int[arrPonto.size()];
    obj = new Objeto();
    int y = 0;
    for (Ponto p : arrPonto) { //Interacao inicial com os pontos do perfil
      BPosi[y] = y;
      y++;
      obj.arrPonto.add(new Ponto(p)); //Copia todos os pontos iniciais para o objeto
      if (p.y < xold) {
        xold = p.y; //xold carrega y minimo
      }
      if (p.y > zold) {
        zold = p.y; //zold carrega y maximo
      }
    }
    //Constroi as arestas iniciais (primeiro perfil)
    obj.ConstroiArestasMaisFaces(fechado);
    int[] FPosi = new int[obj.arrFace.size()]; //Fposi carrega os indices das faces que estao sendo criadas
    int UPP = -1, PPP = -1;
    for (int i = 0; i < FPosi.length; i++){
      FPosi[i] = i;
    }
    if (arrPonto.get(0).x != 0){
      PPP = 0; //Se o primeiro ponto e do eixo
    }
    if (arrPonto.get(arrPonto.size()-1).x != 0){
      UPP = 0; //Se o ultimo ponto e do eixo
    }
    //FAZER Definir se objeto e fechado ou nao (dupla face ou unica face)
    obj.Fechado = false; //Assim esta sempre aberto
    
    ///////////////////////////////////////////////////////////////
    if (Ang == 360.0){ //Rotacao completa
      for (int i = 0; i < Num-1; i++){ //Todas as revolucoes menos uma (ja que junta no fim)
        int ij = 0; //Indice para vetor de indices de face sendo construida
        for (y = 0; y < arrPonto.size(); y++){
          plinha = arrPonto.get(y); //plinha recebe cada um dos pontos do perfil
          if (plinha.x != 0 || plinha.z != 0) { //Ponto fora do eixo
            xold = plinha.x;
            plinha.x = (plinha.x * ccos) + (plinha.z * csin); //Rotaciona
            plinha.z = (xold * (-csin)) + (plinha.z * ccos);
            obj.arrPonto.add(new Ponto(plinha)); //copia plinha para o objeto
            obj.arrAresta.add(new Aresta(BPosi[y], obj.arrPonto.size()-1)); //Aresta entre o ponto rotacionado e o seu irmão anterior
            BPosi[y] = obj.arrPonto.size()-1; //Agora o irmao anterior passa a ser ele
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1); //Adiciona aresta na face
            obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij]; //Adiciona a aresta esta face como a esquerda
            if ((FPosi.length - 1) > (ij + 1)){ //Se nao for a ultima face liga com a proxima tambem
              obj.arrFace.get(FPosi[ij+1]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrAresta.get(obj.arrAresta.size()-1).d = FPosi[ij+1];
            }
            if (y == 0){
              PPP = obj.arrAresta.size()-1; //Pode estar errado!
            } else if (y > 0){ //Qualquer outro ponto que nao o primeiro (liga com o irmao de cima)
              if (arrPonto.get(y - 1).x != 0 || arrPonto.get(y - 1).z != 0){ //Ponto anterior tambem fora do eixo
                obj.arrAresta.add(new Aresta(obj.arrPonto.size() - 2, obj.arrPonto.size() - 1));
              } else { //Ponto anterior no eixo
                obj.arrAresta.add(new Aresta(y - 1, obj.arrPonto.size() - 1));
              }
              obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrFace.add(new Face(obj.arrAresta.size()-1));
              obj.arrAresta.get(obj.arrAresta.size()-1).d = obj.arrFace.size()-1;
              obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
              FPosi[ij] = obj.arrFace.size()-1;
              ij++;
            }
          } else { //Ponto no eixo
            if (y > 0){ //Se nao for o primeiro ponto (ja que so liga com o irmao de cima)
              if (arrPonto.get(y - 1).x != 0 || arrPonto.get(y - 1).z != 0){ //Ponto anterior tambem fora do eixo
                obj.arrAresta.add(new Aresta(y, obj.arrPonto.size() - 1));
              } else {
                ErroPadrao();
              }
              obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrFace.add(new Face(obj.arrAresta.size()-1));
              obj.arrAresta.get(obj.arrAresta.size()-1).d = obj.arrFace.size()-1;
              obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
              FPosi[ij] = obj.arrFace.size()-1;
              ij++; //Para a proxima face
            }
          }
        }
        if (fechado){ //Se o objeto for fechado (Vertice final == inicial)
          obj.arrAresta.add(new Aresta(BPosi[BPosi.length-1], BPosi[0]));
          obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
          if (PPP != -1){
            obj.arrFace.get(FPosi[ij]).fAresta.add(PPP);
            obj.arrAresta.get(PPP).e = FPosi[ij];
          }
          if (UPP == 0){ //Tenho minhas duvidas sobre esse trecho
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-3);
            obj.arrAresta.get(obj.arrAresta.size()-3).e = FPosi[ij];
          }
          
        }
      }
      //Apos a "ultima" rotacao
      int ij = 0;
      for (y = 0; y < arrPonto.size(); y++){ //Praticamente tudo igual, mas liga os pontos nos iniciais ao inves de rotacionar mais uma vez
        if (arrPonto.get(y).x != 0 || arrPonto.get(y).z != 0) { //Se nao for do eixo
          obj.arrAresta.add(new Aresta(BPosi[y], y)); //Ultimos vertices com os primeiros
          if (y > 0){
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
            obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
            if ((FPosi.length - 1) > (ij + 1)){
              obj.arrFace.get(FPosi[ij+1]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrAresta.get(obj.arrAresta.size()-1).d = FPosi[ij+1];
            }
            obj.arrFace.get(FPosi[ij]).fAresta.add(ij);
            obj.arrAresta.get(ij).e = FPosi[ij];
            ij++;
          } else { //y = 0
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
            obj.arrAresta.get(obj.arrAresta.size()-1).d = FPosi[ij];
          }
        } else { //Ponto no eixo
          if (y > 0){
            obj.arrFace.get(FPosi[ij]).fAresta.add(ij);
            obj.arrAresta.get(ij).e = FPosi[ij];
            ij++;
          }
        }
      }
    } else { //Rotacao nao completa
      for (int i = 0; i < Num; i++){ //Todas as revolucoes (vai ate o angulo)
        int ij = 0;
        for (y = 0; y < arrPonto.size(); y++){
          plinha = arrPonto.get(y); //plinha recebe cada um dos pontos do perfil
          if (plinha.x != 0 || plinha.z != 0) { //Ponto fora do eixo
            xold = plinha.x;
            plinha.x = (plinha.x * ccos) + (plinha.z * csin); //Rotaciona
            plinha.z = (xold * (-csin)) + (plinha.z * ccos);
            obj.arrPonto.add(new Ponto(plinha)); //copia plinha
            obj.arrAresta.add(new Aresta(BPosi[y], obj.arrPonto.size()-1));
            BPosi[y] = obj.arrPonto.size()-1;
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
            obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
            if ((FPosi.length - 1) > (ij + 1)){
              obj.arrFace.get(FPosi[ij+1]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrAresta.get(obj.arrAresta.size()-1).d = FPosi[ij+1];
            }
            if (y == 0){
              PPP = obj.arrAresta.size()-1;
            } else if (y > 0){
              if (arrPonto.get(y - 1).x != 0 || arrPonto.get(y - 1).z != 0){ //Ponto anterior tambem fora do eixo
                obj.arrAresta.add(new Aresta(obj.arrPonto.size() - 2, obj.arrPonto.size() - 1));
              } else {
                obj.arrAresta.add(new Aresta(y - 1, obj.arrPonto.size() - 1));
              }
              obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrFace.add(new Face(obj.arrAresta.size()-1));
              obj.arrAresta.get(obj.arrAresta.size()-1).d = obj.arrFace.size()-1;
              obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
              FPosi[ij] = obj.arrFace.size()-1;
              ij++;
            }
          } else { //Ponto no eixo
            if (y > 0){
              if (arrPonto.get(y - 1).x != 0 || arrPonto.get(y - 1).z != 0){ //Ponto anterior tambem fora do eixo
                obj.arrAresta.add(new Aresta(y, obj.arrPonto.size() - 1));
              } else {
                ErroPadrao();
              }
              obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
              obj.arrFace.add(new Face(obj.arrAresta.size()-1));
              obj.arrAresta.get(obj.arrAresta.size()-1).d = obj.arrFace.size()-1;
              obj.arrAresta.get(obj.arrAresta.size()-1).e = FPosi[ij];
              FPosi[ij] = obj.arrFace.size()-1;
              ij++;
            }
          }
        }
        if (fechado){
          obj.arrAresta.add(new Aresta(BPosi[BPosi.length-1], BPosi[0]));
          obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-1);
          if (PPP != -1){
            obj.arrFace.get(FPosi[ij]).fAresta.add(PPP);
            obj.arrAresta.get(PPP).e = FPosi[ij];
          }
          if (UPP == 0){ //Duvidas sobre esse trecho
            obj.arrFace.get(FPosi[ij]).fAresta.add(obj.arrAresta.size()-3);
            obj.arrAresta.get(obj.arrAresta.size()-3).e = FPosi[ij];
          }
        }
      }
    }
    obj.CalculaCentro(); //Calcula centrodo objeto para operacoes posteriores
  }
}
