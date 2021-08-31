package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.tests.models.*;
import at.tugraz.ist.ase.eval.PerformanceEvaluation;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.printPerformance;
import static at.tugraz.ist.ase.common.ConstraintUtils.printConstraints;
import static at.tugraz.ist.ase.common.ConstraintUtils.printListSetConstraints;
import static org.testng.Assert.assertEquals;

public class QuickXplainTest {

    @BeforeMethod
    public void setUp() {
//        showDebugs = true;
    }

    @Test
    public void testQuickXPlain1() {
        TestModel1 testModel = new TestModel1("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    public void testQuickXPlain2() {
        TestModel2 testModel = new TestModel2("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    public void testQuickXPlain3() {
        TestModel3 testModel = new TestModel3("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    public void testQuickXPlain4() {
        TestModel4 testModel = new TestModel4("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        assertEquals(firstConflictSet, testModel.getExpectedFirstConflict());
        assertEquals(allConflictSets, testModel.getExpectedAllConflicts());
    }

    @Test
    public void testQuickXPlain5() {
        TestModel5 testModel = new TestModel5("Test");
        testModel.initialize();

        System.out.println("=========================================");
        System.out.println("Choco's commands translated from the text file:");
        printConstraints(testModel.getPossiblyFaultyConstraints());
        System.out.println("=========================================");

        ChocoConsistencyChecker checker = new ChocoConsistencyChecker(testModel);

        Set<String> C = testModel.getPossiblyFaultyConstraints();
        Set<String> B = testModel.getCorrectConstraints();

        // run the fastDiag to find diagnoses
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

        List<Set<String>> allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, C, B);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
        printListSetConstraints(allConflictSets, "Conflict set");
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
//        UtilsForTest.printConstraints(diagModel.getPossiblyFaultyConstraints());
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