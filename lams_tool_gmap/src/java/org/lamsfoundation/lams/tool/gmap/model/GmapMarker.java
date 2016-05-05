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

package org.lamsfoundation.lams.tool.gmap.model;

import java.util.Date;

import org.apache.log4j.Logger;

/**
 * @author lfoxton
 *
 *         This is a POJO class for a marker object that represents a marker on a
 *         google map.
 *
 *
 */
public class GmapMarker implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 54263746982364732L;

    private static final Logger log = Logger.getLogger(GmapMarker.class);

    private Long uid; // id	
    private Double longitude; // longitude of marker
    private Double latitude; // latitude of marker
    private String infoWindowMessage; // message box that appears when you click the marker
    private String title; // marker title
    private Date created; // date of creation
    private Date updated; // date of last update
    private boolean isAuthored; // flag for whether maker was made in author/learner
    private Gmap gmap; // gmap to which marker belongs
    private GmapUser createdBy; // user who created the marker
    private GmapUser updatedBy; // last user to update the marker
    private GmapSession gmapSession; // tool session to which marker belongs, null if there is in author

    /**
     * Generic POJO constructor
     */
    public GmapMarker() {
    }

    public GmapMarker(Long uid, Double longitude, Double latitude, String infoWindowMessage, String title, Date created,
	    Date updated, boolean isAuthored, Gmap gmap, GmapUser createdBy, GmapUser updatedBy,
	    GmapSession gmapSession) {
	super();
	this.uid = uid;
	this.longitude = longitude;
	this.latitude = latitude;
	this.infoWindowMessage = infoWindowMessage;
	this.title = title;
	this.created = created;
	this.updated = updated;
	this.isAuthored = isAuthored;
	this.gmap = gmap;
	this.createdBy = createdBy;
	this.updatedBy = updatedBy;
	this.gmapSession = gmapSession;
    }

    @Override
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
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
     *
     */
    public boolean isAuthored() {
	return isAuthored;
    }

    public void setAuthored(boolean isAuthored) {
	this.isAuthored = isAuthored;
    }

    /**
     *
     *
     *
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
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the userid of the user who created the Forum.
     *
     *
     *
     *
     *
     */
    public GmapUser getCreatedBy() {
	return createdBy;
    }

    public void setCreatedBy(GmapUser createdBy) {
	this.createdBy = createdBy;
    }

    /**
     * @return Returns the userid of the user who created the Forum.
     *
     *
     *
     *
     *
     */
    public GmapUser getUpdatedBy() {
	return updatedBy;
    }

    public void setUpdatedBy(GmapUser updatedBy) {
	this.updatedBy = updatedBy;
    }

    /**
     * Gets the toolSession
     *
     *
     *
     *
     *
     */
    public GmapSession getGmapSession() {
	return gmapSession;
    }

    public void setGmapSession(GmapSession gmapSession) {
	this.gmapSession = gmapSession;
    }
}
