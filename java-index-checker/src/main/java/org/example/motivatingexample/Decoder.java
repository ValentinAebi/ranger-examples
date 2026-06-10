package org.example.motivatingexample;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import org.checkerframework.checker.index.qual.LessThan;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;


public class Decoder {
    
    public static List<Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>> decodeAll(
        int[] data, @Positive int xSize, @Positive int ySize
    ) {
        Stream<@NonNegative Integer> validData = Arrays.stream(data).filter((d) -> d >= 0).<@NonNegative Integer>mapToObj((@NonNegative int x) -> x);
        return validData.<Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>>map((d) -> decode(d, xSize, ySize)).toList();
    }

    private static Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer> decode(
        @NonNegative int datapoint, @Positive int xSize, @Positive int ySize
    ) {
        @NonNegative @LessThan("xSize") int x = clamp(0, xSize - 1, datapoint >> 8);
        @NonNegative @LessThan("ySize") int y = datapoint % ySize;
        return new Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>(x, y);
    }

    // it seems that the Index Checker is unable to express the constraint result >= min
    private static @LessThan("#2 + 1") int clamp(@LessThan("#2") int min, int max, int x) {
        if (x <= min) {
            return min;
        } else if (x <= max) {
            return x;
        } else {
            return max;
        }
    }

}
