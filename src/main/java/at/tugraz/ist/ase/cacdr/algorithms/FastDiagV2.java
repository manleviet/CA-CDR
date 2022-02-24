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
 * Implementation of FastDiag algorithm using Set structures.
 *
 * <ul>
 *     <li>A. Felfernig, S. Reiterer, F. Reinfrank, G. Ninaus, and M. Jeran.
 *     Conflict Detection and Diagnosis in Configuration. In: A. Felfernig,
 *     L. Hotz, C. Bagley, and J. Tiihonen, (eds.) Knowledge-based Configuration
 *     – From Research to Business Cases (pp. 73-87), Morgan Kaufmann Publishers Inc.,
 *     San Francisco, CA, USA, 2014..</li>
 * </ul>
 *
 * // FastDiag Algorithm
 * //--------------------
 * // Func FastDiag(C ⊆ AC, AC = {c1..ct}) :  Δ
 * // if isEmpty(C) or inconsistent(AC - C) return Φ
 * // else return FD(Φ, C, AC)
 *
 * // Func FD(D, C = {c1..cq}, AC) : diagnosis  Δ
 * // if D != Φ and consistent(AC) return Φ;
 * // if singleton(C) return C;
 * // k = q/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cq};
 * // D1 = FD(C2, C1, AC - C2);
 * // D2 = FD(D1, C2, AC - D1);
 * // return(D1 ∪ D2);
 *
 * #08.2020 - Viet-Man Le: using Set structures to store constraints instead of List
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class FastDiagV2 {

    // for evaluation
    public static final String TIMER_FASTDIAGV2 = "Timer for FD V2:";
    public static final String COUNTER_FASTDIAGV2_CALLS = "The number of FD V2 calls:";

    protected final ChocoConsistencyChecker checker;

    public FastDiagV2(@NonNull ChocoConsistencyChecker checker) {
        this.checker = checker;
    }

    /**
     * This function will activate FastDiag algorithm if there exists at least one constraint,
     * which induces an inconsistency with AC - C. Otherwise, it returns an empty set.
     *
     * // Func FastDiag(C ⊆ AC, AC = {c1..ct}) :  Δ
     * // if isEmpty(C) or inconsistent(AC - C) return Φ
     * // else return FD(Φ, C, AC)
     *
     * @param C a consideration set of constraints. Need to inverse the order of the possibly faulty constraint set.
     * @param AC a background knowledge
     * @return a diagnosis or an empty set
     */
    public Set<Constraint> findDiagnosis(@NonNull Set<Constraint> C, @NonNull Set<Constraint> AC) {
        log.debug("{}Identifying diagnosis for [C={}, AC={}] >>>", LoggerUtils.tab, C, AC);
        LoggerUtils.indent();

        Set<Constraint> ACwithoutC = Sets.difference(AC, C); incrementCounter(COUNTER_DIFFERENT_OPERATOR);

        // if isEmpty(C) or inconsistent(AC - C) return Φ
        if (C.isEmpty() || checker.isConsistent(AC) ||
                (!ACwithoutC.isEmpty() && !checker.isConsistent(ACwithoutC))) {

            LoggerUtils.outdent();
            log.debug("{}<<< No diagnosis found", LoggerUtils.tab);

            return Collections.emptySet();
        } else { // else return FD(Φ, C, AC)
            incrementCounter(COUNTER_FASTDIAGV2_CALLS);
            start(TIMER_FASTDIAGV2);
            Set<Constraint> Δ = fd(Collections.emptySet(), C, AC);
            stop(TIMER_FASTDIAGV2);

            LoggerUtils.outdent();
            log.debug("{}<<< Found diagnosis [diag={}]", LoggerUtils.tab, Δ);

            return Δ;
        }
    }

    /**
     * The implementation of FastDiag algorithm.
     *
     * // Func FD(D, C = {c1..cq}, AC) : diagnosis  Δ
     * // if D != Φ and consistent(AC) return Φ;
     * // if singleton(C) return C;
     * // k = q/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cq};
     * // D1 = FD(C2, C1, AC - C2);
     * // D2 = FD(D1, C2, AC - D1);
     * // return(D1 ∪ D2);
     *
     * @param D check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param AC all constraints
     * @return a diagnosis or an empty set
     */
    private Set<Constraint> fd(Set<Constraint> D, Set<Constraint> C, Set<Constraint> AC) {
        log.trace("{}FD [D={}, C={}, AC={}] >>>", LoggerUtils.tab, D, C, AC);
        LoggerUtils.indent();

        // if D != Φ and consistent(AC) return Φ;
        if( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(AC)) {
                log.trace("{}<<< return Φ", LoggerUtils.tab);
                LoggerUtils.outdent();

                return Collections.emptySet();
            }
        }

        // if singleton(C) return C;
        int q = C.size();
        if (q == 1) {
            LoggerUtils.outdent();
            log.trace("{}<<< return [{}]", LoggerUtils.tab, C);

            return C;
        }

        int k = q / 2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, q);
        Set<Constraint> C1 = new LinkedHashSet<>(firstSubList);
        Set<Constraint> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);
        log.trace("{}Split C into [C1={}, C2={}]", LoggerUtils.tab, C1, C2);

        // D1 = FD(C2, C1, AC - C2);
        Set<Constraint> ACwithoutC2 = Sets.difference(AC, C2); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV2_CALLS);
        Set<Constraint> D1 = fd(C2, C1, ACwithoutC2);

        // D2 = FD(D1, C2, AC - D1);
        Set<Constraint> ACwithoutD1 = Sets.difference(AC, D1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAGV2_CALLS);
        Set<Constraint> D2 = fd(D1, C2, ACwithoutD1);

        LoggerUtils.outdent();
        log.trace("{}<<< return [D1={} ∪ D2={}]", LoggerUtils.tab, D1, D2);

        // return(D1 ∪ D2);
        incrementCounter(COUNTER_UNION_OPERATOR);
        return Sets.union(D1, D2);
    }

//    //calculate all diagnosis starting from the first diagnosis using FastDiag
//    public List<Set<Constraint>> findAllDiagnoses(@NonNull Set<Constraint> firstDiag, @NonNull Set<Constraint> C, @NonNull Set<Constraint> AC) {
//        log.debug("{}Identifying all diagnoses for [firstDiag={}, C={}, AC={}] >>>", LoggerUtils.tab, firstDiag, C, AC);
//        LoggerUtils.indent();
//
//        List<Set<Constraint>> allDiag = new ArrayList<>();
//        allDiag.add(firstDiag); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        diagnoses = new LinkedList<>();
//        considerations = new LinkedList<>();
//
//        pushNode(firstDiag, C);
//        log.trace("{}pushNode(diag={}, C={}) [allDiag={}]", LoggerUtils.tab, firstDiag, C, allDiag);
//
//        while (!diagnoses.isEmpty()) {
//            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
//            exploreNode(allDiag, AC);
//        }
//
//        diagnoses = null;
//        considerations = null;
//
//        LoggerUtils.outdent();
//        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab, allDiag);
//
//        return allDiag;
//    }
//
//    Queue<Set<Constraint>> diagnoses;
//    Queue<Set<Constraint>> considerations;
//
//    private void popNode(Set<Constraint> node, Set<Constraint> C) {
//        incrementCounter(COUNTER_POP_QUEUE, 2);
//        node.addAll(diagnoses.remove());
//        C.addAll(considerations.remove());
//    }
//
//    private void pushNode(Set<Constraint> node, Set<Constraint> C) {
//        incrementCounter(COUNTER_PUSH_QUEUE, 2);
//        diagnoses.add(node);
//        considerations.add(C);
//    }
//
//    // Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
//    private void exploreNode(List<Set<Constraint>> allDiag, Set<Constraint> AC) {
//        Set<Constraint> node = new LinkedHashSet<>();
//        Set<Constraint> C = new LinkedHashSet<>();
//        popNode(node, C);
//
//        log.trace("{}exploreNode(node={}, C={}) [allDiag={}]", LoggerUtils.tab, node, C, allDiag);
//        LoggerUtils.indent();
//
//        List<Constraint> itr = new LinkedList<>(node); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        for (Constraint constraint : itr) {
//            Set<Constraint> AConstraint = new LinkedHashSet<>();
//            AConstraint.add(constraint);
//            incrementCounter(COUNTER_ADD_OPERATOR);
//
//            Set<Constraint> CwithoutAConstraint = SetUtils.difference(C, AConstraint);
//            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//
//            Set<Constraint> diag = findDiagnosis(CwithoutAConstraint, AC);
//
//            if (!diag.isEmpty() && !containsAll(allDiag, diag) && isMinimal(diag, allDiag)) {
//                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//                allDiag.add(diag);
//                incrementCounter(COUNTER_ADD_OPERATOR);
//                pushNode(diag, CwithoutAConstraint);
//
//                log.trace("{}pushNode(diag={}, CwithoutAConstraint={}) [allDiag={}]", LoggerUtils.tab, diag, CwithoutAConstraint, allDiag);
//            }
//        }
//
//        LoggerUtils.outdent();
//    }

//    private boolean isMinimal(Set<Constraint> diag, List<Set<Constraint>> allDiag) {
//        incrementCounter(COUNTER_ISMINIMAL_CALLS);
//        for (Set<Constraint> constraints : allDiag) {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
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