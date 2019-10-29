package com.example.springsecurity.demo;

import java.util.Arrays;
import java.util.IntSummaryStatistics;



public class TestMain {
    public static void main(String[] args) {
      IntSummaryStatistics summaryStatistics = Arrays.asList(1,3,2,5,4).stream().mapToInt(Integer::intValue).summaryStatistics();
        System.out.printf("Max: %d, Min: %d, Ave: %f, Sum: %d",
                summaryStatistics.getMax(),
                summaryStatistics.getMin(),
                summaryStatistics.getAverage(),
                summaryStatistics.getSum());

        System.out.println();
        Arrays.asList(1,3,2,5,4).stream().sorted(Integer::compareTo).forEachOrdered(i-> System.out.println(i));
    }
}
