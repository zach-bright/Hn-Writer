package ca.zach_bright;

import java.io.IOException;
import java.util.Map;
import java.io.File;

/**
 * Abstract class for file-to-tree builders.
 *
 * @author zach-bright
 */
public abstract class EnumTreeBuilder<E extends Enum<E>> {
    protected File sourceFile;
    protected EnumTree<E> tree = null;
    protected Map<String, E> contentEnumMap;
    protected Class<E> eClass;

    public EnumTreeBuilder(
        File sourceFile, 
        Map<String, E> contentEnumMap, 
        Class<E> eClass
    ) throws IOException {
        if (sourceFile.length() == 0) {
            throw new IOException("Provided file is empty.");
        }
        this.sourceFile = sourceFile;
        this.contentEnumMap = contentEnumMap;
        this.eClass = eClass;
    }

    public abstract EnumTree<E> build() throws IOException;
}
