package org.example.ic9;

/*
 * Note: in comparison to the original version of this example found in the manual of the Checker Framework, 
 * this example has been slightly modified by adding @Nullable annotations (without which the Checker Framework 
 * rejects the code because of nullability issues).
 */

import org.checkerframework.checker.index.qual.SameLen;
import org.checkerframework.checker.index.qual.IndexFor;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.LengthOf;
import org.checkerframework.checker.nullness.qual.Nullable;


/** ArrayWrapper is a fixed-size generic collection. */
public class ArrayWrapper<T> {
    private final @Nullable Object @SameLen("this") [] delegate;

    @SuppressWarnings("index") // constructor creates object of size @SameLen(this) by definition
    ArrayWrapper(@NonNegative int size) {
        delegate = new Object[size];
    }

    public @LengthOf("this") int size() {
        return delegate.length;
    }

    public void set(@IndexFor("this") int index, @Nullable T obj) {
        delegate[index] = obj;
    }

    @SuppressWarnings("unchecked") // required for normal Java compilation due to unchecked cast
    public @Nullable T get(@IndexFor("this") int index) {
        return (T) delegate[index];
    }
}
