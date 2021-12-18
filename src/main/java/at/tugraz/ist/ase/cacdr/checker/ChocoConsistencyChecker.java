/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.eval.test.TestCase;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static com.google.common.base.Preconditions.checkState;

/**
 * A consistency checker implementation using the Choco solver
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class ChocoConsistencyChecker implements IConsistencyChecker {
    /**
     * An internal models
     */
    private Model model;
    private CDRModel cdrModel;
    private List<Constraint> cstrs; // store constraints to reset if reuseModel = true

    /**
     * Constructor
     *
     * CDRModel should have all constraints already posted.
     * - Testcases -> constraints should be posted before calling this function
     */
    public ChocoConsistencyChecker(@NonNull CDRModel diagModel) {
        log.trace("{}Initializing ChocoConsistencyChecker for {}", LoggerUtils.tab, diagModel);

        this.cdrModel = diagModel;
        model = ((IChocoModel)diagModel).getModel();
        cstrs = Arrays.asList(model.getCstrs());
    }

    public boolean isConsistent(@NonNull Collection<String> C) {
        log.trace("{}Checking consistency for {} >>>", LoggerUtils.tab, C);
        LoggerUtils.indent();

        // remove constraints do not present in the constraints of the parameter C
        log.trace("{}Removing constraints...", LoggerUtils.tab);
        for (Constraint c1 : cstrs) {
            incrementCounter(COUNTER_CONSTAINS_CONSTRAINT);
            if (!C.contains(c1.toString())) {
                model.unpost(c1); incrementCounter(COUNTER_UNPOST_CONSTRAINT);
            }
        }

        // Call solve()
        try {
            incrementCounter(COUNTER_CHOCO_SOLVER_CALLS);
            log.trace("{}Solving...", LoggerUtils.tab);
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            boolean isFeasible = model.getSolver().solve();
            log.trace("{}Solved: {}", LoggerUtils.tab, isFeasible);

            // resets the model to the beginning status
            // restores constraints which are removed at the beginning of the function
            reset();

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

            LoggerUtils.outdent();

            return isFeasible;
        } catch (Exception e) {
            log.error("{}Error occurred while checking consistency: {}", LoggerUtils.tab, e.getMessage());
            LoggerUtils.outdent();

            return false;
        }
    }

    /**
     * Checks the consistency of a set of constraints with a test case.
     * @param C       set of constraints
     * @param testcase test case
     * @return true if the given test case isn't violated to the set of constraints, and false otherwise.
     */
    public boolean isConsistent(@NonNull Collection<String> C, @NonNull TestCase testcase) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check consistency with a test case if the model is not debugging model");
//        checkArgument(!testcase.isEmpty(), "Test case cannot be empty");

        log.trace("{}Checking consistency for {} with test case {} >>>", LoggerUtils.tab, C, testcase);
        LoggerUtils.indent();

        // remove constraints do not present in the constraints of the parameter C
        log.trace("{}Removing constraints...", LoggerUtils.tab);
        for (Constraint c1 : cstrs) {
            incrementCounter(COUNTER_CONSTAINS_CONSTRAINT);
            if (!C.contains(c1.toString())) {
                model.unpost(c1); incrementCounter(COUNTER_UNPOST_CONSTRAINT);
            }
        }

        // add test case
        log.trace("{}Adding test case's constraints {}...", LoggerUtils.tab, testcase);
        addTestCase(testcase);

        // Call solve()
        try {
            incrementCounter(COUNTER_CHOCO_SOLVER_CALLS);
            log.trace("{}Solving...", LoggerUtils.tab);
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            boolean isFeasible = model.getSolver().solve();
            log.trace("{}Solved: {}", LoggerUtils.tab, isFeasible);

            // resets the model to the beginning status
            // restores constraints which are removed at the beginning of the function
            reset();

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

            LoggerUtils.outdent();

            return isFeasible;
        } catch (Exception e) {
            log.error("{}Error occurred while checking consistency: {}", LoggerUtils.tab, e.getMessage());
            LoggerUtils.outdent();

            return false;
        }
    }

    /**
     * Adds the corresponding constraints of a textual test case to the model.
     * @param testcase a textual test case
     */
    private void addTestCase(TestCase testcase) {
//        TestCase tc = ((IDebuggingModel)cdrModel).getTestCase(testcase);
//        if (tc != null) {
            for (Constraint c : testcase.getConstraints()) {
                model.post(c);
                incrementCounter(COUNTER_POST_CONSTRAINT);
            }
//        }
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases.
     * This function returns true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false. Test cases inducing
     * an inconsistency with {@param C} are stored in {@param TCp}.
     * Note that at the beginning, TC = TCp.
     *
     * Used by DirectDebug, TestHSDAG...
     * @param C a set of constraints
     * @param TC a considering test cases
     * @param TCp a remaining inconsistent test cases
     * @return true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false.
     */
    public boolean isConsistent(@NonNull Collection<String> C, @NonNull Collection<TestCase> TC, @NonNull Collection<TestCase> TCp) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check consistency with a test case if the model is not debugging model");

        log.trace("{}Checking consistency for {} with test cases {} >>>", LoggerUtils.tab, C, TC);
        LoggerUtils.indent();

        boolean consistent = true;
        for (TestCase tc: TC) {
            log.trace("{}Checking consistency for test case {}...", LoggerUtils.tab, tc);
            if (!isConsistent(C, tc)) {
                consistent = false;
            } else {
                TCp.remove(tc);
            }
        }

        LoggerUtils.outdent();
        log.trace("{}Consistent: {}", LoggerUtils.tab, consistent);
        log.trace("{}Remaining test cases: {}", LoggerUtils.tab, TCp);

        return consistent;
    }

    /**
     * Used by QuickXplainV1 - DirectDebugV1 project
     */
    public boolean isConsistent(@NonNull Collection<String> C, @NonNull Collection<TestCase> TC) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check consistency with a test case if the model is not debugging model");

        log.trace("{}Checking consistency for {} with test cases {} >>>", LoggerUtils.tab, C, TC);
        LoggerUtils.indent();

        boolean consistent = true;
        for (TestCase tc: TC) {
            log.trace("{}Checking consistency for test case {}...", LoggerUtils.tab, tc);
            if (!isConsistent(C, tc)) {
                log.trace("{}Test case {} is inconsistent", LoggerUtils.tab, tc);
                consistent = false;
                break;
            }
        }

        LoggerUtils.outdent();
        log.trace("{}Consistency: {}", LoggerUtils.tab, consistent);

        return consistent;
    }

    /**
     * Resets the model to the beginning status
     * Restores constraints which are removed in the {@func isConsistent} function.
     */
    @Override
    public void reset() {
        log.trace("{}Resetting model...", LoggerUtils.tab);

        model.getSolver().reset();
        incrementCounter(COUNTER_UNPOST_CONSTRAINT, model.getNbCstrs());
        model.unpost(model.getCstrs()); // unpost all constraints
        for (Constraint c : cstrs) { // repost all constraints
            model.post(c);
            incrementCounter(COUNTER_POST_CONSTRAINT);
        }
    }

    @Override
    public void dispose() {
        this.model = null;
        this.cdrModel = null;
        this.cstrs = null;
    }
}
