/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Model.messaging;

import Model.messaging.events.CGEvent;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author JFPS
 */
public final class ClassWideEventDispatcher {
    //https://stackoverflow.com/questions/937302/simple-java-message-dispatching-system
    
    /** mapping of class events to active listeners **/
   private final HashMap<Class,ArrayList> map = new HashMap<>(10);

   /** Add a listener to an event class
     * @param <L>
     * @param evtClass
     * @param listener **/
    public <L> void listen( Class<? extends CGEvent<L>> evtClass, L listener) {
        final ArrayList<L> listeners = listenersOf( evtClass );
        synchronized( listeners ) {
            if ( !listeners.contains( listener ) ) {
                listeners.add( listener );
            }
        }
    }

    /** Stop sending an event class to a given listener
     * @param <L>
     * @param evtClass
     * @param listener **/
    public <L> void mute( Class<? extends CGEvent<L>> evtClass, L listener) {
        final ArrayList<L> listeners = listenersOf( evtClass );
        synchronized( listeners ) {
           listeners.remove( listener );
        }
   }

   /** Gets listeners for a given event class **/
   private <L> ArrayList<L> listenersOf(Class<? extends CGEvent<L>> evtClass) {
        synchronized ( map ) {
            @SuppressWarnings("unchecked")
            final ArrayList<L> existing = map.get( evtClass );
            if (existing != null) {
                return existing;
            }

            final ArrayList<L> emptyList = new ArrayList<>(5);
            map.put(evtClass, emptyList);
            return emptyList;
        }
   }


   /** Notify a new event to registered listeners of this event class
     * @param <L>
     * @param evt **/
   public <L> void notify( final CGEvent<L> evt) {
        @SuppressWarnings("unchecked")
        Class<CGEvent<L>> evtClass = (Class<CGEvent<L>>) evt.getClass();

        listenersOf(  evtClass ).forEach((listener) -> {
            evt.notify(listener);
        });
   }
}
