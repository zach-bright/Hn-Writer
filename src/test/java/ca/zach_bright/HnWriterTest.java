package ca.zach_bright;

import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

import org.junit.Test;

public class HnWriterTest {
    /**
     * Test that HnWriter can make an H4 tree.
     */
    @Test
    public void canBuildH4() {
        HnWriter<TestEnum> writer;
        try {
            Map<String, TestEnum> contentEnumMap = buildContentEnumMap();
            File sourceFile = new File(
                HnWriterTest.class.getResource("/h4.json").getFile()
            );
            EnumTreeBuilder<TestEnum> builder = 
                new FlatJSONTreeBuilder<TestEnum>(
                    sourceFile, 
                    contentEnumMap,
                    TestEnum.class
                );
            writer = new HnWriter<TestEnum>(builder);
        } catch (Exception e) {
            e.printStackTrace();
            assertTrue(false);
            return;
        }
        writer.getContentList().values().forEach(System.out::println);
        assertTrue(true);
    }

    private Map<String, TestEnum> buildContentEnumMap() {
        Map<String, TestEnum> m = new HashMap<String, TestEnum>();
        m.put("U", TestEnum.UP);
        m.put("D", TestEnum.DOWN);
        m.put("L", TestEnum.LEFT);
        m.put("R", TestEnum.RIGHT);
        return m;
    }

    private enum TestEnum {
        UP, 
        DOWN, 
        LEFT, 
        RIGHT
    }
}