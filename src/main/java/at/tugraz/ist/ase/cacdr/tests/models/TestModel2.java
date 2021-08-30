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

import static at.tugraz.ist.ase.cacdr.tests.csp.CSPModels.createModel2;

public class TestModel2 extends CDRModel implements IChocoModel, ITestModel {
    @Getter
    private Model model;

    private List<Set<String>> allDiagnoses = null;
    private List<Set<String>> allConflicts = null;

    public TestModel2(String name) {
        super(name);
    }

    public boolean isReverse = false;

    @Override
    public void initialize() {
        model = createModel2();

        // sets possibly faulty constraints to super class
        List<String> C = new ArrayList<>();
        for (Constraint c: model.getCstrs()) {
            C.add(c.toString());
        }
        if (isReverse) {
            Collections.reverse(C);
        }
        this.setPossiblyFaultyConstraints(C);

        identifyExpectedResults();
    }

    public void identifyExpectedResults() {
        Set<String> C = this.getPossiblyFaultyConstraints();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 4));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 1));
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        Set<String> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 3));
        diag3.add(IteratorUtils.get(C.iterator(), 4));
        diag3.add(IteratorUtils.get(C.iterator(), 6));

        Set<String> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 1));
        diag4.add(IteratorUtils.get(C.iterator(), 3));
        diag4.add(IteratorUtils.get(C.iterator(), 6));

        allDiagnoses = new ArrayList<>();
        allDiagnoses.add(diag1);
        allDiagnoses.add(diag2);
        allDiagnoses.add(diag3);
        allDiagnoses.add(diag4);

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 6));
        cs1.add(IteratorUtils.get(C.iterator(), 5));

        Set<String> cs2 = new LinkedHashSet<>();
        cs2.add(IteratorUtils.get(C.iterator(), 1));
        cs2.add(IteratorUtils.get(C.iterator(), 4));

        Set<String> cs3 = new LinkedHashSet<>();
        cs3.add(IteratorUtils.get(C.iterator(), 5));
        cs3.add(IteratorUtils.get(C.iterator(), 3));

        allConflicts = new ArrayList<>();
        allConflicts.add(cs1);
        allConflicts.add(cs2);
        allConflicts.add(cs3);
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
