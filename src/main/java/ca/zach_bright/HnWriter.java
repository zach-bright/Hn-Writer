package ca.zach_bright;

import java.io.IOException;
import java.util.*;

/**
 * Presents a simpler way to use an HnTree.
 * 
 * The HnTree's output is automatically buffered to a string until enter ('\n')
 * is typed, after which it moves to a history buffer. Both these buffers plus 
 * the char buffer are accessable to users for display purposes.
 *
 * @author zach-bright
 */
public class HnWriter<E extends Enum<E>> extends HnTree<E> {
    private LinkedList<String> history;
    private StringBuilder currentSB;
    private String currentString = "";
    private int historySize = 32;

    /**
     * Construct HnWriter with a tree constructed from the given builder.
     *
     * @param builder   tree builder to use in constructing the EnumTree.
     */
    public HnWriter(EnumTreeBuilder<E> builder) throws IOException {
        super(builder);
        this.history = new LinkedList<String>();
        this.currentSB = new StringBuilder();
    }

    /**
     * Handles the output of HnTree's walk function.
     *
     * @param direction direction to walk through the tree.
     * @return result from HnTree's walk function.
     */
    public char walk(E direction) {
        char result = super.walk(direction);

        // Check special characters.
        switch (result) {
            case '\b':
                // Trim last char from sb if possible.
                if (this.currentSB.length() > 1) {
                    this.currentSB.setLength(this.currentSB.length() - 1);
                }
                return result;
            case '\n':
                // Add current str to history and start another.
                this.history.add(currentString);
                this.currentSB.setLength(0);
                this.currentString = "";
                return result;
            case '\0':
            case (char) 16:
            case (char) 20:
            case (char) 114:
                // Caps, shift, and sym are handled by HnTree internally.
                // These and nullchar can just be ignored.
                return result;
        }

        // Update SB and string (only when needed vs building in getter).
        this.currentSB.append(result);
        this.currentString = this.currentSB.toString();

        return result;
    }

    public String getHistory(int index) {
        // Access backwards because its a linked list.
        return this.history.get(this.historySize - index);
    }

    public String[] getHistoryList() {
        return this.history.toArray(new String[0]);
    }

    public String getString() {
        return this.currentString;
    }
}