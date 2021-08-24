/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.model;

import lombok.Getter;
import org.apache.commons.collections4.SetUtils;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * Contains the knowledge base for constraint problems.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public abstract class CDRModel {

    @Getter
    private final String name;

    /**
     * The set of constraints which we assume to be always correct (background knowledge)
     */
    @Getter
    private Set<String> correctConstraints = new LinkedHashSet<>();

    /**
     * The set of constraints which could be faulty = KB (knowledge base).
     */
    @Getter
    private Set<String> possiblyFaultyConstraints = new LinkedHashSet<>();

    /**
     * Creates an empty diagnosis model.
     */
    public CDRModel(String name) {
        this.name = name;
    }

    /**
     * Sets the correct constraints (or background knowledge).
     * @param correctConstraints a collection of correct constraints
     */
    public void setCorrectConstraints(Collection<String> correctConstraints) {
        this.correctConstraints = new LinkedHashSet<>(correctConstraints);
    }

    /**
     * Setter for the possibly faulty constraints (or knowledge base).
     * @param possiblyFaultyConstraints a collection of faulty constraints
     */
    public void setPossiblyFaultyConstraints(Collection<String> possiblyFaultyConstraints) {
        this.possiblyFaultyConstraints = new LinkedHashSet<>(possiblyFaultyConstraints);
    }

    /**
     * Getter for all constraints.
     * @return a set of all constraints
     */
    public Set<String> getAllConstraints() {
//        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(correctConstraints, possiblyFaultyConstraints);
    }

    /**
     * Call this method to load the constraints for the model.
     * Override this method to add all variables and constraints.
     */
    public abstract void initialize();

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("CDRModel{");
        sb.append('\n').append(" Name=").append(this.name);
        sb.append('\n').append(',').append(correctConstraints.size()).append(" correctConstraints=").append(correctConstraints);
        sb.append('\n').append(',').append(possiblyFaultyConstraints.size()).append(" possiblyFaultyConstraints=").append(possiblyFaultyConstraints);
        sb.append('}');
        return sb.toString();
    }
}
