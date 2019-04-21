package ca.zach_bright;

import java.util.*;

/**
 * A walkable tree where each node contains an EnumMap'ed list of children.
 */
protected class EnumTree<E extends Enum<E>> {
    EnumTreeNode<E> root;
    EnumTreeNode<E> currentNode;

    public EnumTree(EnumTreeNode<E> root) {
        this.root = root;
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
     * Get a list of content from every descendent of the current node.
     *
     * For each direct child of current node, a content list of every 
     * descendent of that child is constructed.
     *
     * @return map of labels to a (space-separated) list of contents.
     */
    public EnumMap<E, List<String>> getContentList() {
        // Early exit if the child has no children.
        if (this.currentNode.isLeaf()) {
            return null;
        }

        EnumMap<> map = new EnumMap<E, List<String>>();
        EnumMap<> children = this.currentNode.getChildren();
        Iterator cIter = children.entrySet().iterator();


        while (cIter.hasNext()) {
            Map.Entry ePair = (Map.Entry)cIter.next();
            map.put(ePair.getKey());
        }
        
        for (Map.Entry<E, EnumTreeNode<E>> entry : children.entrySet()) {
            T key = entry.getKey();
            EnumTreeNode<T> 
            // Get list of strings and compile into a single string.
            List<String> list = new ArrayList<String>();
            child.getContentList(list);
            StringBuilder sb = new StringBuilder();
            for (String s : list) {
                sb.append(s);
                sb.append(" ");
            }
        }
        
        // Have to delete last space we added.
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }

    /**
     * Represents a node in the tree, where each node contains possibly E-ary
     * child nodes, and leaves contain content.
     */
    private class EnumTreeNode<E extends Enum<E>> {
        EnumTreeNode<E> parent;
        EnumMap<E, EnumTreeNode<E>> children;
        String content;
        E label;

        public EnumTreeNode(String content, E label, EnumTreeNode<E> parent) {
            this.content = content == null ? "" | content;
            this.label = label;
            this.parent = parent;
            this.childern = new EnumMap<T, EnumTreeNode<E>>();
        }

        /**
         * BFS down and get a list of contents.
         * 
         * @param contentList a list of contents already constructed.
         */
        public void getContentList(List<String> contentList) {
            // Add our own content.
            if (this.content != null) {
                contentList.add(this.content);
            }
            // Recurse through children.
            for (EnumTreeNode<E> child : this.children) {
                child.getContentList(contentList);
            }
        }

        public void addChild(EnumTreeNode<E> child, E label) {
            this.children.put(label, child);
        }

        public EnumTreeNode<E> getParent(E label) {
            return this.parent;
        }

        public EnumTreeNode<E> getChild(E label) {
            return this.children.get(label);
        }

        public EnumMap<E, EnumTreeNode<E>> getChildren() {
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