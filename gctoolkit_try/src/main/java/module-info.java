module org.example{
    requires com.microsoft.gctoolkit.api;
    requires com.microsoft.gctoolkit.vertx;
    requires java.logging;

    exports org.example.aggregation to
            com.microsoft.gctoolkit.api;

    provides com.microsoft.gctoolkit.aggregator.Aggregation with
            org.example.aggregation.HeapOccupancyAfterCollectionSummary,
            org.example.aggregation.CollectionCycleCountsSummary,
            org.example.aggregation.PauseTimeSummary;
}