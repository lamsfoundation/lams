/*
 * Created on Apr 8, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.learningdesign.dto;

import org.lamsfoundation.lams.util.wddx.WDDXTAGS;

/**
 * @author Minhas
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class BaseDTO {
	
	public Integer convertToInteger(Object ob){
		Double doub = (Double)ob;
		Integer integer = new Integer(doub.intValue());
		if(!integer.equals(WDDXTAGS.NUMERIC_NULL_VALUE_INTEGER))
			return integer;
		else
			return null;
	}
	public Long convertToLong(Object ob){
		Double doub = (Double)ob;
		Long longValue = new Long(doub.longValue());
		if(!longValue.equals(WDDXTAGS.NUMERIC_NULL_VALUE_LONG))
			return longValue;
		else
			return null;
	}
}
