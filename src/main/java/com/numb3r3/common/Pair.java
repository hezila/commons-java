package com.numb3r3.common;

import com.google.common.collect.Ordering;

import java.util.ArrayList;
import java.util.List;


public class Pair implements Comparable<Pair> {
    public Object first;
    public Comparable second;
    public static boolean naturalOrder = true;

    public Pair(Object k, Comparable v) {
        first = k;
        second = v;
    }

    public Pair(Object k, Comparable v, boolean naturalOrder) {
        first = k;
        second = v;
        Pair.naturalOrder = naturalOrder;
    }

    public int compareTo(Pair p) {
        if (naturalOrder)
            return this.second.compareTo(p.second);
        else
            return -this.second.compareTo(p.second);
    }


    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
        return "[" + this.first.toString() + ", " + this.second.toString() + "]";
    }

    /**
     * @param args
     */
    public static void main(String[] args) {

        List<Pair> pairs = new ArrayList();
        pairs.add(new Pair("one", 1.2));
        pairs.add(new Pair("two", 3.5));


//		Ordering<Pair> order = new Ordering<Pair>() {
//
//			@Override
//			public int compare(@Nullable Pair arg0, @Nullable Pair arg1) {
//				return arg0.compareTo(arg1);
//			}
//			
//		};

        List<Pair> cp = Ordering.natural().reverse().sortedCopy(pairs);
        for (Pair p : cp) {
            System.out.println(p);
        }

    }

}
