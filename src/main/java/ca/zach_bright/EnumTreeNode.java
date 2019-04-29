package ca.zach_bright;

import java.io.IOException;
import java.util.EnumMap;

/**
 * Represents a node in the tree, where each node contains possibly E-ary
 * child nodes, and leaves contain content.
 *
 * @author zach-bright
 * @param <E> enum used for node labeling.
 */
class EnumTreeNode<E extends Enum<E>> {
    private EnumTreeNode<E> parent;
    private EnumMap<E, EnumTreeNode<E>> children;
    private String content;
    private int weight;

    /**
     * Create unweighted node.
     *
     * @param content node content.
     * @param parent parent node.
     * @param eClass concrete class for EnumMap.
     */
    EnumTreeNode(String content, EnumTreeNode<E> parent, Class<E> eClass) {
        this.content = content == null ? "" : content;
        this.parent = parent;
        this.children = new EnumMap<>(eClass);
    }

    /**
     * Create weighted node.
     *
     * @param content node content.
     * @param parent parent node.
     * @param eClass concrete class for EnumMap.
     * @param weight node connection weight.
     */
    EnumTreeNode(
            String content,
            EnumTreeNode<E> parent,
            Class<E> eClass,
            int weight
    ) {
        this(content, parent, eClass);
        this.weight = weight;
    }

    /**
     * BFS down and get a list of contents.
     *
     * @param sb a builder to add content to.
     */
    void getContentList(StringBuilder sb) {
        // Add our own content.
        if (!this.content.equals("")) {
            sb.append(this.content);
            sb.append(" ");
        }
        // Recurse through children.
        for (EnumTreeNode<E> child : this.children.values()) {
            child.getContentList(sb);
        }
    }

    /**
     * Add node to children map.
     *
     * @param child child node to add.
     * @param label label for new child.
     * @throws IOException if node already has child with label.
     */
    void addChild(EnumTreeNode<E> child, E label) throws IOException {
        if (this.getChild(label) == null) {
            throw new IOException("Node with content " + child.getContent()
                    + " already present with label.");
        }
        this.children.put(label, child);
    }

    EnumTreeNode<E> getParent() {
        return this.parent;
    }

    EnumTreeNode<E> getChild(E label) {
        return this.children.get(label);
    }

    EnumMap<E, EnumTreeNode<E>> getChildren() {
        return this.children;
    }

    String getContent() {
        return this.content;
    }

    boolean isLeaf() {
        return this.children.isEmpty();
    }

    int getWeight() {
        return this.weight;
    }

    void setWeight(int weight) {
        this.weight = weight;
    }
}
