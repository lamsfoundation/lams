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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool.qa;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.log4j.Logger;

/**
 * @author Ozgur Demirtas
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
	   return key1 - key2;
	  }                                    

	 public boolean equals(Object o) {
	    String s = (String)o;
	  	return compare(this, o)==0;
	  }
}
