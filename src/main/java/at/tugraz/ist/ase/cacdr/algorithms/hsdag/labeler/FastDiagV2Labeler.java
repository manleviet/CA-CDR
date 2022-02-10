/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler;

import at.tugraz.ist.ase.cacdr.algorithms.FastDiagV2;
import at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters.FastDiagV2Parameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.Getter;
import lombok.NonNull;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;

/**
 * HSLabeler for FastDiagV2 algorithm
 */
@Getter
public class FastDiagV2Labeler extends FastDiagV2 implements IHSLabelable {

    private FastDiagV2Parameters initialParameters;

    /**
     * Constructor with parameters which contain C, and AC
     * @param checker a {@link ChocoConsistencyChecker} object
     * @param parameters a {@link FastDiagV2Parameters} object
     */
    public FastDiagV2Labeler(@NonNull ChocoConsistencyChecker checker, @NonNull FastDiagV2Parameters parameters) {
        super(checker);
        this.initialParameters = parameters;
    }

    /**
     * Identifies a diagnosis.
     * @param parameters the current parameters
     * @return a diagnosis
     */
    public Set<Constraint> getLabel(@NonNull AbstractHSParameters parameters) {
        checkArgument(parameters instanceof FastDiagV2Parameters, "parameter must be an instance of FastDiagV2Parameters");
        FastDiagV2Parameters params = (FastDiagV2Parameters) parameters;

        return findDiagnosis(params.getC(), params.getAC());
    }

    /**
     * Identifies the new node's parameters on the basis of the parent node's parameters.
     * @param param_parentNode the parameters of the parent node
     * @param arcLabel the arcLabel leading to the new node
     * @return new parameters for the new node
     */
    public AbstractHSParameters createParameter(@NonNull AbstractHSParameters param_parentNode, @NonNull Constraint arcLabel) {
        checkArgument(param_parentNode instanceof FastDiagV2Parameters, "parameter must be an instance of FastDiagV2Parameters");
        FastDiagV2Parameters params = (FastDiagV2Parameters) param_parentNode;

        Set<Constraint> C = new LinkedHashSet<>(params.getC());
        C.remove(arcLabel);

        Set<Constraint> AC = new LinkedHashSet<>(params.getAC());

        return FastDiagV2Parameters.builder()
                .C(C)
                .AC(AC).build();
    }
}
