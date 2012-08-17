/* 
 * Copyright 2004-2005 OpenSymphony 
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not 
 * use this file except in compliance with the License. You may obtain a copy 
 * of the License at 
 * 
 *   http://www.apache.org/licenses/LICENSE-2.0 
 *   
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 * 
 */

/*
 * Previously Copyright (c) 2001-2004 James House
 */
package org.quartz.utils;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * <p>
 * An implementation of <code>Map</code> that wraps another <code>Map</code>
 * and flags itself 'dirty' when it is modified.
 * </p>
 * 
 * @author James House
 */
public class DirtyFlagMap implements Map, Cloneable, java.io.Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */
    private static final long serialVersionUID = 1433884852607126222L;

    private boolean dirty = false;
    private transient boolean locked = false;
    private Map map;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create a DirtyFlagMap that 'wraps' the given <code>Map</code>.
     * </p>
     */
    public DirtyFlagMap(Map mapToWrap) {
        if (mapToWrap == null)
                throw new IllegalArgumentException("mapToWrap cannot be null!");

        map = mapToWrap;
    }

    /**
     * <p>
     * Create a DirtyFlagMap that 'wraps' a <code>HashMap</code>.
     * </p>
     * 
     * @see java.util.HashMap
     */
    public DirtyFlagMap() {
        map = new HashMap();
    }

    /**
     * <p>
     * Create a DirtyFlagMap that 'wraps' a <code>HashMap</code> that has the
     * given initial capacity.
     * </p>
     * 
     * @see java.util.HashMap
     */
    public DirtyFlagMap(int initialCapacity) {
        map = new HashMap(initialCapacity);
    }

    /**
     * <p>
     * Create a DirtyFlagMap that 'wraps' a <code>HashMap</code> that has the
     * given initial capacity and load factor.
     * </p>
     * 
     * @see java.util.HashMap
     */
    public DirtyFlagMap(int initialCapacity, float loadFactor) {
        map = new HashMap(initialCapacity, loadFactor);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */


    public void setMutable(boolean mutable) {
        this.locked = !mutable;
        if(locked)
            map = Collections.unmodifiableMap(map);
        else
            map = new HashMap(map);
    }
    
    
    public boolean isMutable() {
        return !locked;
    }
    
    /**
     * <p>
     * Clear the 'dirty' flag (set dirty flag to <code>false</code>).
     * </p>
     */
    public void clearDirtyFlag() {
        dirty = false;
    }

    /**
     * <p>
     * Determine whether the <code>Map</code> is flagged dirty.
     * </p>
     */
    public boolean isDirty() {
        return dirty;
    }

    /**
     * <p>
     * Get a direct handle to the underlying Map.
     * </p>
     */
    public Map getWrappedMap() {
        return map;
    }

    public void clear() {
        dirty = true;

        map.clear();
    }

    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    public boolean containsValue(Object val) {
        return map.containsValue(val);
    }

    public Set entrySet() {
        return map.entrySet();
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof DirtyFlagMap)) return false;

        return map.equals(((DirtyFlagMap) obj).getWrappedMap());
    }

    public Object get(Object key) {
        return map.get(key);
    }

    public boolean isEmpty() {
        return map.isEmpty();
    }

    public Set keySet() {
        return map.keySet();
    }

    public Object put(Object key, Object val) {
        dirty = true;

        return map.put(key, val);
    }

    public void putAll(Map t) {
        if (!t.isEmpty()) dirty = true;

        map.putAll(t);
    }

    public Object remove(Object key) {
        Object obj = map.remove(key);

        if (obj != null) dirty = true;

        return obj;
    }

    public int size() {
        return map.size();
    }

    public Collection values() {
        return map.values();
    }

    public Object clone() {
        DirtyFlagMap copy;
        try {
            copy = (DirtyFlagMap) super.clone();
            if (map instanceof HashMap)
                    copy.map = (Map) ((HashMap) map).clone();
        } catch (CloneNotSupportedException ex) {
            throw new IncompatibleClassChangeError("Not Cloneable.");
        }

        return copy;
    }

}