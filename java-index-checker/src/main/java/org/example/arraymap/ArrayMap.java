package org.example.arraymap;

import org.checkerframework.checker.index.qual.IndexOrHigh;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.SameLen;
import org.checkerframework.dataflow.qual.Pure;

public class ArrayMap<K, V> {
    private final K[] keys;
    private final V @SameLen("this.keys")[] values;
    private @IndexOrHigh("this.keys") int currSize;

    public ArrayMap(K[] keys, V @SameLen("#1")[] values) {
        this.keys = keys;
        this.values = values;
        this.currSize = 0;
    }

    public @Pure @NonNegative int capacity(){
        return this.keys.length;
    }

    public @IndexOrHigh("this.keys") int currentSize() {
        return this.currSize;
    }

    public V get(K k) {
        var idx = ArrayUtils.indexOf(keys, k);
        if (idx == -1) {
            return null;
        } else {
            return this.values[idx];
        }
    }

    public boolean put(K k, V v) {
        if (k == null || v == null) {
            throw new IllegalArgumentException();
        }
        var preSize = currentSize();
        if (preSize < this.keys.length) {
            // ERROR: should be < instead of <=
            for (int i = 0; i <= this.keys.length; i++) {
                if (this.keys[i] == null) {
                    this.keys[i] = k;
                    this.values[i] = v;
                    break;
                }
            }
            this.currSize = preSize + 1;
            return true;
        }
        return false;
    }

    public boolean remove(K k) {
        var preSize = currentSize();
        if (preSize > 0) {
            var idx = ArrayUtils.indexOf(this.keys, k);
            if (idx == -1) {
                return false;
            }
            this.keys[idx] = null;
            this.values[idx] = null;
            this.currSize = preSize - 1;
            return true;
        }
        return false;
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ArrayMap<K, V> newEmptyOfCapacity(@NonNegative int capacity) {
        var keys = (K[]) new Object[capacity];
        var values = (V[]) new Object[keys.length];  // new Object[capacity] fails the VC on the next line
        return new ArrayMap<>(keys, values);
    }

}
