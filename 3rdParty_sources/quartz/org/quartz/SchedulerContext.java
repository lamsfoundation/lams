
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
package org.quartz;

import java.io.Serializable;
import java.util.Iterator;
import java.util.Map;

import org.quartz.utils.DirtyFlagMap;

/**
 * <p>
 * Holds context/environment data that can be made available to Jobs as they
 * are executed. This feature is much like the ServletContext feature when
 * working with J2EE servlets.
 * </p>
 * 
 * @see Scheduler#getContext
 * 
 * @author James House
 */
public class SchedulerContext extends DirtyFlagMap implements Serializable {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    private boolean allowsTransientData = false;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Create an empty <code>JobDataMap</code>.
     * </p>
     */
    public SchedulerContext() {
        super(15);
    }

    /**
     * <p>
     * Create a <code>JobDataMap</code> with the given data.
     * </p>
     */
    public SchedulerContext(Map map) {
        this();

        putAll(map);
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    /**
     * <p>
     * Tell the <code>SchedulerContext</code> that it should allow non-
     * <code>Serializable</code> data.
     * </p>
     * 
     * <p>
     * Future versions of Quartz may make distinctions on how it propogates
     * data in the SchedulerContext between instances of proxies to a single
     * scheduler instance - i.e. if Quartz is being used via RMI.
     * </p>
     */
    public void setAllowsTransientData(boolean allowsTransientData) {

        if (containsTransientData() && !allowsTransientData)
                throw new IllegalStateException(
                        "Cannot set property 'allowsTransientData' to 'false' "
                                + "when data map contains non-serializable objects.");

        this.allowsTransientData = allowsTransientData;
    }

    public boolean getAllowsTransientData() {
        return allowsTransientData;
    }

    public boolean containsTransientData() {

        if (!getAllowsTransientData()) // short circuit...
                return false;

        String[] keys = getKeys();

        for (int i = 0; i < keys.length; i++) {
            Object o = super.get(keys[i]);
            if (!(o instanceof Serializable)) return true;
        }

        return false;
    }

    /**
     * <p>
     * Nulls-out any data values that are non-Serializable.
     * </p>
     */
    public void removeTransientData() {

        if (!getAllowsTransientData()) // short circuit...
                return;

        String[] keys = getKeys();

        for (int i = 0; i < keys.length; i++) {
            Object o = super.get(keys[i]);
            if (!(o instanceof Serializable)) remove(keys[i]);
        }

    }

    /**
     * <p>
     * Adds the name-value pairs in the given <code>Map</code> to the <code>SchedulerContext</code>.
     * </p>
     * 
     * <p>
     * All keys must be <code>String</code>s.
     * </p>
     */
    public void putAll(Map map) {
        Iterator itr = map.keySet().iterator();
        while (itr.hasNext()) {
            Object key = itr.next();
            Object val = map.get(key);

            put(key, val);
            // will throw IllegalArgumentException if value not serilizable
        }
    }

    /**
     * <p>
     * Adds the given <code>int</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, int value) {
        super.put(key, new Integer(value));
    }

    /**
     * <p>
     * Adds the given <code>long</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, long value) {
        super.put(key, new Long(value));
    }

    /**
     * <p>
     * Adds the given <code>float</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, float value) {
        super.put(key, new Float(value));
    }

    /**
     * <p>
     * Adds the given <code>double</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, double value) {
        super.put(key, new Double(value));
    }

    /**
     * <p>
     * Adds the given <code>boolean</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, boolean value) {
        super.put(key, new Boolean(value));
    }

    /**
     * <p>
     * Adds the given <code>char</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, char value) {
        super.put(key, new Character(value));
    }

    /**
     * <p>
     * Adds the given <code>String</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public void put(String key, String value) {
        super.put(key, value);
    }

    /**
     * <p>
     * Adds the given <code>Object</code> value to the <code>SchedulerContext</code>.
     * </p>
     */
    public Object put(Object key, Object value) {
        if (!(key instanceof String))
                throw new IllegalArgumentException(
                        "Keys in map must be Strings.");

        return super.put(key, value);
    }

    /**
     * <p>
     * Retrieve the identified <code>int</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not an Integer.
     */
    public int getInt(String key) {
        Object obj = get(key);

        try {
            return ((Integer) obj).intValue();
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not an Integer.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>long</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a Long.
     */
    public long getLong(String key) {
        Object obj = get(key);

        try {
            return ((Long) obj).longValue();
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not a Long.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>float</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a Float.
     */
    public float getFloat(String key) {
        Object obj = get(key);

        try {
            return ((Float) obj).floatValue();
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not a Float.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>double</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a Double.
     */
    public double getDouble(String key) {
        Object obj = get(key);

        try {
            return ((Double) obj).doubleValue();
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not a Double.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>boolean</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a Boolean.
     */
    public boolean getBoolean(String key) {
        Object obj = get(key);

        try {
            return ((Boolean) obj).booleanValue();
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not a Boolean.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>char</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a Character.
     */
    public char getChar(String key) {
        Object obj = get(key);

        try {
            return ((Character) obj).charValue();
        } catch (Exception e) {
            throw new ClassCastException(
                    "Identified object is not a Character.");
        }
    }

    /**
     * <p>
     * Retrieve the identified <code>String</code> value from the <code>SchedulerContext</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public String getString(String key) {
        Object obj = get(key);

        try {
            return (String) obj;
        } catch (Exception e) {
            throw new ClassCastException("Identified object is not a String.");
        }
    }

    public String[] getKeys() {
        return (String[]) keySet().toArray(new String[size()]);
    }

}
