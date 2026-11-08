package edu.daa.assignment4.experiment;

import java.util.Arrays;

final class Stats {

    private Stats() {
    }

    /** Median of the values; the mean of the two middle ones when the count is even. */
    static double median(double[] values) {
        if (values.length == 0) {
            throw new IllegalArgumentException("Cannot take the median of no values.");
        }
        double[] sorted = values.clone();
        Arrays.sort(sorted);
        int middle = sorted.length / 2;
        if (sorted.length % 2 == 1) {
            return sorted[middle];
        }
        return (sorted[middle - 1] + sorted[middle]) / 2.0;
    }
}
