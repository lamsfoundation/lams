/* 
  Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

  This program is free software; you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation; either version 2 of the License, or
  (at your option) any later version.

  This program is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with this program; if not, write to the Free Software
  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
  USA

  http://www.gnu.org/licenses/gpl.txt 
*/

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
    public Boolean getBoolean() throws ValueFormatException;
}