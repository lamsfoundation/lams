/*
 * Created on Jan 11, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.authoring;

import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Hashtable;
import java.util.Vector;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class HashtableUtils {

	/*
	 * Takes a string (possibly null) and returns a not null string
	 */
	public static String getValue( String possValue )
	{
		return ( possValue==null ? "" : possValue );	
	}

	/*
	 * Takes a Long (possibly null) and returns a not null Long
	 */
	public static Long getIdLong ( Long possValue )
	{
		return ( possValue==null ? WDDXTAGS.NUMERIC_NULL_VALUE_LONG : possValue );
	}

	/*
	 * Takes a Integer (possibly null) and returns a not null Integer
	 */
	public static Integer getIdInteger ( Integer possValue )
	{
		return ( possValue==null ? WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER : possValue );
	}
	
	public static Date getIdDate(Date possValue){
		return (possValue==null? WDDXTAGS.DATE_NULL_VALUE : possValue);
	}
	
	public static Boolean getBoolean(Boolean possValue){
		return (possValue==null?WDDXTAGS.BOOLEAN_NULL_VALUE : possValue);
	}

    /**
     * Helper function to retrieve dataset from Hashtable. As it is possible to
     * receive object array inside a hashtable. Simply cast it to Collection
     * will generate ClassCasting Exception. Therefore, we need to create this
     * helper function.
     * 
     * @param clientObj
     * @param questionSet
     * @return return vector of data
     * @author Jacky Fang
     */
    public static Vector getCollectionDataFromHashTable(String identifier,Hashtable clientObj)
    {
        Vector dataSet = new Vector();
        if(clientObj.get(identifier)instanceof Collection)
            dataSet = (Vector)clientObj.get(identifier);
        else if( clientObj.get(identifier)!=null)
            dataSet.addAll(Arrays.asList((Object []) clientObj.get(identifier)));
        return dataSet;
    }


	




}
