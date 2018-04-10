package com.wiley.autotest.selenium.elements.upgrade;

import com.wiley.autotest.selenium.SeleniumHolder;
import com.wiley.autotest.selenium.elements.upgrade.should.EmptyListShould;
import com.wiley.autotest.selenium.elements.upgrade.should.GeneralListShould;
import com.wiley.autotest.selenium.elements.upgrade.should.ListShould;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents list of TeasyElements
 * Basically decorates List adding elements-related behavior
 */
public class TeasyElementList implements List<TeasyElement> {

    private final List<TeasyElement> elements;
    private final By locator;

    public TeasyElementList(@NotNull List<TeasyElement> elements, By locator) {
        this.elements = elements;
        this.locator = locator;
    }

    public ListShould should() {
        ListShould should;
        if (this.size() == 0) {
            should = new EmptyListShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
        } else {
            should = new GeneralListShould(this, new TeasyFluentWait<>(SeleniumHolder.getWebDriver()));
        }
        return should;
    }

    public By getLocator() {
        return locator;
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public boolean isEmpty() {
        return elements.isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return elements.contains(o);
    }

    @NotNull
    @Override
    public Iterator<TeasyElement> iterator() {
        return elements.iterator();
    }

    @NotNull
    @Override
    public Object[] toArray() {
        return elements.toArray();
    }

    @NotNull
    @Override
    public <T> T[] toArray(@NotNull T[] a) {
        return elements.toArray(a);
    }

    @Override
    public boolean add(TeasyElement element) {
        return elements.add(element);
    }

    @Override
    public boolean remove(Object o) {
        return elements.remove(o);
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        return elements.containsAll(c);
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends TeasyElement> c) {
        return elements.addAll(c);
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends TeasyElement> c) {
        return elements.addAll(index, c);
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        return elements.removeAll(c);
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        return elements.retainAll(c);
    }

    @Override
    public void clear() {
        elements.clear();
    }

    @Override
    public TeasyElement get(int index) {
        return elements.get(index);
    }

    @Override
    public TeasyElement set(int index, TeasyElement element) {
        return elements.set(index, element);
    }

    @Override
    public void add(int index, TeasyElement element) {
        elements.add(index, element);
    }

    @Override
    public TeasyElement remove(int index) {
        return elements.remove(index);
    }

    @Override
    public int indexOf(Object o) {
        return elements.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        return elements.lastIndexOf(o);
    }

    @NotNull
    @Override
    public ListIterator<TeasyElement> listIterator() {
        return elements.listIterator();
    }

    @NotNull
    @Override
    public ListIterator<TeasyElement> listIterator(int index) {
        return elements.listIterator(index);
    }

    @NotNull
    @Override
    public List<TeasyElement> subList(int fromIndex, int toIndex) {
        return elements.subList(fromIndex, toIndex);
    }
}
