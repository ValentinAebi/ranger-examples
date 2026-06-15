package ic9;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


/** ArrayWrapper is a fixed-size generic collection. */
public class ArrayWrapper<T> {
    private final Object @Refinement("_.length == this.size()") [] delegate;

    ArrayWrapper(@Refinement("_ >= 0") int size) {
        delegate = new Object[size];
    }

    public int size() {
        return delegate.length;
    }

    public void set(@Refinement("0 <= _ && _ < this.size()") int index, T obj) {
        delegate[index] = obj;
    }

    @SuppressWarnings("unchecked") // required for normal Java compilation due to unchecked cast
    public T get(@Refinement("0 <= _ && _ < this.size()") int index) {
        return (T) delegate[index];
    }
}
