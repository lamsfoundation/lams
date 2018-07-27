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

import java.util.AbstractSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Set implementation which provides indexed access to set members via their class,
 * and which allows only one instance of a given class to be present in the set. Null
 * members are not allowed.
 * 
 * @param <T> the type of object stored by this class
 */
public class ClassIndexedSet<T> extends AbstractSet<T> implements Set<T> {
    
    /** Storage for set members. */
    private HashSet<T> set;
    
    /** Storage for index of class -> member. */
    private HashMap<Class<? extends T>, T> index;
    
    /**
     * Constructor.
     *
     */
    public ClassIndexedSet() {
        set = new HashSet<T>();
        index = new HashMap<Class<? extends T>, T>();
    }

    /** {@inheritDoc} */
    public boolean add(T o) {
        return add(o, false);
    }

    /**
     * Add member to set, optionally replacing any existing instance of the 
     * same class.
     * 
     * @param o the object to add
     * @param replace flag indicating whether to replace an existing class type
     * @return true if object was added
     * @throws IllegalArgumentException if set already contained an instance of the object's class
     *              and replacement was specified as false
     * @throws NullPointerException if the object to add was null
     */
    @SuppressWarnings("unchecked")
    public boolean add(T o, boolean replace) throws NullPointerException, IllegalArgumentException {
        if (o == null) {
            throw new NullPointerException("Null elements are not allowed");
        }
        boolean replacing = false;
        Class<? extends T> indexClass = getIndexClass(o);
        T existing = get(indexClass);
        if (existing != null) {
            replacing = true;
            if (replace) {
                remove(existing);
            } else {
                throw new IllegalArgumentException("Set already contains a member of index class " 
                        + indexClass.getName());
            }
        }
        index.put(indexClass, o);
        set.add(o);
        return replacing;
    }

    /** {@inheritDoc} */
    public void clear() {
        set.clear();
        index.clear();
    }

    /** {@inheritDoc} */
    @SuppressWarnings("unchecked")
    public boolean remove(Object o) {
        if  (set.contains(o)) {
            removeFromIndex((T) o);
            set.remove(o);
            return true;
        }
        return false;
    }

    /** {@inheritDoc} */
    public Iterator<T> iterator() {
        return new ClassIndexedSetIterator(this, set.iterator()); 
    }

    /** {@inheritDoc} */
    public int size() {
        return set.size();
    }
    
    /**
     * Check whether set contains an instance of the specified class.
     * 
     * @param clazz the class to check
     * @return true if set contains an instance of the specified class, false otherwise
     */
    public boolean contains(Class<? extends T> clazz) {
        return get(clazz)!=null;
    }
    
    /**
     * Get the set element specified by the class parameter.
     * @param <X> generic parameter which eliminates need for casting by the caller
     * @param clazz the class to whose instance is to be retrieved
     * @return the element whose class is of the type specified, or null
     * 
     */
    @SuppressWarnings("unchecked")
    public <X extends T> X get(Class<X> clazz) {
        return (X) index.get(clazz);
    }
    
    /**
     * Get the index class of the specified object. Subclasses may override to use
     * a class index other than the main runtime class of the object.
     * 
     * @param o the object whose class index to determine
     * @return the class index value associated with the object instance
     */
    @SuppressWarnings("unchecked")
    protected Class<? extends T> getIndexClass(Object o) {
        return (Class<? extends T>) o.getClass();
    }
    
    /**
     * Remove the specified object from the index.
     * 
     * @param o the object to remove
     */
    private void removeFromIndex(T o) {
       index.remove(getIndexClass(o));
    }
    
    /**
     * Iterator for set implementation {@link ClassIndexedSet}.
     * 
     */
    protected class ClassIndexedSetIterator implements Iterator<T> {
        
        /** The set instance over which this instance is an iterator. */
        private ClassIndexedSet<T> set;
        
        /** The iterator for the owner's underlying storage. */
        private Iterator<T> iterator;
        
        /** Flag which tracks whether next() has been called at least once. */
        private boolean nextCalled;
        
        /** Flag which tracks whether remove can currently be called. */
        private boolean removeStateValid;;
        
        /** The element most recently returned by next(), and the target for any subsequent
         * remove() operation. */
        private T current;
        
        /**
         * Constructor.
         *
         * @param parentSet the {@link ClassIndexedSet} over which this instance is an iterator
         * @param parentIterator the iterator for the parent's underlying storage
         */
        protected ClassIndexedSetIterator(ClassIndexedSet<T> parentSet, Iterator<T> parentIterator) {
            set = parentSet;
            iterator = parentIterator;
            current = null;
            nextCalled = false;
            removeStateValid = false;
        }

        /** {@inheritDoc} */
        public boolean hasNext() {
            return iterator.hasNext();
        }

        /** {@inheritDoc} */
        public T next() {
            current = iterator.next();
            nextCalled = true;
            removeStateValid = true;
            return current;
        }

        /** {@inheritDoc} */
        public void remove() {
            if (! nextCalled) {
                throw new IllegalStateException("remove() was called before calling next()");
            }
            if (! removeStateValid) {
                throw new IllegalStateException("remove() has already been called since the last call to next()");
            }
            iterator.remove();
            set.removeFromIndex(current);
            removeStateValid = false;
        }
        
    }

}
