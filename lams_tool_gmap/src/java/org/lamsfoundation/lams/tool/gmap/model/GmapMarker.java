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
import org.apache.log4j.Logger;

/**
 * @author lfoxton
 * 
 * This is a POJO class for a marker object that represents a marker on a 
 * google map.
 * 
 * @hibernate.class table="tl_lagmap10_marker"
 */
public class GmapMarker implements java.io.Serializable, Cloneable{

	private static final long serialVersionUID = 54263746982364732L;
	
	private static final Logger log = Logger.getLogger(GmapMarker.class);
	
	// Unique identifier
	private Long uid;		
	
	// Longitude of marker position
	private Double longitude;
	
	// Latitude of marker position
	private Double latitude;
	
	// String address of marker if given
	//private String address;
	
	// Marker colour, can be: ORANGE, PURPLE, YELLOW, GREEN, BLUE, RED, AQUA, WHITE, or GRAY
	//private String colour;
	
	// A character that will display on the marker
	//private char markerDisplayCharacter;
	
	// Info windo message that appears when you click the marker (in HTML)
	private String infoWindowMessage;
	
	
	// The marker title
	private String title;
	
	
	// Text that appears when the user mouseovers the marker
	//private String mouseoverText;
	
	// The date the marker was created
	private Date created;
	
	// The date the marker was updated
	private Date updated;
	
	// True if it is an authored marker, false if it is a learner marker
	private boolean isAuthored;
	
	// The gmap tool session if in learner, null if in author 
	//private GmapSession toolSession;
	
	// The gmap instance that this marker is part of
	private Gmap gmap;
	//private Long gmapID;
	
	// The user who created the marker
	//private GmapUser createBy;
	
	// The last user to modify the marker
	//private GmapUser modifiedBy;


	/**
	 * Generic POJO constructor
	 */
	public GmapMarker() {}


	public GmapMarker(Long uid, Double longitude, Double latitude,
			String infoWindowMessage, Date created, Date updated,
			boolean isAuthored) {
		super();
		this.uid = uid;
		this.longitude = longitude;
		this.latitude = latitude;
		this.infoWindowMessage = infoWindowMessage;
		this.created = created;
		this.updated = updated;
		this.isAuthored = isAuthored;
	}
	
	public Object clone() {
		Object obj = null;
		try {
			obj = super.clone();
			((GmapMarker) obj).setUid(null);
		} catch (CloneNotSupportedException e) {
			log.error("Failed to clone " + GmapMarker.class);
		}

		return obj;
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
	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	/**
	 * 
	 * @return
	 * @hibernate.property column="latitude"
	 */
	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
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
	 * @hibernate.many-to-one	cascade="none"
	 * 							not-null="true"
	 * @hibernate.column name="gmap_uid"
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
	 * @return
	 * @hibernate.property column="title"
	 */
	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


}
