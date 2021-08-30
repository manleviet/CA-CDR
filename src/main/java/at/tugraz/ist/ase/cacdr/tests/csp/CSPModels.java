/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.tests.csp;

import at.tugraz.ist.ase.common.Utils;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.variables.IntVar;

import java.io.FileNotFoundException;
import java.io.InputStream;

import static at.tugraz.ist.ase.common.ConstraintUtils.loadConstraints;

public class CSPModels {
    public static Model createModel1() {
//        String inputFile = "src/test/resources/csp/csp1.mzn";

        // create a model
        Model model = new Model("Test 1");

        // Decision variables
        IntVar x = model.intVar("x", -10, 10);
        IntVar y = model.intVar("y", -10, 10);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp1.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }

    public static Model createModel2() {
//        String inputFile = "src/test/resources/csp/csp2.mzn";

        // create a model
        Model model = new Model("Test 2");

        // Decision variables
        IntVar v1 = model.intVar("v1", 1, 3);
        IntVar v2 = model.intVar("v2", 1, 3);
        IntVar v3 = model.intVar("v3", 1, 3);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp2.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }

    public static Model createModel3() {
//        String inputFile = "src/test/resources/csp/csp3.mzn";

        // create a model
        Model model = new Model("Test 3");

        // Decision variables
        IntVar v1 = model.intVar("v1", 1, 3);
        IntVar v2 = model.intVar("v2", 1, 3);
        IntVar v3 = model.intVar("v3", 1, 3);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp3.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }

    public static Model createModel4() {
//        String inputFile = "src/test/resources/csp/csp4.mzn";

        // create a model
        Model model = new Model("Test 4");

        // Decision variables
        IntVar v1 = model.intVar("v1", 1, 3);
        IntVar v2 = model.intVar("v2", 1, 3);
        IntVar v3 = model.intVar("v3", 1, 3);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp4.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }

    public static Model createModel5() {
//        String inputFile = "src/test/resources/csp/csp5.mzn";

        // create a model
        Model model = new Model("Test 5");

        // Decision variables
        IntVar v1 = model.intVar("v1", 1, 3);
        IntVar v2 = model.intVar("v2", 1, 3);
        IntVar v3 = model.intVar("v3", 1, 3);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp5.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }

    public static Model createModel6() {
//        String inputFile = "src/test/resources/csp/csp6.mzn";

        // create a model
        Model model = new Model("Test 6");

        // Decision variables
        IntVar v1 = model.intVar("v1", 1, 3);
        IntVar v2 = model.intVar("v2", 1, 3);
        IntVar v3 = model.intVar("v3", 1, 3);

        ClassLoader classLoader = CSPModels.class.getClassLoader();
        InputStream inputStream = null;
        try {
            inputStream = Utils.getInputStream(classLoader, "csp6.mzn");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        loadConstraints(inputStream, model);

        return model;
    }
}
