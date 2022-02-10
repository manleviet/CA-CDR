/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.Getter;
import lombok.Setter;

import java.util.LinkedList;
import java.util.List;
import java.util.Set;

/**
 * An abstract class for HS algorithms
 *
 * source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
public abstract class AbstractHSConstructor {
    // for evaluation
    public static final String TIMER_HS_CONSTRUCTION_SESSION = "Timer for HS construction session:";
    public static final String TIMER_CONFLICT = "Timer for conflict:";
    public static final String TIMER_DIAGNOSIS = "Timer for diagnosis:";

    public static final String COUNTER_CONSTRUCTED_NODES = "The number of constructed nodes:";
    public static final String COUNTER_CLOSE_1 = "The number of 3.i closed nodes:";
    public static final String COUNTER_CLOSE_2 = "The number of 3.ii closed nodes:";
    public static final String COUNTER_REUSE = "The number of reused nodes:";
    public static final String COUNTER_PRUNING = "The number of pruning paths:";

    @Setter
    private int maxNumberOfDiagnoses = -1; // -1 - all diagnoses
    @Setter
    private int maxNumberOfConflicts = -1; // -1 - all conflicts

    @Setter
    private int maxDepth = 0;

    /**
     * Use setter to preset known conflicts
     */
    @Setter
    private List<Set<Constraint>> conflicts = new LinkedList<>(); // labels/F
    private final List<Set<Constraint>> diagnoses = new LinkedList<>();

    private IHSLabelable labeler;
    private ChocoConsistencyChecker checker;

    public AbstractHSConstructor(IHSLabelable labeler, ChocoConsistencyChecker checker) {
        this.labeler = labeler;
        this.checker = checker;
    }

    /**
     * Start the HS construction process
     */
    public abstract void construct();

    /**
     * Returns <code>true</code> if the goals of the diagnosis computations are achieved.
     * Override this method to add more stopping criteria.
     * @return <code>true</code> if the required number of diagnoses is found,
     * or the required number of conflicts is found.
     */
    public boolean stopConstruction() {
        // when the number of already identified diagnoses is greater than the limit, stop the computation
        boolean condition1 = (getMaxNumberOfDiagnoses() != -1 && getMaxNumberOfDiagnoses() <= getDiagnoses().size());
        // OR when the number of already identified conflicts is greater than the limit, stop the computation
        boolean condition2 = (getMaxNumberOfConflicts() != -1 && getMaxNumberOfConflicts() <= getConflicts().size());
        return condition1 || condition2;
    }

    protected abstract void addConflict(Set<Constraint> cs);

    /**
     * Reverts the state of the engine to how it was when first instantiated
     */
    public void resetEngine() {
        conflicts.clear();
        diagnoses.clear();
    }

    public void dispose() {
        this.diagnoses.clear();
        this.conflicts.clear();
        this.checker = null;
        this.labeler = null;
    }
}
