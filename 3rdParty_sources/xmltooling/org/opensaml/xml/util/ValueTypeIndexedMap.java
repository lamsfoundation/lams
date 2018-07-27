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

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Map implementation which allows subsets of entries to be retrieved based on the type of the entry value.
 * 
 * @param <KeyType> the type of object used as keys
 * @param <ValueType> the type of object stored as values
 */
public class ValueTypeIndexedMap<KeyType, ValueType> implements Map<KeyType, ValueType> {

    /** Class to represent null values. */
    private static class NullValue {}
    
    /** Storage for index of class -> members. */
    private Map<Class<?>, Map<KeyType, ValueType>> index;

    /** Storage for map members. */
    private Map<KeyType, ValueType> map;

    /** Set of valid types for this map. */
    private Set<Class> types;

    /** Constructor. */
    public ValueTypeIndexedMap() {
        this(new HashSet<Class>());
    }

    /**
     * Constructor.
     *
     * @param newMap existing map to build from.
     * @param newTypes collection of value types to index
     */
    public ValueTypeIndexedMap(Map<KeyType, ValueType> newMap, Collection<Class> newTypes) {
        map = newMap;
        types = new HashSet<Class>(newTypes);
        index = new HashMap<Class<?>, Map<KeyType, ValueType>>();
        rebuildIndex();
    }
    
    /**
     * Constructor.
     *
     * @param newTypes collection of value types to index
     */
    public ValueTypeIndexedMap(Collection<Class> newTypes) {
        this(new HashMap<KeyType, ValueType>(), newTypes);
    }

    /** {@inheritDoc} */
    public void clear() {
        map.clear();
        rebuildIndex();
    }

    /** {@inheritDoc} */
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    /** {@inheritDoc} */
    public boolean containsValue(Object value) {
        return map.containsValue(value);
    }

    /** {@inheritDoc} */
    public Set<java.util.Map.Entry<KeyType, ValueType>> entrySet() {
        return map.entrySet();
    }

    /** {@inheritDoc} */
    public ValueType get(Object key) {
        return map.get(key);
    }

    /**
     * Get the value types that are indexed.
     * 
     * @return which value types are indexed
     */
    public Set<Class> getTypes() {
        return types;
    }

    /** {@inheritDoc} */
    public boolean isEmpty() {
        return map.isEmpty();
    }

    /** {@inheritDoc} */
    public Set<KeyType> keySet() {
        return map.keySet();
    }

    /**
     * Check if the object is of the specified type, taking null into account as well.
     * 
     * @param type type to check for
     * @param object object to check
     * @return true if the object is of the specified type
     */
    private Boolean matchType(Class<?> type, Object object) {
        return type.isInstance(object) || (type == NullValue.class && object == null);
    }

    /** {@inheritDoc} */
    public ValueType put(KeyType key, ValueType value) {
        ValueType oldValue = map.put(key, value);

        for (Class<?> type : index.keySet()) {
            if (type == null) { type = NullValue.class; }
            if (matchType(type, value)) {
                index.get(type).put(key, value);
            } else if (matchType(type, oldValue)) {
                index.get(type).remove(key);
            }
        }

        return oldValue;
    }

    /** {@inheritDoc} */
    public void putAll(Map<? extends KeyType, ? extends ValueType> t) {
        // this is probably not the most efficient way to do this
        for (KeyType key : t.keySet()) {
            put(key, t.get(key));
        }
    }
    
    /**
     * Rebuild internal index.
     */
    public void rebuildIndex() {
        index.clear();
        ValueType value;

        for (Class<?> type : types) {
            if (type == null) { type = NullValue.class; }
            index.put(type, new HashMap<KeyType, ValueType>());
            for (KeyType key : map.keySet()) {
                value = map.get(key);
                if (matchType(type, value)) {
                    index.get(type).put(key, value);
                }
            }
        }
    }
    
    /** {@inheritDoc} */
    public ValueType remove(Object key) {
        ValueType value = map.remove(key);

        for (Class<?> type : index.keySet()) {
            if (type.isInstance(value)) {
                index.get(type).remove(key);
            }
        }

        return value;
    }
    
    /**
     * Set which value types are indexed.
     * 
     * @param newTypes which value types are indexed
     */
    public void setTypes(Collection<Class> newTypes) {
        this.types = new HashSet<Class>(newTypes);
    }

    /** {@inheritDoc} */
    public int size() {
        return map.size();
    }

    /**
     * Returns an unmodifiable map of the entries whose value is of the specified type.
     * 
     * @param <SubType> type of values to include in the returned map
     * @param type type of values to return
     * @return sub map of entries whose value is of type SubType or null if the specified type is not a valid type for
     *         this map.
     */
    @SuppressWarnings("unchecked")
    public <SubType extends ValueType> Map<KeyType, SubType> subMap(Class<SubType> type) {
        Class<?> key = type;
        if (key == null) { key = NullValue.class; }
        if (index.containsKey(key)) {
            return Collections.unmodifiableMap((Map<KeyType, SubType>) index.get(key));
        } else {
            return Collections.emptyMap();
        }
    }

    /** {@inheritDoc} */
    public String toString() {
        return map.toString();
    }
    
    /** {@inheritDoc} */
    public Collection<ValueType> values() {
        return map.values();
    }


}