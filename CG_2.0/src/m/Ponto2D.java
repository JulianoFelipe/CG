/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m;

import java.io.Serializable;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.value.ChangeListener;

/**
 *
 * @author JFPS
 */
public class Ponto2D implements Serializable{
    private final IntegerProperty x = new SimpleIntegerProperty();
    private final IntegerProperty y = new SimpleIntegerProperty();

    public final IntegerProperty xProperty(){ return x;}
    public final IntegerProperty yProperty(){ return y;}
    
    public final int getX(){ return x.get();}
    public final int getY(){ return y.get();}
    
    public final void setX(int newX){ x.set(newX);}
    public final void setY(int newY){ y.set(newY);}
    
    public Ponto2D(int x, int y) {
        setX(x);
        setY(y);
    }

    public Ponto2D() {
        setX(0);
        setY(0);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 19 * hash + getX();
        hash = 19 * hash + getY();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Ponto2D other = (Ponto2D) obj;
        if (this.x != other.x) {
            return false;
        }
        if (this.y != other.y) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "Ponto2D{" + "x=" + getX() + ", y=" + getY() + '}';
    }
    
    public void addListener(ChangeListener<? super Number> listener){
        xProperty().addListener(listener);
        yProperty().addListener(listener);
    }
}
