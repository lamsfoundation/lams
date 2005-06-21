/*
 * Created on 20/04/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.log4j.Logger;

/**
 * @author ozgurd
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 *  A comparator implementation that can be used as a constructor to collections. 
 *  The TreeMap in the web layer makes use of it.
 * 
 */
public class QaComparator implements Comparator, Serializable {
	static Logger logger = Logger.getLogger(QaComparator.class.getName());
	
	 public int compare(Object o1, Object o2) {
	   String s1 = (String)o1;
	   String s2 = (String)o2;
	 	   
	   int key1=new Long(s1).intValue();
	   int key2=new Long(s2).intValue();
	   //logger.debug(logger + " " + this.getClass().getName() +  "comparing key1 and key2:"  + key1 + " and " + key2);
	   return key1 - key2;
	  }                                    

	 public boolean equals(Object o) {
	    String s = (String)o;
	  	return compare(this, o)==0;
	  }
}
