package com.ivnskv.model;

import java.util.Date;
import java.util.Objects;

public class DateInterval implements Comparable<DateInterval> {
    private final Date start;
    private final Date end;

    public DateInterval(Date start, Date end) {
        this.start = start;
        this.end = end;
    }

    public Date getStart() {
        return start;
    }

    public Date getEnd() {
        return end;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateInterval that = (DateInterval) o;
        return Objects.equals(start, that.start) &&
                Objects.equals(end, that.end);
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }

    @Override
    public int compareTo(DateInterval o) {
        int result = this.start.compareTo(o.start);
        if (result != 0) {
            return result;
        } else {
            return this.end.compareTo(o.end);
        }
    }

    @Override
    public String toString() {
        return "DateInterval{" +
                "start=" + start +
                ", end=" + end +
                '}';
    }
}
