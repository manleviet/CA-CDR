/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.eval;

import at.tugraz.ist.ase.eval.PerformanceEvaluation;

import java.io.BufferedWriter;
import java.io.IOException;

public class Evaluation extends PerformanceEvaluation {
    // Consistency checks
    public static final String COUNTER_FEASIBLE = "The number of consistent:";
    public static final String COUNTER_INFEASIBLE = "The number of INconsistent:";

    public static final String COUNTER_CHOCO_SOLVER_CALLS = "The number of Choco Solver calls:";
    public static final String COUNTER_CONSISTENCY_CHECKS = "The number of Consistency checks:";
    public static final String COUNTER_SIZE_CONSISTENCY_CHECKS = "The size of Consistency checks:";

    public static final String COUNTER_UNPOST_CONSTRAINT = "The number of unpost constraints:";
    public static final String COUNTER_POST_CONSTRAINT = "The number of post constraints:";
    public static final String COUNTER_CONSTAINS_CONSTRAINT = "The number of contains calls:";
    // FastDiag
    public static final String COUNTER_FASTDIAG_CALLS = "The number of FD calls:";
    // QuickXplain
    public static final String COUNTER_QUICKXPLAIN_CALLS = "The number of QX calls:";
    // Set operations
    public static final String COUNTER_UNION_OPERATOR = "The number of union:";
    public static final String COUNTER_ADD_OPERATOR = "The number of add:";
    public static final String COUNTER_DIFFERENT_OPERATOR = "The number of different:";
    public static final String COUNTER_SPLIT_SET = "The number of split set:";
    public static final String COUNTER_LEFT_BRANCH_CALLS = "The number of left branch calls:";
    public static final String COUNTER_RIGHT_BRANCH_CALLS = "The number of right branch calls:";
    public static final String COUNTER_EXPLORE_NODE_CALLS = "The number of explore node calls:";
    public static final String COUNTER_PUSH_QUEUE = "The number of push queue:";
    public static final String COUNTER_POP_QUEUE = "The number of pop queue:";
    public static final String COUNTER_ISMINIMAL_CALLS = "The number of isminimal calls:";
    public static final String COUNTER_CONTAINSALL_CHECKS = "The number of contains calls:";

    public static final String TIMER_FIRST = "Time for first:";
    public static final String TIMER_ALL = "Time for all:";

    public static void printPerformance() {
        String performance = PerformanceEvaluation.getEvaluationResults();
        System.out.println(performance);
    }

    public static void printPerformance(int numIterations) {
        String performance = PerformanceEvaluation.getEvaluationResults(numIterations);
        System.out.println(performance);
    }

    public static void printPerformance(BufferedWriter writer) throws IOException {
        String performance = PerformanceEvaluation.getEvaluationResults();
        writer.write(performance);
    }
}