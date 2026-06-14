package org.example.datetime;

import org.checkerframework.common.value.qual.IntRange;

record Time(
    @IntRange(from = 0, to = 23) int hours,
    @IntRange(from = 0, to = 59) int minutes,
    @IntRange(from = 0, to = 59) int seconds
) {

    public Time deltaTime(int h, int m, int s) {
        var sSum = seconds() + s;
        var seconds = (sSum % 60 + 60) % 60;
        var mSum = minutes() + m + (sSum - seconds) / 60;
        var minutes = (mSum % 60 + 60) % 60;
        var hSum = hours() + h + (mSum - minutes) / 60;
        var hours = (hSum % 24 + 24) % 24;
        return new Time(hours, minutes, seconds);
    }

    public Time deltaHours(int h) {
        return deltaTime(h, 0, 0);
    }

    public Time deltaMinutes(int m) {
        return deltaTime(0, m, 0);
    }

    public Time deltaSeconds(int s) {
        return deltaTime(0, 0, s);
    }

}
