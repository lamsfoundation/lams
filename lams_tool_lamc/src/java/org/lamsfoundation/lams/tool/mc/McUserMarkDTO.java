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

import java.util.LinkedList;

import org.apache.commons.lang.builder.ToStringBuilder;


/**
 * <p> DTO that hols user marks
 * </p>
 * 
 * @author Ozgur Demirtas
 */
public class McUserMarkDTO implements Comparable
{
    private String sessionId;
    private String sessionName;
    private String queUsrId;
    private String userName;
    private LinkedList marks; 
    private String totalMark;

	public String toString() {
        return new ToStringBuilder(this)
        	.append("Listing UserMarkDTO:")
            .append("sessionId", sessionId)
            .append("queUsrId", queUsrId)
            .append("userName", userName)
            .append("marks", marks)            
            .append("totalMark", totalMark)            
            .toString();
    }

	
    /**
     * @return Returns the marks.
     */
    public LinkedList getMarks() {
        return marks;
    }
    /**
     * @param marks The marks to set.
     */
    public void setMarks(LinkedList marks) {
        this.marks = marks;
    }
    /**
     * @return Returns the queUsrId.
     */
    public String getQueUsrId() {
        return queUsrId;
    }
    /**
     * @param queUsrId The queUsrId to set.
     */
    public void setQueUsrId(String queUsrId) {
        this.queUsrId = queUsrId;
    }
    /**
     * @return Returns the userName.
     */
    public String getUserName() {
        return userName;
    }
    /**
     * @param userName The userName to set.
     */
    public void setUserName(String userName) {
        this.userName = userName;
    }
	
	public int compareTo(Object o)
    {
	    McUserMarkDTO mcUserMarkDTO = (McUserMarkDTO) o;
     
        if (mcUserMarkDTO == null)
        	return 1;
		else
			return (int) (new Long(queUsrId).longValue() - new Long(mcUserMarkDTO.queUsrId).longValue());
    }
	
  
    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
        return sessionId;
    }
    /**
     * @param sessionId The sessionId to set.
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }
    /**
     * @return Returns the totalMark.
     */
    public String getTotalMark() {
        return totalMark;
    }
    /**
     * @param totalMark The totalMark to set.
     */
    public void setTotalMark(String totalMark) {
        this.totalMark = totalMark;
    }
    /**
     * @return Returns the sessionName.
     */
    public String getSessionName() {
        return sessionName;
    }
    /**
     * @param sessionName The sessionName to set.
     */
    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }
}
