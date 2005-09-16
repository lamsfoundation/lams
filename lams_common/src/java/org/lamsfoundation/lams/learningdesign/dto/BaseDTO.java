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

import java.util.Date;
import java.util.Hashtable;

import org.lamsfoundation.lams.util.wddx.WDDXProcessor;
import org.lamsfoundation.lams.util.wddx.WDDXProcessorConversionException;


/**
 * @author Manpreet Minhas, Fiona Malikoff
 * 
 * This class acts as base class for all authoring DTO's.
 * It will contain any helper methods for DTOs. 
 * 
 */
public class BaseDTO {	

	/** Get an Integer from a hashtable, based on the given key.
	 * If the key doesn't exist, return null.
	 * Uses WDDXProcessor.getInteger(), which handles string inputs
	 * and Flash null values.
	 * @param table Hashtable containing values from Flash. Must not be null
	 * or NullPointerException will be thrown.
	 * @param key Key of entry in table. Must not be null
	 * or NullPointerException will be thrown.
	 * @return Integer value of entry in table, or null.
	 * @throws WDDXProcessorConversionException if the value cannot be converted.
	 * @author Fiona Malikoff
	 */ 
	protected Integer convertToInteger(Hashtable table, String key) 
		throws WDDXProcessorConversionException {
		if(table.containsKey(key)) {
			return WDDXProcessor.convertToInteger(key, table.get(key));
		}
		return null;
	}

	/** Get an Long from a hashtable, based on the given key.
	 * If the key doesn't exist, return null.
	 * Uses WDDXProcessor.getLong(), which handles string inputs
	 * and Flash null values.
	 * @param table Hashtable containing values from Flash. Must not be null
	 * or NullPointerException will be thrown.
	 * @param key Key of entry in table. Must not be null
	 * or NullPointerException will be thrown.
	 * @return Integer value of entry in table, or null.
	 * @throws WDDXProcessorConversionException if the value cannot be converted.
	 * @author Fiona Malikoff
	 */ 
	protected Long convertToLong(Hashtable table, String key) 
		throws WDDXProcessorConversionException {
		if(table.containsKey(key)) {
			return WDDXProcessor.convertToLong(key, table.get(key));
		}
		return null;
	}
	
	/** Get an String from a hashtable, based on the given key.
	 * If the key doesn't exist, return null.
	 * @param table Hashtable containing values from Flash. Must not be null
	 * or NullPointerException will be thrown.
	 * @param key Key of entry in table. Must not be null
	 * or NullPointerException will be thrown.
	 * @return Integer value of entry in table, or null.
	 * @throws WDDXProcessorConversionException if the value cannot be converted.
	 * @author Fiona Malikoff
	 */ 
	protected String convertToString(Hashtable table, String key) 
		throws WDDXProcessorConversionException {
		if(table.containsKey(key)) {
			return WDDXProcessor.convertToString(key, table.get(key));
		}
		return null;
	}

	/** Get an Boolean from a hashtable, based on the given key.
	 * If the key doesn't exist, return null.
	 * @param table Hashtable containing values from Flash. Must not be null
	 * or NullPointerException will be thrown.
	 * @param key Key of entry in table. Must not be null
	 * or NullPointerException will be thrown.
	 * @return Integer value of entry in table, or null.
	 * @throws WDDXProcessorConversionException if the value cannot be converted.
	 * @author Fiona Malikoff
	 */ 
	protected Boolean convertToBoolean(Hashtable table, String key) 
		throws WDDXProcessorConversionException {
		if(table.containsKey(key)) {
			return WDDXProcessor.convertToBoolean(key, table.get(key));
		}
		return null;
	}
	
	/** Get an Date from a hashtable, based on the given key.
	 * If the key doesn't exist, return null.
	 * @param table Hashtable containing values from Flash. Must not be null
	 * or NullPointerException will be thrown.
	 * @param key Key of entry in table. Must not be null
	 * or NullPointerException will be thrown.
	 * @return Integer value of entry in table, or null.
	 * @throws WDDXProcessorConversionException if the value cannot be converted.
	 * @author Fiona Malikoff
	 */ 
	protected Date convertToDate(Hashtable table, String key) 
		throws WDDXProcessorConversionException {
		if(table.containsKey(key)) {
			return WDDXProcessor.convertToDate(key, table.get(key));
		}
		return null;
	}

}
