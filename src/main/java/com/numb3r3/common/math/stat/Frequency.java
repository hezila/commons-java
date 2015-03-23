package com.numb3r3.common.math.stat;

import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.io.Files;
import com.numb3r3.common.util.ScoredObject;

import java.io.*;
import java.util.*;

public class Frequency<T> {

    private Map<T, Double> freqs = Maps.newHashMap();

    private double total_count = 0;

    public Frequency() {

    }

    public void clear() {
        freqs.clear();
        total_count = 0;
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

    public void add(T key, double value) {
        if (freqs.containsKey(key)) {
            double old = freqs.get(key);
            freqs.put(key, old + value);
        }
        else {
            freqs.put(key, value);
        }
        total_count += value;
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

    public int get_counts(T key) {
        if (this.has(key)) {

            return freqs.get(key).intValue();
        } else {
            return 0;
        }
    }

    public int getCounts(T key) {
        if (this.has(key)) {

            return freqs.get(key).intValue();
        } else {
            return 0;
        }
    }
    public double freq(T key) {
        if (this.has(key)) {
            return freqs.get(key) / this.total_count;
        } else {
            return 0.0;
        }
    }

    public double getFreq(T key) {
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

    /**
     *
     * @param uperbound the uper-bound threshold
     * @param flag which metrics to be used to filter the list; 0 as integer counts, 1 as frequency measure
     * @return
     */
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

    /**
     * Returns the list of keys ordered in decreasing order of
     * value.
     *
     * @return Key list with keys in decreasing order of value.
     */
    public List<T> keysOrderedByValueList() {
        List<T> keys_list = new ArrayList<T>(this.keys().size());
        keys_list.addAll(this.keys());
        Collections.sort(keys_list, valueComparator());
        return keys_list;
    }

    /**
     * Returns a list of scored objects corresponding to the entries
     * in decreasing order of value.
     *
     * @return Scored object list in decreasing order of value.
     */
    public List<ScoredObject<T>> scoredObjectsOrderedByValueList() {

        List<ScoredObject<T>> sos = Lists.newArrayList();
        for (T key : this.keys()) {
            sos.add(new ScoredObject<T>(key, this.counts(key)));
        }

        Collections.sort(sos, ScoredObject.reverseComparator());
        return sos;
    }

    /**
     * Returns the comparator that compares objects based on their
     * values.  If the objects being compared have the same value,
     * then the return result depends on their objects.  If the
     * objects are comparable, their natural ordering is returned.
     * If they are not comparable, the result is 0, so they are
     * treated as equal.  Not-a-number values are considered smaller
     * than any other numbers for the purpose of this ordering.
     *
     * @return The comparator that sorts according to score.
     */
    public Comparator<T> valueComparator() {
        return new Comparator<T>() {
            public int compare(T k1, T k2) {
                double d1 = counts(k1);
                double d2 = counts(k2);
                if (d1 > d2) {
                    return -1;
                }
                if (d1 < d2) {
                    return 1;
                }
                if (Double.isNaN(d1) && !Double.isNaN(d2)) {
                    return 1;
                }
                if (Double.isNaN(d2)) {
                    return -1;
                }
                if (!(k1 instanceof Comparable)
                        || !(k1 instanceof Comparable))
                    return 0;

                // required for basic comparable
                @SuppressWarnings("unchecked")
                Comparable<T> c1 = (Comparable<T>) k1;
                return c1.compareTo(k2);
            }
        };
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
    /**
     * @param: filename: the target file
     * @param: flag: 0 -> Integer, 1 -> String;
     */
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
                //System.out.println(line);
                List<String> items = Lists.newArrayList();
                for (String item : Splitter.on("\t").omitEmptyStrings()
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
                            Double.parseDouble(items.get(1).substring(0,
                                    item.length() - 1)));
                }
            }
            //System.out.println("The size of the freq: " + freq.size());
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
