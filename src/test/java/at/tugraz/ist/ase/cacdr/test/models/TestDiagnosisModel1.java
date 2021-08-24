/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.test.models;

import at.tugraz.ist.ase.cacdr.model.CDRModel;
import at.tugraz.ist.ase.cacdr.model.IChocoModel;
import lombok.Getter;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static at.tugraz.ist.ase.cacdr.test.core.ModelsForTest.createModel1;

public class TestDiagnosisModel1 extends CDRModel implements IChocoModel {

    @Getter
    private Model model;

    public TestDiagnosisModel1(String name) {
        super(name);
    }

    public boolean isReverse = false;

    @Override
    public void initialize() {
        model = createModel1();

        // sets possibly faulty constraints to super class
        List<String> C = new ArrayList<>();
        for (Constraint c: model.getCstrs()) {
            C.add(c.toString());
        }
        if (isReverse) {
            Collections.reverse(C);
        }
        this.setPossiblyFaultyConstraints(C);
    }
}
