package org.smlabtesting.types;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/**
 * A list where the first element index can be offsetted. Once the offset
 * reaches the end of the list, it is cycled back.
 * 
 * This is mostly used for the racetrack where the sample holders move in 
 * unison on slot at a time. Instead of having to actually move the elements
 * in the array one by one, and slowing things down, we just do some 
 * basic arithmetic with the index by adding/subtracting an offset.
 * 
 * This isn't technically a list because the size is fixed. Only the essential
 * List methods are implemented.
 * 
 * TODO: Incomplete.
 * 
 * @author Ahmed El-Hajjar
 *
 * @param <E> Type of the object stored in this list.
 */
public class OffsetList<E> implements List<E> {
	// Contains the actual element data.
	private E[] listData;
	
	// By how much we want to offset the indexes by.
	private int offset;
	
	/**
	 * Initializes the list with the provided size. The size is fixed.
	 * 
	 * @param size The number of elements that can be stored in the list.
	 */
	@SuppressWarnings("unchecked")
	public OffsetList(int size) {
		listData = (E[])new Object[size];
	}

	/**
	 * Shifts the offset (giving the illusion of shifting all the elements) of
	 * the list.
	 * 
	 * @param amount By how much to shift it by. Supports negative shifting.
	 */
	public void offset(int amount) {
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
	 * @return The actual index of the element to retreive.
	 */
	private int offsetIndex(int index) {
		return (index + offset) % size();
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
	public E get(int index) {
		return listData[offsetIndex(index)];
	}

	@Override
	public E set(int index, E element) {
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
				return listData[originalOffset + nextCount++];
			}

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}
		};
	}

	@Override public ListIterator<E> listIterator() {throw new UnsupportedOperationException();}
	@Override public ListIterator<E> listIterator(int index) {throw new UnsupportedOperationException();}
	@Override public boolean contains(Object o) {throw new UnsupportedOperationException();}
	@Override public Object[] toArray() {throw new UnsupportedOperationException();}
	@Override public <T> T[] toArray(T[] a) {throw new UnsupportedOperationException();}
	@Override public boolean add(E e) {throw new UnsupportedOperationException();}
	@Override public boolean remove(Object o) {throw new UnsupportedOperationException();}
	@Override public boolean containsAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(Collection<? extends E> c) {throw new UnsupportedOperationException();}
	@Override public boolean addAll(int index, Collection<? extends E> c) {throw new UnsupportedOperationException();}
	@Override public boolean removeAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public boolean retainAll(Collection<?> c) {throw new UnsupportedOperationException();}
	@Override public void clear() {throw new UnsupportedOperationException();}
	@Override public void add(int index, E element) {throw new UnsupportedOperationException();}
	@Override public E remove(int index) {throw new UnsupportedOperationException();}
	@Override public int indexOf(Object o) {throw new UnsupportedOperationException();}
	@Override public int lastIndexOf(Object o) {throw new UnsupportedOperationException();}
	@Override public List<E> subList(int fromIndex, int toIndex) {throw new UnsupportedOperationException();}

}