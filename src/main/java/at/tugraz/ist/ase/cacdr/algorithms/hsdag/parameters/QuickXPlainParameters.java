/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters;

import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.Builder;
import lombok.Getter;
import lombok.NonNull;

import java.util.Set;

@Getter
public class QuickXPlainParameters extends AbstractHSParameters {
    private Set<Constraint> B;

    @Builder
    public QuickXPlainParameters(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        super(C);
        this.B = B;
    }

    @Override
    public String toString() {
        return "QuickXPlainParameter{" +
                "C=" + getC() +
                ", B=" + B +
                "}";
    }
}