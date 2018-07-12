/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package m.poligonos.we_edge;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 *
 * @author JFPS
 */
public final class IndexList {
    private final int[] indexes;
    
    public IndexList(List<Integer> indexList) {
        this.indexes = indexList.stream().mapToInt(i->i).toArray();
    }
    
    public IndexList(int[] arr){
        this.indexes = new int[arr.length];
        
        System.arraycopy(arr, 0, indexes, 0, arr.length);
    }

    public int get(int i){
        return indexes[i];
    }

    public int size() {
        return indexes.length;
    }

    @Override
    public String toString() {
        return "IndexList{" + "indexes=" + Arrays.toString(indexes) + '}';
    }
    
    public static IndexList builder(int...indexes){
        int[] arr = new int[indexes.length];        
        System.arraycopy(indexes, 0, arr, 0, indexes.length);
        return new IndexList(arr);
    }
}
