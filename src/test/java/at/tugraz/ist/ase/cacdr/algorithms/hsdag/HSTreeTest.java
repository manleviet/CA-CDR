/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler.QuickXPlainLabeler;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test.model.*;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain.TIMER_QUICKXPLAIN;
import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToStringWithMessage;
import static at.tugraz.ist.ase.eval.evaluator.PerformanceEvaluator.getTimer;
import static org.junit.jupiter.api.Assertions.*;

class HSTreeTest {
    @Test
    void testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void shouldStopAfterFirstDiagnosis_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfDiagnoses(1);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(1, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterSecondDiagnosis_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfDiagnoses(2);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(2, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterFirstConflict_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfConflicts(1);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(1, allConflictSets.size());
        assertEquals(0, allDiagnoses.size());
    }

    @Test
    void shouldStopAfterSecondConflict_testQX1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);
        hsTree.setMaxNumberOfConflicts(2);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(2, allConflictSets.size());
//        assertEquals(0, allDiagnoses.size());
    }

    @Test
    void testQX2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }

    @Test
    void testQX5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> B = testModel.getCorrectConstraints();

        // run the hstree to find diagnoses
        QuickXPlainLabeler quickXplain = new QuickXPlainLabeler(checker, C, B);

        HSTree hsTree = new HSTree(quickXplain, checker);

        CAEvaluator.reset();
        hsTree.construct();

        List<Set<Constraint>> allDiagnoses = hsTree.getDiagnoses();
        List<Set<Constraint>> allConflictSets = hsTree.getConflicts();

        System.out.println("=========================================");
        System.out.println("Diagnoses found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allDiagnoses, "Diagnosis"));
        System.out.println("Conflict sets found by HSTree + QuickXplain:");
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(testModel.getExpectedAllConflicts(), allConflictSets);
        assertEquals(testModel.getExpectedAllDiagnoses(), allDiagnoses);
    }
}