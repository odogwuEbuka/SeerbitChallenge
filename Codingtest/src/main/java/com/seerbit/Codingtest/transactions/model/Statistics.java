package com.seerbit.Codingtest.transactions.model;

import java.math.BigDecimal;

public class Statistics {
    private final BigDecimal sum;
    private final BigDecimal avg;
    private final BigDecimal max;
    private final BigDecimal min;
    private final long count;

    public Statistics(BigDecimal sum, BigDecimal avg, BigDecimal max, BigDecimal min, long count)

    {
        this.sum = sum;
        this.avg = avg;
        this.max = max;
        this.min = min;
        this.count = count;
    }

    /**
     * Returns the sum of all transactions.
     *
     * @return the sum
     */
    public BigDecimal getSum() {
        return sum;
    }

    /**
     * Returns the average amount of transactions.
     *
     * @return the average
     */
    public BigDecimal getAvg() {
        return avg;
    }

    /**
     * Returns the maximum transaction amount.
     *
     * @return the maximum
     */
    public BigDecimal getMax() {
        return max;
    }

    /**
     * Returns the minimum transaction amount.
     *
     * @return the minimum
     */
    public BigDecimal getMin() {
        return min;
    }

    /**
     * Returns the total number of transactions.
     *
     * @return the count
     */
    public long getCount() {
        return count;
    }
}
