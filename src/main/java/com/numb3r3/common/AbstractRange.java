package com.numb3r3.common;

/**
 * from https://github.com/knowitall/common-java/blob/master/src/main/java/edu/washington/cs/knowitall/commonlib/AbstractRange.java
 * @author afader
 * @author schmmd
 */
public abstract class AbstractRange {
    public abstract boolean isEmpty();
    public abstract int getStart();
    public abstract int getEnd();
    public abstract boolean contains(int i);
    public abstract boolean contains(Range range);


    /***
     * Returns true if this range ends before the other range starts.
     * @param range
     * @return
     */
    public boolean isLeftOf(AbstractRange range) {
        return this.getEnd() < range.getStart();
    }

    /***
     * Returns true if this range ends before the other range starts.
     * @param range
     * @return
     */
    public boolean isRightOf(AbstractRange range) {
        return range.getEnd() < this.getStart();
    }

    /***
     * Returns true if this range starst before the other range starts.
     * @param range
     * @return
     */
    public boolean startsLeftOf(AbstractRange range) {
        return this.getStart() < range.getStart();
    }

    /***
     * Returns true if this range starts before the other range starts.
     * @param range
     * @return
     */
    public boolean startsRightOf(AbstractRange range) {
        return range.getStart() < this.getStart();
    }
}