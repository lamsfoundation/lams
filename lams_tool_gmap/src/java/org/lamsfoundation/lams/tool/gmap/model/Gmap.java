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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.gmap.service.GmapService;

/**
 *
 */
public class Gmap implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 938457189932877382L;

    static Logger log = Logger.getLogger(GmapService.class.getName());

    // id
    private Long uid;

    // create date
    private Date createDate;

    // update date
    private Date updateDate;

    // user who created gmap
    private Long createBy;

    // gmap title
    private String title;

    // activity instructions
    private String instructions;

    // lock when user finishes activity
    private boolean lockOnFinished;

    // allow users to edit markersr after saving
    private boolean allowEditMarkers;

    // show other user's makres
    private boolean allowShowAllMarkers;

    // limit the ammount of markers
    private boolean limitMarkers;

    // the maximum number or markers
    private int maxMarkers;

    // allow zooming option
    private boolean allowZoom;

    // allow terrain map type
    private boolean allowTerrain;

    // allow satellite map type
    private boolean allowSatellite;

    // allow hybrid satellite/normal map type
    private boolean allowHybrid;

    // latitude of centre of map
    private Double mapCenterLatitude;

    // longitude of centre of map
    private Double mapCenterLongitude;

    // zoom level of the map
    private int mapZoom;

    // map type 
    private String mapType;

    // flag for content in use
    private boolean contentInUse;

    // flag for define later
    private boolean defineLater;

    // tool content id
    private Long toolContentId;

    // Add notebook at the end of activity
    boolean reflectOnActivity;

    // instructions for notebook
    String reflectInstructions;

    // default Geocoder Address (Set to "Macquarie University, Sydney NSW" by default)
    String defaultGeocoderAddress;

    // list of sessions for this gmap
    private Set<GmapSession> gmapSessions;

    // list of markers for this gmap
    private Set<GmapMarker> gmapMarkers;

    // Property accessors
    /**
     *
     * 
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     * 
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     * 
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     * 
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    /**
     * public GmapUser getCreateBy() {
     * return this.createBy;
     * }
     */

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
     * 
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     * 
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     * 
     */

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     * 
     */

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     * 
     */

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     * 
     */

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     *
     *
     * 
     */

    public Set<GmapSession> getGmapSessions() {
	return this.gmapSessions;
    }

    public void setGmapSessions(Set<GmapSession> gmapSessions) {
	this.gmapSessions = gmapSessions;
    }

    /**
     * toString
     * 
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof Gmap)) {
	    return false;
	}
	Gmap castOther = (Gmap) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Gmap newInstance(Gmap fromContent, Long toContentId) {
	Gmap toContent = new Gmap();
	toContent = (Gmap) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Gmap gmap = null;
	try {
	    gmap = (Gmap) super.clone();
	    gmap.setUid(null);

	    Set<GmapMarker> markerSet = new HashSet<GmapMarker>();

	    if (gmapMarkers != null) {
		// create a copy of the markers
		Iterator<GmapMarker> iter = gmapMarkers.iterator();
		while (iter.hasNext()) {
		    GmapMarker originalMarker = iter.next();
		    GmapMarker newMarker = (GmapMarker) originalMarker.clone();
		    markerSet.add(newMarker);
		}

	    }
	    gmap.gmapMarkers = markerSet;

	    // create an empty set for the gmapSession
	    gmap.gmapSessions = new HashSet<GmapSession>();

	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + Gmap.class);
	}
	return gmap;
    }

    /**
     *
     *
     *
     *
     * 
     * @return
     */
    public Set<GmapMarker> getGmapMarkers() {
	return gmapMarkers;
    }

    public void setGmapMarkers(Set<GmapMarker> gmapMarkers) {
	this.gmapMarkers = gmapMarkers;
    }

    public void addMarker(GmapMarker marker) {
	gmapMarkers.add(marker);
    }

    public GmapMarker getMarkerByUid(Long uid) {
	Iterator<GmapMarker> it = gmapMarkers.iterator();
	GmapMarker ret = null;
	while (it.hasNext()) {
	    GmapMarker marker = it.next();
	    if (marker.getUid().equals(uid)) {
		ret = marker;
		break;
	    }
	}
	return ret;
    }

    public void removeMarker(Long uid) {
	Iterator<GmapMarker> it = gmapMarkers.iterator();
	while (it.hasNext()) {
	    GmapMarker marker = it.next();
	    if (marker.getUid().equals(uid)) {
		gmapMarkers.remove(marker);
		break;
	    }
	}
    }

    /**
     *
     * 
     */

    public boolean isAllowEditMarkers() {
	return allowEditMarkers;
    }

    public void setAllowEditMarkers(boolean allowEditMarkers) {
	this.allowEditMarkers = allowEditMarkers;
    }

    /**
     *
     * 
     */

    public boolean isAllowShowAllMarkers() {
	return allowShowAllMarkers;
    }

    public void setAllowShowAllMarkers(boolean allowShowAllMarkers) {
	this.allowShowAllMarkers = allowShowAllMarkers;
    }

    /**
     *
     * 
     */

    public boolean isLimitMarkers() {
	return limitMarkers;
    }

    public void setLimitMarkers(boolean limitMarkers) {
	this.limitMarkers = limitMarkers;
    }

    /**
     *
     * 
     */

    public int getMaxMarkers() {
	return maxMarkers;
    }

    public void setMaxMarkers(int maxMarkers) {
	this.maxMarkers = maxMarkers;
    }

    /**
     *
     * 
     */

    public boolean isAllowZoom() {
	return allowZoom;
    }

    public void setAllowZoom(boolean allowZoom) {
	this.allowZoom = allowZoom;
    }

    /**
     *
     * 
     */

    public boolean isAllowTerrain() {
	return allowTerrain;
    }

    public void setAllowTerrain(boolean allowTerrain) {
	this.allowTerrain = allowTerrain;
    }

    /**
     *
     * 
     */

    public boolean isAllowSatellite() {
	return allowSatellite;
    }

    public void setAllowSatellite(boolean allowSatellite) {
	this.allowSatellite = allowSatellite;
    }

    /**
     *
     * 
     */

    public boolean isAllowHybrid() {
	return allowHybrid;
    }

    public void setAllowHybrid(boolean allowHybrid) {
	this.allowHybrid = allowHybrid;
    }

    /**
     * 
     *
     */

    public Double getMapCenterLatitude() {
	return mapCenterLatitude;
    }

    public void setMapCenterLatitude(Double mapCenterLatitude) {
	this.mapCenterLatitude = mapCenterLatitude;
    }

    /**
     * 
     *
     */

    public Double getMapCenterLongitude() {
	return mapCenterLongitude;
    }

    public void setMapCenterLongitude(Double mapCenterLongitude) {
	this.mapCenterLongitude = mapCenterLongitude;
    }

    /**
     * 
     *
     */

    public int getMapZoom() {
	return mapZoom;
    }

    public void setMapZoom(int mapZoom) {
	this.mapZoom = mapZoom;
    }

    /**
     * 
     *
     */
    public String getMapType() {
	return mapType;
    }

    public void setMapType(String mapType) {
	this.mapType = mapType;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     * 
     */
    public String getDefaultGeocoderAddress() {
	return defaultGeocoderAddress;
    }

    public void setDefaultGeocoderAddress(String defaultGeocoderAddress) {
	this.defaultGeocoderAddress = defaultGeocoderAddress;
    }

}
