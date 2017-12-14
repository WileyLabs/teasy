package com.wiley.autotest.selenium.context.blocklist2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class MyTest {


    private BlockList<TestBlock> testBlocks;

    public void test() {
        List<TestBlock> blocks = new ArrayList<>();
        blocks.add(new TestBlock(null));
        blocks.add(new TestBlock(null));
        blocks.add(new TestBlock(null));

        testBlocks = new BlockList<>(blocks);


        testBlocks.filter(findSpecialBlock()).clickSometh();
    }

    public Function<Collection<TestBlock>, TestBlock> findSpecialBlock() {
        return testBlocks -> {
            for (TestBlock block : testBlocks) {
                if (block != null) {
                    return block;
                }
            }
            return null;
        };
    }

}
