/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.listeners;

import Model.CGObject;
import View.ProjectionPlane;

/**
 *
 * @author JFPS
 * @param <T>
 */
public interface TransformListener<T extends CGObject> {
    /*
    Por que tem dois listeners com exatamente a mesma assinatura?
    Os dois são tratados de maneira diferente. O de adicionar adiciona
    um objeto sem procura nenhuma nos objetos existentes ( O(1) ).

    O de transformar tem que substituir o objeto anterior. Se não usar Hash,
    tem que pesquisar no array ( O(n) ).
    */
    public void objectTransformed(T object, ProjectionPlane planeOfOrigin);
}
