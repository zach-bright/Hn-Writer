package ca.zach_bright;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HnWriterTest {
    /**
     * Test that HnWriter can make an H4 tree.
     */
    @Test
    public void canBuildH4() {
        Map<> contentEnumMap = buildContentEnumMap();
        File sourceFile = this.getClass.getResource("/h4.json");
    }

    private Map<String, TestEnum> buildContentEnumMap() {
        Map<> m = new EnumMap<String, TestEnum>();
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