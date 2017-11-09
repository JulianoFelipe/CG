/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging.events;

/**
 *
 * @author JFPS
 * @param <T>
 */
public interface CGEvent<T> {
    public void notify(final T listener);
}
