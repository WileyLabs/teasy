package com.wiley.elements.find;

/**
 * Basic interface for finding objects of a generic type
 * @param <T> - object type to find
 */
public interface LookUp<T> {

    T find();
}
