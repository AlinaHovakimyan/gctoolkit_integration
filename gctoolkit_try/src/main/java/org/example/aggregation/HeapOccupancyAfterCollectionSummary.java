package org.example.aggregation;

import com.microsoft.gctoolkit.aggregator.Collates;
import com.microsoft.gctoolkit.event.GarbageCollectionTypes;
import com.microsoft.gctoolkit.time.DateTimeStamp;
import org.example.aggregation.HeapOccupancyAfterCollection;
import org.example.aggregation.HeapOccupancyAfterCollectionAggregation;
import org.example.collections.XYDataSet;

import java.util.HashMap;

@Collates(HeapOccupancyAfterCollection.class)
public class HeapOccupancyAfterCollectionSummary extends HeapOccupancyAfterCollectionAggregation {

    private HashMap<GarbageCollectionTypes, XYDataSet> aggregations = new HashMap<>();

    public void addDataPoint(GarbageCollectionTypes gcType, DateTimeStamp timeStamp, long heapOccupancy) {
        aggregations.computeIfAbsent(gcType, key -> new XYDataSet()).add(timeStamp.getTimeStamp(),heapOccupancy);
    }

    public HashMap<GarbageCollectionTypes, XYDataSet> get() {
        return aggregations;
    }

    @Override
    public boolean hasWarning() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public String toString() {
        return "Collected " + aggregations.size() + " different collection types";
    }
}