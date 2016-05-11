/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */



package org.lamsfoundation.lams.tool.gmap.dto;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.tool.gmap.model.GmapMarker;
import org.lamsfoundation.lams.tool.gmap.model.GmapSession;

/**
 * DTO for gmap, contains all the same variables as gmap persistance object + session
 * 
 * @author lfoxton
 *
 */
public class GmapDTO {

    private static Logger logger = Logger.getLogger(GmapDTO.class);

    public Long toolContentId;

    public String title;

    public String instructions;

    public boolean defineLater;

    public boolean contentInUse;

    //public boolean allowRichEditor;

    public boolean lockOnFinish;

    public boolean allowEditMarkers;

    public boolean allowShowAllMarkers;

    public boolean limitMarkers;

    public int maxMarkers;

    public boolean allowZoom;

    public boolean allowTerrain;

    public boolean allowSatellite;

    public boolean allowHybrid;

    public Double mapCenterLatitude;

    public Double mapCenterLongitude;

    public int mapZoom;

    public String mapType;

    public boolean reflectOnActivity;

    public String reflectInstructions;

    String defaultGeocoderAddress;

    public Set<GmapMarkerDTO> gmapMarkers = new HashSet<GmapMarkerDTO>();

    public Set<GmapSessionDTO> sessionDTOs = new TreeSet<GmapSessionDTO>();

    /* Constructors */
    public GmapDTO() {
    }

    public GmapDTO(Gmap gmap) {
	toolContentId = gmap.getToolContentId();
	title = gmap.getTitle();
	instructions = gmap.getInstructions();
	contentInUse = gmap.isContentInUse();
	//allowRichEditor = gmap.isAllowRichEditor();	
	lockOnFinish = gmap.isLockOnFinished();
	allowEditMarkers = gmap.isAllowEditMarkers();
	allowShowAllMarkers = gmap.isAllowShowAllMarkers();
	limitMarkers = gmap.isLimitMarkers();
	maxMarkers = gmap.getMaxMarkers();
	allowZoom = gmap.isAllowZoom();
	allowTerrain = gmap.isAllowTerrain();
	allowSatellite = gmap.isAllowSatellite();
	allowHybrid = gmap.isAllowHybrid();
	mapCenterLatitude = gmap.getMapCenterLatitude();
	mapCenterLongitude = gmap.getMapCenterLongitude();
	mapZoom = gmap.getMapZoom();
	mapType = gmap.getMapType();
	reflectOnActivity = gmap.isReflectOnActivity();
	reflectInstructions = gmap.getReflectInstructions();
	defaultGeocoderAddress = gmap.getDefaultGeocoderAddress();

	for (Iterator<GmapSession> iter = gmap.getGmapSessions().iterator(); iter.hasNext();) {
	    GmapSession session = iter.next();
	    GmapSessionDTO sessionDTO = new GmapSessionDTO(session);
	    sessionDTOs.add(sessionDTO);
	}
    }

    /* Getters / Setters */
    public Set<GmapSessionDTO> getSessionDTOs() {
	return sessionDTOs;
    }

    public void setSessionDTOs(Set<GmapSessionDTO> sessionDTOs) {
	this.sessionDTOs = sessionDTOs;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentID) {
	this.toolContentId = toolContentID;
    }

    public Boolean getContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(Boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isAllowEditMarkers() {
	return allowEditMarkers;
    }

    public void setAllowEditMarkers(boolean allowEditMarkers) {
	this.allowEditMarkers = allowEditMarkers;
    }

    public boolean isAllowShowAllMarkers() {
	return allowShowAllMarkers;
    }

    public void setAllowShowAllMarkers(boolean allowShowAllMarkers) {
	this.allowShowAllMarkers = allowShowAllMarkers;
    }

    public boolean isLimitMarkers() {
	return limitMarkers;
    }

    public void setLimitMarkers(boolean limitMarkers) {
	this.limitMarkers = limitMarkers;
    }

    public int getMaxMarkers() {
	return maxMarkers;
    }

    public void setMaxMarkers(int maxMarkers) {
	this.maxMarkers = maxMarkers;
    }

    public boolean isAllowZoom() {
	return allowZoom;
    }

    public void setAllowZoom(boolean allowZoom) {
	this.allowZoom = allowZoom;
    }

    public boolean isAllowTerrain() {
	return allowTerrain;
    }

    public void setAllowTerrain(boolean allowTerrain) {
	this.allowTerrain = allowTerrain;
    }

    public boolean isAllowSatellite() {
	return allowSatellite;
    }

    public void setAllowSatellite(boolean allowSatellite) {
	this.allowSatellite = allowSatellite;
    }

    public boolean isAllowHybrid() {
	return allowHybrid;
    }

    public void setAllowHybrid(boolean allowHybrid) {
	this.allowHybrid = allowHybrid;
    }

    public Double getMapCenterLatitude() {
	return mapCenterLatitude;
    }

    public void setMapCenterLatitude(Double mapCenterLatitude) {
	this.mapCenterLatitude = mapCenterLatitude;
    }

    public Double getMapCenterLongitude() {
	return mapCenterLongitude;
    }

    public void setMapCenterLongitude(Double mapCenterLongitude) {
	this.mapCenterLongitude = mapCenterLongitude;
    }

    public int getMapZoom() {
	return mapZoom;
    }

    public void setMapZoom(int mapZoom) {
	this.mapZoom = mapZoom;
    }

    public String getMapType() {
	return mapType;
    }

    public void setMapType(String mapType) {
	this.mapType = mapType;
    }

    public Set<GmapMarkerDTO> getGmapMarkers() {
	return gmapMarkers;
    }

    public void setGmapMarkers(List<GmapMarker> gmapMarkers) {

	for (Iterator<GmapMarker> i = gmapMarkers.iterator(); i.hasNext();) {
	    GmapMarker marker = i.next();
	    GmapMarkerDTO markerDTO = new GmapMarkerDTO(marker);
	    this.gmapMarkers.add(markerDTO);
	}
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public boolean isLockOnFinish() {
	return lockOnFinish;
    }

    public void setLockOnFinish(boolean lockOnFinish) {
	this.lockOnFinish = lockOnFinish;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public void setGmapMarkers(Set<GmapMarkerDTO> gmapMarkers) {
	this.gmapMarkers = gmapMarkers;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public String getDefaultGeocoderAddress() {
	return defaultGeocoderAddress;
    }

    public void setDefaultGeocoderAddress(String defaultGeocoderAddress) {
	this.defaultGeocoderAddress = defaultGeocoderAddress;
    }
}
