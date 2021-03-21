package com.ivnskv.service;

import com.ivnskv.model.Price;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class PriceOverlapParameterizedTest {
    private final String pathToTestFile;

    public PriceOverlapParameterizedTest(String pathToTestFile) {
        this.pathToTestFile = pathToTestFile;
    }

    @Parameterized.Parameters
    public static Collection getTestFile() {
        return Arrays.asList("test_data_1.json", "test_data_2.json", "test_data_3.json", "test_data_4.json",
                "test_data_5.json", "test_data_6.json", "test_data_7.json", "test_data_8.json", "test_data_9.json");
    }

    @Test
    public void overlapPricesTest() throws IOException {
        TestDataFactory.TestData testData = new TestDataFactory().getTestData(pathToTestFile);

        List<Price> oldPrices = testData.getOldPrices();
        List<Price> newPrices = testData.getNewPrices();
        List<Price> resultPrices = testData.getResultPrice();

        System.out.println("Test file: " + pathToTestFile);
        Assert.assertEquals(resultPrices, new PriceOverlap().overlapPrices(oldPrices, newPrices));
    }
}
