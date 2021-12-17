/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test.model.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToStringWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class QuickXplainTest {

    @BeforeAll
    static void setUp() {
//        showDebugs = true;
    }

    @Test
    void testQuickXPlain1() {
        TestModel1 testModel = new TestModel1("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain2() {
        TestModel2 testModel = new TestModel2("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain3() {
        TestModel3 testModel = new TestModel3("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain4() {
        TestModel4 testModel = new TestModel4("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    void testQuickXPlain5() {
        TestModel5 testModel = new TestModel5("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        CAEvaluator.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        System.out.println(convertToStringWithMessage(allConflictSets, "Conflict set"));
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

//    @Test
//    public void testQuickXPlain6() {
//        TestDiagnosisModel6 diagModel = new TestDiagnosisModel6("Test");
//        diagModel.initialize();
//
//        System.out.println("=========================================");
//        System.out.println("Choco's commands translated from the text file:");
//        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
//        System.out.println("=========================================");
//
//        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);
//
//        Set<String> C = diagModel.getPossiblyFaultyConstraints();
//        Set<String> B = diagModel.getCorrectConstraints();
//
//        // run the fastDiag to find diagnoses
//        QuickXPlain quickXplain = new QuickXPlain(checker);
//
//        PerformanceEvaluation.reset();
//        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);
//
//        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);
//
//        System.out.println("=========================================");
//        System.out.println("Conflict sets found by QuickXplain:");
//        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
//        printPerformance();
//
//        // Expected results
//        Set<String> cs1 = new LinkedHashSet<>();
//        cs1.add(IteratorUtils.get(C.iterator(), 4));
//        cs1.add(IteratorUtils.get(C.iterator(), 6));
//
//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//
//        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
//    }
}