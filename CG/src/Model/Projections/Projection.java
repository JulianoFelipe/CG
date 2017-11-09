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

/**
 *
 * @author JFPS
 */
public interface Projection {
    public Vertice  project(Vertice v);
    public Aresta   project(Aresta a);
    public Poligono project(Poligono p);
    public Nregular project(Nregular n);
    //public Vertice transfromFromProjection(Vertice v);
    
    public Vertice  transformBack(Vertice v);
    public Aresta   transformBack(Aresta a);
    public Poligono transformBack(Poligono p);
    public Nregular transformBack(Nregular n); 
}
