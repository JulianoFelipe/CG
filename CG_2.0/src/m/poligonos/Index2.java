/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos;

/**
 *
 * @author JFPS
 */
public final class Index2 {
    private final int ind1, ind2;
    
    public Index2(int index1, int index2){
        this.ind1 = index1;
        this.ind2 = index2;
    }

    public int getInd1() {
        return ind1;
    }

    public int getInd2() {
        return ind2;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 23 * hash + this.min();
        hash = 23 * hash + this.max();
        return hash;
    }

    public boolean equalsRev(Object obj) {
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Index2 other = (Index2) obj;
        if (ind1 != other.ind1) {
            return false;
        }
        if (ind2 != other.ind2) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "" + ind1 + ind2;
    }
    
    public int min(){
        return Math.min(ind1, ind2);
    }
    
    public int max(){
        return Math.max(ind1, ind2);
    }
    
    @Override
    public boolean equals(Object obj) {
        int ind1 = Math.min(this.ind1, this.ind2);
        int ind2 = Math.max(this.ind1, this.ind2); 
        
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Index2 other = (Index2) obj;
        if (ind1 != other.min()) {
            return false;
        }
        if (ind2 != other.max()) {
            return false;
        }
        return true;
    }
    
    public static Index2 of(String key){
        int i1 = Integer.parseInt(key.charAt(0) + "");
        int i2 = Integer.parseInt(key.charAt(1) + "");
        
        return new Index2(i1, i2);
    }
}
