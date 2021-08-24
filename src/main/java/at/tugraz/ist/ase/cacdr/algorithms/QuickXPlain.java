package at.tugraz.ist.ase.cacdr.algorithms;

import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import org.apache.commons.collections4.SetUtils;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.Evaluation.*;

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
 * //return (CS2 ∪ CS1)
 *
 * #08.2020 - Viet-Man Le: using Set structures to store constraints instead of List
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class QuickXPlain {

    private ChocoConsistencyChecker checker;

    public QuickXPlain(ChocoConsistencyChecker checker) {
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
    public Set<String> findConflictSet(Set<String> C, Set<String> B)
    {
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
     * //return (CS2 ∪ CS1)
     *
     * @param D check to skip redundant consistency checks
     * @param C a consideration set of constraints
     * @param B a background knowledge
     * @return a conflict set or an empty set
     */
    private Set<String> qx(Set<String> D, Set<String> C, Set<String> B){
        //IF (Δ != Φ AND inconsistent(B)) return Φ;
        if ( !D.isEmpty() ) {
            incrementCounter(COUNTER_CONSISTENCY_CHECKS);
            if (!checker.isConsistent(B)) {
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

        //return (CS2 ∪ CS1)
        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(CS2, CS1);
    }

//    public List<Set<Constraint>> findAllConflictSets(Set<Constraint> firstConflictSet, Set<Constraint> C)
//    {
//        List<Set<Constraint>> allConflictSets = new ArrayList<>();
//        allConflictSets.add(firstConflictSet); incrementCounter(COUNTER_ADD_OPERATOR);
//
//        conflictsets = new LinkedList<>();
//        considerations = new LinkedList<>();
//
//        pushNode(firstConflictSet, C);
//
//        while (!conflictsets.isEmpty())
//        {
//            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
//            exploreNode(allConflictSets);
//        }
//
//        conflictsets = null;
//        considerations = null;
//
//        return allConflictSets;
//    }
//
//    Queue<Set<Constraint>> conflictsets;
//    Queue<Set<Constraint>> considerations;
//
//    private void popNode(Set<Constraint> node, Set<Constraint> C) {
//        node.addAll(conflictsets.remove()); incrementCounter(COUNTER_PUSH_QUEUE);
//        C.addAll(considerations.remove()); incrementCounter(COUNTER_PUSH_QUEUE);
//    }
//
//    private void pushNode(Set<Constraint> node, Set<Constraint> C) {
//        conflictsets.add(node); incrementCounter(COUNTER_POP_QUEUE);
//        considerations.add(C); incrementCounter(COUNTER_POP_QUEUE);
//    }
//
//    //Calculate all conflict sets depending on QuickXplain
//    private void exploreNode(List<Set<Constraint>> allConflictSets)
//    {
//        Set<Constraint> node = new LinkedHashSet<>();
//        Set<Constraint> C = new LinkedHashSet<>();
//        popNode(node, C);
//
//        Iterator itr = IteratorUtils.getIterator(node);
//        while (itr.hasNext()) {
//            Constraint constraint = (Constraint) itr.next();
//
//            Set<Constraint> AConstraint = new LinkedHashSet<>();
//            AConstraint.add(constraint); incrementCounter(COUNTER_ADD_OPERATOR);
//
//            Set<Constraint> CwithoutAConstraint = SetUtils.difference(C, AConstraint); incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//
//            Set<Constraint> conflictSet = findConflictSet(CwithoutAConstraint);
//
//            if (!conflictSet.isEmpty() && isMinimal(conflictSet,allConflictSets) && !allConflictSets.containsAll(conflictSet))
//            {
//                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//
//                allConflictSets.add(conflictSet); incrementCounter(COUNTER_ADD_OPERATOR);
//                pushNode(conflictSet, CwithoutAConstraint);
//            }
//        }
//    }
//
//    private boolean isMinimal(Set<Constraint> diag, List<Set<Constraint>> allDiag)
//    {
//        incrementCounter(COUNTER_ISMINIMAL_CALLS);
//        for (int i = 0; i < allDiag.size(); i++)
//        {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
//            if (diag.containsAll(allDiag.get(i))) {
//                return false;
//            }
//        }
//
//        return true;
//    }
}
