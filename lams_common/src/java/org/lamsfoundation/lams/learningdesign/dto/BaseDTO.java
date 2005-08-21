/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Manpreet Minhas
 * 
 * This class acts as base class for all authoring DTO's.
 * 
 * FLASH returns Long and Integer values as Double.
 * 
 * For example, a value of 10 would returned as 10.0   
 * 
 * This class contains utility methods for converting this 
 * Double value into the required data types.
 * 
 * All Integer and Long null values are represented as
 * -111111.0 in flash, which are indicated by 	
 * 		WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER and
 * 		WDDXTAGS.NUMERIC_NULL_VALUE_LONG 
 * attributes defined in the WDDXTAGS interface.
 * So if the object being converted is equivalent to this
 * value it returns null rather than returning -111111. 
 * 
 */
public class BaseDTO {	
	/**
	 * This method converts the passed
	 * Object into an Integer
	 * 
	 * @param ob The object to be converted
	 * @return Integer The required value
	 */
	public Integer convertToInteger(Object ob){
	    if ( ob != null ) {
			Double doub = (Double)ob;
			Integer integer = new Integer(doub.intValue());
			if(!integer.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
				return integer;
	    }
	    return null;
	}
	/**
	 * This method converts the passed
	 * Object into Long
	 * 
	 * @param ob The object to be converted
	 * @return Long The required value
	 */
	public Long convertToLong(Object ob){
	    if ( ob != null ) {
			Double doub = (Double)ob;
			Long longValue = new Long(doub.longValue());
			if(!longValue.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
				return longValue;
	    }
		return null;
	}
}
