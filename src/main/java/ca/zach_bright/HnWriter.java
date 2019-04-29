package ca.zach_bright;

import java.util.function.Consumer;
import java.io.IOException;
import java.util.*;

/**
 * Wraps an EnumTree with a friendlier interface.
 *
 * @author zach-bright
 */
public class HnWriter<E extends Enum<E>> {
    private EnumTree<E> tree;
    private Consumer<String> callback;
    
    // Char-level variables.
    private boolean isCaps = false;
    private boolean isShift = false;

    // String-level variables.
    private LinkedList<String> history;
    private StringBuilder currentSB;
    private String currentString = "";
    private int historySize = 32;

    /**
     * Construct HnWriter with a tree from the given builder.
     *
     * @param builder   tree builder to use in constructing the EnumTree.
     */
    public HnWriter(EnumTreeBuilder<E> builder) throws IOException {
        this.tree = builder.build();
        this.history = new LinkedList<>();
        this.currentSB = new StringBuilder();
    }

    /**
     * Construct HnWriter with a tree from the given builder, and a callback
     * function that is passed the entered string once user presses enter key.
     *
     * @param builder tree builder to use in constructing the EnumTree.
     * @param callback callback that is passed the constructed string.
     */
    public HnWriter(
            EnumTreeBuilder<E> builder,
            Consumer<String> callback
    ) throws IOException {
        this(builder);
        this.callback = callback;
    }

    /**
     * Walks down the tree, returning new node content.
     *
     * @param direction direction to walk through the tree.
     * @return character value for current node. Only non-null on leaves.
     */
    public char walk(E direction) {
        String content = tree.walkDown(direction);

        // Early-exit for nullchar.
        if (content == "") {
            return '\0';
        }

        // Auto-rewind if we hit a leaf.
        if (tree.currentIsLeaf()) {
            // TODO: add error state for bad tree if non-leaf content
            // TODO: add error state for incomplete tree if no-content leaf
            tree.rewind();
        }

        // Get character representation.
        char charValue;
        if (!content.matches("(<.*>)")) {
            // Check normal character.
            if (this.isCaps || this.isShift) {
                content = content.toUpperCase();
            }

            // Toggle off shift is we applied it.
            // Note that shift doesn't untoggle on special characters.
            if (this.isShift) {
                isShift = false;
            }

            charValue = content.charAt(0);
            this.currentSB.append(charValue);
            this.currentString = this.currentSB.toString();
        } else {
            // Check special characters.
            charValue = this.handleSpecialCharacter(content);
        }

        return charValue;
    }

    /**
     * Turns special char strings to actual chars and handles "action" chars.
     *
     * @param content   special content to handle.
     * @return character representation of content.
     */
    private char handleSpecialCharacter(String content) {
        switch (content) {
            case "<bksp>":
                // Trim last char from sb if possible.
                if (this.currentSB.length() > 1) {
                    this.currentSB.setLength(this.currentSB.length() - 1);
                }
                return '\b';
            case "<enter>":
                // Run callback if present.
                this.callback.accept(this.currentString);

                // Add current str to history and start another.
                this.history.add(currentString);
                this.currentSB.setLength(0);
                this.currentString = "";
                return '\n';
            case "<caps>":
                this.isCaps = !this.isCaps;
                return (char) 20;
            case "<shift>":
                this.isShift = !this.isShift;
                return (char) 16;
            case "<sym>":
                return (char) 114;
            default:
                return '\0';
        }
    }

    public String getHistory(int index) {
        // Access backwards because its a linked list.
        return this.history.get(this.history.size() - index - 1);
    }

    public String[] getHistoryList() {
        return this.history.toArray(new String[0]);
    }

    public String getString() {
        return this.currentString;
    }

    public void setCaps(boolean caps) {
        this.isCaps = caps;
    }

    public boolean nextIsCapitalized() {
        return this.isCaps || this.isShift;
    }

    public boolean isCaps() {
        return this.isCaps;
    }

    public boolean isShift() {
        return this.isShift;
    }

    public EnumMap<E, String> getContentList() {
        return this.tree.getContentList();
    }
}