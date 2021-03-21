package com.ivnskv.service;

import com.ivnskv.model.DateInterval;
import com.ivnskv.model.Price;
import com.ivnskv.model.PriceKey;
import com.ivnskv.utils.DateUtils;

import java.util.*;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

public class PriceOverlap {
    public List<Price> overlapPrices(List<Price> existPrices, List<Price> newPrices) {
        List<Price> result = new ArrayList<>();

        if (!existPrices.isEmpty()) {
            Map<PriceKey, List<Price>> existingPricesByPriceKey = existPrices.stream().collect(groupingBy(p ->
                    new PriceKey(p.getProductCode(), p.getNumber(), p.getDepart()),
                    LinkedHashMap::new,
                    Collectors.toList())
            );
            Map<PriceKey, List<Price>> newPricesByPriceKey = newPrices.stream().collect(groupingBy(p ->
                    new PriceKey(p.getProductCode(), p.getNumber(), p.getDepart()),
                    LinkedHashMap::new,
                    Collectors.toList())
            );

            for (Map.Entry<PriceKey, List<Price>> existingPriceByKey : existingPricesByPriceKey.entrySet()) {
                PriceKey priceKey = existingPriceByKey.getKey();

                List<Price> oldValues = existingPriceByKey.getValue();
                List<Price> newValues = newPricesByPriceKey.get(priceKey);

                if (newValues != null) {
                    if (oldValues != null) {
                        result.addAll(overlapByGroup(priceKey, oldValues, newValues));
                    } else {
                        result.addAll(newPrices);
                    }
                }
            }
        } else {
            return newPrices;
        }

        return result;
    }

    private List<Price> overlapByGroup(PriceKey priceKey, List<Price> existingPrices, List<Price> newPrices) {
        NavigableMap<DateInterval, Price> map = new TreeMap<>();
        existingPrices.forEach(p -> map.put(new DateInterval(p.getBegin(), p.getEnd()), p));

        for (Price newPrice : newPrices) {
            checkIntervals(map);

            DateInterval newKey = new DateInterval(newPrice.getBegin(), newPrice.getEnd());

            DateInterval lowerKey = null;
            Price lowerPrice = null;
            Map.Entry<DateInterval, Price> lowerEntry = map.lowerEntry(newKey);
            if (lowerEntry != null) {
                lowerKey = lowerEntry.getKey();
                lowerPrice = lowerEntry.getValue();
            }

            DateInterval higherKey = null;
            Price higherPrice = null;
            Map.Entry<DateInterval, Price> higherEntry = map.higherEntry(newKey);
            if (higherEntry != null) {
                higherKey = higherEntry.getKey();
                higherPrice = higherEntry.getValue();
            }

            if (lowerEntry != null && higherEntry == null) {
                if (lowerPrice.getValue() == newPrice.getValue() && lowerPrice.getEnd().after(newPrice.getBegin()) &&
                        lowerPrice.getEnd().before(newPrice.getEnd())) {
                    addNewLowerPriceWithIncreasing(newPrice, lowerPrice, map, priceKey);
                    map.remove(lowerKey);
                } else {
                    if (!lowerPrice.getBegin().equals(newPrice.getBegin()) &&
                            lowerPrice.getEnd().after(newPrice.getBegin())) {
                        addNewLowerPrice(newPrice, lowerPrice, map, priceKey);
                        map.remove(lowerKey);
                    }

                    if (lowerPrice.getEnd().after(newPrice.getEnd())) {
                        addNewHigherPrice(newPrice, lowerPrice, map, priceKey);
                        map.remove(lowerKey);
                    }

                    map.put(newKey, newPrice);
                }
                continue;
            }

            if (lowerEntry == null && higherEntry != null) {
                if (!higherPrice.getBegin().equals(newPrice.getEnd())
                        && higherPrice.getBegin().after(newPrice.getEnd())) {
                    map.put(newKey, newPrice);
                    continue;
                }
            }

            if (lowerEntry != null && higherEntry != null) {
                if (lowerPrice.getValue() == newPrice.getValue() &&
                        lowerPrice.getBegin().before(newPrice.getEnd()) &&
                        lowerPrice.getEnd().after(newPrice.getBegin())) {
                    addNewLowerPriceWithIncreasing(newPrice, lowerPrice, map, priceKey);
                    map.remove(lowerKey);

                    addNewHigherPrice(newPrice, higherPrice, map, priceKey);
                    map.remove(higherKey);
                    continue;
                }

                SortedMap<DateInterval, Price> sub = map.subMap(lowerKey, true, higherKey, true);

                if (sub.size() == 2 && lowerPrice.getEnd().after(newPrice.getBegin()) &&
                        higherPrice.getBegin().before(newPrice.getEnd())) {
                    if (!lowerPrice.getBegin().equals(newPrice.getBegin())) {
                        addNewLowerPrice(newPrice, lowerPrice, map, priceKey);
                    }
                    map.remove(lowerKey);

                    if (!higherPrice.getEnd().equals(newPrice.getEnd())) {
                        addNewHigherPrice(newPrice, higherPrice, map, priceKey);
                    }
                    map.remove(higherKey);
                }
                map.put(newKey, newPrice);
                continue;
            }
            throw new IllegalStateException("Price has not been processed: " + newPrice);
        }
        return new ArrayList<>(map.values());
    }

    private void addNewLowerPriceWithIncreasing(Price newPrice, Price oldPrice, NavigableMap<DateInterval, Price> map,
                                                PriceKey priceKey) {
        DateInterval newLowerPeriod = new DateInterval(oldPrice.getBegin(), newPrice.getEnd());
        map.put(newLowerPeriod, new Price(priceKey, newLowerPeriod, oldPrice.getValue()));
    }

    private void addNewLowerPrice(Price newPrice, Price oldPrice, NavigableMap<DateInterval, Price> map,
                                  PriceKey priceKey) {
        Date newLowerEnd = DateUtils.subtractMillisecond(newPrice.getBegin());
        DateInterval newLowerPeriod = new DateInterval(oldPrice.getBegin(), newLowerEnd);
        map.put(newLowerPeriod, new Price(priceKey, newLowerPeriod, oldPrice.getValue()));
    }

    private void addNewHigherPrice(Price newPrice, Price oldPrice, NavigableMap<DateInterval, Price> map,
                                   PriceKey priceKey) {
        Date newHigherStart = DateUtils.addMillisecond(newPrice.getEnd());
        DateInterval newHigherPeriod = new DateInterval(newHigherStart, oldPrice.getEnd());
        map.put(newHigherPeriod, new Price(priceKey, newHigherPeriod, oldPrice.getValue()));
    }

    private void checkIntervals(NavigableMap<DateInterval, Price> map) {
        List<DateInterval> intervals = new ArrayList<>(map.keySet());
        for (int i = 0; i <= intervals.size() - 1; i++) {
            DateInterval curr = intervals.get(i);
            if (curr.getStart().after(curr.getEnd()) || curr.getEnd().equals(curr.getStart())) {
                throw new IllegalArgumentException("DateInterval is incorrect: " + curr);
            }

            DateInterval next = null;
            if (i != intervals.size() - 1) {
                next = intervals.get(i + 1);
            }

            if (next != null) {
                if (curr.getEnd().after(next.getStart()) || curr.getEnd().equals(next.getStart())) {
                    throw new IllegalArgumentException("Date intervals contains intersections. Current = " + curr
                            + ", next = " + next);
                }
            }
        }
    }
}
