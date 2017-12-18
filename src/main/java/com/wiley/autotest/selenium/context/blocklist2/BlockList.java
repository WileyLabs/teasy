package com.wiley.autotest.selenium.context.blocklist2;

import com.wiley.autotest.selenium.context.AbstractBlock;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElement;
import com.wiley.autotest.selenium.elements.upgrade.TeasyElementList;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;

public class BlockList<E extends AbstractBlock> extends ArrayList<E> {

    public BlockList(@NotNull Collection<E> blocks) {
        super(blocks);
    }

    public BlockList(@NotNull TeasyElementList elements) {
        super();
        List<E> blocks = new ArrayList<>();
        elements.forEach(el -> blocks.add(createBlockInstance(el)));
        addAll(blocks);
    }

    public E filter(Function<Collection<E>, E> func) {
        return func.apply(this);
    }

    private E createBlockInstance(TeasyElement element) {
        try {
            Constructor<E> constructor = getBlockClass().getConstructor(TeasyElement.class);
            return constructor.newInstance(element);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Error occurred during creating block.", e);
        }
    }

    @SuppressWarnings("unchecked")
    private Class<E> getBlockClass() {
        Type type = getClass().getGenericSuperclass();
        ParameterizedType paramType = (ParameterizedType) type;
        return (Class<E>) paramType.getActualTypeArguments()[0];
    }

}
