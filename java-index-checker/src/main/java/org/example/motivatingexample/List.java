package org.example.motivatingexample;

import java.util.function.Function;
import java.util.function.Predicate;

public sealed interface List<T> permits Cons, Nil {

    public static <T> List<T> arrayToList(T[] array) {
        List<T> ls = new Nil<>();
        for (var i = array.length-1; i >= 0; i--) {
            ls = new Cons<>(array[i], ls);
        }
        return ls;
    }

    public static <T, U> List<U> map(List<T> ls, Function<T, U> f) {
        List<U> rev = new Nil<>();
        while (ls instanceof Cons<T> lsc) {
            rev = new Cons<>(f.apply(lsc.head()), rev);
            ls = lsc.tail();
        }
        return reverse(rev);
    }

    public static <T> List<T> filter(List<T> ls, Predicate<T> pred) {
        List<T> rev = new Nil<>();
        while (ls instanceof Cons<T> lsc) {
            if (pred.test(lsc.head())) {
                rev = new Cons<>(lsc.head(), rev);
            }
            ls = lsc.tail();
        }
        return reverse(rev);
    }

    private static <T> List<T> reverse(List<T> ls) {
        List<T> result = new Nil<>();
        while (ls instanceof Cons<T> lsc) {
            result = new Cons<T>(lsc.head(), result);
        }
        return result;
    }

}
