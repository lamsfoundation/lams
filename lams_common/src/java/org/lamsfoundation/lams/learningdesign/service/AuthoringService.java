/*
 * Created on Dec 7, 2004
 */
package org.lamsfoundation.lams.learningdesign.service;

import java.io.IOException;
import java.util.Hashtable;
import java.util.List;


import org.lamsfoundation.lams.learningdesign.authoring.LDWDDXValueObjectFactory;
import org.lamsfoundation.lams.learningdesign.authoring.LDWDDXValueObjectStorer;
import org.lamsfoundation.lams.learningdesign.authoring.WDDXTAGS;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningDesignDAO;
import org.lamsfoundation.lams.learningdesign.dao.hibernate.LearningLibraryDAO;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.learningdesign.LearningLibrary;
import org.lamsfoundation.lams.learningdesign.service.IAuthoringService;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.util.ClientStatusMessage;
import org.lamsfoundation.lams.util.WDDXProcessor;



import org.apache.log4j.Logger;

/**
 * @author manpreet
 */
public class AuthoringService implements IAuthoringService {
	
	protected LearningDesignDAO learningDesignDAO;
	protected LearningLibraryDAO learningLibraryDAO;
	
	private static Logger logger = Logger.getLogger(AuthoringService.class.getName());
	
	/**
	 * @param learningDesignDAO The learningDesignDAO to set.
	 */
	public void setLearningDesignDAO(LearningDesignDAO learningDesignDAO) {
		this.learningDesignDAO = learningDesignDAO;
	}
	
	/**
	 * @param learningLibraryDAO The learningLibraryDAO to set.
	 */
	public void setLearningLibraryDAO(LearningLibraryDAO learningLibraryDAO) {
		this.learningLibraryDAO = learningLibraryDAO;
	}
	
	public LearningDesign getLearningDesign(Long learningDesignID){
		return learningDesignDAO.getLearningDesignById(learningDesignID);
	}
	
	public void saveLearningDesign(LearningDesign learningDesign){
		learningDesignDAO.insert(learningDesign);
	}
	public List getAllLearningDesigns(){
		return learningDesignDAO.getAllLearningDesigns();		
	}
	public void updateLearningDesign(Long learningDesignID) {		
		
	}
	public List getAllLearningLibraries(){
		return learningLibraryDAO.getAllLearningLibraries();
		
	}
	public String requestLearningLibraryWDDX(){
		String wddxPacket = null;
		LearningLibrary library = null;
		List libraries = getAllLearningLibraries();
		Hashtable result = null;
		result = LDWDDXValueObjectFactory.getInstance().requestLearningLibraryList(libraries);		
		try{
			wddxPacket = WDDXProcessor.serialize(result);
		}catch(IOException e){
			/* TODO Proper Exception Handling and 
			 * TODO Generate client status error messages 
			 */
		}		
		return wddxPacket;
	}
	public String requestLearningDesignListWDDX(){
		String wddxPacket = null;
		List designs = learningDesignDAO.getAllLearningDesigns();		
		Hashtable result = null;
		result = LDWDDXValueObjectFactory.getInstance().requestLearningDesignList(designs);
		try{
			wddxPacket = WDDXProcessor.serialize(result);
		}catch(IOException e){
			/* TODO Proper Exception Handling and
			 * TODO Generate client status error messages
			 * */
		}		
		return wddxPacket;		
	}
	public String requestLearningDesignWDDX(Long learningDesignID){
		String wddxPacket = null;
		LearningDesign design = learningDesignDAO.getLearningDesignById(learningDesignID);
		Hashtable result = null;
		ClientStatusMessage statusMessage = null;
		if(design==null){
			statusMessage =  new ClientStatusMessage(ClientStatusMessage.ERROR,
					"No such learning design with learning_design_id " + learningDesignID + " exist",
					"");
			wddxPacket = serializeStatusMessage(statusMessage);
		}
		else{
			result = LDWDDXValueObjectFactory.getInstance().buildLearningDesignObject(design);
			try{
				wddxPacket = WDDXProcessor.serialize(result);				
			}catch(IOException ie){
				statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
														"An Error has occured while serializing the LeaarningDesign Object",
														"");
				wddxPacket = serializeStatusMessage(statusMessage);
			}
		}
		return wddxPacket;
	}
	public String requestLearningDesignWDDX(User user){
		Long userID = new Long(user.getUserId().longValue());		
		List designs = learningDesignDAO.getLearningDesignByUserId(userID);
		String wddxPacket = null;
		Hashtable result = null;
		result = LDWDDXValueObjectFactory.getInstance().requestLearningDesignList(designs);
		try{
			wddxPacket = WDDXProcessor.serialize(result);
		}catch(IOException e){
			/* TODO Proper Exception Handling and
			 * TODO Generate client status error messages
			 * */
		}
		return wddxPacket;
	}
	
	/**
	 * @param wddxPacket The WDDX Packet received from the flash side to be stored/updated
	 * 					 in the database
	 * @return String An acknowledgement message in WDDX format
	 */
	public String storeWDDXData(String wddxPacket){
		Hashtable wddxData = null;
		ClientStatusMessage statusMessage=null;
		Long learningDesignID = null;
		String resultWddx=null;
		String responseData="";
		if(containsNulls(wddxPacket)){
			statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
													"Unable to store the WDDX packet as it contains null",
													responseData);
			resultWddx = serializeStatusMessage(statusMessage);
			return resultWddx;
		}
		
		try{
			wddxData =(Hashtable)WDDXProcessor.deserialize(wddxPacket);			
		}catch(Exception e){
			statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
													"Unable to deserialize WDDX packet " + e.getMessage(),
													responseData);
			resultWddx = serializeStatusMessage(statusMessage);
			return resultWddx;
		}
		if(wddxData==null){
			statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
													"The WDDX packet returned is NULL",
													responseData);
			resultWddx = serializeStatusMessage(statusMessage);
			return resultWddx;
		}
		else{
			String objectType =(String)wddxData.get(WDDXTAGS.OBJECT_TYPE);
			if(objectType.equals(LearningDesign.DESIGN_OBJECT)){
				try{
					learningDesignID = LDWDDXValueObjectStorer.getInstance().processLearningDesign(wddxData);
					statusMessage = new ClientStatusMessage(ClientStatusMessage.RECEIVED,
															"Design successfully stored",
															(learningDesignID!=null?learningDesignID.toString():""));
					resultWddx = WDDXProcessor.serialize(statusMessage);
				}catch(Exception e){
					statusMessage = new ClientStatusMessage(ClientStatusMessage.ERROR,
															e.getMessage(),
															(learningDesignID!=null?learningDesignID.toString():""));
					try{
						resultWddx = WDDXProcessor.serialize(statusMessage);
					}catch(IOException ie){
						resultWddx = LDWDDXValueObjectStorer.getStandardErrorStatusMessages();
					}
					return resultWddx;
				}
				
			}
			else if (objectType.equals(LearningDesign.DESIGN_LIST_OBJECT)){
				
			}
			else if (objectType.equals(LearningLibrary.LIBRARY_OBJECT)){
				
			}
			else{
				/*TODO Handle all other cases*/
			}
			
		}
		return resultWddx;
	}	
	private String serializeStatusMessage(ClientStatusMessage message){
		String result = null;
		try{
			result = WDDXProcessor.serialize(message);
		}catch(IOException ie){
			result = LDWDDXValueObjectStorer.getStandardErrorStatusMessages();
		}
		return result;
	}	
	/**
	 * Checks whether the WDDX packet contains any invalid
	 * "<null/>". It returns true if there exists any such null
	 */
	private boolean containsNulls(String packet)
	{
		if (packet.indexOf("<null />") != -1)
			return true;
		else
			return false;
	}
	
}
