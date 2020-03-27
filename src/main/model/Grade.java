package model;

import java.io.Serializable;

/**
 * Represents a grade.
 */
public class Grade<M, O> implements Serializable {
    public final Double mark;
    public final Double outOf;

    public Grade(Double mark, Double outOf) {
        this.mark = mark;
        this.outOf = outOf;
    }
}
