/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.*;

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
public class FastDiagV2 {

    private final ChocoConsistencyChecker checker;

    public FastDiagV2(ChocoConsistencyChecker checker) {
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
    public Set<String> findDiagnosis(Set<String> C, Set<String> AC)
    {
        Set<String> ACwithoutC = SetUtils.difference(AC, C); incrementCounter(COUNTER_DIFFERENT_OPERATOR);

        // if isEmpty(C) or inconsistent(AC - C) return Φ
        if (C.isEmpty()
                || !checker.isConsistent(ACwithoutC)) {
            return Collections.emptySet();
        } else { // else return FD(Φ, C, AC)
            incrementCounter(COUNTER_FASTDIAG_CALLS);
            start(TIMER_FIRST);
            Set<String> Δ = fd(Collections.emptySet(), C, AC);
            stop(TIMER_FIRST);

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
     * @return a maximal satisfiable subset MSS of C U B.
     */
    private Set<String> fd(Set<String> D, Set<String> C, Set<String> AC) {
        // if D != Φ and consistent(AC) return Φ;
        if( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(AC)) {
                return Collections.emptySet();
            }
        }

        // if singleton(C) return C;
        int q = C.size();
        if (q == 1) {
            return C;
        }

        int k = q / 2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        List<String> firstSubList = new ArrayList<>(C).subList(0, k);
        List<String> secondSubList = new ArrayList<>(C).subList(k, q);
        Set<String> C1 = new LinkedHashSet<>(firstSubList);
        Set<String> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);

        // D1 = FD(C2, C1, AC - C2);
        Set<String> ACwithoutC2 = SetUtils.difference(AC, C2); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        Set<String> D1 = fd(C2, C1, ACwithoutC2);

        // D2 = FD(D1, C2, AC - D1);
        Set<String> ACwithoutD1 = SetUtils.difference(AC, D1); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        Set<String> D2 = fd(D1, C2, ACwithoutD1);

        // return(D1 ∪ D2);
        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(D1, D2);
    }

    //calculate all diagnosis starting from the first diagnosis using FastDiag
    public List<Set<String>> findAllDiagnoses(Set<String> firstDiag, Set<String> C, Set<String> AC)
    {
        List<Set<String>> allDiag = new ArrayList<>();
        allDiag.add(firstDiag); incrementCounter(COUNTER_ADD_OPERATOR);

        diagnoses = new LinkedList<>();
        considerations = new LinkedList<>();

        pushNode(firstDiag, C);

        while (!diagnoses.isEmpty()) {
            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
            exploreNode(allDiag, AC);
        }

        diagnoses = null;
        considerations = null;

        return allDiag;
    }

    Queue<Set<String>> diagnoses;
    Queue<Set<String>> considerations;

    private void popNode(Set<String> node, Set<String> C) {
        incrementCounter(COUNTER_POP_QUEUE, 2);
        node.addAll(diagnoses.remove());
        C.addAll(considerations.remove());
    }

    private void pushNode(Set<String> node, Set<String> C) {
        incrementCounter(COUNTER_PUSH_QUEUE, 2);
        diagnoses.add(node);
        considerations.add(C);
    }

    // Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
    private void exploreNode(List<Set<String>> allDiag, Set<String> AC)
    {
        Set<String> node = new LinkedHashSet<>();
        Set<String> C = new LinkedHashSet<>();
        popNode(node, C);

        List<String> itr = new LinkedList<>(node); incrementCounter(COUNTER_ADD_OPERATOR);

//        for (int i = itr.size() - 1; i >= 0; i--) {
        for (String constraint : itr) {
            Set<String> AConstraint = new LinkedHashSet<>();
            AConstraint.add(constraint);
            incrementCounter(COUNTER_ADD_OPERATOR);

            Set<String> CwithoutAConstraint = SetUtils.difference(C, AConstraint);
            incrementCounter(COUNTER_DIFFERENT_OPERATOR);

            Set<String> diag = findDiagnosis(CwithoutAConstraint, AC);

//            if (!diag.isEmpty() && isMinimal(diag, allDiag) && !allDiag.containsAll(diag))
            if (!diag.isEmpty() && isMinimal(diag, allDiag) && !containsAll(allDiag, diag)) {
                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
                allDiag.add(diag);
                incrementCounter(COUNTER_ADD_OPERATOR);
                pushNode(diag, CwithoutAConstraint);
            }
        }
    }

    private boolean isMinimal(Set<String> diag, List<Set<String>> allDiag) {
        incrementCounter(COUNTER_ISMINIMAL_CALLS);
        for (Set<String> strings : allDiag) {
            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
            if (diag.containsAll(strings)) {
                return false;
            }
        }
        return true;
    }

    private boolean containsAll(List<Set<String>> allDiag, Set<String> diag) {
        for (Set<String> adiag: allDiag) {
            if (adiag.containsAll(diag)) {
                return true;
            }
        }
        return false;
    }
}