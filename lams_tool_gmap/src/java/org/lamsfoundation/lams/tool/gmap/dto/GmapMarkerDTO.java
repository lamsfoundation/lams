/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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


package org.lamsfoundation.lams.tool.gmap.dto;

import java.util.Date;

import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;

/**
 * DTO for GmapMarker containts all the same variables as GMapMarker
 * but with session added
 * 
 * @author lfoxton
 *
 */
public class GmapMarkerDTO {
    public Long uid;
    public Double longitude;
    public Double latitude;
    public String infoWindowMessage;
    public String title;
    public Date created;
    public Date updated;
    public boolean isAuthored;
    public GmapUserDTO createdBy;
    public GmapUserDTO updatedBy;
    public GmapSessionDTO gmapSession;

    public GmapUserDTO getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(GmapUserDTO createdBy) {
	this.createdBy = createdBy;
    }

    public GmapUserDTO getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(GmapUserDTO updatedBy) {
	this.updatedBy = updatedBy;
    }

    public GmapMarkerDTO() {
    }

    public GmapMarkerDTO(GmapMarker marker) {
	this.uid = marker.getUid();
	this.longitude = marker.getLongitude();
	this.latitude = marker.getLatitude();
	this.infoWindowMessage = marker.getInfoWindowMessage();
	this.title = marker.getTitle();
	this.created = marker.getCreated();
	this.updated = marker.getUpdated();
	this.isAuthored = marker.isAuthored();
	this.createdBy = marker.getCreatedBy() == null ? null : new GmapUserDTO(marker.getCreatedBy());
	this.updatedBy = marker.getUpdatedBy() == null ? null : new GmapUserDTO(marker.getCreatedBy());
	this.gmapSession = new GmapSessionDTO(marker.getGmapSession());
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Double getLongitude() {
	return longitude;
    }

    public void setLongitude(Double longitude) {
	this.longitude = longitude;
    }

    public Double getLatitude() {
	return latitude;
    }

    public void setLatitude(Double latitude) {
	this.latitude = latitude;
    }

    public String getInfoWindowMessage() {
	return infoWindowMessage;
    }

    public void setInfoWindowMessage(String infoWindowMessage) {
	this.infoWindowMessage = infoWindowMessage;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Date getCreated() {
	return created;
    }

    public void setCreated(Date created) {
	this.created = created;
    }

    public Date getUpdated() {
	return updated;
    }

    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    public boolean isAuthored() {
	return isAuthored;
    }

    public void setAuthored(boolean isAuthored) {
	this.isAuthored = isAuthored;
    }

    public boolean getIsAuthored() {
	return isAuthored;
    }

    public GmapSessionDTO getGmapSession() {
	return gmapSession;
    }

    public void setGmapSession(GmapSessionDTO gmapSession) {
	this.gmapSession = gmapSession;
    }
}
