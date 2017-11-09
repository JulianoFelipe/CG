/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.Projections;

import Model.Aresta;
import Model.Nregular;
import Model.Poligono;
import Model.Vertice;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author JFPS
 */
public class OrtographicProjection implements Projection{
    //<editor-fold defaultstate="collapsed" desc="Delegate control">
    private final Projection internal;
    private final Orientation internalOrientation;
    
    public OrtographicProjection(Orientation orientation) {
        switch(orientation){
            case Frente:
                internal = new FrenteProjection();
                break;
                
            case Lateral:
                internal = new LateralProjection();
                break;
                
            case Topo:
                internal = new TopoProjection();
                break;
                
            default:
                throw new UnsupportedOperationException("Orientação não implementada."); //Mais para tirar erro de compilação do que utilizável
        }
        
        this.internalOrientation = orientation;
    }
    
    public Orientation getOrientation() {
        return internalOrientation;
    }
    
    @Override
    public Vertice project(Vertice v) {
        if (v != null)
            return internal.project(v);
        else
            return null;
    }
    
    @Override
    public Aresta project(Aresta a) {
        if (a != null)
            return internal.project(a);
        else
            return null;
    }
    
    @Override
    public Poligono project(Poligono p) {
        if (p != null)
            return internal.project(p);
        else
            return null;
    }
    
    @Override
    public Nregular project(Nregular n) {
        if (n != null)
            return internal.project(n);
        else
            return null;
    }

    @Override
    public Vertice transformBack(Vertice v) {
        if (v != null)
            return internal.transformBack(v);
        else
            return null;
    }

    @Override
    public Aresta transformBack(Aresta a) {
        if (a != null)
            return internal.transformBack(a);
        else
            return null;
    }

    @Override
    public Poligono transformBack(Poligono p) {
        if (p != null)
            return internal.transformBack(p);
        else
            return null;
    }

    @Override
    public Nregular transformBack(Nregular n) {
        if (n != null)
            return internal.transformBack(n);
        else
            return null;
    }
    
    public static enum Orientation{ Topo, Lateral, Frente; }
//</editor-fold>
    
    /* esquema de coords
    Frente  XY
    Lateral YZ
    Topo    XZ
    */
    
    private class TopoProjection implements Projection{
        @Override
        public Vertice project(Vertice v) {
            Vertice newA = new Vertice(v);
            float y = v.getY();
            newA.setY(v.getZ());
            newA.setZ(y);
            return newA;
        }

        @Override
        public Aresta project(Aresta a) {
            Aresta newA = new Aresta(a);
            float iniY = a.getvInicial().getY(), finY = a.getvFinal().getY();
            newA.getvInicial().setY(a.getvInicial().getZ());
            newA.getvFinal().setY(a.getvInicial().getZ());
            
            newA.getvInicial().setZ(iniY);
            newA.getvFinal().setZ(finY);
            return newA;
        }

        @Override
        public Poligono project(Poligono p) {
            List<Vertice> lista = new ArrayList<>(p.getVertices());
            
            lista.forEach((v) -> {
                float y = v.getY();
                v.setY(v.getZ());
                v.setZ(y);
            });
            Poligono newP = new Poligono(lista);
            
            return newP;
        }    

        @Override
        public Nregular project(Nregular n) {
            Nregular newN = new Nregular(n.getNoLados(), n.getRadius(), n.getCenter());
            
            List<Vertice> lista = newN.getVertices();
            lista.forEach((v) -> {
                float y = v.getY();
                v.setY(v.getZ());
                v.setZ(y);
            });
            
            return newN;
        }

        @Override
        public Vertice transformBack(Vertice v) {
            return project(v);
        }

        @Override
        public Aresta transformBack(Aresta a) {
            return project(a);
        }

        @Override
        public Poligono transformBack(Poligono p) {
            return project(p);
        }

        @Override
        public Nregular transformBack(Nregular n) {
            return project(n);
        }
    }
    
    private class LateralProjection implements Projection{
        @Override
        public Vertice project(Vertice v) {
            Vertice newA = new Vertice(v);
            float x = v.getX();
            newA.setX(v.getY());
            newA.setY(v.getZ());
            newA.setZ(x);
            return newA;
        }

        @Override
        public Aresta project(Aresta a) {
            Aresta newA = new Aresta(a);
            Vertice ini = a.getvInicial(), fim=a.getvFinal();
            float iniX= a.getvInicial().getX(), finX = a.getvFinal().getX();
            
            newA.getvInicial().setX(ini.getY());
            newA.getvInicial().setY(ini.getZ());
            newA.getvFinal().setX(fim.getY());
            newA.getvFinal().setY(fim.getZ());
            
            newA.getvInicial().setZ(iniX);
            newA.getvFinal().setZ(finX);
            return newA;
        }

        @Override
        public Poligono project(Poligono p) {
            List<Vertice> lista = new ArrayList<>(p.getVertices());
            
            lista.forEach((v) -> {
                float x = v.getX();
                v.setX(v.getY());
                v.setY(v.getZ());
                v.setZ(x);
            });
            Poligono newP = new Poligono(lista);
            
            return newP;
        }    

        @Override
        public Nregular project(Nregular n) {
            Nregular newN = new Nregular(n.getNoLados(), n.getRadius(), n.getCenter());
            
            List<Vertice> lista = newN.getVertices();
            lista.forEach((v) -> {
                float x = v.getX();
                v.setX(v.getY());
                v.setY(v.getZ());
                v.setZ(x);
            });
            
            return newN;
        }

        @Override
        public Vertice transformBack(Vertice v) {
            Vertice newA = new Vertice(v);
            float x = v.getZ();
            newA.setZ(v.getY());
            newA.setY(v.getX());
            newA.setX(x);
            return newA;
        }

        @Override
        public Aresta transformBack(Aresta a) {
            Aresta newA = new Aresta(a);
            Vertice ini = a.getvInicial(), fim=a.getvFinal();
            float iniX= a.getvInicial().getZ(), finX = a.getvFinal().getZ();
            
            newA.getvInicial().setZ(ini.getY());
            newA.getvFinal().setZ(ini.getY());
            
            newA.getvInicial().setY(ini.getX());
            newA.getvFinal().setY(fim.getX());
            
            newA.getvInicial().setX(iniX);
            newA.getvFinal().setX(finX);

            return newA;
        }

        @Override
        public Poligono transformBack(Poligono p) {
            List<Vertice> lista = new ArrayList<>(p.getVertices());
            
            lista.forEach((v) -> {
                float x = v.getZ();
                v.setZ(v.getY());
                v.setY(v.getX());
                v.setX(x); 
            });
            Poligono newP = new Poligono(lista);
            
            return newP;
        }    

        @Override
        public Nregular transformBack(Nregular n) {
            Nregular newN = new Nregular(n.getNoLados(), n.getRadius(), n.getCenter());
            
            List<Vertice> lista = newN.getVertices();
            lista.forEach((v) -> {
                float x = v.getZ();
                v.setZ(v.getY());
                v.setY(v.getX());
                v.setX(x);
            });
            
            return newN;
        }
    }
    
    private class FrenteProjection implements Projection{
        @Override
        public Vertice project(Vertice v) {
            Vertice newA = new Vertice(v);
            //newA.setZ(0);
            return newA;
        }

        @Override
        public Aresta project(Aresta a) {
            Aresta newA = new Aresta(a);
            //newA.getvInicial().setZ(0);
            //newA.getvFinal().setZ(0);
            return newA;
        }

        @Override
        public Poligono project(Poligono p) {
            /*List<Vertice> lista = new ArrayList<>(p.getVertices());
            
            lista.forEach((v) -> {
                v.setZ(0);
            });
            Poligono newP = new Poligono(lista);*/
            
            return new Poligono(p);
        }    

        @Override
        public Nregular project(Nregular n) {
            /*Nregular newN = new Nregular(n.getNoLados(), n.getRadius(), n.getCenter());
            
            List<Vertice> lista = newN.getVertices();
            lista.forEach((v) -> {
                v.setZ(0);
            });
            */
            return new Nregular(n);
        }

        @Override
        public Vertice transformBack(Vertice v) {
            return project(v);
        }

        @Override
        public Aresta transformBack(Aresta a) {
            return project(a);
        }

        @Override
        public Poligono transformBack(Poligono p) {
            return project(p);
        }

        @Override
        public Nregular transformBack(Nregular n) {
            return project(n);
        }
    }
}
