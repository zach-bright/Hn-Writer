package ca.zach_bright;

import org.json.simple.parser.ParseException;
import org.json.simple.parser.JSONParser;

import java.util.stream.Collectors;
import java.util.*;
import java.io.*;

/**
 * Builds tree from a "direction list" type JSON file, where each line relates
 * content to a list of directions.
 * 
 * @author zach-bright
 * @param <E> enum that the EnumTree will use.
 */
public class FlatJSONTreeBuilder<E extends Enum<E>> extends FileEnumTreeBuilder<E> {
    /**
     * @param sourceFile file to use in generating the tree.
     * @param contentEnumMap map from file strings to enum.
     * @param eClass class of the enum.
     * @throws IOException if file is empty.
     */
    public FlatJSONTreeBuilder(
        File sourceFile, 
        Map<String, E> contentEnumMap, 
        Class<E> eClass
    ) throws IOException {
        super(sourceFile, contentEnumMap, eClass);
    }

    /**
     * Build a tree from a JSON source file.
     * 
     * @return built tree.
     * @throws IOException if build fails.
     */
    public EnumTree<E> build() throws IOException {
        if (this.tree != null) {
            return this.tree;
        }

        EnumTree<E> tree = new EnumTree<>(this.eClass);
        JSONParser parser = new JSONParser();

        // Parse JSON file and add each node to tree.
        try (FileReader fr = new FileReader(this.sourceFile)) {
            Object o = parser.parse(fr);
            Map<String, String> contentDirMap = (Map<String, String>) o;

            // Turn all the string:string pairs into string:List<E> pairs and
            // add node to the tree.
            for (Map.Entry<String, String> entry : contentDirMap.entrySet()) {
                String content = entry.getKey();
                List<E> dirList = Arrays.stream(entry.getValue().split(""))
                    .map(k -> this.contentEnumMap.get(k))
                    .collect(Collectors.toList());
                tree.addNodeToPath(content, dirList);
            }
        } catch (FileNotFoundException e) {

        } catch (ParseException e) {

        }

        this.tree = tree;
        return tree;
    }
}