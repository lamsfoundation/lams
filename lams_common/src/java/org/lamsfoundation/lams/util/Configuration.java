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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.util;

import java.util.Map;

/**
 * Configuration Object
 * 
 * @author Fei Yang
 */
public class Configuration {

	private static Map items = ConfigurationLoader.load();
	
	public static String get(String key)
	{
		if ((items != null)&&(items.get(key)!=null))
			return (String) items.get(key);
		return null;			
	}

	public static int getAsInt(String key)
	{
		if ((items != null)&&(items.get(key)!=null))
			//could throw NumberFormatException which is a RuntimeException
			return new Integer((String)items.get(key)).intValue();
		return -1;			
	}

	public static boolean getAsBoolean(String key)
	{
		if((items != null)&&(items.get(key)!=null))
		{
			return new Boolean((String)items.get(key)).booleanValue();
		}
		return false;
	}
	
	public String toString()
	{
		return "Configuration items:"
			+ ( items!=null ? items.toString() : "none" ) ;
	}
	
	/**
	 * Retrieves the date in which the specified dictionary was created.
	 * If the dictionary isnt found, null is returned.
	 * @param language
	 * @return
	 */
	public static String getDictionaryDateForLanguage(String dictionary)
	{
		if ((items != null)&&(items.get(ConfigurationKeys.DICTIONARY_DATES)!=null))
		{
			Map map = (Map)items.get(ConfigurationKeys.DICTIONARY_DATES);
			if (map!=null && map.get(dictionary) != null)
			{
				return (String)map.get(dictionary);
			}
		}
		return null;
	}

}
