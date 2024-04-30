package org.example;

import com.microsoft.gctoolkit.time.DateTimeStamp;

import java.util.Map;

public class DataPrinter {
    public static void printDataWithTimestamp(Map<DateTimeStamp, Double> data) {
        for (Map.Entry<DateTimeStamp, Double> dataPoint : data.entrySet()) {
            DateTimeStamp key = dataPoint.getKey();
            Double value = dataPoint.getValue();
            System.out.println("TimeStamp: " + key + ", Value: " + value);
        }
    }
}
