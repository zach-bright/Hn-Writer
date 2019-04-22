package ca.zach_bright;

import static org.junit.Assert.*;
import java.util.*;
import java.io.*;

import org.junit.Test;

public class HnWriterTest {
    /**
     * Test that HnWriter can make a valid H4 tree.
     */
    @Test
    public void testH4() {
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

            // Try a sequence: "hi\n"
            writer.walk(TestEnum.UP);
            writer.walk(TestEnum.LEFT); // h
            writer.walk(TestEnum.UP);
            writer.walk(TestEnum.DOWN); // i
            writer.walk(TestEnum.LEFT);
            writer.walk(TestEnum.UP);   // \n
            
            // Make sure Writer knew we hit enter and the string is in history.
            assertEquals("", writer.getString());
            assertEquals("hi", writer.getHistory(0));
        } catch (Exception e) {
            e.printStackTrace();
            fail();
            return;
        }
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