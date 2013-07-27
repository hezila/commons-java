package com.numb3r3.common.util;


import java.text.NumberFormat;
import java.util.ArrayList;


public class Counter {

    private static final NumberFormat nf = NumberFormat.getIntegerInstance();
    private Integer value;

    /**
     * Constructor for class Counter. New Counter has a value of zero;
     */
    public Counter() {
        this.value = 0;
    }

    public Counter(int begin) {
        this.value = begin;
    }

    /**
     * Increment the counter value by 1
     */
    public int increment() {
        this.value += 1;
        return this.value;
    }

    public int increment(int x) {
        this.value += x;
        return this.value;
    }

    public int value() {
        return this.value;
    }

    public void set(int x) {
        this.value = x;
    }

    public static ArrayList<Integer> toIntegerArray(ArrayList<Counter> counters) {
        int size = counters.size();
        ArrayList<Integer> integers = new ArrayList<Integer>(size);
        for (int i = 0; i < size; i++) {
            integers.add(i, counters.get(i).value());
        }
        return integers;
    }

    public String getValueString(int num_digits) {
        nf.setMinimumIntegerDigits(num_digits);
        nf.setGroupingUsed(false);
        return nf.format(value);
    }

    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return value.toString();
    }
}