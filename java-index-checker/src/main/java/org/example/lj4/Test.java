package org.example.lj4;

import org.checkerframework.common.value.qual.IntRange;


@SuppressWarnings("unused")
public class Test {

    public static @IntRange(from = 2021, to = Integer.MAX_VALUE) int getYear() {
        return 2024;
    }

    public static void main(String[] args) {
        int a = 1998;
        Car c = new Car();
        c.setYear(a);

        @IntRange(from = 1801)
        int j = c.getYear();

        @IntRange(from = 2021)
        int k = getYear();
    }
}
