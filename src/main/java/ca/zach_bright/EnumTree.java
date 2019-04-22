package ca.zach_bright;

import java.util.*;

/**
 * A walkable tree where each node contains an EnumMap'ed list of children.
 *
 * @author zach-bright
 */
class EnumTree<E extends Enum<E>> {
    private EnumTreeNode root;
    private EnumTreeNode currentNode;
    private Class<E> eClass;

    public EnumTree(Class<E> eClass) {
        this.eClass = eClass;
        this.root = new EnumTreeNode(null, null, null);
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
            EnumTreeNode newCurrent = this.currentNode.getChild(label);
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
        EnumTreeNode parent = this.currentNode.getParent();
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
     * Get the enum (E) for this tree.
     *
     * @return the enum this tree uses.
     */
    public Class<E> getEnumClass() {
        return eClass;
    }

    /**
     * Follows a path down from root, creating nodes as necessary, and places  
     * a new node containing the provided content at the last place.
     * 
     * @param content   what to write in new node.
     * @param labelPath path to the new node.
     */
    public void addNodeToPath(String content, List<E> labelPath) {
        EnumTreeNode tempNode = root;
        
        // Traverse down (making new nodes as necessary) to 2nd-last label.
        for (int i = 0; i < labelPath.size() - 1; i++) {
            E label = labelPath.get(i);
            EnumTreeNode nextNode = tempNode.getChild(label);
            
            // Create blank nodes if necessary.
            if (nextNode == null) {
                nextNode = new EnumTreeNode(null, label, tempNode);
                currentNode.addChild(nextNode, label);
            }
            
            tempNode = nextNode;
        }
      
        // Create new node and add it.
        E lastLabel = labelPath.get(labelPath.size() - 1);
        EnumTreeNode newNode = 
            new EnumTreeNode(content, lastLabel, tempNode);
        tempNode.addChild(newNode, lastLabel);
    }

    /**
     * Get a list of content from every descendent of the current node.
     *
     * For each direct child of current node, a content list of every 
     * descendent of that child is constructed.
     *
     * @return map of labels to a (space-separated) list of contents.
     */
    public EnumMap<E, String> getContentList() {
        // Early exit if the child has no children.
        if (this.currentNode.isLeaf()) {
            return null;
        }

        EnumMap<E, String> contentMap = new EnumMap<E, String>(eClass);
        EnumMap<E, EnumTreeNode> children = this.currentNode.getChildren();
        
        // Iterate over children map and write to content map.
        for (Map.Entry<E, EnumTreeNode> entry : children.entrySet()) {
            E key = entry.getKey();
            EnumTreeNode child = entry.getValue();

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
     * Represents a node in the tree, where each node contains possibly E-ary
     * child nodes, and leaves contain content.
     *
     * @author zach-bright
     */
    private class EnumTreeNode {
        protected EnumTreeNode parent;
        protected EnumMap<E, EnumTreeNode> children;
        protected String content;
        protected E label;

        public EnumTreeNode(String content, E label, EnumTreeNode parent) {
            this.content = content == null ? "" : content;
            this.label = label;
            this.parent = parent;
            this.children = 
                new EnumMap<E, EnumTreeNode>(EnumTree.this.getEnumClass());
        }

        /**
         * BFS down and get a list of contents.
         * 
         * @param sb    a builder to add content to.
         */
        public void getContentList(StringBuilder sb) {
            // Add our own content.
            if (this.content != null) {
                sb.append(this.content);
                sb.append(" ");
            }
            // Recurse through children.
            for (EnumTreeNode child : this.children.values()) {
                child.getContentList(sb);
            }
        }

        public void addChild(EnumTreeNode child, E label) {
            this.children.put(label, child);
        }

        public EnumTreeNode getParent() {
            return this.parent;
        }

        public EnumTreeNode getChild(E label) {
            return this.children.get(label);
        }

        public EnumMap<E, EnumTreeNode> getChildren() {
            return this.children;
        }

        public String getContent() {
            return this.content;
        }

        public boolean isLeaf() {
            return this.children.isEmpty();
        }
    }
}
