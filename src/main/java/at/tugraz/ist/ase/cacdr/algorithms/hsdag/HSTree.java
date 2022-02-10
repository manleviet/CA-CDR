/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import com.google.common.collect.Sets;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

import java.util.*;

import static at.tugraz.ist.ase.cacdr.eval.CAEvaluator.*;
import static at.tugraz.ist.ase.common.ConstraintUtils.*;

/**
 * Implementation of the HS-tree algorithm.
 * IHSDAGLabeler algorithms have to return labels (conflict or diagnosis) which are guaranteed to be minimal.
 * For example: QuickXPlain, MXP, FastDiag
 *
 * source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSTree extends AbstractHSConstructor {

    @Getter
    private Node root = null;
    private final Queue<Node> openNodes = new LinkedList<>();

    public HSTree(IHSLabelable labeler, ChocoConsistencyChecker checker) {
        super(labeler, checker);
    }

    /**
     * Builds the HS-tree.
     */
    public void construct() {
        Set<Constraint> C = getLabeler().getC();
        Set<Constraint> B = getLabeler().getB();

        log.debug("{}Constructing the HS-tree for [C={}, B={}] >>>", LoggerUtils.tab, C, B);
        LoggerUtils.indent();

        start(TIMER_HS_CONSTRUCTION_SESSION);
        start(TIMER_DIAGNOSIS);

        // generate root if there is none
        if (!hasRoot()) {
            start(TIMER_CONFLICT);
            Set<Constraint> cs = getLabeler().getLabel(C);
            stop(TIMER_CONFLICT);

            if (cs.isEmpty()) {
                endConstruction();
                return;
            }

            addConflict(cs); // to reuse conflicts
            log.debug("{}Conflict #{} is found: {}", LoggerUtils.tab, getConflicts().size(), cs);

            // create root node
            root = Node.createRoot(cs, C);
            root.setB(B);
            incrementCounter(COUNTER_CONSTRUCTED_NODES);

            if (stopConstruction()) {
                endConstruction();
                return;
            }

            expand(root);
        }

        while (hasNodesToExpand()) {
            Node node = getNextNode();
            if (skipNode(node)) continue;
            log.trace("{}Processing [node={}]", LoggerUtils.tab, node);
            LoggerUtils.indent();

            label(node);
            if (stopConstruction()) {
                LoggerUtils.outdent();
                endConstruction();
                return;
            }

            if (node.getStatus() == NodeStatus.Open) {
                expand(node);
            }

            System.gc();
            LoggerUtils.outdent();
        }

        endConstruction();
    }

    protected void endConstruction() {
        LoggerUtils.outdent();
        log.debug("{}<<< return [conflicts={}]", LoggerUtils.tab, getConflicts());
        log.debug("{}<<< return [diagnoses={}]", LoggerUtils.tab, getDiagnoses());

        stop(TIMER_HS_CONSTRUCTION_SESSION);
        stop(TIMER_DIAGNOSIS, false);
    }

    protected void label(Node node) {
        if (node.getLabel() == null) {
            // Reusing conflicts - H(node) ∩ S = {}, then label node by S
            Set<Constraint> cs = getReusableConflicts(node);

            // compute conflicts if there are none to reuse
            if (cs == null) {
                cs = computeLabel(node);
            }
            if (cs.isEmpty()) {
                node.setStatus(NodeStatus.Checked);
                Set<Constraint> diag = new LinkedHashSet<>(node.getPathLabels());
                getDiagnoses().add(diag);
                log.debug("{}Diagnosis #{} is found: {}", LoggerUtils.tab, getDiagnoses().size(), node.getPathLabels());

                stop(TIMER_DIAGNOSIS);
                start(TIMER_DIAGNOSIS);
            }
            node.setLabel(cs);
        }
    }

    protected Set<Constraint> getReusableConflicts(Node node) {
        for (Set<Constraint> conflict : getConflicts()) {
            // H(node) ∩ S = {}
            if (!hasIntersection(node.getPathLabels(), conflict)) {
                incrementCounter(COUNTER_REUSE);
                log.trace("{}Reuse [conflict={}, node={}]", LoggerUtils.tab, conflict, node);
                return conflict;
            }
        }
        return null;
    }

    protected Set<Constraint> computeLabel(Node node) {
        Set<Constraint> C = node.getC();

        start(TIMER_CONFLICT);
        Set<Constraint> cs = getLabeler().getLabel(C);

        if (!cs.isEmpty()) {
            stop(TIMER_CONFLICT);
            addConflict(cs);
            log.debug("{}Conflict #{} is found: {}", LoggerUtils.tab, getConflicts().size(), cs);
        } else {
            // stop TIMER_CONFLICT without saving the time
            stop(TIMER_CONFLICT, false);
        }
        return cs;
    }

    protected void addConflict(Set<Constraint> cs) {
        getConflicts().add(cs);
    }

    protected boolean hasNodesToExpand() {
        return !openNodes.isEmpty();
    }

    protected Node getNextNode() {
        return openNodes.remove();
    }

    protected boolean skipNode(Node node) {
        boolean condition1 = getMaxDepth() != 0 && getMaxDepth() <= node.getLevel();
        return node.getStatus() != NodeStatus.Open || condition1 || canPrune(node);
    }

    protected void expand(Node nodeToExpand) {
        log.trace("{}Generating the children nodes of [node={}]", LoggerUtils.tab, nodeToExpand);
        LoggerUtils.indent();

        for (Constraint label : nodeToExpand.getLabel()) {
            Set<Constraint> C = new LinkedHashSet<>(nodeToExpand.getC());
            C.remove(label);

            Set<Constraint> B = new LinkedHashSet<>(nodeToExpand.getB());
//            B.add(label);

            Node node = Node.builder()
                    .parent(nodeToExpand)
                    .C(C)
                    .arcLabel(label)
                    .build();
            node.setB(B);
            nodeToExpand.addChild(node);
            incrementCounter(COUNTER_CONSTRUCTED_NODES);

            if (!canPrune(node)) {
                openNodes.add(node);
            }
        }

        LoggerUtils.outdent();
    }

    protected boolean canPrune(Node node) {
        // 3.i - if n is checked, and n' is such that H(n) ⊆ H(n'), then close the node n'
        // n is a diagnosis
        for (Set<Constraint> diag : getDiagnoses()) {
            if (node.getPathLabels().containsAll(diag)) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_1);

                log.trace("{}Closed [node={}]", LoggerUtils.tab, node);

                return true;
            }
        }

        // 3.ii - if n has been generated and node n' is such that H(n') = H(n), then close node n'
        for (Node n : openNodes) {
            if (n.getPathLabels().size() == node.getPathLabels().size()
                    && Sets.difference(n.getPathLabels(), node.getPathLabels()).isEmpty()) {
                node.setStatus(NodeStatus.Closed);
                incrementCounter(COUNTER_CLOSE_2);

                log.trace("{}Closed [node={}]", LoggerUtils.tab, node);

                return true;
            }
        }

        return false;
    }

    protected boolean hasRoot() {
        return this.root != null;
    }

    @Override
    public void dispose() {
        super.dispose();
        this.root = null;
        this.openNodes.clear();
    }
}
