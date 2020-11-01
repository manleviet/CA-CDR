/* MBDiagLib - A library for Model-based Diagnosis

 * Copyright (C) 2020-2020  Viet-Man Le
 *
 * Contact: http://ase.ist.tugraz.at/ASE/
 */

package at.tugraz.ist.ase.MBDiag.diagnosis;

import at.tugraz.ist.ase.MBDiag.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.MBDiag.model.DiagnosisModel;
import org.apache.commons.collections4.SetUtils;
import org.chocosolver.solver.constraints.Constraint;

import java.util.*;

// TODO - measurement
//import static at.tugraz.ist.ase.MBDiagLib.measurement.PerformanceMeasurement.*;

/**
 * Implementation of FastDiag algorithm using Set structures.
 *
 * <ul>
 *     <li>Felfernig, Alexander, Monika Schubert, and Christoph Zehentner.
 *     "An efficient diagnosis algorithm for inconsistent constraint sets."
 *     Artificial Intelligence for Engineering Design, Analysis and Manufacturing:
 *     AI EDAM 26.1 (2012): 53.</li>
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
 * // D1 = FD(C1, C2, AC - C1);
 * // D2 = FD(D1, C1, AC - D1);
 * // return(D2 ∪ D1);
 *
 * #08.2020 - Viet-Man Le: using Set structures to store constraints instead of List
 *
 * @author Muslum Atas (muesluem.atas@ist.tugraz.at)
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
public class FastDiagV2 {

    private ChocoConsistencyChecker checker;
    private DiagnosisModel diagModel;

    public FastDiagV2(DiagnosisModel diagModel) {
        this.diagModel = diagModel;

        checker = new ChocoConsistencyChecker(diagModel);
    }

    public Set<Constraint> findDiagnosis(Set<Constraint> C)
    {
//        incrementCounter(COUNTER_FASTDIAG_CALLS);

        Set<Constraint> AC = diagModel.getAllConstraints();
        Set<Constraint> ACwithoutC = SetUtils.difference(AC, C); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);

        // if isEmpty(C) or inconsistent(AC - C) return Φ
        if (C.isEmpty()
//                || (ACwithoutC.isEmpty() && checker.isConsistent(AC, true))
                || !checker.isConsistent(ACwithoutC, false)) {
            return Collections.emptySet();
        }
        else{ // else return FD(Φ, C, AC)
            return fd(Collections.emptySet(), C, AC);
        }
    }

    // func FD(D, C = {c1..cq}, AC) : diagnosis  Δ
    private Set<Constraint> fd(Set<Constraint> D, Set<Constraint> C, Set<Constraint> AC){
        int q = C.size();

        // if D != Φ and consistent(AC) return Φ;
        if( !D.isEmpty() && checker.isConsistent(AC, false)) {
            return Collections.emptySet();
        }
        // if singleton(C) return C;
        if (q == 1) {
            return C;
        }

        int k = q / 2;  // k = q/2;
        // C1 = {c1..ck}; C2 = {ck+1..cq};
        List<Constraint> firstSubList = new ArrayList<>(C).subList(0, k);
        List<Constraint> secondSubList = new ArrayList<>(C).subList(k, q);
        Set<Constraint> C1 = new LinkedHashSet<>(firstSubList);
        Set<Constraint> C2 = new LinkedHashSet<>(secondSubList);
//        incrementCounter(COUNTER_SPLIT_SET);

        // D1 = FD(C1, C2, AC - C1);
        Set<Constraint> ACwithoutC1 = SetUtils.difference(AC, C1); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//        incrementCounter(COUNTER_LEFT_BRANCH_CALLS);
        Set<Constraint> D1 = fd(C1, C2, ACwithoutC1);

        // D2 = FD(D1, C1, AC - D1);
        Set<Constraint> ACwithoutD1 = SetUtils.difference(AC, D1); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);
//        incrementCounter(COUNTER_RIGHT_BRANCH_CALLS);
        Set<Constraint> D2 = fd(D1, C1, ACwithoutD1);

//        incrementCounter(COUNTER_UNION_OPERATOR);
        return SetUtils.union(D2, D1);
    }

    //calculate all diagnosis starting from the first diagnosis using FastDiag
    public List<Set<Constraint>> findAllDiagnoses(Set<Constraint> firstDiag, Set<Constraint> C)
    {
        List<Set<Constraint>> allDiag = new ArrayList<>();
        allDiag.add(firstDiag); //incrementCounter(COUNTER_ADD_OPERATOR);

        diagnoses = new LinkedList<>();
        considerations = new LinkedList<>();

        pushNode(firstDiag, C);

        while (!diagnoses.isEmpty()) {
//            incrementCounter(COUNTER_EXPLORE_NODE_CALLS);
            exploreNode(allDiag);
        }

        diagnoses = null;
        considerations = null;

        return allDiag;
    }

    Queue<Set<Constraint>> diagnoses;
    Queue<Set<Constraint>> considerations;

    private void popNode(Set<Constraint> node, Set<Constraint> C) {
//        incrementCounter(COUNTER_POP_QUEUE, 2);
        node.addAll(diagnoses.remove());
        C.addAll(considerations.remove());
    }

    private void pushNode(Set<Constraint> node, Set<Constraint> C) {
//        incrementCounter(COUNTER_PUSH_QUEUE, 2);
        diagnoses.add(node);
        considerations.add(C);
    }

    //Calculate diagnoses from a node depending on FastDiag (returns children (diagnoses) of a node)
    private void exploreNode(List<Set<Constraint>> allDiag)
    {
        Set<Constraint> node = new LinkedHashSet<>();
        Set<Constraint> C = new LinkedHashSet<>();
        popNode(node, C);

        List<Constraint> itr = new LinkedList<>(node); //incrementCounter(COUNTER_ADD_OPERATOR);

        for (int i = itr.size() - 1; i >= 0; i--) {
            Constraint constraint = (Constraint) itr.get(i);

            Set<Constraint> AConstraint = new LinkedHashSet<>();
            AConstraint.add(constraint); //incrementCounter(COUNTER_ADD_OPERATOR);

            Set<Constraint> CwithoutAConstraint = SetUtils.difference(C, AConstraint); //incrementCounter(COUNTER_DIFFERENT_OPERATOR);

            Set<Constraint> diag = findDiagnosis(CwithoutAConstraint);

            if (!diag.isEmpty() && isMinimal(diag, allDiag) && !allDiag.containsAll(diag))
            {
//                incrementCounter(COUNTER_CONTAINSALL_CHECKS);
                allDiag.add(diag); //incrementCounter(COUNTER_ADD_OPERATOR);
                pushNode(diag, CwithoutAConstraint);
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