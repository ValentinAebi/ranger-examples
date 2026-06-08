package org.example.filterlessthan;

import org.checkerframework.checker.index.qual.LessThan;

public class FilterLessThan {

    static List<@LessThan("#1") Integer> filterLessThan_unsound(int a, List<Integer> ls) {
        List<Integer> revFiltered = new Nil<>();
        var rem = ls;
        while (rem instanceof Cons<Integer> cons) {
            var head = cons.head;
            if (head <= a) {    // ERROR: should be <, but the Index Checker seems to miss it
                revFiltered = new Cons<>(head, revFiltered);
            }
            rem = cons.tail;
        }
        return reverse(revFiltered);
    }

    static List<@LessThan("#1") Integer> filterLessThan_too_conservative(int a, List<Integer> ls) {
        List<@LessThan("#1") Integer> revFiltered = new Nil<>();
        var rem = ls;
        while (rem instanceof Cons<Integer> cons) {
            var head = cons.head;
            if (head < a) {     // here the bound is correct, but the Index Checker fails to check it
                revFiltered = new Cons<@LessThan("#1") Integer>(head, revFiltered);
            }
            rem = cons.tail;
        }
        return reverse(revFiltered);
    }

    interface List<T> {
    }

    record Cons<T>(T head, List<T> tail) implements List<T> {
    }

    record Nil<T>() implements List<T> {
    }

    static <T> List<T> reverse(List<T> ls) {
        List<T> reversed = new Nil<>();
        var rem = ls;
        while (rem instanceof Cons<T>(T head, List<T> tail)) {
            reversed = new Cons<>(head, reversed);
            rem = tail;
        }
        return reversed;
    }

}

