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
