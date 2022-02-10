/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.util.Set;

@UtilityClass
@Slf4j
public class Utils {
    // Only for testing
    private void printInfo(Node root, Set<Constraint> conflicts, Set<Constraint> diagnoses) {
        printNode(root);
        log.info("{}conflicts: {}", LoggerUtils.tab, conflicts);
        log.info("{}diagnoses: {}", LoggerUtils.tab, diagnoses);
    }

    private void printNode(Node node) {
        if (node != null) {
            log.info("{}[node={}]", LoggerUtils.tab, node);
            LoggerUtils.indent();

            for (Node child : node.getChildren()) {
                printNode(child);
            }

            LoggerUtils.outdent();
        }
    }
}
