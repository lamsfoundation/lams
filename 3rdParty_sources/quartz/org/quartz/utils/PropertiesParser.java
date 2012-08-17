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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

/**
 * <p>
 * This is an utility calss used to parse the properties.
 * </p>
 * 
 * @author James House
 */
public class PropertiesParser {

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Data members.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    Properties props = null;

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Constructors.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public PropertiesParser(Properties props) {
        this.props = props;
    }

    /*
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     * 
     * Interface.
     * 
     * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
     */

    public Properties getUnderlyingProperties() {
        return props;
    }

    public String getStringProperty(String name) {
        String val = props.getProperty(name);
        if (val == null) return null;
        return val.trim();
    }

    public String getStringProperty(String name, String def) {
        String val = props.getProperty(name, def);
        if (val == null) return def;
        val = val.trim();
        if (val.length() == 0) return def;
        return val;
    }

    public String[] getStringArrayProperty(String name) {
        return getStringArrayProperty(name, null);
    }

    public String[] getStringArrayProperty(String name, String[] def) {
        String vals = getStringProperty(name);
        if (vals == null) return def;

        if (vals != null && !vals.trim().equals("")) {
            StringTokenizer stok = new StringTokenizer(vals, ",");
            Vector strs = new Vector();
            try {
                while (stok.hasMoreTokens()) {
                    strs.addElement(stok.nextToken());
                }
                String[] outStrs = new String[strs.size()];
                for (int i = 0; i < strs.size(); i++)
                    outStrs[i] = (String) strs.elementAt(i);
                return outStrs;
            } catch (Exception e) {
                return def;
            }
        }

        return def;
    }

    public boolean getBooleanProperty(String name) {
        String val = getStringProperty(name);
        if (val == null) return false;

        return new Boolean(val).booleanValue();
    }

    public boolean getBooleanProperty(String name, boolean def) {
        String val = getStringProperty(name);
        if (val == null) return def;

        return new Boolean(val).booleanValue();
    }

    public byte getByteProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public byte getByteProperty(String name, byte def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Byte.parseByte(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public char getCharProperty(String name) {
        String param = getStringProperty(name);
        if (param == null) return '\0';

        if (param.length() == 0) return '\0';

        return param.charAt(0);
    }

    public char getCharProperty(String name, char def) {
        String param = getStringProperty(name);
        if (param == null) return def;

        if (param.length() == 0) return def;

        return param.charAt(0);
    }

    public double getDoubleProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public double getDoubleProperty(String name, double def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public float getFloatProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public float getFloatProperty(String name, float def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Float.parseFloat(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int getIntProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int getIntProperty(String name, int def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Integer.parseInt(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public int[] getIntArrayProperty(String name) throws NumberFormatException {
        return getIntArrayProperty(name, null);
    }

    public int[] getIntArrayProperty(String name, int[] def)
            throws NumberFormatException {
        String vals = getStringProperty(name);
        if (vals == null) return def;

        if (vals != null && !vals.trim().equals("")) {
            StringTokenizer stok = new StringTokenizer(vals, ",");
            Vector ints = new Vector();
            try {
                while (stok.hasMoreTokens()) {
                    try {
                        ints.addElement(new Integer(stok.nextToken()));
                    } catch (NumberFormatException nfe) {
                        throw new NumberFormatException(" '" + vals + "'");
                    }
                }
                int[] outInts = new int[ints.size()];
                for (int i = 0; i < ints.size(); i++)
                    outInts[i] = ((Integer) ints.elementAt(i)).intValue();
                return outInts;
            } catch (Exception e) {
                return def;
            }
        }

        return def;
    }

    public long getLongProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public long getLongProperty(String name, long def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Long.parseLong(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public short getShortProperty(String name) throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) throw new NumberFormatException(" null string");

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public short getShortProperty(String name, short def)
            throws NumberFormatException {
        String val = getStringProperty(name);
        if (val == null) return def;

        try {
            return Short.parseShort(val);
        } catch (NumberFormatException nfe) {
            throw new NumberFormatException(" '" + val + "'");
        }
    }

    public String[] getPropertyGroups(String prefix) {
        Enumeration keys = props.propertyNames();
        HashMap groups = new HashMap(10);

        if (!prefix.endsWith(".")) prefix += ".";

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {
                String groupName = key.substring(prefix.length(), key.indexOf(
                        '.', prefix.length()));
                groups.put(groupName, groupName);
            }
        }

        return (String[]) groups.values().toArray(new String[groups.size()]);
    }

    public Properties getPropertyGroup(String prefix) {
        return getPropertyGroup(prefix, false);
    }

    public Properties getPropertyGroup(String prefix, boolean stripPrefix) {
        Enumeration keys = props.propertyNames();
        Properties group = new Properties();

        if (!prefix.endsWith(".")) prefix += ".";

        while (keys.hasMoreElements()) {
            String key = (String) keys.nextElement();
            if (key.startsWith(prefix)) {
                if (stripPrefix) group.put(key.substring(prefix.length()),
                        props.getProperty(key));
                else
                    group.put(key, props.getProperty(key));
            }
        }

        return group;
    }
}
