/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.dokumaran.dto;

import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;

public class SessionDTO {

    private Long sessionId;
    private String sessionName;
    private String padId;
    private String readOnlyPadId;
    private boolean sessionFaulty;
    private ItemRatingDTO itemRatingDto;

    public Long getSessionId() {
	return sessionId;
    }

    public void setSessionId(Long sessionId) {
	this.sessionId = sessionId;
    }

    public String getSessionName() {
	return sessionName;
    }

    public void setSessionName(String sessionName) {
	this.sessionName = sessionName;
    }

    public String getPadId() {
	return padId;
    }

    public void setPadId(String padId) {
	this.padId = padId;
    }

    public String getReadOnlyPadId() {
	return readOnlyPadId;
    }

    public void setReadOnlyPadId(String readOnlyPadId) {
	this.readOnlyPadId = readOnlyPadId;
    }

    public boolean isSessionFaulty() {
	return sessionFaulty;
    }

    public void setSessionFaulty(boolean sessionFaulty) {
	this.sessionFaulty = sessionFaulty;
    }

    public ItemRatingDTO getItemRatingDto() {
	return itemRatingDto;
    }

    public void setItemRatingDto(ItemRatingDTO ratingDto) {
	this.itemRatingDto = ratingDto;
    }
}