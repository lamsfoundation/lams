/***************************************************************************
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
 * ************************************************************************
 */
package org.lamsfoundation.lams.util.wddx;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import com.allaire.wddx.WddxDeserializationException;
import com.allaire.wddx.WddxDeserializer;
import com.allaire.wddx.WddxSerializer;


/**
 * @author Manpreet Minhas 
 */
public class WDDXProcessor {
	private static Logger logger = Logger.getLogger(WDDXProcessor.class.getName());

	WDDXProcessor m_theRealMe = null;
	
	/**
	 * Constructor for Serialiser. Private to ensure singleton behaviour
	 */
	private WDDXProcessor() 
	{
		super();
	}


	WDDXProcessor getInstance() 
	{
		if ( m_theRealMe == null )
			m_theRealMe = new WDDXProcessor();
		return m_theRealMe;
	}
	
	/** Replace any "%0D0A" in the wddx packet with true java characters
	 * Flash clients will put in "%0D0A" to get around the Flash code 
	 * removing newlines before creating the packet. 
	 * <p>
	 * Made public as some parts of the system use the same format when
	 * sending from Flash clients, but not via a wddx packet (just a normal post).
	 */
	public static String replaceNewline(String inputPacket)
	{
		String ret = null;
		if ( inputPacket != null )
		{
			ret = StringUtils.replace(inputPacket,"%0D%0A","\r\n");
		}
		return ret;
	}

    /**
     * Deserialize an arbitrary packet.
     */
    public static Object deserialize(String wddxPacket)
		throws WddxDeserializationException
    {
    	String replacedString = replaceNewline(wddxPacket);
    
        // Create an input source (org.xml.sax.InputSource) bound to the packet
        InputSource tempSource = new InputSource(new StringReader(replacedString));

        // Create a WDDX deserializer (com.allaire.wddx.WddxDeserializer)
        WddxDeserializer tempDeserializer = new WddxDeserializer("org.apache.xerces.parsers.SAXParser");

        // Deserialize the WDDX packet
        Object result;
		try
		{
			result = tempDeserializer.deserialize(tempSource);
		}
		catch (IOException e)
		{
			throw new WddxDeserializationException( e );
		}
        
        return result;
    }

    /**
     * Serialize an arbitrary packet.
     */
    public static String serialize(Object data) throws IOException
    {
        // Create a WDDX serializer
        WddxSerializer tempws = new WddxSerializer();

        // Create a writer to store the generated WDDX
        StringWriter tempsw = new StringWriter();
        // Serialize the data
        tempws.serialize(data, tempsw);        
        // Return the WDDX packet
        return tempsw.toString();
    }
    
    /** Convert a string to an int, based on how WDDX usually passes numbers.
	 * @param identifier - name of value being converted - using in the exception thrown
	 *   if the number cannot be converted.
	 * @param value - the value to be converted
	 * @return int value
	 * @throws Exception - will contain a message "Unable to convert value (identifier): (value) to an int"
	 */
	public static int convertToInt( String identifier, Object value)
		throws WDDXProcessorConversionException 
	{
		int result=-255;		
		if ( value == null )
		{
			throw new WDDXProcessorConversionException(identifier+" is null, cannot convert to an int");
		}
		
		try {
			// WDDX should give us a double 
			result = ((Number)value).intValue();
			return(result);
		} catch (Exception e) {}
		
		try {
			String textValue = (String) value;
			if ( textValue.length() == 0 )
				return -1;

			double dTemp = Double.parseDouble(textValue);
			result = (int) dTemp;
		} catch ( Exception e2) {
			throw new WDDXProcessorConversionException("Unable to convert value "+identifier+":"+value+" to an int");
		}
		return(result);
	}

	/**
	 * Some conversion doesn't allow null value. Doing so will result in hidden
	 * nullpointer exception. We do need this helper function.
	 * 
	 * @param identifier
	 * @param value
	 * @return converted integer.
	 * @throws WDDXProcessorConversionException
	 * @author Jacky Fang
	 */
	public static Integer nullSafeCovertToInteger(String identifier,Object value) throws WDDXProcessorConversionException
	{
	    if(value == null)
            throw new IllegalArgumentException("["+identifier+"] is null. We can't convert null value to integer");
	    //delegate to convertToInteger
	    return convertToInteger(identifier,value);
	}
   	/** Convert a string to an integer, based on how WDDX usually passes numbers.
   	 * As it is an integer, if the field is blank (or not there), then return null
	 * @param identifier - name of value being converted - using in the exception thrown
	 *   if the number cannot be converted.
	 * @param value - the value to be converted
	 * @return integer value or null
	 * @throws Exception - will contain a message "Unable to convert value (identifier): (value) to an int"
	 */
	public static Integer convertToInteger( String identifier, Object value)
		throws WDDXProcessorConversionException 
	{
		Integer result = null;		
		if ( value == null )
		    return null;
		
		try {
			// WDDX should give us a double 
			result = new Integer (((Number)value).intValue() );
			return(result);
		} catch (Exception e) {}
		
		try {
			String textValue = (String) value;
			if ( textValue.length() == 0 )
				return null;
			
			int posPeriod = textValue.indexOf('.');
			if ( posPeriod > 0 )
			{
				textValue = textValue.substring(0,posPeriod);
			}
			result = new Integer (textValue);
		} catch ( Exception e2) {
			throw new WDDXProcessorConversionException("Unable to convert value "+identifier+":"+value+" to an int");
		}
		return(result);
	}

	/** Convert a String to an Long, based on how WDDX usually passes numbers.
   	 * As it is a Long, if the field is blank (or not there), then return null
	 * @param identifier - name of value being converted - using in the exception thrown
	 *   if the number cannot be converted.
	 * @param value - the value to be converted
	 * @return Long value or null
	 * @throws Exception - will contain a message "Unable to convert value (identifier): (value) to an int"
	 */
	public static Long convertToLong( String identifier, Object value)
		throws WDDXProcessorConversionException 
	{
		Long result = null;		
		if ( value == null )
		{
			return null;
		}
		
		try {
			// WDDX should give us a double 
			result = new Long(((Number)value).intValue() );
			return(result);
		} catch (Exception e) {}
		
		try {
			String textValue = (String) value;
			if ( textValue.length() == 0 )
				return null;

			int posPeriod = textValue.indexOf('.');
			if ( posPeriod > 0 )
			{
				textValue = textValue.substring(0,posPeriod);
			}
			result = new Long (textValue);
		} catch ( Exception e2) {
			throw new WDDXProcessorConversionException("Unable to convert value "+identifier+":"+value+" to an int");
		}
		return(result);
	}

	/** Convert a String/Boolean to an Boolean
	 * @param identifier - name of value being converted - using in the exception thrown
	 *   if the value cannot be converted.
	 * @param value - the value to be converted
	 * @return Boolean value or null
	 * @throws Exception - will contain a message "Unable to convert value (identifier): (value) to an int"
	 */
	public static Boolean convertToBoolean( String identifier, Object value)
		throws WDDXProcessorConversionException 
	{
		Boolean result = null;		
		if ( value == null )
		{
			return null;
		}
		
		try {
			result = (Boolean) value;
			logger.debug("identifier "+identifier+" was Boolean value "+value+" becomes "+result);
			return(result);
		} catch (Exception e) {}
		
		try {
			String textValue = (String) value;
			if ( textValue.length() == 0 )
				return null;

			result = new Boolean (textValue);
			logger.debug("identifier "+identifier+" was String value "+value+" becomes "+result);
		return(result);
		} catch ( Exception e2) {
			throw new WDDXProcessorConversionException("Unable to convert value "+identifier+":"+value+" to a Boolean");
		}
	}
}
