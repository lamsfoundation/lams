/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */

package org.lamsfoundation.lams.util.wddx;

import java.io.IOException;
import java.io.Serializable;

/**
 * @author Manpreet Minhas
 *         This class represents the message sent by the server to the
 *         Flash client. The error messages in this class should only
 *         be generic error messages that may be used in various modules.
 *         The errors are not I18N'd as we assume Flash will not show the
 *         error to the user - but the error is included in the Flash dump
 *         file so it should still be meaningful.
 */
public class FlashMessage implements Serializable {

    /**
     * Message type indicating that operation was
     * unsuccessful due to some error on FLASH side.
     * For example the WDDX packet contains a null value
     */
    public static final int ERROR = 1;

    /**
     * Message type indicating that operation failed
     * due to some system eror. For example, the client
     * was unable to serilaize the WDDX packet
     */
    public static final int CRITICAL_ERROR = 2;

    /**
     * Message type indicating that operation
     * was executed successfully
     */
    public static final int OBJECT_MESSAGE = 3;

    /** Usually the name of the method that was called by the flash */
    private String messageKey;

    /**
     * The response to the flash's request. Normally a string either
     * stating the error message or the WDDX packet
     */
    private Object messageValue;

    /**
     * Represents the type of message being sent to Flash. Can be one of
     * the following values
     * <ul>
     * <b>
     * <li>ERROR</li>
     * <li>CRITICAL_ERROR</li>
     * <li>OBJECT_MESSAGE</li>
     * </b>
     * </ul>
     */
    private int messageType;

    /** Minimal Constructor */
    public FlashMessage(String messageKey, Object messageValue) {
	this.messageKey = messageKey;
	this.messageValue = messageValue;
	this.messageType = OBJECT_MESSAGE;
    }

    /** Full Constructor */
    public FlashMessage(String messageKey, Object messageValue, int messageType) {
	this.messageKey = messageKey;
	this.messageValue = messageValue;
	this.messageType = messageType;
    }

    public String getMessageKey() {
	return messageKey;
    }

    public int getMessageType() {
	return messageType;
    }

    public Object getMessageValue() {
	return messageValue;
    }

    /**
     * Return String representation of the message that will be sent to flash.
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer(getClass().getName() + ": ");
	sb.append("messageKey='" + getMessageKey() + "'; ");
	sb.append("messageType='" + getMessageType() + "';");
	sb.append("messageValue='" + getMessageValue() + "'; ");
	return sb.toString();
    }

    public String serializeMessage() throws IOException {
	String wddxPacket = null;
	try {
	    wddxPacket = WDDXProcessor.serialize(this);
	} catch (IOException ie) {
	    throw new IOException("IOException occured while serializing " + ie.getMessage());
	}
	return wddxPacket;
    }

    public static FlashMessage getNoSuchUserExists(String methodName, Integer userID) {
	return new FlashMessage(methodName, "No such User with a user_id of :" + userID + " exists", ERROR);
    }

    public static FlashMessage getNoSuchOrganisationExists(String methodName, Integer organisationID) {
	return new FlashMessage(methodName,
		"No such Organisation with a organisationID of :" + organisationID + " exists", ERROR);
    }

    public static FlashMessage getUserNotAuthorized(String methodName, Integer userID) {
	return new FlashMessage(methodName,
		"User with user_id of:" + userID + " is not authorized to perfom this action:", FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchWorkspaceFolderExsists(String methodName, Integer folderID) {
	return new FlashMessage(methodName,
		"No such WorkspaceFolder with a workspace_folder_id of " + folderID + " exists", FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchLearningDesignExists(String methodName, Long learningDesignID) {
	return new FlashMessage(methodName,
		"No such LearningDesign with a learning_design_id of " + learningDesignID + " exists",
		FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchActivityExists(String methodName, Long activityID) {
	return new FlashMessage(methodName, "No such Activity with an activity_id of " + activityID + " exists",
		FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchWorkspaceFolderContentExsists(String methodName, Long folderContentID) {
	return new FlashMessage(methodName,
		"No such WorkspaceFolderContent with a folder_content_id of " + folderContentID + " exists",
		FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchTheme(String methodName, Long themeID) {
	return new FlashMessage(methodName, "No such theme with a theme id of " + themeID + " exists",
		FlashMessage.ERROR);
    }

    public static FlashMessage getWDDXPacketGetReceived(String urlCall) {
	return new FlashMessage(urlCall, "Invalid call. Expected WDDX packet in POST but received a GET",
		FlashMessage.ERROR);
    }

    public static FlashMessage getExceptionOccured(String methodName, String message) {
	return new FlashMessage(methodName,
		"Unable to store due to an internal error. Contact support for help. Exception message was " + message,
		FlashMessage.ERROR);
    }

    public static FlashMessage getNoSuchTool(String methodName, Long toolID) {
	return new FlashMessage(methodName, "No such Tool with tool id of " + toolID + " exists", FlashMessage.ERROR);
    }

    public static FlashMessage getDataMissing(String methodName, String[] dataNames) {
	String str = "Request can not be completed as data is missing. ";
	if (dataNames != null && dataNames.length > 0) {
	    str += " Fields:";
	    for (int i = 0; i < dataNames.length; i++) {
		str = str + (i != 0 ? ", " : "") + dataNames[i];
	    }
	}

	return new FlashMessage(methodName, str, FlashMessage.ERROR);
    }

    public static FlashMessage getUnavailableLearningDesign(String methodName, Long designID, Integer userID) {
	return new FlashMessage(methodName, "The design  with learning_design_id: " + designID
		+ " is unavailable for edit by User with user_id of :" + userID, ERROR);
    }
}
