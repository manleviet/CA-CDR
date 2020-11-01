package at.tugraz.ist.ase.MBDiag.checker;

import org.chocosolver.solver.constraints.Constraint;

import java.util.Collection;

/**
 * A common interface for the different consistency checkers.
 * Note that checkers must not modify any of the input parameters!
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface IConsistencyChecker {

    /**
     * Checks consistency of a set of constraints
     *
     * @param constraints       set of constraints
     * @param resetAfterCheck - if true, the function should reset the model after checking
     * @return <code>true</code> if constraints are consistent and <code>false</code> otherwise
     */
    boolean isConsistent(Collection<Constraint> constraints, boolean resetAfterCheck);

    /**
     * Supports a way to reset the internal checker
     */
    void reset();

    void dispose();
}
