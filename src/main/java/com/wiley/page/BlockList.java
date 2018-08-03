package com.wiley.page;

import com.wiley.elements.TeasyElement;
import com.wiley.elements.types.TeasyElementList;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.function.Function;

/**
 * Represents List of blocks concept introducing filtering via {@link #filter(Function)} method
 * <p>
 * Useful for cases when you have a collection of UI components (aka blocks) and you need
 * to select a specific block to continue working with it.
 *
 * @param <T> - class representing a particular block from a collection
 */
public class BlockList<T extends AbstractBlock> extends ArrayList<T> {

    private final Class<T> blockClass;

    public BlockList(TeasyElementList elements, Class<T> blockClass) {
        super();
        this.blockClass = blockClass;
        elements.forEach(el -> add(createBlockInstance(el)));
    }

    /**
     * Filters a block list
     *
     * @param func - function to filter block list
     * @return the desired block from list
     */
    public T filter(Function<Collection<T>, T> func) {
        return func.apply(this);
    }

    private T createBlockInstance(TeasyElement element) {
        try {
            Constructor<T> constructor = blockClass.getDeclaredConstructor(TeasyElement.class);
            return constructor.newInstance(element);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            throw new RuntimeException("Error occurred during a call to the block constructor.", e);
        }
    }
}
