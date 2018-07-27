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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.contentrepository;

import java.util.Calendar;

import org.lamsfoundation.lams.contentrepository.exception.ValueFormatException;

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
     * @throws ValueFormatException
     *             If able to convert the value to a string.
     */
    public String getString() throws ValueFormatException;

    /**
     * Returns a double representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a double.
     */
    public double getDouble() throws ValueFormatException;

    /**
     * Returns a Calendar representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a Calendar.
     */
    public Calendar getDate() throws ValueFormatException;

    /**
     * Returns a long representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a long.
     */
    public long getLong() throws ValueFormatException;

    /**
     * Returns a boolean representation of the value.
     *
     * @throws ValueFormatException
     *             If able to convert the value to a boolean.
     */
    public Boolean getBoolean() throws ValueFormatException;
}