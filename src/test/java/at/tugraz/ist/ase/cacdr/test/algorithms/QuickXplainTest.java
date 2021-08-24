package at.tugraz.ist.ase.cacdr.test.algorithms;

import at.tugraz.ist.ase.cacdr.algorithms.QuickXPlain;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.cacdr.test.core.UtilsForTest;
import at.tugraz.ist.ase.cacdr.test.models.*;
import at.tugraz.ist.ase.eval.PerformanceEvaluation;
import org.apache.commons.collections4.IteratorUtils;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.LinkedHashSet;
import java.util.Set;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.printPerformance;
import static org.testng.Assert.assertEquals;

public class QuickXplainTest {

    @BeforeMethod
    public void setUp() {
//        showDebugs = true;
    }

    @Test
    public void testQuickXPlain1() {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 1));
        cs1.add(IteratorUtils.get(C.iterator(), 3));

//        Set<Constraint> cs2 = new LinkedHashSet<>();
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 0));
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 2));
//
//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//        allDiagTest.add(cs2);

        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }

    @Test
    public void testQuickXPlain2() {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 6));
        cs1.add(IteratorUtils.get(C.iterator(), 5));

//        Set<Constraint> cs2 = new LinkedHashSet<>();
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 5));
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 3));
//
//        Set<Constraint> cs3 = new LinkedHashSet<>();
//        cs3.add((Constraint) IteratorUtils.get(c.iterator(), 1));
//        cs3.add((Constraint) IteratorUtils.get(c.iterator(), 4));
//
//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//        allDiagTest.add(cs2);
//        allDiagTest.add(cs3);

        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }

    @Test
    public void testQuickXPlain3() {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 5));
        cs1.add(IteratorUtils.get(C.iterator(), 6));

//        Set<Constraint> cs2 = new LinkedHashSet<>();
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 3));
//        cs2.add((Constraint) IteratorUtils.get(c.iterator(), 5));
//
//        Set<Constraint> cs3 = new LinkedHashSet<>();
//        cs3.add((Constraint) IteratorUtils.get(c.iterator(), 0));
//        cs3.add((Constraint) IteratorUtils.get(c.iterator(), 2));
//
//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//        allDiagTest.add(cs2);
//        allDiagTest.add(cs3);

        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }

    @Test
    public void testQuickXPlain4() {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 5));
        cs1.add(IteratorUtils.get(C.iterator(), 6));

//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);

        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }

    @Test
    public void testQuickXPlain5() {
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
        Set<String> cs1 = new LinkedHashSet<>();
        cs1.add(IteratorUtils.get(C.iterator(), 0));
        cs1.add(IteratorUtils.get(C.iterator(), 2));

//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);

        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }

    @Test
    public void testQuickXPlain6() {
        TestDiagnosisModel6 diagModel = new TestDiagnosisModel6("Test");
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
        QuickXPlain quickXplain = new QuickXPlain(checker);

        PerformanceEvaluation.reset();
        Set<String> firstConflictSet = quickXplain.findConflictSet(C, B);

//        List<Set<Constraint>>allConflictSets = quickXplain.findAllConflictSets(firstConflictSet, c);

        System.out.println("=========================================");
        System.out.println("Conflict sets found by QuickXplain:");
        System.out.println(firstConflictSet);
//        UtilsForTest.printListSetConstraints(allConflictSets, "Conflict set");
        printPerformance();

        // Expected results
//        Set<Constraint> cs1 = new LinkedHashSet<>();
//        cs1.add((Constraint) IteratorUtils.get(c.iterator(), 4));
//        cs1.add((Constraint) IteratorUtils.get(c.iterator(), 6));
//
//        List<Set<Constraint>> allDiagTest = new ArrayList<>();
//        allDiagTest.add(cs1);
//
//        assertEquals(firstConflictSet, cs1);
//        assertEquals(allConflictSets, allDiagTest);
    }
}