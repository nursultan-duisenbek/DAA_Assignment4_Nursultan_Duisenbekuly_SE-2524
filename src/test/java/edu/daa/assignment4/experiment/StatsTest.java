package edu.daa.assignment4.experiment;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class StatsTest {

    @Test
    void medianOfAnOddCountIsTheMiddleValue() {
        assertEquals(3.0, Stats.median(new double[]{5, 1, 3}), 0.0);
    }

    @Test
    void medianOfAnEvenCountIsTheMeanOfTheTwoMiddleValues() {
        assertEquals(2.5, Stats.median(new double[]{4, 1, 3, 2}), 0.0);
    }

    @Test
    void medianOfOneValueIsThatValue() {
        assertEquals(7.5, Stats.median(new double[]{7.5}), 0.0);
    }

    @Test
    void medianIgnoresOneVerySlowRun() {
        // This is why the experiment reports the median of its runs and not the mean: one run hit by a GC pause
        // or a busy machine does not move it.
        assertEquals(1.0, Stats.median(new double[]{1, 1, 1000, 1, 1}), 0.0);
    }

    @Test
    void medianDoesNotReorderTheCallersArray() {
        double[] values = {3, 1, 2};
        Stats.median(values);
        assertArrayEquals(new double[]{3, 1, 2}, values, 0.0);
    }

    @Test
    void medianOfNothingIsRejected() {
        assertThrows(IllegalArgumentException.class, () -> Stats.median(new double[0]));
    }
}
