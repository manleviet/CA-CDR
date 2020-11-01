package at.tugraz.ist.ase.MBDiag.checker;

import at.tugraz.ist.ase.MBDiag.model.DiagnosisModel;
import at.tugraz.ist.ase.MBDiag.model.IChocoModelCreator;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.Collection;
import java.util.List;

// TODO - future task
//import static at.tugraz.ist.ase.MBDiag.measurement.PerformanceMeasurement.*;

/**
 * A consistency checker implementation using the Choco solver, version 4.10.2.
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class ChocoConsistencyChecker implements IConsistencyChecker {
    /**
     * An internal models
     */
    private Model model;
    private DiagnosisModel diagModel;
    private List<Constraint> cstrs; // store constraints to reset if reuseModel = true

    public ChocoConsistencyChecker(DiagnosisModel diagModel) {
        this.diagModel = diagModel;
        model = ((IChocoModelCreator) diagModel).newChocoModelInstance();
        cstrs = java.util.Arrays.asList(model.getCstrs());
    }

    public boolean isConsistent(Collection<Constraint> constraints, boolean reuseModel) {
        if (!reuseModel) {
            model = ((IChocoModelCreator) diagModel).newChocoModelInstance(); // all constraints
            cstrs = java.util.Arrays.asList(model.getCstrs());
        }

        // remove constraints are not belongs to parameter constraints
        for (Constraint c1 : cstrs) {
            boolean same = false;
            for (Constraint c2 : constraints) {
                if (c1.toString().equals(c2.toString())) {
                    same = true;
                    break;
                }
            }
            if (!same) {
                model.unpost(c1);
            }
        }

//        for (Constraint c : constraints) {
//            model.post(c);
//        }

        // Call solve()
        try {
            // System.out.println("Start solve..");
//            incrementCounter(COUNTER_CONSISTENCY_CHECKS); // Measurement
//            incrementCounter(COUNTER_SIZE_CONSISTENCY_CHECKS, model.getNbCstrs());

            boolean isFeasible = model.getSolver().solve();
            // System.out.println("Solution: " + isFeasible);

            if (reuseModel) {
                reset();
            }

            return isFeasible;
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Exception here, " + e.getMessage());
            return false;
        }
    }

    @Override
    public void reset() {
        model.getSolver().reset();
        model.unpost(model.getCstrs());
        for (Constraint c : cstrs) {
            model.post(c);
        }
    }

    @Override
    public void dispose() {
        this.model = null;
    }
}
