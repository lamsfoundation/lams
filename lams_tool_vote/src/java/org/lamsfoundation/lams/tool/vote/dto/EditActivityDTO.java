/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */
package org.lamsfoundation.lams.tool.vote.dto;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> DTO that holds question and user attempts data for jsp purposes
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class EditActivityDTO implements Comparable
{
    private String monitoredContentInUse;
	
	
    /**
     * @return Returns the monitoredContentInUse.
     */
    public String getMonitoredContentInUse() {
        return monitoredContentInUse;
    }
    /**
     * @param monitoredContentInUse The monitoredContentInUse to set.
     */
    public void setMonitoredContentInUse(String monitoredContentInUse) {
        this.monitoredContentInUse = monitoredContentInUse;
    }
	public String toString() {
        return new ToStringBuilder(this)
            .append("monitoredContentInUse", monitoredContentInUse)
            .toString();
    }
	
	public int compareTo(Object o)
    {
	    EditActivityDTO editActivityDTO = (EditActivityDTO) o;
     
        if (editActivityDTO == null)
        	return 1;
		else
			return 0;
    }
	
}
