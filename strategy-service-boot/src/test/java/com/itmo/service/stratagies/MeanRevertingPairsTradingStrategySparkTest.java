//package com.itmo.service.stratagies;
//
//import org.junit.jupiter.api.Test;
//
////@SpringBootTest(classes = StrategyApplication.class)
//class MeanRevertingPairsTradingStrategySparkTest {
//    //    @Autowired
//    MeanRevertingPairsTradingStrategySpark service = new MeanRevertingPairsTradingStrategySpark();
//
//    @Test
//    void calculateSignals() {
//        Long lookbackPeriod = 30L; // Example lookback period
//        double entryThreshold = 2.0; // Example entry Z-score threshold
//        double exitThreshold = 1.0; // Example exit Z-score threshold
//        service.setup("/AAPL_2006-01-01_to_2018-01-01.csv",
//                "/MSFT_2006-01-01_to_2018-01-01.csv",
//                lookbackPeriod,
//                entryThreshold,
//                exitThreshold);
//        service.calculateSignals();
//    }
//
//
//    public static void main(String[] args) {
//        MeanRevertingPairsTradingStrategySparkTest c = new MeanRevertingPairsTradingStrategySparkTest();
//        c.calculateSignals();
//    }
//}
