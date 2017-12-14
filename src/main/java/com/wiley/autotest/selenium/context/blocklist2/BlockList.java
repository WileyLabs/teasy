package com.wiley.autotest.selenium.context.blocklist2;

import com.wiley.autotest.selenium.context.AbstractBlock;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

public class BlockList<E extends AbstractBlock> extends ArrayList<E> {

    public BlockList(@NotNull Collection<? extends E> blocks) {
        super(blocks);
    }

    public E filter(Function<Collection<E>, E> func) {
        return func.apply(this);
    }
}
