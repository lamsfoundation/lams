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


package org.lamsfoundation.lams.tool.gmap.model;

import java.util.Date;


/**
 * @author lfoxton
 * 
 * This is a POJO class for a marker object that represents a marker on a 
 * google map.
 * 
 * @hibernate.class table="tl_lagmap10_marker"
 */
public class GmapMarker{

	// Fields

	// Unique identifier
	private Long uid;		
	
	// Longitude of marker position
	private Long longitude;
	
	// Lattitude of marker position
	private Long latitude;
	
	// String address of marker if given
	private String address;
	
	// Marker colour, can be: ORANGE, PURPLE, YELLOW, GREEN, BLUE, RED, AQUA, WHITE, or GRAY
	private String colour;
	
	// A character that will display on the marker
	private char markerDisplayCharacter;
	
	// Info windo message that appears when you click the marker (in HTML)
	private String infoWindowMessage;
	
	// Text that appears when the user mouseovers the marker
	private String mouseoverText;
	
	// The date the marker was created
	private Date created;
	
	// The date the marker was updated
	private Date updated;
	
	// True if it is an authored marker, false if it is a learner marker
	private boolean isAuthored;
	
	// The gmap tool session if in learner, null if in author 
	private GmapSession toolSession;
	
	// The gmap instance that this marker is part of
	private Gmap gmap;
	
	// The user who created the marker
	private GmapUser createBy;
	
	// The last user to modify the marker
	private GmapUser modifiedBy;

	/**
	 * Generic POJO constructor
	 */
	public GmapMarker() {}

	
	/**
	 * Full Constructor
	 */
	public GmapMarker(Long uid, Long longitude, Long latitude, String address,
			String colour, char markerDisplayCharacter,
			String infoWindowMessage, String mouseoverText, Date created,
			Date updated, boolean isAuthored, GmapSession toolSession,
			Gmap gmap, GmapUser createBy, GmapUser modifiedBy) {
		this.uid = uid;
		this.longitude = longitude;
		this.latitude = latitude;
		this.address = address;
		this.colour = colour;
		this.markerDisplayCharacter = markerDisplayCharacter;
		this.infoWindowMessage = infoWindowMessage;
		this.mouseoverText = mouseoverText;
		this.created = created;
		this.updated = updated;
		this.isAuthored = isAuthored;
		this.toolSession = toolSession;
		this.gmap = gmap;
		this.createBy = createBy;
		this.modifiedBy = modifiedBy;
	}



	/**
	 * 
	 * @return
	 * @hibernate.id column="uid" generator-class="native"
	 */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="longitude"
	 */
	public Long getLongitude() {
		return longitude;
	}

	public void setLongitude(Long longitude) {
		this.longitude = longitude;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="latitude"
	 */
	public Long getLatitude() {
		return latitude;
	}

	public void setLatitude(Long latitude) {
		this.latitude = latitude;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="address"
	 */
	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="colour"
	 */
	public String getColour() {
		return colour;
	}

	public void setColour(String colour) {
		this.colour = colour;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="marker_display_charater"
	 */
	public char getMarkerDisplayCharacter() {
		return markerDisplayCharacter;
	}

	public void setMarkerDisplayCharacter(char markerDisplayCharacter) {
		this.markerDisplayCharacter = markerDisplayCharacter;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="info_window_message"
	 */
	public String getInfoWindowMessage() {
		return infoWindowMessage;
	}

	public void setInfoWindowMessage(String infoWindowMessage) {
		this.infoWindowMessage = infoWindowMessage;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="mouseover_text"
	 */
	public String getMouseoverText() {
		return mouseoverText;
	}


	public void setMouseoverText(String mouseoverText) {
		this.mouseoverText = mouseoverText;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="create_date"
	 */
	public Date getCreated() {
		return created;
	}

	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="update_date"
	 */
	public Date getUpdated() {
		return updated;
	}

	public void setUpdated(Date updated) {
		this.updated = updated;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="is_authored"
	 */
	public boolean isAuthored() {
		return isAuthored;
	}

	
	public void setAuthored(boolean isAuthored) {
		this.isAuthored = isAuthored;
	}

	/**
     *
     * @hibernate.many-to-one cascade="none"
     *	class="org.lamsfoundation.lams.tool.gmap.model.GmapSession"
     *	column="gmap_session_uid"
     *
     */
	public GmapSession getToolSession() {
		return toolSession;
	}


	public void setToolSession(GmapSession toolSession) {
		this.toolSession = toolSession;
	}

	/**
	 * @hibernate.many-to-one column="gmap_uid"
	 * 			  cascade="none"
	 * @return
	 */
	public Gmap getGmap() {
		return gmap;
	}


	public void setGmap(Gmap gmap) {
		this.gmap = gmap;
	}


    /**
     *
     * @hibernate.many-to-one
     * 		column="create_by"
     *  	cascade="none"
     *
     */
	public GmapUser getCreatedBy() {
		return createBy;
	}


	public void setCreateBy(GmapUser createBy) {
		this.createBy = createBy;
	}

	/**
     * @hibernate.many-to-one
     * 		column="modified_by"
     * 		cascade="none"
     * 
     * @return Returns the userid of the user who modified the posting.
     */
	public GmapUser getModifiedBy() {
		return modifiedBy;
	}


	public void setModifiedBy(GmapUser modifiedBy) {
		this.modifiedBy = modifiedBy;
	}
	
	


}
