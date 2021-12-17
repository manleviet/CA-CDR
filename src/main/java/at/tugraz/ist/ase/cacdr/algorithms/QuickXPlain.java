/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2021
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;

/**
 * Implementation of QuickXplain algorithm using Set structures.
 *
 * <ul>
 *     <li>"Ulrich Junker"</li>
 * </ul>
 * //QuickXPlain Algorithm
 * //--------------------
 * //QuickXPlain(C={c1,c2,…, cm}, B): CS
 * //IF consistent(B∪C) return "No conflict";
 * //IF isEmpty(C) return Φ;
 * //ELSE return QX(Φ, C, B);
 *
 * //func QX(Δ, C={c1,c2, …, cq}, B): CS
 * //IF (Δ != Φ AND inconsistent(B)) return Φ;
 * //IF singleton(C) return C;
 * //k=n/2;
 * //C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq};
 * //CS1 <-- QX(C2, C1, B ∪ C2);
 * //CS2 <-- QX(CS1, C2, B ∪ CS1);
 * //return (CS1 ∪ CS2)
 *
 * #08.2020 - Viet-Man Le: using Set structures to store constraints instead of List
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class QuickXPlain {

    private final ChocoConsistencyChecker checker;

    public QuickXPlain(@NonNull ChocoConsistencyChecker checker) {
        this.checker = checker;
    }

    /**
     * //QuickXPlain(C={c1,c2,…, cm}, B): CS
     * //IF consistent(B∪C) return "No conflict";
     * //IF isEmpty(C) return Φ;
     * //ELSE return QX(Φ, C, B);
     *
     * @param C a consideration set. Need to inverse the order of the possibly faulty constraint set.
     * @param B a background knowledge
     * @return a conflict set or an empty set
     */
    public Set<String> findConflictSet(@NonNull Set<String> C, @NonNull Set<String> B) {
        Set<String> BwithC = SetUtils.union(B, C); incrementCounter(COUNTER_UNION_OPERATOR);

        //IF (is empty(C) or consistent(B ∪ C)) return Φ
        if (C.isEmpty() || checker.isConsistent(BwithC)) {
            return Collections.emptySet();
        } else { //ELSE return QX(Φ, C, B)
            incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
            start(TIMER_FIRST);
            Set<String> cs = qx(Collections.emptySet(), C, B);
            stop(TIMER_FIRST);

            return cs;
        }
    }

    /**
     * //func QX(Δ, C={c1,c2, …, cq}, B): CS
     * //IF (Δ != Φ AND inconsistent(B)) return Φ;
     * //IF singleton(C) return C;
     * //k = q/2;
     * //C1 <-- {c1, …, ck}; C2 <-- {ck+1, …, cq};
     * //CS1 <-- QX(C2, C1, B ∪ C2);
     * //CS2 <-- QX(CS1, C2, B ∪ CS1);
     * //return (CS1 ∪ CS2)
     *
     * @param D check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a conflict set or an empty set
     */
    private Set<String> qx(Set<String> D, Set<String> C, Set<String> B) {
        log.trace("{}QX(D={}, C={}, B={})", LoggerUtils.tab, D, C, B);
        LoggerUtils.indent();

        //IF (Δ != Φ AND inconsistent(B)) return Φ;
        if ( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (!checker.isConsistent(B)) {
                log.trace("{}return Φ", LoggerUtils.tab);
                LoggerUtils.outdent();
                return Collections.emptySet();
            }
        }

        // if singleton(C) return C;
        int q = C.size();
        if (q == 1) {
            log.trace("{}return C={}", LoggerUtils.tab, C);
            LoggerUtils.outdent();
            return C;
        }

        int k = q / 2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        List<String> firstSubList = new ArrayList<>(C).subList(0, k);
        List<String> secondSubList = new ArrayList<>(C).subList(k, q);
        Set<String> C1 = new LinkedHashSet<>(firstSubList);
        Set<String> C2 = new LinkedHashSet<>(secondSubList);
        incrementCounter(COUNTER_SPLIT_SET);
        log.trace("{}C1={}", LoggerUtils.tab, C1);
        log.trace("{}C2={}", LoggerUtils.tab, C2);

        // CS1 <-- QX(C2, C1, B ∪ C2);
        Set<String> BwithC2 = SetUtils.union(B, C2); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<String> CS1 = qx(C2, C1, BwithC2);

        // CS2 <-- QX(CS1, C2, B ∪ CS1);
        Set<String> BwithCS1 = SetUtils.union(B, CS1); incrementCounter(COUNTER_UNION_OPERATOR);
        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        incrementCounter(COUNTER_QUICKXPLAIN_CALLS);
        Set<String> CS2 = qx(CS1, C2, BwithCS1);

        log.trace("{}return (CS1={} ∪ CS2={})", LoggerUtils.tab, CS1, CS2);
        LoggerUtils.outdent();

        //return (CS1 ∪ CS2)
        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(CS1, CS2);
    }

    public List<Set<String>> findAllConflictSets(@NonNull Set<String> firstConflictSet, @NonNull Set<String> C, @NonNull Set<String> B) {
        List<Set<String>> allConflictSets = new ArrayList<>();
        allConflictSets.add(firstConflictSet); incrementCounter(COUNTER_ADD_OPERATOR);

        conflictsets = new LinkedList<>();
        considerations = new LinkedList<>();

        pushNode(firstConflictSet, C);

        while (!conflictsets.isEmpty())
        {
            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
            exploreNode(allConflictSets, B);
        }

        conflictsets = null;
        considerations = null;

        return allConflictSets;
    }

    Queue<Set<String>> conflictsets;
    Queue<Set<String>> considerations;

    private void popNode(Set<String> node, Set<String> C) {
        node.addAll(conflictsets.remove()); incrementCounter(COUNTER_PUSH_QUEUE);
        C.addAll(considerations.remove()); incrementCounter(COUNTER_PUSH_QUEUE);
    }

    private void pushNode(Set<String> node, Set<String> C) {
        conflictsets.add(node); incrementCounter(COUNTER_POP_QUEUE);
        considerations.add(C); incrementCounter(COUNTER_POP_QUEUE);
    }

    //Calculate all conflict sets depending on QuickXplain
    private void exploreNode(List<Set<String>> allConflictSets, Set<String> B) {
        Set<String> node = new LinkedHashSet<>();
        Set<String> C = new LinkedHashSet<>();
        popNode(node, C);

        log.trace("{}exploreNode(node={}, C={})", LoggerUtils.tab, node, C);
        LoggerUtils.indent();

        List<String> itr = new LinkedList<>(node); incrementCounter(COUNTER_ADD_OPERATOR);

        for (String constraint : itr) {

            Set<String> AConstraint = new LinkedHashSet<>();
            AConstraint.add(constraint); incrementCounter(COUNTER_ADD_OPERATOR);

            Set<String> CwithoutAConstraint = SetUtils.difference(C, AConstraint); incrementCounter(COUNTER_DIFFERENT_OPERATOR);

            Set<String> conflictSet = findConflictSet(CwithoutAConstraint, B);

//            if (!conflictSet.isEmpty() && isMinimal(conflictSet,allConflictSets) && !allConflictSets.containsAll(conflictSet))
            if (!conflictSet.isEmpty() && isMinimal(conflictSet,allConflictSets) && !containsAll(allConflictSets, conflictSet)) {
                incrementCounter(COUNTER_CONTAINSALL_CHECKS);

                allConflictSets.add(conflictSet); incrementCounter(COUNTER_ADD_OPERATOR);
                pushNode(conflictSet, CwithoutAConstraint);
                log.trace("{}pushNode(conflictSet={}, CwithoutAConstraint={})", LoggerUtils.tab, conflictSet, CwithoutAConstraint);
            }
        }

        LoggerUtils.outdent();
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
