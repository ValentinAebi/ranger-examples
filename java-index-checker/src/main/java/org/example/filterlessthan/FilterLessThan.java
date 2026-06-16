package org.example.filterlessthan;

import java.util.stream.Stream;

import org.checkerframework.checker.index.qual.LessThan;

public class FilterLessThan {

    static List<@LessThan("#1") Integer> filterLessThan_adHoc_correct(int a, List<Integer> ls) {
        List<@LessThan("a") Integer> revFiltered = new Nil<>();
        var rem = ls;
        while (rem instanceof Cons<Integer> cons) {
            var head = cons.head;
            if (head < a) {
                revFiltered = new Cons<>(head, revFiltered);
            }
            rem = cons.tail;
        }
        return reverse(revFiltered);
    }

    static List<@LessThan("#1") Integer> filterLessThan_adHoc_buggy(int a, List<Integer> ls) {
        List<@LessThan("a") Integer> revFiltered = new Nil<>();
        var rem = ls;
        while (rem instanceof Cons<Integer> cons) {
            var head = cons.head;
            if (head <= a) {    // ERROR: should be <
                revFiltered = new Cons<>(head, revFiltered);
            }
            rem = cons.tail;
        }
        return reverse(revFiltered);
    }

    static List<@LessThan("#1") Integer> filterLessThan_functional_correct(int a, List<Integer> ls) {
        return List.fromStream(ls.stream().filter((elem) -> elem < a));
    }

    static List<@LessThan("#1") Integer> filterLessThan_functional_buggy(int a, List<Integer> ls) {
        return List.fromStream(ls.stream().filter((elem) -> elem <= a));    // ERROR: should be elem < a
    }

    interface List<T> {
        Stream<T> stream();

        static <T> List<T> fromStream(Stream<T> stream) {
            throw new AssertionError("placeholder, not implemented");
        }
    }

    record Cons<T>(T head, List<T> tail) implements List<T> {
        @Override
        public Stream<T> stream() {
            return Stream.concat(Stream.of(head), tail.stream());
        }
    }

    record Nil<T>() implements List<T> {
        @Override
        public Stream<T> stream() {
            return Stream.empty();
        }
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

