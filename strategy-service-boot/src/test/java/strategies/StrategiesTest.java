package strategies;

import com.itmo.service.FileConverterService;
import com.itmo.service.stratagies.MeanRevertingPairsTradeStrategyImpl;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.List;

@SpringBootTest
class StrategiesTest {
    MeanRevertingPairsTradeStrategyImpl service;

    void sparkTest() throws IOException {
        Long lookbackPeriod = 30L; // Example lookback period
        double entryThreshold = 2.0; // Example entry Z-score threshold
        double exitThreshold = 1.0; // Example exit Z-score threshold
        List<Double> doubles1 = FileConverterService.convertCsvToPriceList("/AAPL_2006-01-01_to_2018-01-01.csv");
        List<Double> doubles2 = FileConverterService.convertCsvToPriceList("/MSFT_2006-01-01_to_2018-01-01.csv");

        service.setup(doubles1, doubles2, "aapl", "msft");
        System.out.println(service.checkForEntry());
        System.out.println(service.checkForExit());
    }

    public static void main(String[] args) throws IOException {
        MeanRevertingPairsTradeStrategyImpl service = new MeanRevertingPairsTradeStrategyImpl();
        Long lookbackPeriod = 30L; // Example lookback period
        double entryThreshold = 2.0; // Example entry Z-score threshold
        double exitThreshold = 1.0; // Example exit Z-score threshold
        List<Double> doubles1 = FileConverterService.convertCsvToPriceList("./AAPL_2006-01-01_to_2018-01-01.csv");
        List<Double> doubles2 = FileConverterService.convertCsvToPriceList("./MSFT_2006-01-01_to_2018-01-01.csv");

        service.setup(doubles1, doubles2, "aapl", "msft");
        System.out.println(service.checkForEntry());
        System.out.println(service.checkForExit());
    }
}
