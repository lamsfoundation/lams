/*
 * Licensed to the University Corporation for Advanced Internet Development, 
 * Inc. (UCAID) under one or more contributor license agreements.  See the 
 * NOTICE file distributed with this work for additional information regarding
 * copyright ownership. The UCAID licenses this file to You under the Apache 
 * License, Version 2.0 (the "License"); you may not use this file except in 
 * compliance with the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opensaml.xml.util;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import net.jcip.annotations.NotThreadSafe;

/**
 * A list that is lazy initialized. This list takes very little memory when storing zero or one item.
 * 
 * @param <ElementType> type of elements within the list
 */
@NotThreadSafe
public class LazyList<ElementType> implements List<ElementType>, Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = -7741904523916701817L;

    /** Delegate list. */
    private List<ElementType> delegate = Collections.emptyList();

    /** {@inheritDoc} */
    public boolean add(ElementType item) {
        if (delegate.isEmpty()) {
            delegate = Collections.singletonList(item);
            return true;
        } else {
            delegate = buildList();
            return delegate.add(item);
        }
    }

    /** {@inheritDoc} */
    public void add(int index, ElementType element) {
        delegate = buildList();
        delegate.add(index, element);
    }

    /** {@inheritDoc} */
    public boolean addAll(Collection<? extends ElementType> collection) {
        delegate = buildList();
        return delegate.addAll(collection);
    }

    /** {@inheritDoc} */
    public boolean addAll(int index, Collection<? extends ElementType> collection) {
        delegate = buildList();
        return delegate.addAll(index, collection);
    }

    /** {@inheritDoc} */
    public void clear() {
        delegate = Collections.emptyList();
    }

    /** {@inheritDoc} */
    public boolean contains(Object element) {
        return delegate.contains(element);
    }

    /** {@inheritDoc} */
    public boolean containsAll(Collection<?> collections) {
        return delegate.containsAll(collections);
    }

    /** {@inheritDoc} */
    public ElementType get(int index) {
        return delegate.get(index);
    }

    /** {@inheritDoc} */
    public int indexOf(Object element) {
        return delegate.indexOf(element);
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /** {@inheritDoc} */
    public Iterator<ElementType> iterator() {
        delegate = buildList();
        return delegate.iterator();
    }

    /** {@inheritDoc} */
    public int lastIndexOf(Object element) {
        return delegate.lastIndexOf(element);
    }

    /** {@inheritDoc} */
    public ListIterator<ElementType> listIterator() {
        delegate = buildList();
        return delegate.listIterator();
    }

    /** {@inheritDoc} */
    public ListIterator<ElementType> listIterator(int index) {
        delegate = buildList();
        return delegate.listIterator(index);
    }

    /** {@inheritDoc} */
    public boolean remove(Object element) {
        delegate = buildList();
        return delegate.remove(element);
    }

    /** {@inheritDoc} */
    public ElementType remove(int index) {
        delegate = buildList();
        return delegate.remove(index);
    }

    /** {@inheritDoc} */
    public boolean removeAll(Collection<?> collection) {
        delegate = buildList();
        return delegate.removeAll(collection);
    }

    /** {@inheritDoc} */
    public boolean retainAll(Collection<?> collection) {
        delegate = buildList();
        return delegate.retainAll(collection);
    }

    /** {@inheritDoc} */
    public ElementType set(int index, ElementType element) {
        delegate = buildList();
        return delegate.set(index, element);
    }

    /** {@inheritDoc} */
    public int size() {
        return delegate.size();
    }

    /** {@inheritDoc} */
    public List<ElementType> subList(int fromIndex, int toIndex) {
        delegate = buildList();
        return delegate.subList(fromIndex, toIndex);
    }

    /** {@inheritDoc} */
    public Object[] toArray() {
        return delegate.toArray();
    }

    /** {@inheritDoc} */
    public <T> T[] toArray(T[] type) {
        return delegate.toArray(type);
    }

    /**
     * Builds an appropriate delegate for this list.
     * 
     * @return delegate for this list
     */
    protected List<ElementType> buildList() {
        if (delegate instanceof ArrayList) {
            return delegate;
        }

        return new ArrayList<ElementType>(delegate);
    }

    /** {@inheritDoc} */
    public String toString() {
        return delegate.toString();
    }

    /** {@inheritDoc} */
    public int hashCode() {
        return delegate.hashCode();
    }

    /** {@inheritDoc} */
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        return delegate.equals(((LazyList<?>) obj).delegate);
    }
}