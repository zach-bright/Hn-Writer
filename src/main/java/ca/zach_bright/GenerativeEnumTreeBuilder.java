package ca.zach_bright;

import java.io.IOException;
import java.util.*;

/**
 * Builds an EnumTree by generating an n-ary Huffman code.
 *
 * @author zach-bright
 * @param <E> enum that the EnumTree will use.
 */
public class GenerativeEnumTreeBuilder<E extends Enum<E>> implements EnumTreeBuilder<E> {
    private static String[] KEYS = new String[]{" ", "e", "t", "a", "o", "i", "n", "<bksp>",
        "<enter>", "<shift>", "s", "h", "r", "l", "d", "c", "u", "<caps>", ".", ",", "f",
        "m", "w", "y", "p", "g", "b", "v", "k", "x", "j", "q", "z", "?", ";", "'", "-", "%",
        "$", "<sym>", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"};

    private EnumTree<E> tree = null;
    private Class<E> eClass;

    /**
     * @param eClass class of the enum.
     */
    public GenerativeEnumTreeBuilder(Class<E> eClass) {
        this.eClass = eClass;
    }

    /**
     * Build an EnumTree by generating an n-ary Huffman coding.
     *
     * @return constructed EnumTree.
     * @throws IOException if a problem happens.
     */
    public EnumTree<E> build() throws IOException {
        if (this.tree != null) {
            return this.tree;
        }

        // Get list of enums and their length (m), as well as alphabet size (n).
        E[] enumList = eClass.getEnumConstants();
        int m = enumList.length;
        int n = KEYS.length;

        // Convert key list into forest of one-node trees ordered by root weight.
        Queue<EnumTreeNode<E>> keyForest = new PriorityQueue<>(
                Comparator.comparingInt(EnumTreeNode<E>::getWeight)
        );
        int weight = m;
        for (String key : Arrays.asList(KEYS)) {
            keyForest.add(new EnumTreeNode<>(key, null, this.eClass, weight--));
        }

        // Pick an initial merge count (a). This number is chosen such that
        // each subsequent iteration is guaranteed to take m nodes.
        int a = 2 + (n - 2) % (m - 1);

        // Merge trees until only one is left.
        while (keyForest.size() >= 1) {
            // Create blank 'merger' node.
            EnumTreeNode<E> mergerNode = new EnumTreeNode<>(
                    "",
                    null,
                    this.eClass
            );

            // Remove a-number of trees and add them to merger node.
            // Also get the sum of their weights (this is mergerNode's weight).
            int mergerWeight = 0;
            for (int i = 0; i < a || keyForest.isEmpty(); i++) {
                EnumTreeNode<E> tempNode = keyForest.remove();
                mergerWeight += tempNode.getWeight();
                mergerNode.addChild(tempNode, enumList[i]);
            }

            // Assign weight to new node.
            mergerNode.setWeight(mergerWeight);

            // Add node back to forest.
            keyForest.add(mergerNode);

            a = m;
        }

        // The last tree contains the complete Huffman coding.
        this.tree = new EnumTree<>(this.eClass, keyForest.remove());
        return tree;
    }
}
