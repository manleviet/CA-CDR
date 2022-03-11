/*
 *
 *  * Consistency-based Algorithms for Conflict Detection and Resolution
 *  *
 *  * Copyright (c) 2021-2022
 *  *
 *  * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 *
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import com.google.common.collect.Sets;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;

/**
 * Implementation of an MSS-based FastDiag algorithm.
 * MSS - Maximal Satisfiable Set
 *
 * // MSS-based FastDiag Algorithm
 * //--------------------
 * // B: correctConstraints (background knowledge)
 * // C: possiblyFaultyConstraints
 * //--------------------
 * // Func FastDiag(C, B) : Δ
 * // if isEmpty(C) or consistent(B U C) return Φ
 * // else return C \ FD(C, B, Φ)
 *
 * // Func FD(Δ, C = {c1..cn}, B) : MSS
 * // if Δ != Φ and consistent(B U C) return C;
 * // if singleton(C) return Φ;
 * // k = n/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cn};
 * // Δ2 = FD(C2, C1, B);
 * // Δ1 = FD(C1 - Δ2, C2, B U Δ2);
 * // return Δ1 ∪ Δ2;
 */
@Slf4j
public class FastDiagV3 {

    // for evaluation
    public static final String TIMER_FASTDIAGV3 = "Timer for FD V3:";
    public static final String COUNTER_FASTDIAGV3_CALLS = "The number of FD V3 calls:";

    protected final ChocoConsistencyChecker checker;

    public FastDiagV3(@NonNull ChocoConsistencyChecker checker) {
        this.checker = checker;
    }

    /**
     * This function will activate FastDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency in B. Otherwise, it returns an empty set.
     *
     * // Func FastDiag(C, B) : Δ
     * // if isEmpty(C) or consistent(B U C) return Φ
     * // else return C \ FD(C, B, Φ)
     *
     * @param C a consideration set of constraints. Need to inverse the order of the possibly faulty constraint set.
     * @param B a background knowledge
     * @return a diagnosis or an empty set
     */
    public Set<Constraint> findDiagnosis(@NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
        log.debug("{}Identifying diagnosis for [C={}, B={}] >>>", LoggerUtils.tab, C, B);
        LoggerUtils.indent();

        Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        if (C.isEmpty()
                || checker.isConsistent(BwithC)) {

            LoggerUtils.outdent();
            log.debug("{}<<< No diagnosis found", LoggerUtils.tab);

            return Collections.emptySet();
        } else { // else return C \ FD(C, B, Φ)
            incrementCounter(COUNTER_FASTDIAGV3_CALLS);
            start(TIMER_FASTDIAGV3);
            Set<Constraint> mss = fd(Collections.emptySet(), C, B);
            stop(TIMER_FASTDIAGV3);

            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            Set<Constraint> diag = Sets.difference(C, mss);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab, mss);

            return diag;
        }
    }

    /**
     * The implementation of MSS-based FastDiag algorithm.
     * The algorithm determines a maximal satisfiable subset MSS (Γ) of C U B.
     *
     * // Func FD(Δ, C = {c1..cn}, B) : MSS
     * // if Δ != Φ and consistent(B U C) return C;
     * // if singleton(C) return Φ;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Δ2 = FD(C2, C1, B);
     * // Δ1 = FD(C1 - Δ2, C2, B U Δ2);
     * // return Δ1 ∪ Δ2;
     *
     * @param Δ check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a maximal satisfiable subset MSS of C U B.
     */
    private Set<Constraint> fd(Set<Constraint> Δ, Set<Constraint> C, Set<Constraint> B) {
        log.trace("{}FD [Δ={}, C={}, B={}] >>>", LoggerUtils.tab, Δ, C, B);
        LoggerUtils.indent();

        // if Δ != Φ and consistent(B U C) return C;
        if( !Δ.isEmpty()) {
            Set<Constraint> BwithC = Sets.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(BwithC)) {
                LoggerUtils.outdent();
                log.trace("{}<<< return [{}]", LoggerUtils.tab, C);

                return C;
            }
        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            LoggerUtils.outdent();
            log.trace("{}<<< return Φ", LoggerUtils.tab);

            return Collections.emptySet();
        }

        int k = n / 2;  // k = n/2;
        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, n);
        Set<Constraint> C1 = new LinkedHashSet<>(firstSubList);
        Set<Constraint> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab, C1, C2);

        // Δ2 = FD(C2, C1, B);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV3_CALLS);
        Set<Constraint> Δ2 = fd(C2, C1, B);

        // Δ1 = FD(C1 - Δ2, C2, B U Δ2);
        Set<Constraint> BwithΔ2 = Sets.union(B, Δ2); incrementCounter(COUNTER_UNION_OPERATOR);
        Set<Constraint> C1withoutΔ2 = Sets.difference(C1, Δ2); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV3_CALLS);
        Set<Constraint> Δ1 = fd(C1withoutΔ2, C2, BwithΔ2);

        LoggerUtils.outdent();
        log.trace("{}<<< return [Δ1={} ∪ Δ2={}]", LoggerUtils.tab, Δ1, Δ2);

        // return Δ1 ∪ Δ2;
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(Δ1, Δ2);
    }

//    //calculate all diagnosis starting from the first diagnosis using FastDiag
//    public List<Set<Constraint>> findAllDiagnoses(@NonNull Set<Constraint> firstDiag, @NonNull Set<Constraint> C, @NonNull Set<Constraint> B) {
//        log.debug("{}Identifying all diagnoses for [firstDiag={}, C={}, B={}] >>>", LoggerUtils.tab, firstDiag, C, B);
//        LoggerUtils.indent();
//
//        this.originalBackground = B;
//
//        List<Set<Constraint>> allDiag = new ArrayList<>();
//        allDiag.add(firstDiag); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        diagnoses = new LinkedList<>();
//        considerations = new LinkedList<>();
//        background = new LinkedList<>();
//
//        pushNode(firstDiag, C, B);
//        log.trace("{}pushNode(diag={}, C={}, B={}) [allDiag={}]", LoggerUtils.tab, firstDiag, C, B, allDiag);
//
//        while (!diagnoses.isEmpty()) {
//            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
//            exploreNode(allDiag);
//        }
//
//        diagnoses = null;
//        considerations = null;
//        background = null;
//
//        LoggerUtils.outdent();
//        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab, allDiag);
//
//        return allDiag;
//    }
//
//    Queue<Set<Constraint>> diagnoses;
//    Queue<Set<Constraint>> considerations;
//    Queue<Set<Constraint>> background;
//    Set<Constraint> originalBackground = null;
//
//    private void popNode(Set<Constraint> node, Set<Constraint> C, Set<Constraint> B) {
//        node.addAll(diagnoses.remove());
//        C.addAll(considerations.remove());
//        B.addAll(background.remove());
//        incrementCounter(COUNTER_POP_QUEUE, 3);
//    }
//
//    private void pushNode(Set<Constraint> node, Set<Constraint> C, Set<Constraint> B) {
//        diagnoses.add(node);
//        considerations.add(C);
//        background.add(B);
//        incrementCounter(COUNTER_PUSH_QUEUE, 3);
//    }
//
//    //Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
//    public void exploreNode(List<Set<Constraint>> allDiag) {
//        Set<Constraint> node = new LinkedHashSet<>();
//        Set<Constraint> C = new LinkedHashSet<>();
//        Set<Constraint> B = new LinkedHashSet<>();
//        popNode(node, C, B);
//
//        log.trace("{}exploreNode(node={}, C={}, B={}) [allDiag={}]", LoggerUtils.tab, node, C, B, allDiag);
//        LoggerUtils.indent();
//
//        List<Constraint> itr = new LinkedList<>(node); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        for (Constraint constraint : itr) {
//
//            Set<Constraint> AConstraint = new LinkedHashSet<>();
//            AConstraint.add(constraint); incrementCounter(COUNTER_ADD_OPERATOR);
//
//            Set<Constraint> CwithoutAConstraint = SetUtils.difference(C, AConstraint); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//            Set<Constraint> BwithAConstraint = SetUtils.union(B, AConstraint); incrementCounter(COUNTER_UNION_OPERATOR);
//
//            if (CwithoutAConstraint.size() > 1 && checker.isConsistent(BwithAConstraint)) {
//                incrementCounter(COUNTER_CONSISTENCY_CHECKS);
//                Set<Constraint> diag = findDiagnosis(CwithoutAConstraint, BwithAConstraint);
//
//                if (!diag.isEmpty() && !containsAll(allDiag, diag) && isMinimal(diag, allDiag)) {
//                    Set<Constraint> BackgroundwithDiag = SetUtils.union(this.originalBackground, diag);
//                    incrementCounter(COUNTER_UNION_OPERATOR);
//
//                    incrementCounter(COUNTER_CONSISTENCY_CHECKS);
//                    if (checker.isConsistent(BackgroundwithDiag)) {
//
//                        incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//                        allDiag.add(diag);
//                        incrementCounter(COUNTER_ADD_OPERATOR);
//                        pushNode(diag, CwithoutAConstraint, BwithAConstraint);
//
//                        log.trace("{}pushNode(diag={}, C={}, B={}) [allDiag={}]", LoggerUtils.tab, diag, CwithoutAConstraint, BwithAConstraint, allDiag);
//                    }
//                }
//            }
//        }
//
//        LoggerUtils.outdent();
//    }

//    private boolean isMinimal(Set<Constraint> diag, List<Set<Constraint>> allDiag) {
//        for (Set<Constraint> constraints : allDiag) {
//            if (diag.containsAll(constraints)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    private boolean containsAll(List<Set<Constraint>> allDiag, Set<Constraint> diag) {
//        for (Set<Constraint> adiag: allDiag) {
//            if (adiag.containsAll(diag)) {
//                return true;
//            }
//        }
//        return false;
//    }
}