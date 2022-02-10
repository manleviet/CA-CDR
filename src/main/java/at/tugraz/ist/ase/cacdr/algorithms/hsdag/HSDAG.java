/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.labeler.IHSLabelable;
import at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.cacdr.checker.ChocoConsistencyChecker;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.map.HashedMap;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static at.tugraz.ist.ase.eval.evaluator.PerformanceEvaluator.start;
import static at.tugraz.ist.ase.eval.evaluator.PerformanceEvaluator.stop;

/**
 * Implementation of the HS-dag algorithm.
 * IHSLabeler algorithms could return labels (conflict or diagnosis) which are not minimal.
 *
 * source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Slf4j
public class HSDAG extends HSTree {
    private Map<Set<Constraint>, List<Node>> conflicts = new HashedMap();
    private Map<Set<Constraint>, Node> nodesLookup = new HashedMap();

    public HSDAG(IHSLabelable labeler, ChocoConsistencyChecker checker) {
        super(labeler, checker);
    }

    @Override
    protected Set<Constraint> computeLabel(Node node) {
        AbstractHSParameters param = node.getParameter();

        start(TIMER_CONFLICT);
        Set<Constraint> cs = getLabeler().getLabel(param);

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

    private void cleanUpNodes(Node node) {
        if (!node.getParent().isEmpty()) {
            return;
        }
    }
}
