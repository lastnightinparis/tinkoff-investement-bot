package com.itmo.service.stratagies;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.spark.sql.Dataset;
import org.apache.spark.sql.Row;
import org.apache.spark.sql.SparkSession;
import org.apache.spark.sql.expressions.Window;
import org.apache.spark.sql.expressions.WindowSpec;
import org.apache.spark.sql.functions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.apache.spark.sql.functions.lit;
import static org.apache.spark.sql.functions.not;
import static org.apache.spark.sql.functions.when;


@Slf4j
@Service
@RequiredArgsConstructor
public class MeanRevertingPairsTradingStrategySpark {
    @Autowired
    private final SparkSession sparkSession;
    private Dataset<Row> stockData;
    private Long lookbackPeriod;
    private double entryThreshold;
    private double exitThreshold;
    private boolean inMarket = false;


    public void setup(String stock1FilePath, String stock2FilePath, Long lookbackPeriod,
                      double entryThreshold, double exitThreshold) {
        this.lookbackPeriod = lookbackPeriod;
        this.entryThreshold = entryThreshold;
        this.exitThreshold = exitThreshold;

        // Load stock data into Spark DataFrames
        Dataset<Row> stock1 = sparkSession.read().option("header", "true").csv(stock1FilePath);
        Dataset<Row> stock2 = sparkSession.read().option("header", "true").csv(stock2FilePath);

        // Join the DataFrames on the date column and select only the closing prices
        stockData = stock1.join(stock2, stock1.col("date").equalTo(stock2.col("date")))
                .select(stock1.col("date"),
                        stock1.col("close").alias("close1"),
                        stock2.col("close").alias("close2"));
    }

    public void calculateSignals() {
        // Define window specification for rolling operations
        WindowSpec rollingWindow = Window.orderBy("date").rowsBetween(-lookbackPeriod + 1L, Window.currentRow());

        // Calculate rolling regression parameters
        // Note: This is a simplified version and should be replaced by actual regression calculations
        stockData = stockData.withColumn("rollingMean1", functions.avg("close1")
                        .over(rollingWindow))
                .withColumn("rollingMean2", functions.avg("close2").over(rollingWindow))
                .withColumn("residual", stockData.col("close1").minus(stockData.col("rollingMean2")));

        // Calculate the rolling standard deviation of the residuals
        stockData = stockData.withColumn("stdDevResidual", functions.stddev("residual").over(rollingWindow));

        // Calculate the Z-score of the residuals
        stockData = stockData.withColumn("zScore", stockData.col("residual").minus(functions.avg("residual").over(rollingWindow))
                .divide(stockData.col("stdDevResidual")));

        // Generate signals based on the Z-score
        stockData = stockData.withColumn("signal",
                when(stockData.col("zScore").gt(entryThreshold).and(not(lit(inMarket))), "Enter")
                        .when(stockData.col("zScore").lt(exitThreshold).and(lit(inMarket)), "Exit")
                        .otherwise(lit(null)));

        // Collect the signals and take action
        stockData.filter("signal is not null").collectAsList().forEach(row -> {
            String signal = row.getAs("signal");
            String date = row.getAs("date");
            if ("Enter".equals(signal)) {
                inMarket = true;
                System.out.println(date + ": Enter the market with positions based on Z-score");
            } else if ("Exit".equals(signal)) {
                inMarket = false;
                System.out.println(date + ": Exit the market");
            }
        });

        // Stop the Spark session
        sparkSession.stop();
    }
}
