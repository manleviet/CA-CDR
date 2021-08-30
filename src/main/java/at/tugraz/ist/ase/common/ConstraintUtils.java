/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.common;

import at.tugraz.ist.ase.csp2choco.CSP2ChocoTranslator;
import org.chocosolver.solver.Model;
import org.chocosolver.solver.constraints.Constraint;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

// TODO: add vao library
public class ConstraintUtils {
    /**
     * Print diagnoses to the console
     *
     * @param allDiag - a list of list of constraints
     */
    public static void printListListConstraints(List<List<String>> allDiag, String mess) {
        int count = 0;
        for (List<String> diag : allDiag) {
            count++;
            System.out.println(mess + " " + count + ":");
            diag.forEach(System.out::println);
        }
    }

    public static void printListSetConstraints(List<Set<String>> allDiag, String mess) {
        int count = 0;
        for (Set<String> diag : allDiag) {
            count++;
            System.out.println(mess + " " + count + ":");
            diag.forEach(System.out::println);
        }
    }

    /**
     * Print all constraints of a Choco model to the console
     *
     * @param model - a Choco model
     */
    public static void printConstraints(Model model) {
        List<Constraint> ac = Arrays.asList(model.getCstrs());
        ac.forEach(System.out::println);
    }

    public static void printConstraints(Set<String> ac) {
        ac.forEach(System.out::println);
    }

    public static void loadConstraints(InputStream inputFile, Model model) {
        CSP2ChocoTranslator translator = new CSP2ChocoTranslator(model);
        try {
            translator.translate(inputFile); // translate the input file into a Model with constraints
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
