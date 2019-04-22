package ca.zach_bright;

import java.io.IOException;
import java.util.*;

/**
 * Wraps the EnumTree with a more friendly interface.
 *
 * @author zach-bright
 */
public class HnTree<E extends Enum<E>> {
    private EnumTree<E> tree;
    private LinkedList<String> buffer;
    private int bufferSize;
    private boolean isCaps = false;
    private boolean isShift = false;

    /**
     * Construct HnTree with user-specfied buffer size.
     *
     * @param builder   tree builder to use in constructing the EnumTree.
     * @param buffSize  size of buffer that holds old inputs.
     */
    public HnTree(EnumTreeBuilder<E> builder, int buffSize) throws IOException {
        this.tree = builder.build();
        this.buffer = new LinkedList<String>();
        this.bufferSize = buffSize;
    }

    /**
     * Construct HnTree with default buffer size of 32.
     *
     * @param builder   tree builder to use in constructing the EnumTree.
     */
    public HnTree(EnumTreeBuilder<E> builder) throws IOException {
        this(builder, 32);
    }

    /**
     * Walks down the tree, returning new node content.
     *
     * @param direction direction to walk down tree.
     * @return character value for current node. Only non-null on leaves.
     */
    public char walk(E direction) {
        String content = tree.walkDown(direction);

        if (content == "") {
            return '\0';
        }

        if (tree.currentIsLeaf()) {
            // TODO: add error state for bad tree if non-leaf content
            // TODO: add error state for incomplete tree if no-content leaf
            // Auto-rewind if we hit a leaf.
            tree.rewind();
        }

        return this.contentToChar(content);
    }

    /**
     * Turn string content into a character. 
     *
     * @param content   content to transform.
     * @return character representation of content.
     */
    private char contentToChar(String content) {
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
        } else {
            // Check special characters.
            charValue = this.handleSpecialCharacter(content);
        }

        this.addToBuffer(content);
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
                return '\b';
            case "<space>":
                return ' ';
            case "<enter>":
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

    /**
     * 'Gated' add to buffer that deques as necessary to keep size down.
     *
     * @param content   content to add.
     */
    private void addToBuffer(String content) {
        this.buffer.add(content);

        if (this.buffer.size() > this.bufferSize) {
            this.buffer.remove();
        }
    }

    /**
     * Get string from the buffer.
     *
     * @param index index of string to take from buffer.
     */
    public String getFromBuffer(int index) {
        // Buffer is linked list, so we need to reverse get direction.
        return this.buffer.get(this.bufferSize - index);
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
