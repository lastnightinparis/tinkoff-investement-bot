package strategies;

import com.itmo.StrategyApplication;
import com.itmo.service.stratagies.MeanRevertingPairsTradingStrategySpark;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

@SpringBootTest
@RequiredArgsConstructor
class StrategiesTest {
    MeanRevertingPairsTradingStrategySpark service;

    @Test
    void sparkTest() {
        Long lookbackPeriod = 30L; // Example lookback period
        double entryThreshold = 2.0; // Example entry Z-score threshold
        double exitThreshold = 1.0; // Example exit Z-score threshold
        service.setup("/AAPL_2006-01-01_to_2018-01-01.csv",
                "/MSFT_2006-01-01_to_2018-01-01.csv",
                lookbackPeriod,
                entryThreshold,
                exitThreshold);
        service.calculateSignals();
    }
}
