package com.numb3r3.common;


import com.numb3r3.common.util.ArrayPrinting;

public class OnlineArgSort {

    private int size = 0;

    private double[] array = null;

    private int[] ranks = null;

    private int next_index = 0;

    private int poolsize = 100;

    private int poolindex = 0;

    private double minmax = 0.0;


    public OnlineArgSort(int size, int poolsize) {
        this.array = new double[size];
        this.ranks = new int[poolsize];
        this.poolsize = poolsize;
        this.size = size;
    }

    public void add(double value) {
        this.array[this.next_index] = value;

        if (this.poolindex < this.poolsize) {
            if (this.poolindex == 0) {
                this.minmax = value;
            } else {
                if (value < this.minmax) {
                    this.minmax = value;
                }
            }
            int rank = this.poolindex;
            for (int i = 0; i < this.poolindex; i++) {
                if (value > this.array[this.ranks[i]]) {
                    rank = i;
                    break;
                }
            }
            for (int j = this.poolindex; j > rank; j--) {
                this.ranks[j] = this.ranks[j - 1];
            }
            this.ranks[rank] = this.next_index;
            this.poolindex++;
        } else if (value > minmax) {
            int rank = this.poolsize - 1;
            for (int i = 0; i < this.poolsize; i++) {
                if (value > this.array[this.ranks[i]]) {
                    rank = i;
                    break;
                }
            }
            for (int j = this.poolsize - 1; j > rank; j--) {
                this.ranks[j] = this.ranks[j - 1];
            }
            this.ranks[rank] = this.next_index;
            minmax = this.array[this.ranks[this.poolsize - 1]];
        }


        this.next_index++;
    }

    public int[] ranks() {
        return this.ranks;
    }

    public double value(int i) {
        return this.array[i];
    }

    public double[] values() {
        return this.array;
    }

    public double[] topvalues() {
        double[] values = new double[this.poolsize];
        for (int i = 0; i < this.poolsize; i++) {
            values[i] = this.array[this.ranks[i]];
        }
        return values;
    }

    public int size() {
        return this.size;
    }

    /**
     * @param args
     */
    public static void main(String[] args) {
        OnlineArgSort sort = new OnlineArgSort(10, 5);
        sort.add(0.1);
        sort.add(0.3);
        sort.add(0.2);
        sort.add(0.8);
        sort.add(0.6);
        sort.add(0.23);
        sort.add(3.0);
        sort.add(0.9);
        sort.add(0.05);
        sort.add(0.5);


        ArrayPrinting.printDoubleArray(sort.values(), "null");

        for (int i : sort.ranks()) {
            System.out.println(i + " " + sort.value(i));
        }

    }

}
