/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.forum.util;

import java.util.Comparator;
import java.util.Date;

import org.apache.log4j.Logger;

public class DateComparator implements Comparator {

	private static final Logger log = Logger.getLogger(DateComparator.class);
	
	public int compare(Object arg0, Object arg1) {
		if(!(arg0 instanceof Date) || !(arg1 instanceof Date)){
			log.error("Topic is not Date instance.");
			return 0;
		}
		return ((Date)arg0).after(((Date)arg0))?1:-1;
	}

}
