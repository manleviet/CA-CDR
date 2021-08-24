/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.checker;

import at.tugraz.ist.ase.cacdr.model.CDRModel;
import at.tugraz.ist.ase.cacdr.model.IChocoModel;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Collection;
import java.util.List;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.*;
import static at.tugraz.ist.ase.eval.PerformanceEvaluation.incrementCounter;

/**
 * A consistency checker implementation using the Choco solver, version 4.10.6.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class ChocoConsistencyChecker implements IConsistencyChecker {
    /**
     * An internal models
     */
    private Model model;
    private CDRModel cdrModel;
    private List<Constraint> cstrs; // store constraints to reset if reuseModel = true

    public ChocoConsistencyChecker(CDRModel diagModel) {
        this.cdrModel = diagModel;
        model = ((IChocoModel)diagModel).getModel();
        cstrs = java.util.Arrays.asList(model.getCstrs());
    }

    public boolean isConsistent(Collection<String> C) {
        // remove constraints do not present in the constraints of the parameter C
        for (Constraint c1 : cstrs) {
            incrementCounter(COUNTER_CONSTAINS_CONSTRAINT);
            if (!C.contains(c1.toString())) {
                model.unpost(c1); incrementCounter(COUNTER_UNPOST_CONSTRAINT);
            }
        }

        // Call solve()
        try {
            incrementCounter(COUNTER_CHOCO_SOLVER_CALLS);
            // System.out.println("Start solve..");
            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            boolean isFeasible = model.getSolver().solve();
            // System.out.println("Solution: " + isFeasible);

            // resets the model to the beginning status
            // restores constraints which are removed at the beginning of the function
            reset();

            if (isFeasible) {
                incrementCounter(COUNTER_FEASIBLE);
            } else {
                incrementCounter(COUNTER_INFEASIBLE);
            }

            return isFeasible;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception here, " + e.getMessage());
            return false;
        }
    }

//    public boolean isConsistent(Collection<String> C, boolean resetAfterCheck) {
//        if (!resetAfterCheck) {
//            model = ((IChocoModelCreator) diagModel).newChocoModelInstance(); // all constraints
//            cstrs = java.util.Arrays.asList(model.getCstrs());
//        }
//
//        // remove constraints do not present in the constraints of the parameter C
//        for (Constraint c1 : cstrs) {
//            if (!C.contains(c1.toString())) {
//                model.unpost(c1);
//            }
//        }
//
//        // Call solve()
//        try {
//            // System.out.println("Start solve..");
////            incrementCounter(COUNTER_CONSISTENCY_CHECKS); // Measurement
////            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());
//
//            boolean isFeasible = model.getSolver().solve();
//            // System.out.println("Solution: " + isFeasible);
//
//            if (resetAfterCheck) {
//                reset();
//            }
//
//            return isFeasible;
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("Exception here, " + e.getMessage());
//            return false;
//        }
//    }

    /**
     * Resets the model to the beginning status
     * Restores constraints which are removed in the {@func isConsistent} function.
     */
    @Override
    public void reset() {
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
