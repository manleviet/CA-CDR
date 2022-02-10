/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

@Getter
public class QuickXPlainLabeler extends QuickXPlain implements IHSLabelable {

    private Set<Constraint> C;
    private Set<Constraint> B;

    public QuickXPlainLabeler(@NonNull ChocoConsistencyChecker checker, @NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        super(checker);
        this.C = C;
        this.B = B;
    }

    @Override
    public Set<Constraint> getLabel(@NonNull Set<Constraint> C) {
        Set<Constraint> cs = findConflictSet(C, B);
        // reverse the order of the constraints
        List<Constraint> csList = new LinkedList<>(cs);
        Collections.reverse(csList);

        return new LinkedHashSet<>(csList);
    }
}
