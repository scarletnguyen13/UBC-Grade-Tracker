package model;

import java.util.HashSet;
import java.util.Set;

/*
 * Represents a UBC session such as 2018S, 2019W, etc.
 */
public class Session {
    private int year;
    private SessionType type;
    private Set<Term> terms;

    public Session(int year, SessionType type, Set<Term> terms) {
        this.year = year;
        this.type = type;
        this.terms = terms;
    }

    public Session(int year, SessionType type) {
        this(year, type, new HashSet<>());
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }

    public SessionType getType() {
        return type;
    }

    public void setType(SessionType type) {
        this.type = type;
    }

    public void setTerms(Set<Term> terms) {
        this.terms = terms;
    }

    public Set<Term> getTerms() {
        return terms;
    }

    // MODIFIES: this
    // EFFECTS:  adds the given term to the current term list
    public void addTerm(Term term) {
        this.terms.add(term);
    }

    // MODIFIES: this
    // EFFECTS:  removes the given term from the current term list
    public void removeTerm(Term term) {
        this.terms.remove(term);
    }

    // EFFECTS:  returns an instance of this session
    public Session copy() {
        return this;
    }
}
