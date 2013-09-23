package com.numb3r3.common.math.stat;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;

import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Frequency<T> {

    private Map<T, Double> freqs = Maps.newHashMap();

    private double total_count = 0;

    public Frequency() {

    }

    public boolean has(T key) {
        return freqs.containsKey(key);
    }

    public void set(T key, double value) {
        if (this.has(key)) {
            double old = this.counts(key);
            total_count = total_count - old + value;
        } else {
            total_count += value;
        }
        this.freqs.put(key, value);
    }

    public void add(T key) {
        if (freqs.containsKey(key)) {
            double old = freqs.get(key);
            freqs.put(key, old + 1.0);

        } else {
            freqs.put(key, 1.0);
        }
        total_count++;
    }

    public void delete(T key) {
        if (this.has(key)) {
            freqs.remove(key);
        }
    }

    public void remove(T key) {
        if (this.has(key)) {
            double old = freqs.get(key);
            freqs.put(key, old - 1.0);
            total_count--;
        }
    }

    public double counts(T key) {
        if (this.has(key)) {
            return freqs.get(key);
        } else {
            return 0.0;
        }
    }

    public double freq(T key) {
        if (this.has(key)) {
            return freqs.get(key) / this.total_count;
        } else {
            return 0.0;
        }
    }

    public int size() {
        return this.freqs.size();
    }

    public Set<T> keys() {
        return this.freqs.keySet();
    }

    public List<T> lte_container(double uperbound, int flag) {
        List<T> lts = Lists.newArrayList();

        if (flag == 0) {
            for (T key : this.freqs.keySet()) {
                if (this.counts(key) <= uperbound) {
                    lts.add(key);
                }
            }
        } else {
            for (T key : this.freqs.keySet()) {
                if (this.freq(key) <= uperbound) {
                    lts.add(key);
                }
            }
        }

        return lts;
    }

    public List<T> gte_container(double lowerbound, int flag) {
        List<T> gts = Lists.newArrayList();
        if (flag == 0) {
            for (T key : this.freqs.keySet()) {
                if (this.counts(key) >= lowerbound) {
                    gts.add(key);
                }
            }
        } else {
            for (T key : this.freqs.keySet()) {
                if (this.freq(key) >= lowerbound) {
                    gts.add(key);
                }
            }
        }

        return gts;
    }

    public boolean lte(T key, double upperbound) {
        if (this.has(key)) {
            return this.counts(key) <= upperbound;
        } else {
            return false;
        }
    }

    public boolean gte(T key, double lowerbound) {
        if (this.has(key)) {
            return this.counts(key) >= lowerbound;
        } else {
            return false;
        }
    }

    public void dump(String filename) {
        try {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                    new FileOutputStream(filename), "UTF-8"));

            for (T key : this.freqs.keySet()) {
                String line = key.toString() + "\t" + this.counts(key);
                writer.write(line + "\n");
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @SuppressWarnings("unchecked")
    public static Frequency load(String filename, int flag) {
        Frequency freq = null;
        if (flag == 0) {
            freq = new Frequency<Integer>();
        } else {
            freq = new Frequency<String>();
        }

        try {
            for (String line : Files.readLines(new File(filename),
                    Charsets.UTF_8)) {

                List<String> items = Lists.newArrayList();
                for (String item : Splitter.on(" ").omitEmptyStrings()
                        .trimResults().split(line)) {
                    items.add(item);
                }
                if (items.size() < 2) continue;
                if (flag == 0) {
                    freq.set(Integer.parseInt(items.get(0)),
                            Double.parseDouble(items.get(1)));
                } else {
                    String item = items.get(1);
                    freq.set(
                            items.get(0),
                            Double.parseDouble(items.get(1).substring(1,
                                    item.length() - 1)));
                }
            }
            return freq;
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }


    /**
     * @param args
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
