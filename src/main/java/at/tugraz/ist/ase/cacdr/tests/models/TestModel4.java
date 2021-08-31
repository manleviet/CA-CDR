/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.tests.models;

import at.tugraz.ist.ase.cacdr.model.CDRModel;
import at.tugraz.ist.ase.cacdr.model.IChocoModel;
import lombok.Getter;
import org.apache.commons.collections4.IteratorUtils;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.tests.csp.CSPModels.createModel4;

public class TestModel4 extends CDRModel implements IChocoModel, ITestModel {
    @Getter
    private Model model;

    private List<Set<String>> allDiagnoses = null;
    private List<Set<String>> allConflicts = null;

    public TestModel4(String name) {
        super(name);
    }

    @Override
    public void initialize() {
        model = createModel4();

        // sets possibly faulty constraints to super class
        List<String> C = new ArrayList<>();
        for (Constraint c: model.getCstrs()) {
            C.add(c.toString());
        }
        Collections.reverse(C);
        this.setPossiblyFaultyConstraints(C);

        identifyExpectedResults();
    }

    public void identifyExpectedResults() {
        Set<String> C = this.getPossiblyFaultyConstraints();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 6));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 5));
        cs1.add(IteratorUtils.get(C.iterator(), 6));

        allConflicts = new ArrayList<>();
        allConflicts.add(cs1);
    }

    @Override
    public Set<String> getExpectedFirstDiagnosis() {
        if (allDiagnoses != null)
            return allDiagnoses.get(0);
        return null;
    }

    @Override
    public List<Set<String>> getExpectedAllDiagnoses() {
        return allDiagnoses;
    }

    @Override
    public Set<String> getExpectedFirstConflict() {
        if (allConflicts != null)
            return allConflicts.get(0);
        return null;
    }

    @Override
    public List<Set<String>> getExpectedAllConflicts() {
        return allConflicts;
    }
}
