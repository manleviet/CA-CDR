/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021-2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.eval.CAEvaluator;
import at.tugraz.ist.ase.cdrmodel.test.model.*;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.*;
import static org.junit.jupiter.api.Assertions.*;

public class FastDiagV2Test {

    @Test
    void testFindDiagnosis1() throws Exception {
        TestModel1 testModel = new TestModel1();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
    }

    @Test
    void testFindDiagnosis2() throws Exception {
        TestModel2 testModel = new TestModel2();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
    }

    @Test
    void testFindDiagnosis3() throws Exception {
        TestModel3 testModel = new TestModel3();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
    }

    @Test
    void testFindDiagnosis4() throws Exception {
        TestModel4 testModel = new TestModel4();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }

    @Test
    void testFindDiagnosis5() throws Exception {
        TestModel5 testModel = new TestModel5();
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        System.out.println(convertToString(testModel.getPossiblyFaultyConstraints()));
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<Constraint> C = testModel.getPossiblyFaultyConstraints();
        Set<Constraint> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        CAEvaluator.reset();
        Set<Constraint> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        System.out.println(convertToStringWithMessage(allDiag, "Diagnosis"));
        printPerformance();

//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis().size(), firstDiag.size()),
//                () -> {
//                    for (Constraint constraint : firstDiag) {
//                        assertTrue(testModel.getExpectedFirstDiagnosis().contains(constraint));
//                    }},
//                () -> assertEquals(testModel.getExpectedAllDiagnoses().size(), allDiag.size()),
//                () -> {
//                    for (int i = 0; i < allDiag.size(); i++) {
//                        Set<Constraint> expectedDiagnoses = testModel.getExpectedAllDiagnoses().get(i);
//                        Set<Constraint> actualDiagnoses = allDiag.get(i);
//                        for (Constraint constraint : actualDiagnoses) {
//                            assertTrue(expectedDiagnoses.contains(constraint));
//                        }
//                    }
//                }
//        );
        assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag);
//        assertAll(() -> assertEquals(testModel.getExpectedFirstDiagnosis(), firstDiag),
//                () -> assertEquals(testModel.getExpectedAllDiagnoses(), allDiag));
    }
}