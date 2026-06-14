package org.example.datetime;

import org.checkerframework.common.value.qual.IntRange;

public class MutTime {
    private @IntRange(from = 0, to = 23) int hours;
    private @IntRange(from = 0, to = 59) int minutes;
    private @IntRange(from = 0, to = 59) int seconds;

    public MutTime(
        @IntRange(from = 0, to = 23) int hours,
        @IntRange(from = 0, to = 59) int minutes,
        @IntRange(from = 0, to = 59) int seconds
    ) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void moveBy(int h, int m, int s) {
        var sSum = this.seconds + s;
        this.seconds = (sSum % 60 + 60) % 60;
        var mSum = this.minutes + m + (sSum / 60);
        this.minutes = (mSum % 60 + 60) % 60;
        var hSum = this.hours + h + (mSum / 60);
        this.hours = (hSum % 24 + 24) % 24;
    }

    public void moveByHours(int h) {
        moveBy(h, 0, 0);
    }

    public void moveByMinutes(int m) {
        moveBy(0, m, 0);
    }

    public void moveBySeconds(int s) {
        moveBy(0, 0, s);
    }

}
