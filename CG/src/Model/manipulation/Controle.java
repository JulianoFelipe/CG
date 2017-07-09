package Model.manipulation;

import Model.Classificacao.Prismas;
import Model.Classificacao.SolidosExtras;
import Model.visualizacao.Camera;
import Model.visualizacao.Cena;
import utils.MVMath;

/**
 *
 * @author Maycon
 */
public class Controle {
    private static Cena cena = new Cena();

    public static Cena getCena() {
        return cena;
    }

    public static void setCena(Cena cena) {
        Controle.cena = cena;
    }
    
    public static Camera getCamera(){
        return Camera.getCamera();
    }
    
    public static void paintPolygons(){
        cena.getListaPoligonos().stream().forEach((poligono) -> {
            CPintura.PintarPoligono(poligono);
        });
    }
    
    public static void paintPolygonBorders(){
        cena.getListaPoligonos().stream().forEach((poligono) -> {
            CPintura.PintarBordasPoligono(poligono);
        });
    }
    
    public static void addSolido(Prismas solido){
        cena.add( CriadorSolidos.criarPrisma(solido) );
    }
    
    public static void addSolido(SolidosExtras solido){
        cena.add( CriadorSolidos.criarSolido(solido) );
    }
    
    public static void transfCoordCamera(){
        getCamera().init();
        
        cena.getListaPoligonos().stream().forEach((poligono) -> {
            poligono.getVertices().stream().forEach((vertice) -> {
                MVMath.multiplicar(
                        vertice, 
                        getCamera().getProjecao().getmCamera()
                );
            });
        }); 
        
        /*
        for (Poligono poligono : cena.getListaPoligonos()){
            for (Vertice vertice : poligono.getVertices()){
                MVMath.multiplicar(
                    vertice,
                    getCamera().getProjecao().getmCamera()
                );
            }
        }*/
    }
}
