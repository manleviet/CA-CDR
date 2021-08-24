package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.FastDiagV2;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.test.core.UtilsForTest;
import at.tugraz.ist.ase.cacdr.test.models.*;
import at.tugraz.ist.ase.eval.PerformanceEvaluation;
import org.apache.commons.collections4.IteratorUtils;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.printPerformance;
import static org.testng.Assert.assertEquals;

public class FastDiagV2Test {

    @BeforeMethod
    public void setUp() {
//        showDebugs = true;
    }

    @AfterMethod
    public void tearDown() {
    }

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
        Set<String> AC = diagModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, c);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));
        diag1.add(IteratorUtils.get(C.iterator(), 3));

//        Set<String> diag2 = new LinkedHashSet<>();
//        diag2.add((String) IteratorUtils.get(C.iterator(), 0));
//        diag2.add((String) IteratorUtils.get(C.iterator(), 3));
//
//        Set<String> diag3 = new LinkedHashSet<>();
//        diag3.add((String) IteratorUtils.get(C.iterator(), 1));
//        diag3.add((String) IteratorUtils.get(C.iterator(), 2));
//
//        Set<String> diag4 = new LinkedHashSet<>();
//        diag4.add((String) IteratorUtils.get(C.iterator(), 0));
//        diag4.add((String) IteratorUtils.get(C.iterator(), 1));

//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(diag1);
//        allDiagTest.add(diag2);
//        allDiagTest.add(diag3);
//        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
//        assertEquals(allDiag, allDiagTest);
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
        Set<String> AC = diagModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, c);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 4));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

//        Set<String> diag2 = new LinkedHashSet<>();
//        diag2.add(IteratorUtils.get(c.iterator(), 1));
//        diag2.add(IteratorUtils.get(c.iterator(), 5));
//
//        Set<String> diag3 = new LinkedHashSet<>();
//        diag3.add(IteratorUtils.get(c.iterator(), 3));
//        diag3.add(IteratorUtils.get(c.iterator(), 4));
//        diag3.add(IteratorUtils.get(c.iterator(), 6));
//
//        Set<String> diag4 = new LinkedHashSet<>();
//        diag4.add(IteratorUtils.get(c.iterator(), 1));
//        diag4.add(IteratorUtils.get(c.iterator(), 3));
//        diag4.add(IteratorUtils.get(c.iterator(), 6));

//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(diag1);
//        allDiagTest.add(diag2);
//        allDiagTest.add(diag3);
//        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
//        assertEquals(allDiag, allDiagTest);
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
        Set<String> AC = diagModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, c);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));
        diag1.add(IteratorUtils.get(C.iterator(), 5));

//        Set<String> diag2 = new LinkedHashSet<>();
//        diag2.add((Constraint) IteratorUtils.get(c.iterator(), 0));
//        diag2.add((Constraint) IteratorUtils.get(c.iterator(), 5));
//
//        Set<String> diag3 = new LinkedHashSet<>();
//        diag3.add((Constraint) IteratorUtils.get(c.iterator(), 2));
//        diag3.add((Constraint) IteratorUtils.get(c.iterator(), 3));
//        diag3.add((Constraint) IteratorUtils.get(c.iterator(), 6));
//
//        Set<String> diag4 = new LinkedHashSet<>();
//        diag4.add((Constraint) IteratorUtils.get(c.iterator(), 0));
//        diag4.add((Constraint) IteratorUtils.get(c.iterator(), 3));
//        diag4.add((Constraint) IteratorUtils.get(c.iterator(), 6));

//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(diag1);
//        allDiagTest.add(diag2);
//        allDiagTest.add(diag3);
//        allDiagTest.add(diag4);

        assertEquals(firstDiag, diag1);
//        assertEquals(allDiag, allDiagTest);
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
        Set<String> AC = diagModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, c);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 6));

//        Set<String> diag2 = new LinkedHashSet<>();
//        diag2.add((Constraint) IteratorUtils.get(c.iterator(), 5));
//
//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(diag1);
//        allDiagTest.add(diag2);

        assertEquals(firstDiag, diag1);
//        assertEquals(allDiag, allDiagTest);
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
        Set<String> AC = diagModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

//        List<Set<Constraint>> allDiag = fastDiag.findAllDiagnoses(firstDiag, c);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
//        UtilsForTest.printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        // Expected results
        Set<String> diag1 = new LinkedHashSet<>();
        diag1.add(IteratorUtils.get(C.iterator(), 2));

//        Set<String> diag2 = new LinkedHashSet<>();
//        diag2.add((Constraint) IteratorUtils.get(c.iterator(), 0));
//
//        List<Set<String>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(diag1);
//        allDiagTest.add(diag2);

        assertEquals(firstDiag, diag1);
//        assertEquals(allDiag, allDiagTest);
    }
}