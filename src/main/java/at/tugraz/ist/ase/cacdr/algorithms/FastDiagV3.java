/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.*;

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
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FastDiagV3 {

    private final ChocoConsistencyChecker checker;

    public FastDiagV3(ChocoConsistencyChecker checker) {
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
    public Set<String> findDiagnosis(Set<String> C, Set<String> B)
    {
        Set<String> BwithC = SetUtils.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        if (C.isEmpty()
                || checker.isConsistent(BwithC)) {
            return Collections.emptySet();
        } else{ // else return C \ FD(C, B, Φ)
            incrementCounter(COUNTER_FASTDIAG_CALLS);
            start(TIMER_FIRST);
            Set<String> mss = fd(Collections.emptySet(), C, B);
            stop(TIMER_FIRST);

            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            return SetUtils.difference(C, mss);
        }
    }

//    private Set<String> findDiagnosis(Set<String> C, Set<String> B)
//    {
////        incrementCounter(COUNTER_FASTDIAG_CALLS);
//        Set<String> BwithC = SetUtils.union(B, C); //incrementCounter(COUNTER_UNION_OPERATOR);
//
//        // if isEmpty(C) or consistent(B U C) return Φ
//        if (C.size() <= 1
//                || !checker.isConsistent(B)
//                || checker.isConsistent(BwithC)) {
//            return Collections.emptySet();
//        }
//        else{ // else return C - FD(C, B, Φ)
////            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//            Set<Constraint> diag = SetUtils.difference(C, fd(C, B, Collections.emptySet()));
//
////            incrementCounter(COUNTER_UNION_OPERATOR);
//            Set<String> BackgroundwithDiag = SetUtils.union(diagModel.getCorrectConstraints(), diag);
//            if (checker.isConsistent(BackgroundwithDiag, true)) {
//                return diag;
//            }
//            else {
//                return Collections.emptySet();
//            }
//        }
//    }

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
    private Set<String> fd(Set<String> Δ, Set<String> C, Set<String> B) {
        // if Δ != Φ and consistent(B U C) return C;
        if( !Δ.isEmpty()) {
            Set<String> BwithC = SetUtils.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (checker.isConsistent(BwithC)) {
                return C;
            }
        }

        // if singleton(C) return Φ;
        int n = C.size();
        if (n == 1) {
            return Collections.emptySet();
        }

        int k = n / 2;  // k = n/2;
        // C1 = {c1..ck}; C2 = {ck+1..cn};
        List<String> firstSubList = new ArrayList<>(C).subList(0, k);
        List<String> secondSubList = new ArrayList<>(C).subList(k, n);
        Set<String> C1 = new LinkedHashSet<>(firstSubList);
        Set<String> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);

        // Δ2 = FD(C2, C1, B);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        Set<String> Δ2 = fd(C2, C1, B);

        // Δ1 = FD(C1 - Δ2, C2, B U Δ2);
        Set<String> BwithΔ2 = SetUtils.union(Δ2, B); incrementCounter(COUNTER_UNION_OPERATOR);
        Set<String> C1withoutΔ2 = SetUtils.difference(C1, Δ2); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_FASTDIAG_CALLS);
        Set<String> Δ1 = fd(C1withoutΔ2, C2, BwithΔ2);

        // return Δ1 ∪ Δ2;
        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(Δ1, Δ2);
    }

    //calculate all diagnosis starting from the first diagnosis using FastDiag
//    public List<Set<String>> findAllDiagnoses(Set<String> firstDiag, Set<String> C)
//    {
//        Set<String> B = diagModel.getCorrectConstraints();
//
//        List<Set<String>> allDiag = new ArrayList<>();
//        allDiag.add(firstDiag); //incrementCounter(COUNTER_ADD_OPERATOR);
//
//        diagnoses = new LinkedList<>();
//        considerations = new LinkedList<>();
//        background = new LinkedList<>();
//
//        pushNode(firstDiag, C, B);
//
//        while (!diagnoses.isEmpty()) {
////            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
//            exploreNode(allDiag);
//        }
//
//        diagnoses = null;
//        considerations = null;
//        background = null;
//
//        return allDiag;
//    }
//
//    Queue<Set<String>> diagnoses;
//    Queue<Set<String>> considerations;
//    Queue<Set<String>> background;
//
//    private void popNode(Set<String> node, Set<String> C, Set<String> B) {
//        node.addAll(diagnoses.remove());
//        C.addAll(considerations.remove());
//        B.addAll(background.remove());
////        incrementCounter(COUNTER_POP_QUEUE, 3);
//    }
//
//    private void pushNode(Set<String> node, Set<String> C, Set<String> B) {
//        diagnoses.add(node);
//        considerations.add(C);
//        background.add(B);
////        incrementCounter(COUNTER_PUSH_QUEUE, 3);
//    }
//
//    //Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
//    public void exploreNode(List<Set<String>> allDiag)
//    {
//        Set<String> node = new LinkedHashSet<>();
//        Set<String> C = new LinkedHashSet<>();
//        Set<String> B = new LinkedHashSet<>();
//        popNode(node, C, B);
//
////        List<Constraint> itr = new LinkedList<>(node); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        Iterator<?> itr = IteratorUtils.getIterator(node);
//        while (itr.hasNext()) {
//            String constraint = (String) itr.next();
////        for (int i = itr.size() - 1; i >= 0; i--) {
////            Constraint constraint = (Constraint) itr.get(i);
//
//            Set<String> AConstraint = new LinkedHashSet<>();
//            AConstraint.add(constraint); //incrementCounter(COUNTER_ADD_OPERATOR);
//
//            Set<String> CwithoutAConstraint = SetUtils.difference(C, AConstraint); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//            Set<String> BwithAConstraint = SetUtils.union(B, AConstraint); //incrementCounter(COUNTER_UNION_OPERATOR);
//
//            Set<String> diag = findDiagnosis(CwithoutAConstraint, BwithAConstraint);
//
//            if (!diag.isEmpty() && isMinimal(diag, allDiag) && !allDiag.containsAll(diag))
//            {
////                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//                allDiag.add(diag); //incrementCounter(COUNTER_ADD_OPERATOR);
//                pushNode(diag, CwithoutAConstraint, BwithAConstraint);
//            }
//        }
//    }
//
//    private boolean isMinimal(Set<String> diag, List<Set<String>> allDiag)
//    {
////        incrementCounter(COUNTER_ISMINIMAL_CALLS);
//        for (Set<String> constraints : allDiag) {
////            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//            if (diag.containsAll(constraints)) {
//                return false;
//            }
//        }
//
//        return true;
//    }
}