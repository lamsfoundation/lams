/*
 * Created on Jan 14, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.util;

/**
 * Status information to be sent to a client. Currently designed to be sent to a flash 
 * client using WDDX packet. Usually used to acknowledge receipt of data
 * 
 * @author fmalikoff
 *
 */
public class ClientStatusMessage{
	
	/** "NACK" message - error message */	
	public final static String ERROR = "Error";
	/** "ACK" messages - data has been received and no errors found*/	
	public final static String RECEIVED = "Received";
	/** "ACK messages - data has been deleted from database */
	public final static String DELETED = "Deleted";
	
	public static final String OBJECT_TYPE = "StatusMessage";

	private String statusType;
	private String message;
	private Object responseData;
	
	/** Constructor for a client status message
	 * @param String status type - must be one of the final values defined in this class (e.g. ERROR, RECEIVED)
	 * @param String message - the message to be sent to the client. Should be understood by a human reader.
	 * @param Object responseData - packet specific data, intending for the client software
	 */
	public ClientStatusMessage(String statusType, String message, String responseData)
	{
		setStatusType(statusType);
		setMessage(message);
		setResponseData(responseData);
	}
		
	/**
	 * Returns the message.
	 * @return String
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Returns the statusType.
	 * @return String
	 */
	public String getStatusType() {
		return statusType;
	}

	/**
	 * Sets the message.
	 * @param message The message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * Sets the statusType.
	 * @param statusType The statusType to set
	 */
	public void setStatusType(String statusType) {
		this.statusType = statusType;
	}

	/**
	 * Returns the responseData.
	 * @return Object
	 */
	public Object getResponseData() {
		return responseData;
	}

	/**
	 * Sets the responseData.
	 * @param responseData The responseData to set
	 */
	public void setResponseData(Object responseData) {
		this.responseData = responseData;
	}

	/**
	 * Returns the objectType.
	 * @return String
	 */
	public String getObjectType() {
		return OBJECT_TYPE;
	}
	
	public String toString()
	{
		StringBuffer buf = new StringBuffer(200);
		buf.append(this.getClass().getName());	buf.append(": ");
		buf.append(" Status Type=");buf.append(this.getStatusType());	
		buf.append(" Message=");buf.append(this.getMessage());	
		buf.append(" Reponse Data=");buf.append(this.getResponseData());	
		return(buf.toString());
	}

}

