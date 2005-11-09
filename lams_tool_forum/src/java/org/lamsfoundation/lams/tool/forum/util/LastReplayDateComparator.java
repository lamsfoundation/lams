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

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.forum.persistence.Message;

public class LastReplayDateComparator implements Comparator {

	private static final Logger log = Logger.getLogger(LastReplayDateComparator.class);
	
	public int compare(Object arg0, Object arg1) {
		if(!(arg0 instanceof Message) || !(arg1 instanceof Message)){
			log.error("Topic is not Message instance.");
			return 0;
		}
		return ((Message)arg0).getUpdated().after(((Message)arg1).getUpdated())?1:-1;
	}

}
