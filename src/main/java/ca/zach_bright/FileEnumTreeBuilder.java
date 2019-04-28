package ca.zach_bright;

import java.io.IOException;
import java.util.Map;
import java.io.File;

/**
 * Abstract class for file-to-tree builders.
 *
 * @author zach-bright
 * @param <E> enum that the EnumTree will use.
 */
public abstract class FileEnumTreeBuilder<E extends Enum<E>> implements EnumTreeBuilder<E> {
    protected File sourceFile;
    protected EnumTree<E> tree = null;
    protected Map<String, E> contentEnumMap;
    protected Class<E> eClass;

    /**
     * @param sourceFile file to use in generating the tree.
     * @param contentEnumMap map from file strings to enum.
     * @param eClass class of the enum.
     * @throws IOException if file is empty.
     */
    public FileEnumTreeBuilder(
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
}
