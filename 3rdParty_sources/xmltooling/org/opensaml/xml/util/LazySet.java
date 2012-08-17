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
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import net.jcip.annotations.NotThreadSafe;

/**
 * A set that is lazy initialized. This set takes very little memory when storing zero or one item.
 * 
 * @param <ElementType> type of the elements within the set
 */
@NotThreadSafe
public class LazySet<ElementType> implements Set<ElementType>, Serializable {

    /** Serial version UID. */
    private static final long serialVersionUID = -1596445680460115174L;

    /** The delegate set. */
    private Set<ElementType> delegate = Collections.emptySet();

    /** {@inheritDoc} */
    public boolean add(ElementType element) {
        if (delegate.isEmpty()) {
            delegate = Collections.singleton(element);
            return true;
        } else {
            delegate = createImplementation();
            return delegate.add(element);
        }
    }

    /** {@inheritDoc} */
    public boolean addAll(Collection<? extends ElementType> collection) {
        delegate = createImplementation();
        return delegate.addAll(collection);
    }

    /** {@inheritDoc} */
    public void clear() {
        delegate = Collections.emptySet();
    }

    /** {@inheritDoc} */
    public boolean contains(Object element) {
        return delegate.contains(element);
    }

    /** {@inheritDoc} */
    public boolean containsAll(Collection<?> collection) {
        return delegate.containsAll(collection);
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return delegate.isEmpty();
    }

    /** {@inheritDoc} */
    public Iterator<ElementType> iterator() {
        delegate = createImplementation();
        return delegate.iterator();
    }

    /** {@inheritDoc} */
    public boolean remove(Object element) {
        delegate = createImplementation();
        return delegate.remove(element);
    }

    /** {@inheritDoc} */
    public boolean removeAll(Collection<?> collection) {
        delegate = createImplementation();
        return delegate.removeAll(collection);
    }

    /** {@inheritDoc} */
    public boolean retainAll(Collection<?> collection) {
        delegate = createImplementation();
        return delegate.retainAll(collection);
    }

    /** {@inheritDoc} */
    public int size() {
        return delegate.size();
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
     * Builds an appropriate delegate set.
     * 
     * @return the delegate set
     */
    private Set<ElementType> createImplementation() {
        if (delegate instanceof HashSet) {
            return delegate;
        }

        return new HashSet<ElementType>(delegate);
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

        return delegate.equals(((LazySet<?>) obj).delegate);
    }
}