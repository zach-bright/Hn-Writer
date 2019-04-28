package ca.zach_bright;

import java.io.IOException;

/**
 * Interface for classes that build an EnumTree.
 *
 * @author zach-bright
 * @param <E> enum that the EnumTree will use.
 */
public interface EnumTreeBuilder<E extends Enum<E>> {
    EnumTree<E> build() throws IOException;
}
