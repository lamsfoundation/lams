package org.lamsfoundation.lams.contentrepository;

import java.util.Calendar;

/**
 * A value is an object which is explicitly marked with a type. This type can
 * be used for hints to the persistence layer as to how to store/retrieve
 * the value.
 */
public interface IValue {

    /**
     * @return the type of this Value. See {@link PropertyType} for values.
     */
    public int getType();

    /**
	 * Returns a string representation of the value. 
	 *
     * @throws ValueFormatException If able to convert the value to a string.
     */
    public String getString() throws ValueFormatException;

    /**
	 * Returns a double representation of the value. 
	 *
     * @throws ValueFormatException If able to convert the value to a double.
     */
    public double getDouble() throws ValueFormatException;

    /**
	 * Returns a Calendar representation of the value. 
	 *
     * @throws ValueFormatException If able to convert the value to a Calendar.
     */
    public Calendar getDate() throws ValueFormatException;

    /**
	 * Returns a long representation of the value. 
	 *
     * @throws ValueFormatException If able to convert the value to a long.
     */
    public long getLong() throws ValueFormatException;

    /**
	 * Returns a boolean representation of the value. 
	 *
     * @throws ValueFormatException If able to convert the value to a boolean.
     */
    public boolean getBoolean() throws ValueFormatException;
}