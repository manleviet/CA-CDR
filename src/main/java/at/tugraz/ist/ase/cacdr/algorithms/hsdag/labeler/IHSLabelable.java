/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.NonNull;

import java.util.Set;

/**
 * Defines an interface for HS algorithms' theorem prover.
 * In the "label" step, the HS-tree and HSDAG algorithms use a theorem prover to determine
 * a conflict or a diagnosis.
 *
 * @author David - source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface IHSLabelable {

    /**
     * Returns the initial parameters
     * @return the initial parameters
     */
    AbstractHSParameters getInitialParameters();

    /**
     * Identifies a conflict or diagnosis.
     * @param parameters the current parameters
     * @return a conflict or diagnosis
     */
    Set<Constraint> getLabel(@NonNull AbstractHSParameters parameters);

    /**
     * Identifies the new node's parameters on the basis of the parent node's parameters.
     * @param param_parentNode the parameters of the parent node
     * @param arcLabel the arcLabel leading to the new node
     * @return new parameters for the new node
     */
    AbstractHSParameters createParameter(@NonNull AbstractHSParameters param_parentNode, @NonNull Constraint arcLabel);
}
