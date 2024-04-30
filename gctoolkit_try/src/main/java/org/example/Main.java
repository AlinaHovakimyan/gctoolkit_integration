package org.example;

import com.microsoft.gctoolkit.GCToolKit;
import com.microsoft.gctoolkit.io.SingleGCLogFile;
import com.microsoft.gctoolkit.jvm.JavaVirtualMachine;
import org.example.aggregation.CollectionCycleCountsSummary;
import org.example.aggregation.HeapOccupancyAfterCollectionSummary;
import org.example.aggregation.PauseTimeSummary;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

public class Main {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello");

        Main main = new Main();
        main.analyze("/Users/alinahovakimyan/Documents/workDocs/GCLogAnalyser/openjdkLogs/tiket.log");
        //main.analyze("/Users/alinahovakimyan/Documents/workDocs/GCLogAnalyser/openjdkLogs/yum.log");
    }

    public void analyze(String gcLogFile) throws IOException {
        URL url = Main.class.getResource("/images/back.png");
        Path path = Paths.get(gcLogFile);
        SingleGCLogFile logFile = new SingleGCLogFile(path);

        GCToolKit gcToolKit = new GCToolKit();
        gcToolKit.loadAggregationsFromServiceLoader();
        try {
            JavaVirtualMachine jvm = gcToolKit.analyze(logFile);
            if (jvm.isG1GC()) {
                System.out.println("The provided log file is a G1 GC log");
            } else if (jvm.isCMS()){
                System.out.println("The provided log file is a CMS GC log");
            } else if (jvm.isZGC()) {
                System.out.println("The provided log file is a ZGC GC log");
            } else if (jvm.isShenandoah()) {
                System.out.println("The provided log file is a Shenandoah GC log");
            } else if (jvm.isParallel()) {
                System.out.println("The provided log file is a Parallel GC log");
            } else if (jvm.isSerial()) {
                System.out.println("The provided log file is a Serial GC log");
            } else if (jvm.isUnifiedLogging()) {
                System.out.println("The provided log file is a Unified GC log");
            }  else {
                System.out.println("The provided log file format was not detected");
            }

            String message = "The XYDataSet for %s contains %s items.\n";
            jvm.getAggregation(HeapOccupancyAfterCollectionSummary.class).
                    map(HeapOccupancyAfterCollectionSummary::get).
                    ifPresent(summary -> {
                        summary.forEach((gcType, dataSet) -> {
                            System.out.printf(message, gcType, dataSet.size());
                            switch (gcType) {
                                case DefNew:
                                    defNewCount = dataSet.size();
                                    break;
                                case InitialMark:
                                    initialMarkCount = dataSet.size();
                                    break;
                                case Remark:
                                    remarkCount = dataSet.size();
                                    break;
                                case ParNew:
                                    parNewCount = dataSet.size();
                                    break;
                                default:
                                    System.out.println(gcType + " not managed");
                                    break;
                            }
                        });
                    });
            Optional<CollectionCycleCountsSummary> summary = jvm.getAggregation(CollectionCycleCountsSummary.class);
            summary.ifPresent(s -> s.printOn(System.out));
            // Retrieves the Aggregation for PauseTimeSummary. This is a com.microsoft.gctoolkit.sample.aggregation.RuntimeAggregation.
            jvm.getAggregation(PauseTimeSummary.class).ifPresent(pauseTimeSummary -> {
                System.out.printf("Total pause time  : %.4f\n", pauseTimeSummary.getTotalPauseTime());
                System.out.printf("Total run time    : %.4f\n", pauseTimeSummary.getTotalPauseTime());
                System.out.printf("Percent pause time: %.2f\n", pauseTimeSummary.getPercentPaused());
                System.out.println("Pause times");
                DataPrinter.printDataWithTimestamp(pauseTimeSummary.getData());
            });
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private int initialMarkCount = 0;
    private int remarkCount = 0;
    private int defNewCount = 0;

    private int parNewCount = 0;

    public int getInitialMarkCount() {
        return initialMarkCount;
    }

    public int getRemarkCount() {
        return remarkCount;
    }

    public int getParNewCount() { return parNewCount;}

    public int getDefNewCount() {
        return defNewCount;
    }
}