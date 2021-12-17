/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.FastDiagV3;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test.model.*;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToString;
import static at.tugraz.ist.ase.common.ConstraintUtils.convertToStringWithMessage;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class FastDiagV3Test {

    @Test
    void testFindDiagnosis1() {
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
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    void testFindDiagnosis2() {
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
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    void testFindDiagnosis3() {
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
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    void testFindDiagnosis4() {
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
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    void testFindDiagnosis5() {
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
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        CAEvaluator.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }
}