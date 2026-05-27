package motivation;

import java.util.function.Predicate;
import java.util.function.Function;
import static motivation.Lists.*;

public class Motivation {

    public static List<Point> decodeAll(Integer[] data, int xSize, int ySize) {
        var validData = filter(Arrays.toList(data), (d) -> d >= 0);
        return map(validData, (d) -> decode(d, xSize, ySize));
    }

    private static Point decode(int datapoint, int xSize, int ySize) {
        var x = clamp(0, xSize - 1, datapoint / 256);
        var y = datapoint % ySize;
        return new Point(x, y);
    }

    private static int clamp(int min, int max, int x) {
        if (x <= min) {
            return min;
        } else if (x <= max) {
            return x;
        } else {
            return max;
        }
    }

}

record Point(int x, int y){}

class Lists {

    public static <T, U> List<U> map(List<T> ls, Function<T, U> f) {
        return null;
    }

    public static <T> List<T> filter(List<T> ls, Predicate<T> pred) {
        List<T> rev = new Nil<>();
        for (var rem = ls; rem instanceof Cons<T> remc; rem = remc.rest()) {
            if (pred.test(remc.first())) {
                rev = new Cons<>(remc.first(), rev);
            }
        }
        return reverse(rev);
    }

    private static <T> List<T> reverse(List<T> ls) {
        return null;
    }

}

class Arrays {
    public static <T> List<T> toList(T[] array){
        List<T> ls = new Nil<>();
        for (var i = array.length-1; i >= 0; i--) {
            ls = new Cons<>(array[i], ls);
        }
        return ls;
    }
}

sealed interface List<T> {}
record Nil<T>() implements List<T> {}
record Cons<T>(T first, List<T> rest) implements List<T> {}


