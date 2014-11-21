package org.smlabtesting.simabs.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list where the first element index can be offset. Once the offset
 * reaches the end of the list, it is cycled back.
 *
 * This is mostly used for the rqRacetrack where the icSample holders move in
 * unison on slot at a time. Instead of having to actually move the elements
 * in the array one by one, and slowing things down, we just do some
 * basic arithmetic with the index by adding/subtracting an offset.
 *
 * This isn't technically a list because the size is fixed. Only the essential
 * List methods are implemented.
 *
 * @author Ahmed El-Hajjar
 *
 * @param <E> Type of the object stored in this list.
 */
public class OffsetList<E> implements List<E> {
    // Contains the actual element data.
    private final E[] listData;

    // By how much we want to offset the indexes by.
    private int offset;
    public int getOffset() {return offset;}

    /**
     * Initializes the list with the provided size. The size is fixed.
     *
     * @param size The number of elements that can be stored in the list.
     */
    @SuppressWarnings("unchecked")
    public OffsetList(final int size) {
        listData = (E[])new Object[size];
    }

    /**
     * Shifts the offset (giving the illusion of shifting all the elements) of
     * the list.
     *
     * @param amount By how much to shift it by. Supports negative shifting.
     */
    public void offset(final int amount) {
        offset = (offset + amount) % size();

        if (offset < 0) {
            offset = size() - offset;
        }
    }

    /**
     * Used to calculate the position of where the actual data is stored based
     * on the offset.
     *
     * @param index The index of the element we want.
     * @return The actual index of the element to retrieve.
     */
    private int offsetIndex(final int index) {
        return (index + offset) % size();
    }

    /**
     * Counts how many non-null elements are in this list.
     * 
     * @return An integer with the number of elements in this list.
     */
    public int count() {
        int count = 0; 
        for (E e: listData) if (e != null) count++;
        return count;
    }
    
    // List API
    @Override
    public int size() {
        return listData.length;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public E get(final int index) {
        return listData[offsetIndex(index)];
    }

    @Override
    public E set(final int index, final E element) {
        return listData[offsetIndex(index)] = element;
    }

    @Override
    public Iterator<E> iterator() {
        return new Iterator<E>() {
            private final int originalOffset = offset;
            private int nextCount = 0;

            @Override
            public boolean hasNext() {
                return nextCount < size();
            }

            @Override
            public E next() {
                return listData[(originalOffset + nextCount++) % size()];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }

    @Override public ListIterator<E> listIterator() {throw new UnsupportedOperationException();}
    @Override public ListIterator<E> listIterator(final int index) {throw new UnsupportedOperationException();}
    @Override public boolean contains(final Object o) {throw new UnsupportedOperationException();}
    @Override public Object[] toArray() {throw new UnsupportedOperationException();}
    @Override public <T> T[] toArray(final T[] a) {throw new UnsupportedOperationException();}
    @Override public boolean add(final E e) {throw new UnsupportedOperationException();}
    @Override public boolean remove(final Object o) {throw new UnsupportedOperationException();}
    @Override public boolean containsAll(final Collection<?> c) {throw new UnsupportedOperationException();}
    @Override public boolean addAll(final Collection<? extends E> c) {throw new UnsupportedOperationException();}
    @Override public boolean addAll(final int index, final Collection<? extends E> c) {throw new UnsupportedOperationException();}
    @Override public boolean removeAll(final Collection<?> c) {throw new UnsupportedOperationException();}
    @Override public boolean retainAll(final Collection<?> c) {throw new UnsupportedOperationException();}
    @Override public void clear() {throw new UnsupportedOperationException();}
    @Override public void add(final int index, final E element) {throw new UnsupportedOperationException();}
    @Override public E remove(final int index) {throw new UnsupportedOperationException();}
    @Override public int indexOf(final Object o) {throw new UnsupportedOperationException();}
    @Override public int lastIndexOf(final Object o) {throw new UnsupportedOperationException();}
    @Override public List<E> subList(final int fromIndex, final int toIndex) {throw new UnsupportedOperationException();}
}