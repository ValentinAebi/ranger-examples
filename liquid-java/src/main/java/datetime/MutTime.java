package datetime;

import liquidjava.specification.Refinement;
import liquidjava.specification.RefinementAlias;


public class MutTime {
    private @Refinement("0 <= _ && _ <= 23") int hours;
    private @Refinement("0 <= _ && _ <= 59") int minutes;
    private @Refinement("0 <= _ && _ <= 59") int seconds;

    public MutTime(
        @Refinement("0 <= _ && _ <= 23") int hours,
        @Refinement("0 <= _ && _ <= 59") int minutes,
        @Refinement("0 <= _ && _ <= 59") int seconds
    ) {
        this.hours = hours;
        this.minutes = minutes;
        this.seconds = seconds;
    }

    public void moveBy(int h, int m, int s) {
        int sSum = this.seconds + s;
        this.seconds = (sSum % 60 + 60) % 60;
        int mSum = this.minutes + m + (sSum / 60);
        this.minutes = (mSum % 60 + 60) % 60;
        int hSum = this.hours + h + (mSum / 60);
        this.hours = (hSum % 60 + 60) % 60;     // <- unsound
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
