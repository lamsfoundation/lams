/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.mc;

import org.apache.commons.lang.builder.ToStringBuilder;



/**
 * <p> DTO that holds temporary data
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McTempDataHolderDTO implements Comparable
{
    protected String learnerMark;
    protected String totalUserWeight;

    
	public int compareTo(Object o)
    {
	    McTempDataHolderDTO mcTempDataHolderDTO = (McTempDataHolderDTO) o;
     
        if (mcTempDataHolderDTO == null)
        	return 1;
		else
			return 0;
    }

	public String toString() {
        return new ToStringBuilder(this)
            .append("learnerMark: ", learnerMark)
            .append("totalUserWeight : ", totalUserWeight)
            .toString();
    }
    
    /**
     * @return Returns the learnerMark.
     */
    public String getLearnerMark() {
        return learnerMark;
    }
    /**
     * @param learnerMark The learnerMark to set.
     */
    public void setLearnerMark(String learnerMark) {
        this.learnerMark = learnerMark;
    }
    /**
     * @return Returns the totalUserWeight.
     */
    public String getTotalUserWeight() {
        return totalUserWeight;
    }
    /**
     * @param totalUserWeight The totalUserWeight to set.
     */
    public void setTotalUserWeight(String totalUserWeight) {
        this.totalUserWeight = totalUserWeight;
    }
}
