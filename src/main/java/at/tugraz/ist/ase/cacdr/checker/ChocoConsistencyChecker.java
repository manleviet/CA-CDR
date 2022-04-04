/*
 *
 *  * Consistency-based Algorithms for Conflict Detection and Resolution
 *  *
 *  * Copyright (c) 2021-2022
 *  *
 *  * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 *
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.cdrmodel.CDRModel;
import at.tugraz.ist.ase.cdrmodel.IChocoModel;
import at.tugraz.ist.ase.cdrmodel.IDebuggingModel;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.eval.test.TestCase;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.chocosolver.solver.Model;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.*;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;

/**
 * A consistency checker implementation using the Choco solver
 */
@Slf4j
public class ChocoConsistencyChecker implements IConsistencyChecker {
    public static final String TIMER_SOLVER = "Timer for solver:";

    /**
     * An internal models
     */
    private Model model;
    private CDRModel cdrModel;

    /**
     * Constructor
     *
     * CDRModel should have all constraints already posted.
     * - Testcases -> constraints should be posted before calling this function
     */
    public ChocoConsistencyChecker(@NonNull CDRModel diagModel) {
        this.cdrModel = diagModel;
        model = ((IChocoModel)diagModel).getModel();

        log.debug("{}Created ChocoConsistencyChecker for {}", LoggerUtils.tab, diagModel);
    }

    /**
     * Checks the consistency of a set of constraints.
     * @param C       set of {@link Constraint}s
     * @return true if the given set of constraints are consistent, and false otherwise.
     */
    public boolean isConsistent(@NonNull Collection<Constraint> C) {
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");

        log.debug("{}Checking consistency for [C={}] >>>", LoggerUtils.tab, C);
        LoggerUtils.indent();

        // post constraints of the parameter C
        postConstraints(C, model);

        // Call solve()
        return check();
    }

    /**
     * Checks the consistency of a set of constraints with a test case.
     * @param C       set of {@link Constraint}s
     * @param testcase a {@link TestCase}
     * @return true if the given test case isn't violated to the set of constraints, and false otherwise.
     */
    public boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull TestCase testcase) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");

        log.debug("{}Checking consistency for [C={}, testcase={}] >>>", LoggerUtils.tab, C, testcase);
        LoggerUtils.indent();

        // post constraints of the parameter C
        postConstraints(C, model);

        // post test case's constraints
        postTestCase(testcase, false);

        // Call solve()
        return check();
    }

    /**
     * Checks the consistency of a test case with a negation of other test case.
     * @param testcase a {@link TestCase}
     * @param neg_testcase a {@link TestCase}
     * @return true if the given test cases are not contradict, and false otherwise.
     */
    public boolean isConsistent(@NonNull TestCase testcase, @NonNull TestCase neg_testcase) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");

        log.debug("{}Checking consistency for [testcase={}, neg_testcase={}] >>>", LoggerUtils.tab, testcase, neg_testcase);
        LoggerUtils.indent();

        // post test case's constraints
        postTestCase(testcase, false);

        // post neg test case's constraints
        postTestCase(neg_testcase, true);

        // Call solve()
        return check();
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases.
     * This function returns true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false. Test cases inducing
     * an inconsistency with {@param C} are stored in {@param TCp}.
     * Note that at the beginning, TC = TCp.
     *
     * Used by DirectDebug, TestHSDAG...
     * @param C a set of {@link Constraint}s
     * @param TC considering {@link TestCase}s
     * @param TCp remaining inconsistent {@link TestCase}s
     * @return true if every test case in {@param TC} is consistent with
     * the given set of constraints {@param C}, otherwise false.
     */
    @Deprecated
    public boolean isConsistent(@NonNull Collection<Constraint> C, @NonNull Collection<TestCase> TC, @NonNull Collection<TestCase> TCp) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");
        checkArgument(!TC.isEmpty(), "Cannot check the consistency with an empty test case set");

        log.debug("{}Checking consistency [C={}, TC={}] >>>", LoggerUtils.tab, C, TC);
        LoggerUtils.indent();

        boolean consistent = true;
        for (TestCase tc: TC) {
            if (!isConsistent(C, tc)) {
                consistent = false;
            } else {
                TCp.remove(tc);
            }
        }

        LoggerUtils.outdent();
        log.debug("{}Checked [consistent={}, TCp={}]", LoggerUtils.tab, consistent, TCp);

        return consistent;
    }

    /**
     * Checks the consistency of a set of constraints with a set of test cases, and
     * returns remaining inconsistent {@link TestCase}s.
     *
     * Used by DirectDebug, TestHSDAG...
     * @param C a set of {@link Constraint}s
     * @param TC a considering {@link TestCase}s
     * @param onlyOne true - to get only one inconsistent test case, false - to get all inconsistent test cases
     * @return remaining inconsistent {@link TestCase}s.
     */
    public Set<TestCase> isConsistent(@NonNull Collection<Constraint> C, @NonNull Collection<TestCase> TC, boolean onlyOne) {
        checkState(cdrModel instanceof IDebuggingModel, "Cannot check the consistency with a test case if the model is not debugging model");
        checkArgument(!C.isEmpty(), "Cannot check the consistency with an empty set of constraints");
        checkArgument(!TC.isEmpty(), "Cannot check the consistency with an empty test case set");

        log.debug("{}Checking consistency [C={}, TC={}] >>>", LoggerUtils.tab, C, TC);
        LoggerUtils.indent();

        Set<TestCase> TCp = new LinkedHashSet<>();
        for (TestCase tc: TC) {
            if (!isConsistent(C, tc)) {
                TCp.add(tc);

                if (onlyOne) {
                    break;
                }
            }
        }

        LoggerUtils.outdent();
        log.debug("{}Checked [TCp={}]", LoggerUtils.tab, TCp);

        return TCp;
    }

    /**
     * Resets the model to the original status
     * Restores constraints which are removed in the {@func isConsistent} function.
     */
    @Override
    public void reset() {
        model.getSolver().reset();
        incrementCounter(COUNTER_UNPOST_CONSTRAINT, model.getNbCstrs());
        model.unpost(model.getCstrs()); // unpost all constraints

        log.trace("{}Reset model", LoggerUtils.tab);
    }

    @Override
    public void dispose() {
        this.model = null;
        this.cdrModel = null;
    }

    /**
     * Runs the solver to check the consistency of the model.
     * @return true if the model is consistent, and false otherwise.
     */
    private boolean check() {
        try {
            incrementCounter(COUNTER_CHOCO_SOLVER_CALLS);
            log.trace("{}Checking...", LoggerUtils.tab);
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            start(TIMER_SOLVER);
            boolean isFeasible = model.getSolver().solve();
            stop(TIMER_SOLVER);

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

//            model.getEnvironment().worldPush();
//            try {
//                start(TIMER_SOLVER);
//                model.getSolver().propagate(); // propagate
//                isFeasible = true; incrementCounter(COUNTER_FEASIBLE);
//
//            } catch (ContradictionException ex) { // in case of a contradiction
//
//                isFeasible = false; incrementCounter(COUNTER_INFEASIBLE);
//                model.getSolver().getEngine().flush();
//
//            } finally {
//                stop(TIMER_SOLVER);
//                // get back the original model
//                model.getEnvironment().worldPop();
//            }

            // resets the model to the beginning status
            reset();

            LoggerUtils.outdent();
            log.debug("{}<<< Checked [consistency={}]", LoggerUtils.tab, isFeasible);

            return isFeasible;
        } catch (Exception e) {
            log.error("{}Error occurred while checking consistency: {}", LoggerUtils.tab, e.getMessage());
            LoggerUtils.outdent();

            return false;
        }
    }

    /**
     * Posts the corresponding constraints of a textual test case to the model.
     * @param testcase a {@link TestCase}
     */
    private void postTestCase(TestCase testcase, boolean negative) {
        if (!negative) {
            testcase.getChocoConstraints().forEach(model::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, testcase.getChocoConstraints().size());
            log.trace("{}Added test case's constraints", LoggerUtils.tab);
        } else {
            testcase.getNegChocoConstraints().forEach(model::post);
            incrementCounter(COUNTER_POST_CONSTRAINT, testcase.getNegChocoConstraints().size());
            log.trace("{}Added neg test case's constraints", LoggerUtils.tab);
        }
    }
}
