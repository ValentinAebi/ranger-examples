package org.example.motivatingexample;

import static org.example.motivatingexample.List.*;

import org.checkerframework.checker.index.qual.LessThan;
import org.checkerframework.checker.index.qual.NonNegative;
import org.checkerframework.checker.index.qual.Positive;


public class Decoder {
    
    public static List<Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer>> decodeAll(
        Integer[] data, @Positive int xSize, @Positive int ySize
    ) {
        var validData = filter(arrayToList(data), (d) -> d >= 0);
        return map(validData, (d) -> {
            // for an unknown reason, this assertion does not help
            assert d >= 0 : "@AssumeAssertion(index) : semantics of filter";
            return decode(d, xSize, ySize);
        });
    }

    private static Point<@NonNegative @LessThan("#2") Integer, @NonNegative @LessThan("#3") Integer> decode(
        @NonNegative int datapoint, @Positive int xSize, @Positive int ySize
    ) {
        var zero = 0;
        assert zero < xSize - 1 + 1 : "@AssumeAssertion(index) : xSize is @Positive";
        var x = clamp(xSize - 1, zero, datapoint >> 8);
        assert x < xSize : "@AssumeAssertion(index) : spec of clamp tells us that x < xSize - 1 + 1";
        assert x >= 0 : "@AssumeAssertion(index) : semantics of clamp";
        var y = datapoint % ySize;
        assert y < ySize : "@AssumeAssertion(index) : @NonNegative % @Positive";
        return new Point<>(x, y);
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
