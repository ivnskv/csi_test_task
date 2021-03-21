package com.ivnskv.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.ivnskv.model.Price;

import java.io.IOException;
import java.util.List;

import static java.lang.ClassLoader.getSystemClassLoader;

public class TestDataFactory {
    private static final ObjectMapper mapper = new ObjectMapper();

    static {
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    public TestData getTestData(String pathToTestFile) throws IOException {
        return mapper.readValue(getSystemClassLoader().getResourceAsStream(pathToTestFile), TestData.class);
    }

    public static class TestData {
        List<Price> oldPrices;
        List<Price> newPrices;
        List<Price> resultPrice;

        public List<Price> getOldPrices() {
            return oldPrices;
        }

        public void setOldPrices(List<Price> oldPrices) {
            this.oldPrices = oldPrices;
        }

        public List<Price> getNewPrices() {
            return newPrices;
        }

        public void setNewPrices(List<Price> newPrices) {
            this.newPrices = newPrices;
        }

        public List<Price> getResultPrice() {
            return resultPrice;
        }

        public void setResultPrice(List<Price> resultPrice) {
            this.resultPrice = resultPrice;
        }
    }
}
