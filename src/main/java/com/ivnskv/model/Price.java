package com.ivnskv.model;

import java.util.Date;
import java.util.Objects;

public class Price {
    long id;
    String productCode;
    int number;
    int depart;
    Date begin;
    Date end;
    long value;

    public Price() {
    }

    public Price(PriceKey key, DateInterval dateInterval, long value) {
        this.productCode = key.getProductCode();
        this.number = key.getNumber();
        this.depart = key.getDepart();
        this.begin = dateInterval.getStart();
        this.end = dateInterval.getEnd();
        this.value = value;
    }

    public long getId() {
        return id;
    }

    public String getProductCode() {
        return productCode;
    }

    public int getNumber() {
        return number;
    }

    public int getDepart() {
        return depart;
    }

    public Date getBegin() {
        return begin;
    }

    public Date getEnd() {
        return end;
    }

    public long getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Price price = (Price) o;
        return id == price.id &&
                number == price.number &&
                depart == price.depart &&
                value == price.value &&
                Objects.equals(productCode, price.productCode) &&
                Objects.equals(begin, price.begin) &&
                Objects.equals(end, price.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, productCode, number, depart, begin, end, value);
    }

    @Override
    public String toString() {
        return "Price{" +
                "id=" + id +
                ", productCode='" + productCode + '\'' +
                ", number=" + number +
                ", depart=" + depart +
                ", begin=" + begin +
                ", end=" + end +
                ", value=" + value +
                '}';
    }
}
