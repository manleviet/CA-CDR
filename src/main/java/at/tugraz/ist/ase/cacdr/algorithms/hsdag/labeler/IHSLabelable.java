/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler;

import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.NonNull;

import java.util.Set;

/**
 * Defines an interface for HS-dag's theorem prover.
 * In the "label" step, the HSDAG algorithm uses a theorem prover to determine
 * a conflict or a diagnosis.
 *
 * @author David - source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public interface IHSLabelable {

    Set<Constraint> getC();
    Set<Constraint> getB();
    Set<Constraint> getLabel(@NonNull Set<Constraint> C);
}
