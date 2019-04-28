package ca.zach_bright;

import java.io.IOException;

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

    protected EnumTree<E> tree = null;
    protected Class<E> eClass;

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

        EnumTree<E> tree = new EnumTree<>(this.eClass);
        E[] enumList = eClass.getEnumConstants();

        for (String key : KEYS) {
            // Select k nodes.
            // First, find a = n mod (m-1)
            // Then, find k = a (mod m-1)
        }

        this.tree = tree;
        return tree;
    }
}
