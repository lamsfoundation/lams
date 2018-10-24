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

package org.lamsfoundation.lams.tool.mc.dto;

import java.util.Map;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * <p>
 * DTO that hols session user marks
 * </p>
 *
 * @author Ozgur Demirtas
 */
public class McSessionMarkDTO implements Comparable {
    private String sessionId;
    private String sessionName;
    private Map<String, McUserMarkDTO> userMarks;

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("Listing SessionMarkDTO: ").append("sessionId: ", sessionId)
		.append("sessionName: ", sessionName).append("userMarks: ", userMarks).toString();
    }

    @Override
    public int compareTo(Object o) {
	McSessionMarkDTO mcSessionMarkDTO = (McSessionMarkDTO) o;

	if (mcSessionMarkDTO == null) {
	    return 1;
	} else {
	    return (int) (new Long(sessionId).longValue() - new Long(mcSessionMarkDTO.sessionId).longValue());
	}
    }

    /**
     * @return Returns the sessionId.
     */
    public String getSessionId() {
	return sessionId;
    }

    /**
     * @param sessionId
     *            The sessionId to set.
     */
    public void setSessionId(String sessionId) {
	this.sessionId = sessionId;
    }

    /**
     * @return Returns the sessionName.
     */
    public String getSessionName() {
	return sessionName;
    }

    /**
     * @param sessionName
     *            The sessionName to set.
     */
    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    /**
     * @return Returns the userMarks.
     */
    public Map<String, McUserMarkDTO> getUserMarks() {
	return userMarks;
    }

    /**
     * @param userMarks
     *            The userMarks to set.
     */
    public void setUserMarks(Map<String, McUserMarkDTO> userMarks) {
	this.userMarks = userMarks;
    }
}
