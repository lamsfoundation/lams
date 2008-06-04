package org.lamsfoundation.lams.tool.gmap.dto;

import java.util.Date;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapUser;

/**
 * 
 * @author lfoxton
 *
 */
public class GmapMarkerDTO 
{
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

	public GmapMarkerDTO() {}
	
	public GmapMarkerDTO(GmapMarker marker)
	{
		this.uid = marker.getUid();
		this.longitude = marker.getLongitude();
		this.latitude = marker.getLatitude();
		this.infoWindowMessage = marker.getInfoWindowMessage();
		this.title = marker.getTitle();
		this.created = marker.getCreated();
		this.updated = marker.getUpdated();
		this.isAuthored = marker.isAuthored();
		this.createdBy = new GmapUserDTO(marker.getCreatedBy());
		this.updatedBy = new GmapUserDTO(marker.getCreatedBy());
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

}
