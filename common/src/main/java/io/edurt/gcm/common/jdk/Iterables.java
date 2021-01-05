package io.edurt.gcm.common.jdk;

import java.util.function.BiConsumer;

public class Iterables
{
    private Iterables()
    {}

    /**
     * foreach Enhanced Edition, supporting index setting
     *
     * @param elements elements
     * @param action anonymous function
     * @param <E> iterable
     */
    public static <E> void forEach(Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action)
    {
        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}
