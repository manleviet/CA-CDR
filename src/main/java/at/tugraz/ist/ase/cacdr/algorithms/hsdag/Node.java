/*
 * Consistency-based Algorithms for Conflict Detection and Resolution
 *
 * Copyright (c) 2022
 *
 * @author: Viet-Man Le (vietman.le@ist.tugraz.at)
 */

package at.tugraz.ist.ase.cacdr.algorithms.hsdag;

import at.tugraz.ist.ase.cacdr.algorithms.hsdag.parameters.AbstractHSParameters;
import at.tugraz.ist.ase.common.LoggerUtils;
import at.tugraz.ist.ase.knowledgebases.core.Constraint;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * A data structure representing a node of an HS-dag.
 *
 * <ul>
 *     <li>R. Reiter, A theory of diagnosis from first principles, Artificial Intelligence,
 *     Volume 32, Issue 1, 1987, pp. 57-95.</li>
 *     <li>R. Greiner, B. A. Smith, and R. W. Wilkerson,
 *     A correction to the algorithm in reiter’s theory of diagnosis, Artif Intell,
 *     vol. 41, no. 1, pp. 79–88, 1989</li>
 * </ul>
 *
 * @author z003pczy (Rosu Mara)
 * @author rtaupe (Richard Taupe) - source: https://github.com/siemens/JMiniZinc
 * @author Dietmar - source: https://github.com/jaccovs/Master-project
 * @author Viet-Man Le (vietman.le@ist.tugraz.at)
 */
@Getter
@NoArgsConstructor
@Slf4j
public class Node {

    private static long generatingNodeId = -1;
    private final long id = ++generatingNodeId;

    /**
     * The tree level
     */
    private int level = 0;

    /**
     * The node status
     */
    @Setter
    private NodeStatus status = NodeStatus.Open;

    /**
     * A label of this node.
     * It can be a minimal conflict set, or a diagnosis. Can be null.
     */
    @Setter
    private Set<Constraint> label;

    @Setter
    private AbstractHSParameters parameter;

    /**
     * This is the constraint associated to the arch which comes to this node.
     * Can be null for the root node.
     */
    private Constraint arcLabel = null;

    /**
     * Labels of the path to here
     */
    private final Set<Constraint> pathLabels = new LinkedHashSet<>();

    /**
     * The node's children
     */
    private final Set<Node> children = new LinkedHashSet<>();

    /**
     * The node's parent. Can be null for the root node.
     */
    private Node parent = null;

    /**
     * Constructor for the root node.
     */
    public static Node createRoot(@NonNull Set<Constraint> label,
                                  @NonNull AbstractHSParameters parameter) {
        generatingNodeId = -1;

        Node root = new Node();
        root.label = label;
        root.parameter = parameter;

        log.trace("{}Created root node with [label={}]", LoggerUtils.tab, label);
        return root;
    }

    /**
     * Constructor for child nodes.
     */
    @Builder
    public Node(@NonNull Node parent,
                @NonNull Constraint arcLabel,
                @NonNull AbstractHSParameters parameter) {
        this.parent = parent;
        this.level = parent.level + 1;
        this.arcLabel = arcLabel;
        this.parameter = parameter;

        this.pathLabels.addAll(parent.pathLabels);
        this.pathLabels.add(arcLabel);

        log.trace("{}Created child node with [parent={}, arcLabel={}]", LoggerUtils.tab, parent, pathLabels);
    }

    /**
     * Adds a child node to this node.
     */
    public void addChild(@NonNull Node child) {
        this.children.add(child);

        log.trace("{}Added child node with [parent={}, child={}]", LoggerUtils.tab, this, child);
    }

    /**
     * Returns isRoot value
     *
     * @return true if this node is the root node, otherwise false.
     */
    public boolean isRoot() {
        return this.parent == null;
    }

    @Override
    public String toString() {
        return "HSDAGNode{" +
                "id=" + id +
                ", level=" + level +
                ", status=" + status +
                ", label=" + label +
                ", parameter=" + parameter +
                ", arcLabel=" + arcLabel +
                ", pathLabels=" + pathLabels +
                '}';
    }
}
