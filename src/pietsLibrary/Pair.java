/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package pietsLibrary;

/**
 *
 * @author Piet
 */
public class Pair<K, V> {
    final public K k;
    final V v;
    
    public Pair(K k, V v) {
        this.k = k;
        this.v = v;
    }
   
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (!(o instanceof Pair)) return false;
        Pair p = (Pair) o;
        return k.equals(p.k) && v.equals(p.v);
    }
    
    @Override
    public String toString() {
        return k + ", " + v;
    }
}

