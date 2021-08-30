package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.FastDiagV2;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.tests.models.*;
import at.tugraz.ist.ase.eval.PerformanceEvaluation;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.printConstraints;
import static at.tugraz.ist.ase.common.ConstraintUtils.printListSetConstraints;
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
        TestModel1 testModel = new TestModel1("Test");
        testModel.isReverse = true;
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
        printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    public void testFindDiagnosis2() {
        TestModel2 testModel = new TestModel2("Test");
        testModel.isReverse = true;
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
        printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    public void testFindDiagnosis3() {
        TestModel3 testModel = new TestModel3("Test");
        testModel.isReverse = true;
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
        printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    public void testFindDiagnosis4() {
        TestModel4 testModel = new TestModel4("Test");
        testModel.isReverse = true;
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
        printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }

    @Test
    public void testFindDiagnosis5() {
        TestModel5 testModel = new TestModel5("Test");
        testModel.isReverse = true;
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> AC = testModel.getAllConstraints();

        // run the fastDiag to find diagnoses
        FastDiagV2 fastDiag = new FastDiagV2(checker);

        PerformanceEvaluation.reset();
        Set<String> firstDiag = fastDiag.findDiagnosis(C, AC);

        List<Set<String>> allDiag = fastDiag.findAllDiagnoses(firstDiag, C, AC);

        System.out.println("=========================================");
        System.out.println("Diagnoses found by FastDiag:");
        System.out.println(firstDiag);
        printListSetConstraints(allDiag, "Diagnosis");
        printPerformance();

        assertEquals(firstDiag, testModel.getExpectedFirstDiagnosis());
        assertEquals(allDiag, testModel.getExpectedAllDiagnoses());
    }
}