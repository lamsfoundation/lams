/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool;

/**
 * The ToolOutputValue is the actual output data from the tool, as wrapped up in a
 * ToolOutput object and defined by a ToolOutputDefinition.
 */
public class ToolOutputValue {
    private OutputType type;
    private Object value;

    /**
     * Create a ToolOutputValue based on a Boolean. This will create a value of type OUTPUT_BOOLEAN
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(Boolean val) {
	this.type = OutputType.OUTPUT_BOOLEAN;
	this.value = val;
    }

    /**
     * Create a ToolOutputValue based on a Long. This will create a value of type OUTPUT_LONG.
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(Long val) {
	this.type = OutputType.OUTPUT_LONG;
	this.value = val;
    }

    /**
     * Create a ToolOutputValue based on a Integer. This will create a value of type OUTPUT_LONG.
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(Integer val) {
	this.type = OutputType.OUTPUT_LONG;
	this.value = new Long(val.longValue());
    }

    /**
     * Create a ToolOutputValue based on a Double. This will create a value of type OUTPUT_DOUBLE.
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(Double val) {
	this.type = OutputType.OUTPUT_DOUBLE;
	this.value = val;
    }

    /**
     * Create a ToolOutputValue based on a Integer. This will create a value of type OUTPUT_DOUBLE.
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(Float val) {
	this.type = OutputType.OUTPUT_DOUBLE;
	this.value = new Double(val.doubleValue());
    }

    /**
     * Create a ToolOutputValue based on a String. This will create a value of type OUTPUT_STRING.
     *
     * @param val
     *            mandatory
     */
    public ToolOutputValue(String val) {
	this.type = OutputType.OUTPUT_STRING;
	this.value = val;
    }

    /**
     * Create a ToolOutputValue based on a String. It tries to convert it to the type defined by the type.
     *
     * @throws ToolOutputFormatException
     *             If unable to convert the value to the requested type.
     * @param val
     *            mandatory
     */
    public ToolOutputValue(String val, OutputType type) {
	this.type = type;
	try {
	    switch (type) {
		case OUTPUT_LONG:
		    this.value = Long.parseLong(val);
		    break;

		case OUTPUT_DOUBLE:
		    this.value = Double.parseDouble(val);
		    break;

		case OUTPUT_BOOLEAN:
		    this.value = Boolean.parseBoolean(val);
		    break;

		case OUTPUT_STRING:
		case OUTPUT_COMPLEX:
		default:
		    this.value = val;
	    }
	} catch (Exception e) {
	    throw new ToolOutputFormatException("Unable to convert value " + value + " to Long.", e);
	}
    }

    /**
     * Create a Complex ToolOutputValue based on an Object. This will create a value of type OUTPUT_COMPLEX. The
     * junk parameter is to ensure this method isn't "accidently" in place of one of the other constructors. ie to stop
     * accidental bugs!
     *
     * @param val
     *            mandatory
     * @param junk
     *            ignored
     */
    public ToolOutputValue(Object val, Boolean junk) {
	type = OutputType.OUTPUT_COMPLEX;
	value = val;
    }

    /**
     * @return the type of this Value.
     */
    public OutputType getType() {
	return type;
    }

    /**
     * Get the raw value. Useful if you want to know about its type or you have some other code will look at the
     * object's class directly. Normally you would use getString(), getLong(), etc as all the mucking around
     * with types is done for you.
     */
    public Object getValue() {
	return value;
    }

    /**
     * Returns a string representation of the value. Available for any type of output.
     *
     * @throws ToolOutputFormatException
     *             If unable to convert the value to a string.
     */
    public String getString() throws ToolOutputFormatException {
	switch (type) {
	    case OUTPUT_STRING:
		return (String) value;
	    case OUTPUT_COMPLEX:
		if (value instanceof Object[]) {
		    Object[] array = (Object[]) value;
		    StringBuilder result = new StringBuilder("[");
		    for (Object valuePart : array) {
			result.append(valuePart.toString()).append(",");
		    }
		    if (array.length > 0) {
			result.replace(result.length() - 1, result.length(), "]");
		    } else {
			result.append("]");
		    }

		    return result.toString();
		}
		return value.toString();
	    default:
		return value.toString();
	}
    }

    /**
     * Returns a Long representation of the value. This should always work for OUTPUT_LONG
     * and we will try to convert other types but that isn't guaranteed. A boolean value will convert to 1 or 0.
     * A double value may round.
     *
     * @throws ToolOutputFormatException
     *             If unable to convert the value to a Long.
     */
    public Long getLong() throws ToolOutputFormatException {

	try {
	    switch (type) {
		case OUTPUT_LONG:
		    return (Long) value;

		case OUTPUT_DOUBLE:
		    return new Long(((Double) value).longValue());

		case OUTPUT_BOOLEAN:
		    return ((Boolean) value).booleanValue() ? new Long(1) : new Long(0);

		case OUTPUT_STRING:
		    return Long.parseLong((String) value);

		case OUTPUT_COMPLEX:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Long.");

		default:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Long.");
	    }
	} catch (Exception e) {
	    throw new ToolOutputFormatException("Unable to convert value " + value + " to Long.", e);
	}

    }

    /**
     * Returns a Double representation of the value. This should always work for OUTPUT_DOUBLE
     * and we will try to convert other types but that isn't guaranteed. A boolean value will convert to 1 or 0.
     *
     * @throws ToolOutputFormatException
     *             If unable to convert the value to a Double.
     */
    public Double getDouble() throws ToolOutputFormatException {

	try {
	    switch (type) {
		case OUTPUT_LONG:
		    return new Double(((Long) value).doubleValue());

		case OUTPUT_DOUBLE:
		    return (Double) value;

		case OUTPUT_BOOLEAN:
		    return ((Boolean) value).booleanValue() ? new Double(1) : new Double(0);

		case OUTPUT_STRING:
		    return Double.parseDouble((String) value);

		case OUTPUT_COMPLEX:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Double.");

		default:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Double.");
	    }
	} catch (Exception e) {
	    throw new ToolOutputFormatException("Unable to convert value " + value + " to Double.", e);
	}

    }

    /**
     * Returns a boolean representation of the value. This should always work for OUTPUT_BOOLEAN
     * and we will try to convert other types but that isn't guaranteed. A numeric of 1 will be converted to true,
     * otherwise false.
     *
     * @throws ToolOutputFormatException
     *             If unable to convert the value to a boolean.
     */
    public Boolean getBoolean() throws ToolOutputFormatException {

	try {
	    switch (type) {
		case OUTPUT_LONG:
		    return ((Long) value).longValue() == 1;

		case OUTPUT_DOUBLE:
		    return ((Double) value).longValue() == 1;

		case OUTPUT_BOOLEAN:
		    return (Boolean) value;

		case OUTPUT_STRING:
		    return Boolean.parseBoolean((String) value);

		case OUTPUT_COMPLEX:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Boolean.");

		default:
		    throw new ToolOutputFormatException("Unable to convert value " + value + " to Boolean.");
	    }
	} catch (Exception e) {
	    throw new ToolOutputFormatException("Unable to convert value " + value + " to Boolean.", e);
	}

    }

    /**
     * Complex isn't handled for 2.1, so just return the value unchanged. Currently does that same thing as getValue
     */
    public Object getComplex() throws ToolOutputFormatException {
	return value;
    }
}
