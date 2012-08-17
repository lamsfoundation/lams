
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
 * Holds state information for <code>Job</code> instances.
 * </p>
 * 
 * <p>
 * <code>JobDataMap</code> instances are stored once when the <code>Job</code>
 * is added to a scheduler. They are also re-persisted after every execution of
 * <code>StatefulJob</code> instances.
 * </p>
 * 
 * <p>
 * <code>JobDataMap</code> instances can also be stored with a 
 * <code>Trigger</code>.  This can be useful in the case where you have a Job
 * that is stored in the scheduler for regular/repeated use by multiple 
 * Triggers, yet with each independent triggering, you want to supply the
 * Job with different data inputs.  
 * </p>
 * 
 * <p>
 * The <code>JobExecutionContext</code> passed to a Job at execution time 
 * also contains a convenience <code>JobDataMap</code> that is the result
 * of merging the contents of the trigger's JobDataMap (if any) over the
 * Job's JobDataMap (if any).  
 * </p>
 * 
 * @see Job
 * @see StatefulJob
 * @see Trigger
 * @see JobExecutionContext
 * 
 * @author James House
 */
public class JobDataMap extends DirtyFlagMap implements Serializable {

    private static final long serialVersionUID = -6939901990106713909L;
    
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
    public JobDataMap() {
        super(15);
    }

    /**
     * <p>
     * Create a <code>JobDataMap</code> with the given data.
     * </p>
     */
    public JobDataMap(Map map) {
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
     * Tell the <code>JobDataMap</code> that it should allow non- <code>Serializable</code>
     * data.
     * </p>
     * 
     * <p>
     * If the <code>JobDataMap</code> does contain non- <code>Serializable</code>
     * objects, and it belongs to a non-volatile <code>Job</code> that is
     * stored in a <code>JobStore</code> that supports persistence, then
     * those elements will be nulled-out during persistence.
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
     * Adds the name-value pairs in the given <code>Map</code> to the <code>JobDataMap</code>.
     * </p>
     * 
     * <p>
     * All keys must be <code>String</code>s, and all values must be <code>Serializable</code>.
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
     * Adds the given <code>int</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, int value) {
        super.put(key, new Integer(value));
    }

    /**
     * <p>
     * Adds the given <code>long</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, long value) {
        super.put(key, new Long(value));
    }

    /**
     * <p>
     * Adds the given <code>float</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, float value) {
        super.put(key, new Float(value));
    }

    /**
     * <p>
     * Adds the given <code>double</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, double value) {
        super.put(key, new Double(value));
    }

    /**
     * <p>
     * Adds the given <code>boolean</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, boolean value) {
        super.put(key, new Boolean(value));
    }

    /**
     * <p>
     * Adds the given <code>char</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, char value) {
        super.put(key, new Character(value));
    }

    /**
     * <p>
     * Adds the given <code>String</code> value to the <code>Job</code>'s
     * data map.
     * </p>
     */
    public void put(String key, String value) {
        super.put(key, value);
    }

    /**
     * <p>
     * Adds the given <code>boolean</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, boolean value) {
        String strValue = new Boolean(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Boolean</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Boolean value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>char</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, char value) {
        String strValue = new Character(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Character</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Character value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>double</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, double value) {
        String strValue = new Double(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Double</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Double value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>float</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, float value) {
        String strValue = new Float(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Float</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Float value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>int</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, int value) {
        String strValue = new Integer(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Integer</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Integer value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>long</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, long value) {
        String strValue = new Long(value).toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Long</code> value as a string version to the
     * <code>Job</code>'s data map.
     * </p>
     */
    public void putAsString(String key, Long value) {
        String strValue = value.toString();

        super.put(key, strValue);
    }

    /**
     * <p>
     * Adds the given <code>Serializable</code> object value to the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>int</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>long</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>float</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>double</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>boolean</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>char</code> value from the <code>JobDataMap</code>.
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
     * Retrieve the identified <code>String</code> value from the <code>JobDataMap</code>.
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

    /**
     * <p>
     * Retrieve the identified <code>int</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public int getIntFromString(String key) {
        Object obj = get(key);

        return new Integer((String) obj).intValue();
    }

    /**
     * <p>
     * Retrieve the identified <code>int</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String or Integeger.
     */
    public long getIntValue(String key) {
        Object obj = get(key);

        if(obj instanceof String)
            return getIntFromString(key);
        else
            return getInt(key);
    }
    
    /**
     * <p>
     * Retrieve the identified <code>int</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Integer getIntegerFromString(String key) {
        Object obj = get(key);

        return new Integer((String) obj);
    }

    /**
     * <p>
     * Retrieve the identified <code>boolean</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public boolean getBooleanValueFromString(String key) {
        Object obj = get(key);

        return new Boolean((String) obj).booleanValue();
    }

    /**
     * <p>
     * Retrieve the identified <code>boolean</code> value from the 
     * <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String or Boolean.
     */
    public boolean getBooleanValue(String key) {
        Object obj = get(key);

        if(obj instanceof String)
            return getBooleanValueFromString(key);
        else
            return getBoolean(key);
    }

    /**
     * <p>
     * Retrieve the identified <code>Boolean</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Boolean getBooleanFromString(String key) {
        Object obj = get(key);

        return new Boolean((String) obj);
    }

    /**
     * <p>
     * Retrieve the identified <code>char</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public char getCharFromString(String key) {
        Object obj = get(key);

        return ((String) obj).charAt(0);
    }

    /**
     * <p>
     * Retrieve the identified <code>Character</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Character getCharacterFromString(String key) {
        Object obj = get(key);

        return new Character(((String) obj).charAt(0));
    }

    /**
     * <p>
     * Retrieve the identified <code>double</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public double getDoubleValueFromString(String key) {
        Object obj = get(key);

        return new Double((String) obj).doubleValue();
    }

    /**
     * <p>
     * Retrieve the identified <code>double</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String or Double.
     */
    public double getDoubleValue(String key) {
        Object obj = get(key);

        if(obj instanceof String)
            return getDoubleValueFromString(key);
        else
            return getDouble(key);
    }

    /**
     * <p>
     * Retrieve the identified <code>Double</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Double getDoubleFromString(String key) {
        Object obj = get(key);

        return new Double((String) obj);
    }

    /**
     * <p>
     * Retrieve the identified <code>float</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public float getFloatValueFromString(String key) {
        Object obj = get(key);

        return new Float((String) obj).floatValue();
    }

    /**
     * <p>
     * Retrieve the identified <code>float</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String or Float.
     */
    public float getFloatValue(String key) {
        Object obj = get(key);

        if(obj instanceof String)
            return getFloatValueFromString(key);
        else
            return getFloat(key);
    }
    
    /**
     * <p>
     * Retrieve the identified <code>Float</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Float getFloatFromString(String key) {
        Object obj = get(key);

        return new Float((String) obj);
    }

    /**
     * <p>
     * Retrieve the identified <code>long</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public long getLongValueFromString(String key) {
        Object obj = get(key);

        return new Long((String) obj).longValue();
    }

    /**
     * <p>
     * Retrieve the identified <code>long</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String or Long.
     */
    public long getLongValue(String key) {
        Object obj = get(key);

        if(obj instanceof String)
            return getLongValueFromString(key);
        else
            return getLong(key);
    }
    
    /**
     * <p>
     * Retrieve the identified <code>Long</code> value from the <code>JobDataMap</code>.
     * </p>
     * 
     * @throws ClassCastException
     *           if the identified object is not a String.
     */
    public Long getLongFromString(String key) {
        Object obj = get(key);

        return new Long((String) obj);
    }

    public String[] getKeys() {
        return (String[]) keySet().toArray(new String[size()]);
    }

}
