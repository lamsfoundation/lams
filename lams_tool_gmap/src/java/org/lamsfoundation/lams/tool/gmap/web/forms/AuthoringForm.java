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



package org.lamsfoundation.lams.tool.gmap.web.forms;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.tool.gmap.model.Gmap;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 *
 */
public class AuthoringForm extends ActionForm {

    private static final long serialVersionUID = 3950453134542135495L;

    // Properties

    String title;

    String instructions;

    String offlineInstruction;

    String onlineInstruction;

    boolean lockOnFinished;

    private boolean allowEditMarkers;

    private boolean allowShowAllMarkers;

    private boolean limitMarkers;

    private int maxMarkers;

    private boolean allowZoom;

    private boolean allowTerrain;

    private boolean allowSatellite;

    private boolean allowHybrid;

    private Double mapCenterLatitude;

    private Double mapCenterLongitude;

    private int mapZoom;

    private String mapType;

    FormFile onlineFile;

    FormFile offlineFile;

    String currentTab;

    String dispatch;

    String sessionMapID;

    Long deleteFileUuid;

    SessionMap sessionMap;

    Gmap gmap;

    String markersXML;

    boolean reflectOnActivity;

    String reflectInstructions;

    String defaultGeocoderAddress;

    public String getMarkersXML() {
	return markersXML;
    }

    public void setMarkersXML(String markersXML) {
	this.markersXML = markersXML;
    }

    public Gmap getGmap() {
	return gmap;
    }

    public void setGmap(Gmap gmap) {
	this.gmap = gmap;
    }

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));

	return ac;
    }

    public String getSessionMapID() {
	return sessionMapID;
    }

    public void setSessionMapID(String sessionMapID) {
	this.sessionMapID = sessionMapID;
    }

    public String getCurrentTab() {
	return currentTab;
    }

    public void setCurrentTab(String currentTab) {
	this.currentTab = currentTab;
    }

    public String getDispatch() {
	return dispatch;
    }

    public void setDispatch(String dispatch) {
	this.dispatch = dispatch;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public FormFile getOfflineFile() {
	return offlineFile;
    }

    public void setOfflineFile(FormFile offlineFile) {
	this.offlineFile = offlineFile;
    }

    public String getOfflineInstruction() {
	return offlineInstruction;
    }

    public void setOfflineInstruction(String offlineInstruction) {
	this.offlineInstruction = offlineInstruction;
    }

    public FormFile getOnlineFile() {
	return onlineFile;
    }

    public void setOnlineFile(FormFile onlineFile) {
	this.onlineFile = onlineFile;
    }

    public String getOnlineInstruction() {
	return onlineInstruction;
    }

    public void setOnlineInstruction(String onlineInstruction) {
	this.onlineInstruction = onlineInstruction;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public void setSessionMap(SessionMap sessionMap) {
	this.sessionMap = sessionMap;
    }

    public SessionMap getSessionMap() {
	return sessionMap;
    }

    public Long getDeleteFileUuid() {
	return deleteFileUuid;
    }

    public void setDeleteFileUuid(Long deleteFile) {
	this.deleteFileUuid = deleteFile;
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
