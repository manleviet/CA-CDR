/* MBDiagLib - A library for Model-based Diagnosis

 * Copyright (C) 2020-2020  Viet-Man Le
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiag.diagnosis;

import at.tugraz.ist.ase.MBDiag.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.MBDiag.model.DiagnosisModel;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.SetUtils;
import org.chocosolver.solver.constraints.Constraint;

import java.util.*;

// TODO - measurement
//import static at.tugraz.ist.ase.MBDiag.measurement.PerformanceMeasurement.*;

/**
 * Implementation for a MSS-based FastDiag algorithm.
 * MSS - Maximal Satisfiable Set
 *
 * // FastDiag Algorithm
 * //--------------------
 * // B: correctConstraints (background knowledge)
 * // C: possiblyFaultyConstraints
 * //--------------------
 * // Func FastDiag(C, B) : Δ
 * // if isEmpty(C) or consistent(B U C) return Φ
 * // else return C - FD(C, B, Φ)
 *
 * // Func FD(C = {c1..cn}, B, Δ) : MSS
 * // if Δ != Φ and consistent(B U C) return C;
 * // if singleton(C) return Φ;
 * // k = n/2;
 * // C1 = {c1..ck}; C2 = {ck+1..cn};
 * // Δ2 = FD(C1, B, C2);
 * // Δ1 = FD(C2, B U Δ2, C1 - Δ2);
 * // return Δ1 ∪ Δ2;
 *
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */

// TODO - add parallel to this class
public class FastDiagV3 {

    private ChocoConsistencyChecker checker;
    private DiagnosisModel diagModel;

    public FastDiagV3(DiagnosisModel diagModel) {
        this.diagModel = diagModel;

        checker = new ChocoConsistencyChecker(diagModel);
    }

    /**
     *  // Func FastDiag(C, B) : Δ
     *  // if isEmpty(C) or consistent(B U C) return Φ
     *  // else return C - FD(C, B, Φ)
     *
     * @param C
     * @return
     */
    public Set<Constraint> findDiagnosis(Set<Constraint> C)
    {
//        incrementCounter(COUNTER_FASTDIAG_CALLS);

        Set<Constraint> B = diagModel.getCorrectConstraints();
        Set<Constraint> BwithC = SetUtils.union(B, C); //incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        if (C.isEmpty()
                || checker.isConsistent(BwithC, false)) {
            return Collections.emptySet();
        }
        else{ // else return C \ FD(C, B, Φ)
//            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            return SetUtils.difference(C, fd(C, B, Collections.emptySet()));
        }
    }

    private Set<Constraint> findDiagnosis(Set<Constraint> C, Set<Constraint> B)
    {
//        incrementCounter(COUNTER_FASTDIAG_CALLS);
        Set<Constraint> BwithC = SetUtils.union(B, C); //incrementCounter(COUNTER_UNION_OPERATOR);

        // if isEmpty(C) or consistent(B U C) return Φ
        if (C.size() <= 1
                || !checker.isConsistent(B, false)
                || checker.isConsistent(BwithC, false)) {
            return Collections.emptySet();
        }
        else{ // else return C - FD(C, B, Φ)
//            incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            Set<Constraint> diag = SetUtils.difference(C, fd(C, B, Collections.emptySet()));

//            incrementCounter(COUNTER_UNION_OPERATOR);
            Set<Constraint> BackgroundwithDiag = SetUtils.union(diagModel.getCorrectConstraints(), diag);
            if (checker.isConsistent(BackgroundwithDiag, false)) {
                return diag;
            }
            else {
                return Collections.emptySet();
            }
        }
    }

    /**
     * // Func FD(C = {c1..cn}, B, Δ) : MSS
     * // if Δ != Φ and consistent(B U C) return C;
     * // if singleton(C) return Φ;
     * // k = n/2;
     * // C1 = {c1..ck}; C2 = {ck+1..cn};
     * // Δ2 = FD(C1, B, C2);
     * // Δ1 = FD(C2, B U Δ2, C1 - Δ2);
     * // return Δ1 ∪ Δ2;
     *
     * @return
     */
    private Set<Constraint> fd(Set<Constraint> C, Set<Constraint> B, Set<Constraint> Δ){
        // if Δ != Φ and consistent(B U C) return C;
        if( !Δ.isEmpty()) {
            Set<Constraint> BwithC = SetUtils.union(B, C); //incrementCounter(COUNTER_UNION_OPERATOR);
            if (checker.isConsistent(BwithC, false)) {
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
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, n);
        Set<Constraint> C1 = new LinkedHashSet<>(firstSubList);
        Set<Constraint> C2 = new LinkedHashSet<>(secondSubList);
//        incrementCounter(COUNTER_SPLIT_SET);

        // Δ2 = FD(C1, B, C2);
//        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        Set<Constraint> Δ2 = fd(C1, B, C2);

        // Δ1 = FD(C2, B U Δ2, C1 - Δ2);
        Set<Constraint> BwithΔ2 = SetUtils.union(Δ2, B); //incrementCounter(COUNTER_UNION_OPERATOR);
        Set<Constraint> C1withoutΔ2 = SetUtils.difference(C1, Δ2); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        Set<Constraint> Δ1 = fd(C2, BwithΔ2, C1withoutΔ2);

//        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(Δ1, Δ2);
    }

    //calculate all diagnosis starting from the first diagnosis using FastDiag
    public List<Set<Constraint>> findAllDiagnoses(Set<Constraint> firstDiag, Set<Constraint> C)
    {
        Set<Constraint> B = diagModel.getCorrectConstraints();

        List<Set<Constraint>> allDiag = new ArrayList<>();
        allDiag.add(firstDiag); //incrementCounter(COUNTER_ADD_OPERATOR);

        diagnoses = new LinkedList<>();
        considerations = new LinkedList<>();
        background = new LinkedList<>();

        pushNode(firstDiag, C, B);

        while (!diagnoses.isEmpty()) {
//            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
            exploreNode(allDiag);
        }

        diagnoses = null;
        considerations = null;
        background = null;

        return allDiag;
    }

    Queue<Set<Constraint>> diagnoses;
    Queue<Set<Constraint>> considerations;
    Queue<Set<Constraint>> background;

    private void popNode(Set<Constraint> node, Set<Constraint> C, Set<Constraint> B) {
        node.addAll(diagnoses.remove());
        C.addAll(considerations.remove());
        B.addAll(background.remove());
//        incrementCounter(COUNTER_POP_QUEUE, 3);
    }

    private void pushNode(Set<Constraint> node, Set<Constraint> C, Set<Constraint> B) {
        diagnoses.add(node);
        considerations.add(C);
        background.add(B);
//        incrementCounter(COUNTER_PUSH_QUEUE, 3);
    }

    //Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
    public void exploreNode(List<Set<Constraint>> allDiag)
    {
        Set<Constraint> node = new LinkedHashSet<>();
        Set<Constraint> C = new LinkedHashSet<>();
        Set<Constraint> B = new LinkedHashSet<>();
        popNode(node, C, B);

        Iterator itr = IteratorUtils.getIterator(node);
        while (itr.hasNext()) {
            Constraint constraint = (Constraint) itr.next();

            Set<Constraint> AConstraint = new LinkedHashSet<>();
            AConstraint.add(constraint); //incrementCounter(COUNTER_ADD_OPERATOR);

            Set<Constraint> CwithoutAConstraint = SetUtils.difference(C, AConstraint); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
            Set<Constraint> BwithAConstraint = SetUtils.union(B, AConstraint); //incrementCounter(COUNTER_UNION_OPERATOR);

            Set<Constraint> diag = findDiagnosis(CwithoutAConstraint, BwithAConstraint);

            if (!diag.isEmpty() && isMinimal(diag, allDiag) && !allDiag.containsAll(diag))
            {
//                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
                allDiag.add(diag); //incrementCounter(COUNTER_ADD_OPERATOR);
                pushNode(diag, CwithoutAConstraint, BwithAConstraint);
            }
        }
    }

    private boolean isMinimal(Set<Constraint> diag, List<Set<Constraint>> allDiag)
    {
//        incrementCounter(COUNTER_ISMINIMAL_CALLS);
        for (int i = 0; i < allDiag.size(); i++)
        {
//            incrementCounter(COUNTER_CONTAINSALL_CHECKS);
            if (diag.containsAll(allDiag.get(i))) {
                return false;
            }
        }

        return true;
    }
}