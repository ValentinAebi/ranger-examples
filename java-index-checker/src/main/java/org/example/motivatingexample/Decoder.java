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
        Stream<@NonNegative Integer> validData = Arrays.stream(data).filter((d) -> d >= 0).<@NonNegative Integer>mapToObj(x -> {
            assert x >= 0 : "@AssumeAssertion(index) : result of filtering";
            return x;
        });
        return validData.<Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>>map((d) -> decode(d, xSize, ySize)).toList();
    }

    private static Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer> decode(
        @NonNegative int datapoint, @Positive int xSize, @Positive int ySize
    ) {
        int zero = 0;
        assert zero < xSize - 1 + 1 : "@AssumeAssertion(index) : xSize is @Positive";
        int x = clamp(xSize - 1, zero, datapoint >> 8);
        assert x < xSize : "@AssumeAssertion(index) : spec of clamp tells us that x < xSize - 1 + 1";
        assert x >= 0 : "@AssumeAssertion(index) : semantics of clamp";
        @NonNegative int y = datapoint % ySize;
        assert y < ySize : "@AssumeAssertion(index) : @NonNegative % @Positive";
        return new Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>(x, y);
    }

    // It seems that the Index Checker is unable to express the constraint result >= min.
    // Also we have to swap min and max since the Checker Framework does not allow to express @GreaterThan and we need a way to express min <= max.
    private static @LessThan("#1 + 1") int clamp(int max, @LessThan("#1 + 1") int min, int x) {
        if (x <= min) {
            return min;
        } else if (x <= max) {
            return x;
        } else {
            return max;
        }
    }

}
