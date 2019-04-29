package ca.zach_bright;

import java.io.IOException;
import java.util.*;

/**
 * A walkable tree where each node contains an EnumMap'ed list of children.
 *
 * @author zach-bright
 */
public class EnumTree<E extends Enum<E>> {
    private EnumTreeNode<E> root;
    private EnumTreeNode<E> currentNode;
    private Class<E> eClass;

    public EnumTree(Class<E> eClass) {
        this.eClass = eClass;
        this.root = new EnumTreeNode<>(null, null, eClass);
        this.currentNode = root;
    }

    /**
     * Updates current node to it's child with the specified label.
     * 
     * @param label label to walk to.
     * @return contents of the new current node. May be empty.
     */
    public String walkDown(E label) {
        if (!this.currentNode.isLeaf()) {
            // Try to walk to new node.
            EnumTreeNode<E> newCurrent = this.currentNode.getChild(label);
            if (newCurrent != null) {
                // Only update if we actually had a valid node.
                this.currentNode = newCurrent;
            }
        }

        return this.currentNode.getContent();
    }

    /**
     * Updates current node to it's parent.
     */
    public void walkUp() {
        // Avoid null currentNode for root.
        EnumTreeNode<E> parent = this.currentNode.getParent();
        this.currentNode = parent == null ? root: parent;
    }

    /**
     * Updates current node to the root node.
     */
    public void rewind() {
        this.currentNode = this.root;
    }

    /**
     * Check if current node is leaf node.
     *
     * @return true if node is leaf.
     */
    public boolean currentIsLeaf() {
        return this.currentNode.isLeaf();
    }

    /**
     * Follows a path down from root, creating nodes as necessary, and places  
     * a new node containing the provided content at the last place.
     * 
     * @param content what to write in new node.
     * @param labelPath path to the new node.
     */
    public void addNodeToPath(String content, List<E> labelPath) throws IOException {
        EnumTreeNode<E> tempNode = root;
        
        // Traverse down (making new nodes as necessary) to 2nd-last label.
        for (int i = 0; i < labelPath.size() - 1; i++) {
            E label = labelPath.get(i);
            EnumTreeNode<E> nextNode = tempNode.getChild(label);
            
            // Create blank nodes if necessary.
            if (nextNode == null) {
                nextNode = new EnumTreeNode<>("", tempNode, this.eClass);
                tempNode.addChild(nextNode, label);
            }
            
            tempNode = nextNode;
        }

        // Create new node and add it.
        E lastLabel = labelPath.get(labelPath.size() - 1);
        EnumTreeNode<E> newNode =
            new EnumTreeNode<>(content, tempNode, this.eClass);
        tempNode.addChild(newNode, lastLabel);
    }

    /**
     * Get a list of content from every descendant of the current node.
     *
     * For each direct child of current node, a content list of every 
     * descendant of that child is constructed.
     *
     * @return map of labels to a (space-separated) list of contents.
     */
    public EnumMap<E, String> getContentList() {
        EnumMap<E, String> contentMap = new EnumMap<>(eClass);

        // Early exit if the child has no children.
        if (this.currentNode.isLeaf()) {
            return contentMap;
        }

        EnumMap<E, EnumTreeNode<E>> children = this.currentNode.getChildren();
        
        // Iterate over children map and write to content map.
        for (Map.Entry<E, EnumTreeNode<E>> entry : children.entrySet()) {
            E key = entry.getKey();
            EnumTreeNode<E> child = entry.getValue();

            // Get string for all the children.
            StringBuilder sb = new StringBuilder();
            child.getContentList(sb);

            // Have to delete last space we added.
            sb.deleteCharAt(sb.length() - 1);

            // Now add to content map.
            contentMap.put(key, sb.toString());
        }

        return contentMap;
    }

    /**
     * Add new child to current node.
     *
     * @param content content for new node.
     * @param label enum label for new node.
     * @throws IOException if current node already has child with label.
     */
    public void addChildToCurrent(String content, E label) throws IOException {
        EnumTreeNode<E> newNode = new EnumTreeNode<>(content, this.currentNode, this.eClass);
        this.currentNode.addChild(newNode, label);
    }
}
