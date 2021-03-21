package com.ivnskv.service;

import com.ivnskv.model.Price;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PriceOverlapTest {
    @Test
    public void overlapPricesWithDifferentNumberAndDepartTest() throws IOException {
        TestDataFactory.TestData testData1 = new TestDataFactory().getTestData("test_data_1.json");
        TestDataFactory.TestData testData2 = new TestDataFactory().getTestData("test_data_2.json");
        TestDataFactory.TestData testData3 = new TestDataFactory().getTestData("test_data_3.json");

        List<Price> oldPrices = Stream.of(testData1.getOldPrices(), testData2.getOldPrices(), testData3.getOldPrices())
                .flatMap(Collection::stream).collect(Collectors.toList());
        List<Price> newPrices = Stream.of(testData1.getNewPrices(), testData2.getNewPrices(), testData3.getNewPrices())
                .flatMap(Collection::stream).collect(Collectors.toList());
        List<Price> resultPrices = Stream.of(testData1.getResultPrice(), testData2.getResultPrice(), testData3.getResultPrice())
                .flatMap(Collection::stream).collect(Collectors.toList());

        Assert.assertEquals(resultPrices, new PriceOverlap().overlapPrices(oldPrices, newPrices));
    }
}
