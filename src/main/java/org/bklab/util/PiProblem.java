/*
 * Class: org.bklab.util.PiProblem
 * Modify date: 2020/3/20 上午10:25
 * Copyright (c) 2008 - 2020. - Broderick Labs.
 */

package org.bklab.util;

import java.util.stream.IntStream;

public class PiProblem {
    public static void main(String[] args) {
        System.err.println("相差" + (new PiProblem().computePi(Integer.MAX_VALUE) - Math.PI));
    }

    public double computePi(int times) {
        double insideTimes = IntStream.range(0, times).parallel().filter(a ->
                Math.pow(Math.random(), 2) + Math.pow(Math.random(), 2) < 1).count();
        double result = insideTimes / times * 4;
        System.err.println(String.format("%.0f / %d * 4 = %.8f", insideTimes, times * 4, result));
        return result;
    }

}
