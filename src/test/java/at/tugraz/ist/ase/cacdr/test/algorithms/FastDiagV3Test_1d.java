package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.FastDiagV3;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.test.core.UtilsForTest;
import at.tugraz.ist.ase.cacdr.test.models.*;
import at.tugraz.ist.ase.eval.PerformanceEvaluation;
import org.apache.commons.collections4.IteratorUtils;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.printPerformance;
import static org.testng.Assert.assertEquals;

public class FastDiagV3Test_1d {

    @Test
    public void testFindDiagnosis1() {
        TestDiagnosisModel1 diagModel = new TestDiagnosisModel1("Test");
        diagModel.isReverse = true;
        diagModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<String> C = diagModel.getPossiblyFaultyConstraints();
        Set<String> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));
        diag1.add(IteratorUtils.get(C.iterator(), 3));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 0));
        diag2.add(IteratorUtils.get(C.iterator(), 3));

        Set<String> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 1));
        diag3.add(IteratorUtils.get(C.iterator(), 2));

        Set<String> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 0));
        diag4.add(IteratorUtils.get(C.iterator(), 1));

        List<Set<String>> allDiagTest = new ArrayList<>();
        allDiagTest.add(diag1);
        allDiagTest.add(diag2);
        allDiagTest.add(diag3);
        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
        assertEquals(allDiag, allDiagTest);
    }

    @Test
    public void testFindDiagnosis2() {
        TestDiagnosisModel2 diagModel = new TestDiagnosisModel2("Test");
        diagModel.isReverse = true;
        diagModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<String> C = diagModel.getPossiblyFaultyConstraints();
        Set<String> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

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

        List<Set<String>> allDiagTest = new ArrayList<>();
        allDiagTest.add(diag1);
        allDiagTest.add(diag2);
        allDiagTest.add(diag3);
        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
        assertEquals(allDiag, allDiagTest);
    }

    @Test
    public void testFindDiagnosis3() {
        TestDiagnosisModel3 diagModel = new TestDiagnosisModel3("Test");
        diagModel.isReverse = true;
        diagModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<String> C = diagModel.getPossiblyFaultyConstraints();
        Set<String> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 0));
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        Set<String> diag3 = new LinkedHashSet<>();
        diag3.add(IteratorUtils.get(C.iterator(), 2));
        diag3.add(IteratorUtils.get(C.iterator(), 3));
        diag3.add(IteratorUtils.get(C.iterator(), 6));

        Set<String> diag4 = new LinkedHashSet<>();
        diag4.add(IteratorUtils.get(C.iterator(), 0));
        diag4.add(IteratorUtils.get(C.iterator(), 3));
        diag4.add(IteratorUtils.get(C.iterator(), 6));

        List<Set<String>> allDiagTest = new ArrayList<>();
        allDiagTest.add(diag1);
        allDiagTest.add(diag2);
        allDiagTest.add(diag3);
        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
        assertEquals(allDiag, allDiagTest);
    }

    @Test
    public void testFindDiagnosis4() {
        TestDiagnosisModel4 diagModel = new TestDiagnosisModel4("Test");
        diagModel.isReverse = true;
        diagModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<String> C = diagModel.getPossiblyFaultyConstraints();
        Set<String> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add((String) IteratorUtils.get(C.iterator(), 6));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 5));

        List<Set<String>> allDiagTest = new ArrayList<>();
        allDiagTest.add(diag1);
        allDiagTest.add(diag2);

        assertEquals(firstDiag, diag1);
        assertEquals(allDiag, allDiagTest);
    }

    @Test
    public void testFindDiagnosis5() {
        TestDiagnosisModel5 diagModel = new TestDiagnosisModel5("Test");
        diagModel.isReverse = true;
        diagModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(diagModel);

        Set<String> C = diagModel.getPossiblyFaultyConstraints();
        Set<String> B = diagModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV3 fastDiag = new FastDiagV3(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, B);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, B);

        System.out.println("=========================================");
        System.out.println("Preferred diagnosis found by FastDiag:");
        System.out.println(firstDiag);
        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));

        Set<String> diag2 = new LinkedHashSet<>();
        diag2.add(IteratorUtils.get(C.iterator(), 0));

        List<Set<String>> allDiagTest = new ArrayList<>();
        allDiagTest.add(diag1);
        allDiagTest.add(diag2);

        assertEquals(firstDiag, diag1);
        assertEquals(allDiag, allDiagTest);
    }
}