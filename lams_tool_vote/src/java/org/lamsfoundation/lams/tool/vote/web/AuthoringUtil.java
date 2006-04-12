

/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.contentrepository.FileException;
import org.lamsfoundation.lams.contentrepository.NodeKey;
import org.lamsfoundation.lams.contentrepository.RepositoryCheckedException;
import org.lamsfoundation.lams.tool.exception.ToolException;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteUtils;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;

/**
 * 
 * Keeps all operations needed for Authoring mode. 
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements VoteAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

	public static void readData(HttpServletRequest request, VoteAuthoringForm mcAuthoringForm)
    {
    	/** define the next tab as Basic tab by default*/
     	request.getSession().setAttribute(EDIT_OPTIONS_MODE, new Integer(0));
     	
     	VoteUtils.persistRichText(request);
     	AuthoringUtil.populateParameters(request, mcAuthoringForm);
    }
 	
	
    protected static void populateParameters(HttpServletRequest request, VoteAuthoringForm mcAuthoringForm)
    {
        String selectedQuestion=request.getParameter(SELECTED_QUESTION);
  
        if ((selectedQuestion != null) && (selectedQuestion.length() > 0))
    	{
    		request.getSession().setAttribute(SELECTED_QUESTION,selectedQuestion);
    		logger.debug("updated SELECTED_QUESTION");
    	}
    }
    

    protected static void setRadioboxes(VoteContent mcContent, VoteAuthoringForm mcAuthoringForm)
	{
		if (mcContent.isQuestionsSequenced())
		{
			mcAuthoringForm.setQuestionsSequenced(ON);
			logger.debug("setting questionsSequenced to true");
		}
		else
		{
			mcAuthoringForm.setQuestionsSequenced(OFF);	
			logger.debug("setting questionsSequenced to false");				
		}

		if (mcContent.isRetries())
		{
			mcAuthoringForm.setRetries(ON);	
			logger.debug("setting retries to true");
		}
		else
		{
			mcAuthoringForm.setRetries(OFF);	
			logger.debug("setting retries to false");				
		}

		
		if (mcContent.isShowReport())
		{
			mcAuthoringForm.setSln(ON);	
			logger.debug("setting sln to true");
		}
		else
		{
			mcAuthoringForm.setSln(OFF);	
			logger.debug("setting sln to false");				
		}
	}

    
    public static boolean verifyDuplicatesOptionsMap(Map mapOptionsContent)
	{
    	Map originalMapOptionsContent=mapOptionsContent;
    	Map backupMapOptionsContent=mapOptionsContent;
    	
    	int optionCount=0;
    	for (long i=1; i <= MAX_OPTION_COUNT ; i++)
		{
    		String currentOption=(String)originalMapOptionsContent.get(new Long(i).toString());
    		logger.debug("verified currentOption  " + currentOption);
    		
    		optionCount=0;
    		for (long j=1; j <= MAX_OPTION_COUNT ; j++)
    		{
        		String backedOption=(String)backupMapOptionsContent.get(new Long(j).toString());
        		
        		if ((currentOption != null) && (backedOption !=null))
        		{
        			if (currentOption.equals(backedOption))
    				{
    					optionCount++;
    			    	logger.debug("optionCount for  " + currentOption + " is: " + optionCount);
    				}
    				
            		if (optionCount > 1)
            			return false;	
        		}
    		}	
		}
    	return true;
	}
    
    
    public static boolean verifyMapNoEmptyString(Map map)
	{
    	Iterator itMap = map.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0))
            	return false;
            
		}
    	return true;
	}
    
	    
    public static boolean validateQuestionsNotEmpty(Map mapQuestionsContent)
    {
    	Iterator itMap = mapQuestionsContent.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            
            if ((pairs.getValue() != null) && (pairs.getValue().toString().length() == 0))
            	return false;
            
		}
    	return true;
    }


    public static Map sequenceMap(Map globalMap)
	{
    	logger.debug("globalMap:"+ globalMap);
    	Map mapTemp= new TreeMap(new VoteComparator());
    	
    	long mapCounter=0;
    	Iterator itMap = globalMap.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            Map optionsMap=(Map)pairs.getValue();
            logger.debug("optionsMap:"+ optionsMap);
            mapCounter++;
            mapTemp.put(new Long(mapCounter).toString(), optionsMap);
		}
    	
    	logger.debug("final mapTemp:"+ mapTemp);
    	return mapTemp;
	}
	

    public static Map repopulateMap(HttpServletRequest request, String parameterType)
    {
    	Map mapTempQuestionsContent= new TreeMap(new VoteComparator());
    	logger.debug("parameterType: " + parameterType);
    	
    	long mapCounter=0;
    	for (long i=1; i <= MAX_QUESTION_COUNT ; i++)
		{
			String candidateEntry =request.getParameter(parameterType + i);
			if (
				(candidateEntry != null) && 
				(candidateEntry.length() > 0)   
				)
			{
				mapCounter++;
				mapTempQuestionsContent.put(new Long(mapCounter).toString(), candidateEntry);
			}
		}
    	logger.debug("return repopulated Map: " + mapTempQuestionsContent);
    	return mapTempQuestionsContent;
    }


    /**
     * returns the value of the entry for a given index
     * getRequiredWeightEntry(Map mapWeights, String questionIndex)
     * 
     * @param mapWeights
     * @param questionIndex
     * @return
     */	
    public static String getRequiredWeightEntry(Map mapWeights, String questionIndex)
    {
    	logger.debug("mapWeights: " +  mapWeights);
    	
    	Iterator itMap = mapWeights.entrySet().iterator();
    	while (itMap.hasNext()) {
        	Map.Entry pairs = (Map.Entry)itMap.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            if (questionIndex.equals(pairs.getKey().toString()))
    		{
            	String weight=pairs.getValue().toString();
            	logger.debug("required weight:" + weight);
            	return weight;
    		}
		}
    	return null;
    }
    

    
    
    /**
     * removes options from mapGlobalOptionsContent
	 * removeFromOptionsMap(Map mapGlobalOptionsContent, String questionIndex )
	 * 
	 * @param mapGlobalOptionsContent
	 * @param questionIndex
	 * @param direction
	 * @return Map
	 */
    public static Map removeFromMap(Map mapContent, String index)
    {
    	/* mapGlobalOptionsContent refers to mapGenaralOptionsContent and mapGeneralSelectedlOptionsContent */
    	/* map to be returned */
    	Map mapTempContent= new TreeMap(new VoteComparator());
    	mapTempContent= mapContent;
    	
    	mapTempContent.remove(index);
		logger.debug("entry at index removed from mapTempContent...");
    	
		logger.debug("final mapTempContent: " +  mapTempContent);
    	return mapTempContent;
    }
    
	
    
    public static VoteContent createContent(HttpServletRequest request, VoteAuthoringForm mcAuthoringForm)
    {
    	logger.debug("doing createContent...");
    	IVoteService mcService =VoteUtils.getToolService(request);
    	
    	/* the tool content id is passed from the container to the tool and placed into session in the VoteStarterAction */
    	Long toolContentId=(Long)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (toolContentId.longValue() != 0))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + toolContentId);
    		/*delete the existing content in the database before applying new content*/
    		mcService.deleteVoteById(toolContentId);  
    		logger.debug("post-deletion existing content");
		}
    	
		String title;
    	String instructions;
    	Long createdBy;
    	String monitoringReportTitle="";
    	String reportTitle="";
    	
    	String offlineInstructions="";
    	String onlineInstructions="";
    	String endLearningMessage="";
    	Date creationDate=null;
    	int passmark=0;
    	
    	boolean isQuestionsSequenced=false;
    	boolean isSynchInMonitor=false;
    	boolean isUsernameVisible=false;
    	boolean isRunOffline=false;
    	boolean isDefineLater=false;
    	boolean isContentInUse=false;
    	boolean isRetries=false;
    	boolean isShowFeedback=false;
    	boolean isSln=false;
    	
    	logger.debug("isQuestionsSequenced: " +  mcAuthoringForm.getQuestionsSequenced());
    	if (mcAuthoringForm.getQuestionsSequenced().equalsIgnoreCase(ON))
    		isQuestionsSequenced=true;
    	
    	logger.debug("isSynchInMonitor: " +  mcAuthoringForm.getSynchInMonitor());
    	if (mcAuthoringForm.getSynchInMonitor().equalsIgnoreCase(ON))
    		isSynchInMonitor=true;
    	
    	logger.debug("isUsernameVisible: " +  mcAuthoringForm.getUsernameVisible());
    	if (mcAuthoringForm.getUsernameVisible().equalsIgnoreCase(ON))
    		isUsernameVisible=true;
    	
    	logger.debug("isRetries: " +  mcAuthoringForm.getRetries());
    	if (mcAuthoringForm.getRetries().equalsIgnoreCase(ON))
    		isRetries=true;
    	
		logger.debug("isSln" +  mcAuthoringForm.getSln());
		if (mcAuthoringForm.getSln().equalsIgnoreCase(ON))
			isSln=true;
    	
    	logger.debug("passmark: " +  mcAuthoringForm.getPassmark());
    	if ((mcAuthoringForm.getPassmark() != null) && (mcAuthoringForm.getPassmark().length() > 0)) 
    		passmark= new Integer(mcAuthoringForm.getPassmark()).intValue();
    	
    	logger.debug("isShowFeedback: " +  mcAuthoringForm.getShowFeedback());
    	if (mcAuthoringForm.getShowFeedback().equalsIgnoreCase(ON))
    		isShowFeedback=true;
    	    	
    	
    	String richTextTitle="";
    	richTextTitle = (String)request.getSession().getAttribute(RICHTEXT_TITLE);
    	logger.debug("createContent richTextTitle from session: " + richTextTitle);
    	if (richTextTitle == null) richTextTitle="";
    	
    	String richTextInstructions="";
    	richTextInstructions = (String)request.getSession().getAttribute(RICHTEXT_INSTRUCTIONS);
    	logger.debug("createContent richTextInstructions from session: " + richTextInstructions);
    	if (richTextInstructions == null) richTextInstructions="";

    	String richTextOfflineInstructions="";
    	richTextOfflineInstructions = (String)request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOfflineInstructions from session: " + richTextOfflineInstructions);
    	if (richTextOfflineInstructions == null) richTextOfflineInstructions="";
    	
    	String richTextOnlineInstructions="";
    	richTextOnlineInstructions = (String)request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
    	logger.debug("createContent richTextOnlineInstructions from session: " + richTextOnlineInstructions);
    	if (richTextOnlineInstructions == null) richTextOnlineInstructions="";
    	
    	
    	String richTextReportTitle=(String)request.getSession().getAttribute(RICHTEXT_REPORT_TITLE);
		logger.debug("richTextReportTitle: " + richTextReportTitle);
		
		String richTextEndLearningMessage=(String)request.getSession().getAttribute(RICHTEXT_END_LEARNING_MSG);
		logger.debug("richTextEndLearningMessage: " + richTextEndLearningMessage);
    	
		creationDate=(Date)request.getSession().getAttribute(CREATION_DATE);
		if (creationDate == null)
			creationDate=new Date(System.currentTimeMillis());
		
		logger.debug("using creationDate: " + creationDate);
    		
    	/*obtain user object from the session*/
	    HttpSession ss = SessionManager.getSession();
	    /* get back login user DTO */
	    UserDTO toolUser = (UserDTO) ss.getAttribute(AttributeNames.USER);
    	logger.debug("retrieving toolUser: " + toolUser);
    	logger.debug("retrieving toolUser userId: " + toolUser.getUserID());
    	String fullName= toolUser.getFirstName() + " " + toolUser.getLastName();
    	logger.debug("retrieving toolUser fullname: " + fullName);
    	long userId=toolUser.getUserID().longValue();
    	logger.debug("userId: " + userId);
    	
    	/* create a new qa content and leave the default content intact*/
    	VoteContent mc = new VoteContent();
		mc.setVoteContentId(toolContentId);
		mc.setTitle(richTextTitle);
		mc.setInstructions(richTextInstructions);
		mc.setCreationDate(creationDate); /*preserve this from the db*/ 
		mc.setUpdateDate(new Date(System.currentTimeMillis())); /* keep updating this one*/
		mc.setCreatedBy(userId); /* make sure we are setting the userId from the User object above*/
	    mc.setUsernameVisible(isUsernameVisible);
	    mc.setQuestionsSequenced(isQuestionsSequenced); /* the default question listing in learner mode will be all in the same page*/
	    mc.setOnlineInstructions(richTextOnlineInstructions);
	    mc.setOfflineInstructions(richTextOfflineInstructions);
	    mc.setRunOffline(false);
	    mc.setDefineLater(false);
	    mc.setContentInUse(isContentInUse);
	    mc.setEndLearningMessage("Thanks");
	    mc.setRunOffline(isRunOffline);
	    mc.setReportTitle(richTextReportTitle);
	    mc.setMonitoringReportTitle(monitoringReportTitle);
	    mc.setEndLearningMessage(richTextEndLearningMessage);
	    mc.setRetries(isRetries);
	    mc.setShowReport(isSln);
	    mc.setVoteQueContents(new TreeSet());
	    mc.setVoteSessions(new TreeSet());
	    logger.debug("mc content :" +  mc);
    	
    	/*create the content in the db*/
        mcService.createVote(mc);
        logger.debug("mc created with content id: " + toolContentId);
        
        return mc;
    }

    
    public static Map mergeMaps(Map map1, Map map2)
    {
    	Map mapMergedMap= new TreeMap(new VoteComparator());
    	logger.debug("merging maps now...");
    	
    	Iterator itMap1 = map1.entrySet().iterator();
		while (itMap1.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itMap1.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            mapMergedMap.put(pairs.getKey(), pairs.getValue());
        }
		
		logger.debug("adding the other map...");
		Iterator itMap2 = map2.entrySet().iterator();
		while (itMap2.hasNext()) 
        {
        	Map.Entry pairs = (Map.Entry)itMap2.next();
            logger.debug("using the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
            mapMergedMap.put(pairs.getKey(), pairs.getValue());
        }
		
		logger.debug("final merged map: " + mapMergedMap);
		return mapMergedMap;
    }
	


	public static boolean isOptionSelected(Map mapGeneralSelectedOptionsContent, String optionText, String questionIndex)
	{
	   Iterator itGSOMap = mapGeneralSelectedOptionsContent.entrySet().iterator();
	   logger.debug("questionIndex: " + questionIndex);
	   logger.debug("optionText: " + optionText);
	   while (itGSOMap.hasNext()) 
        {
            Map.Entry pairs = (Map.Entry)itGSOMap.next();
            if (pairs.getKey().toString().equals(questionIndex))
            {
            	Map currentOptionsMap= (Map)pairs.getValue();
            	logger.debug("currentOptionsMap: " + currentOptionsMap);
            	boolean isOptionSelectedInMap=isOptionSelectedInMap(optionText, currentOptionsMap);
            	logger.debug("isOptionSelectedInMap: " + isOptionSelectedInMap);
            	return isOptionSelectedInMap;
            }
        }
	   return false;
	}
	
	
	public static boolean isOptionSelectedInMap(String optionText, Map currentOptionsMap)
	{
		logger.debug("optionText: " + optionText);
		Iterator itCOMap = currentOptionsMap.entrySet().iterator();
		while (itCOMap.hasNext()) 
	    {
			Map.Entry pairs = (Map.Entry)itCOMap.next();
			if (pairs.getValue().toString().equals(optionText))
			{
				logger.debug("option text found in the map: " + optionText);
				return true;
			}
	    }
		return false;
	}

	
    public static void  assignStaterMapsToCurrentMaps(HttpServletRequest request)
    {
    	logger.debug("assigning maps..");
		Map mapStartupGeneralOptionsContent=(Map)request.getSession().getAttribute(MAP_STARTUP_GENERAL_OPTIONS_CONTENT);
		logger.debug("mapStartupGeneralOptionsContent: " + mapStartupGeneralOptionsContent);
    	
		Map mapStartupGeneralSelectedOptionsContent=(Map) request.getSession().getAttribute(MAP_STARTUP_GENERAL_SELECTED_OPTIONS_CONTENT);
		logger.debug("mapStartupGeneralSelectedOptionsContent: " + mapStartupGeneralSelectedOptionsContent);
    	
		request.getSession().setAttribute(MAP_GENERAL_OPTIONS_CONTENT, mapStartupGeneralOptionsContent);
		request.getSession().setAttribute(MAP_GENERAL_SELECTED_OPTIONS_CONTENT, mapStartupGeneralSelectedOptionsContent);
    }
    
    
    public static VoteAttachmentDTO uploadFile(HttpServletRequest request, VoteAuthoringForm mcAuthoringForm, boolean isOfflineFile) throws RepositoryCheckedException 
	{
    	logger.debug("doing uploadFile...");
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	InputStream stream=null; 
		String fileName=null; 
		String mimeType=null;
		String fileProperty=null;
    	
    	if (isOfflineFile)
    	{
    		FormFile theOfflineFile = mcAuthoringForm.getTheOfflineFile();
    		logger.debug("retrieved theOfflineFile: " + theOfflineFile);
    		
    		try
    		{
    			stream = theOfflineFile.getInputStream();
    			fileName=theOfflineFile.getFileName();
    			if (fileName.length() == 0)      
        		{
        			return null;
        		}
    			logger.debug("retrieved fileName: " + fileName);
    	    	fileProperty="OFFLINE";
    	    	
    	    }
    		catch(FileNotFoundException e)
    		{
    			logger.debug("filenotfound exception occured in accessing the repository server for the offline file : " + e.getMessage());
    		}
    		catch(IOException e)
    		{
    			logger.debug("io exception occured in accessing the repository server for the offline file : " + e.getMessage());
    		}
    		
    		if (fileName.length() > 0)     
    		{
    			List listUploadedOfflineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
        		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
        		int index=findFileNameIndex(listUploadedOfflineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOfflineFileNames.add(fileName);
            		logger.debug("listUploadedOfflineFileNames after add :" + listUploadedOfflineFileNames);
            		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);	
        		}
    		}
    		
    	}
    	else
    	{
    		FormFile theOnlineFile = mcAuthoringForm.getTheOnlineFile();
    		logger.debug("retrieved theOnlineFile: " + theOnlineFile);
    		
    		try
    		{
    			stream = theOnlineFile.getInputStream();
    			fileName=theOnlineFile.getFileName();
    			
    			if (fileName.length() == 0)      
        		{
        			return null;
        		}
    			
    			logger.debug("retrieved fileName: " + fileName);
    			fileProperty="ONLINE";
    	    	
    	    }
    		catch(FileNotFoundException e)
    		{
    			logger.debug("filenotfound exception occured in accessing the repository server for the online file : " + e.getMessage());
    		}
    		catch(IOException e)
    		{
    			logger.debug("io exception occured in accessing the repository server for the online file : " + e.getMessage());
    		}

    		if (fileName.length() > 0)     
    		{
    			List listUploadedOnlineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
        		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
        		int index=findFileNameIndex(listUploadedOnlineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOnlineFileNames.add(fileName);
            		logger.debug("listUploadedOnlineFileNames after add :" + listUploadedOnlineFileNames);
            		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);	
        		}
    		}
    	}
    	
    	IVoteService mcService =VoteUtils.getToolService(request);
		logger.debug("calling uploadFile with:");
		logger.debug("istream:" + stream);
		logger.debug("filename:" + fileName);
		logger.debug("mimeType:" + mimeType);
		logger.debug("fileProperty:" + fileProperty);
		
		NodeKey nodeKey=null;
		try{
			nodeKey=mcService.uploadFile(stream, fileName, mimeType, fileProperty);
			logger.debug("nodeKey:" + nodeKey);
			logger.debug("nodeKey uuid:" + nodeKey.getUuid());	
		}
		catch(FileException e)
		{
			logger.debug("exception writing raw data:" + e);
			/* return a null dto*/
			return null;
		}
		
		VoteAttachmentDTO mcAttachmentDTO= new VoteAttachmentDTO();
		mcAttachmentDTO.setUid(null);
 		mcAttachmentDTO.setUuid(nodeKey.getUuid().toString());
 		mcAttachmentDTO.setFilename(fileName);
 		mcAttachmentDTO.setOfflineFile(isOfflineFile);
 		
		return mcAttachmentDTO;
	}
    

    public static List populateMetaDataAsAttachments(List listOfflineFilesMetaData)
    {
    	List listAttachments=new LinkedList();
    	
    	Iterator itList = listOfflineFilesMetaData.iterator();
    	while (itList.hasNext())
        {
        	VoteUploadedFile mcUploadedFile=(VoteUploadedFile)itList.next();
        	logger.debug("mcUploadedFile:" + mcUploadedFile);
        	logger.debug("mcUploadedFile details, uid" + mcUploadedFile.getUid().toString());
        	logger.debug("mcUploadedFile details, uuid" + mcUploadedFile.getUuid());
        	logger.debug("mcUploadedFile details, filename" + mcUploadedFile.getFilename());
        	logger.debug("mcUploadedFile details, isOfflineFile" + !mcUploadedFile.isFileOnline());
        	
        	VoteAttachmentDTO mcAttachmentDTO= new VoteAttachmentDTO();
        	mcAttachmentDTO.setUid(mcUploadedFile.getUid().toString());
     		mcAttachmentDTO.setUuid(mcUploadedFile.getUuid());
     		mcAttachmentDTO.setFilename(mcUploadedFile.getFilename());
     		mcAttachmentDTO.setOfflineFile(!mcUploadedFile.isFileOnline());
     		
     		listAttachments.add(mcAttachmentDTO);
     		logger.debug("listAttachments after add" + listAttachments);
        }
 		logger.debug("final listAttachments after populating all: " + listAttachments);
    	return listAttachments;
    }
    
    
    public static List populateMetaDataAsFilenames(List listFilesMetaData)
    {
    	List listFilenames=new LinkedList();
    	
    	Iterator itList = listFilesMetaData.iterator();
    	while (itList.hasNext())
        {
    		VoteAttachmentDTO mcAttachmentDTO=(VoteAttachmentDTO)itList.next();
    		logger.debug("current filename" + mcAttachmentDTO.getFilename());
     		listFilenames.add(mcAttachmentDTO.getFilename());
     		logger.debug("listFilenames after add" + listFilenames);
        }
 		logger.debug("final listFilenames after populating all: " + listFilenames);
    	return listFilenames;
    }
    

    public static void removeFileItem(HttpServletRequest request, String filename, String offlineFile)
	{
    	logger.debug("offlineFile:" + offlineFile);
    	if (offlineFile.equals("1"))
    	{
    		logger.debug("will remove an offline file");
    		List listUploadedOfflineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_OFFLINE_FILENAMES);
    		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
    		
    		listUploadedOfflineFileNames.remove(filename);
    		logger.debug("removed offline filename:" + filename);
    		
    		logger.debug("listUploadedOfflineFileNames after remove :" + listUploadedOfflineFileNames);
    		request.getSession().setAttribute(LIST_UPLOADED_OFFLINE_FILENAMES,listUploadedOfflineFileNames);
    	}
    	else
    	{
    		logger.debug("will remove an online file");
    		List listUploadedOnlineFileNames=(List)request.getSession().getAttribute(LIST_UPLOADED_ONLINE_FILENAMES);
    		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
    		
    		listUploadedOnlineFileNames.remove(filename);
    		logger.debug("removed online filename:" + filename);
    		
    		logger.debug("listUploadedOnlineFileNames after remove :" + listUploadedOnlineFileNames);
    		request.getSession().setAttribute(LIST_UPLOADED_ONLINE_FILENAMES,listUploadedOnlineFileNames);
    	}
	}
    
    
    /**
     * findFileNameIndex(List listUploadedFileNames, String filename)
     * 
     * @param listUploadedFileNames
     * @param filename
     * @return int
     */
    public static int findFileNameIndex(List listUploadedFileNames, String filename)
    {
    	Iterator itListUploadedFileNames = listUploadedFileNames.iterator();
    	int mainIndex=0;
    	while (itListUploadedFileNames.hasNext())
        {
    		mainIndex++;
        	String currentFilename=(String) itListUploadedFileNames.next();
        	logger.debug("currentFilename :" + currentFilename);
        	if (currentFilename.equals(filename))
			{
        		logger.debug("currentFilename found in the list at mainIndex :" + mainIndex);
        		return mainIndex;
			}
        }
    	return 0;
    }
    
    
    /**
     * removeFileItem(List listFilesMetaData, String uuid)
     * 
     * @param listFilesMetaData
     * @param uuid
     * @return List
     */
    public static List removeFileItem(List listFilesMetaData, String uuid)
    {
        VoteAttachmentDTO deletableAttachmentDTO=null;
    	
    	Iterator itList = listFilesMetaData.iterator();
    	int mainIndex=0;
    	while (itList.hasNext())
        {
    		mainIndex++;
    		VoteAttachmentDTO currentAttachmentDTO=(VoteAttachmentDTO) itList.next();
    		logger.debug("currentAttachmentDTO :" + currentAttachmentDTO);
    		logger.debug("currentAttachmentDTO uuid :" + currentAttachmentDTO.getUuid());
    		
    		if (currentAttachmentDTO.getUuid().equals(uuid))
			{
    			logger.debug("equal uuid found uuid :" + uuid);
    			deletableAttachmentDTO=currentAttachmentDTO;
    			break;
			}
        }
    	
    	logger.debug("equal uuid found at index :" + mainIndex);
    	logger.debug("deletable attachment is:" + deletableAttachmentDTO);
    	
    	listFilesMetaData.remove(deletableAttachmentDTO);
    	logger.debug("listOfflineFilesMetaData after remove:" + listFilesMetaData);
    	
    	return listFilesMetaData;
    }
    
    
    public static void persistFilesMetaData(HttpServletRequest request, boolean isOfflineFile, VoteContent mcContent)
    {
    	IVoteService mcService =VoteUtils.getToolService(request);

    	List listFilesMetaData=null;
    	logger.debug("doing persistFilesMetaData...");
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	if (isOfflineFile)
    	{
    		listFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
    	}
    	else
    	{
    		listFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
    	}
    	logger.debug("listFilesMetaData:" + listFilesMetaData);
    	
    	Iterator itListFilesMetaData = listFilesMetaData.iterator();
        while (itListFilesMetaData.hasNext())
        {
            VoteAttachmentDTO mcAttachmentDTO=(VoteAttachmentDTO)itListFilesMetaData.next();
        	logger.debug("mcAttachmentDTO:" + mcAttachmentDTO);
        	String uid=mcAttachmentDTO.getUid();
        	logger.debug("uid:" + uid);
        	
        	String uuid=mcAttachmentDTO.getUuid();
        	boolean isOnlineFile=!mcAttachmentDTO.isOfflineFile();
        	String fileName=mcAttachmentDTO.getFilename();
        	
        	if (uid == null)
        	{
        		logger.debug("persisting files metadata...");
        		if (!mcService.isUuidPersisted(uuid))
        		{
        			mcService.persistFile(uuid, isOnlineFile, fileName, mcContent);
        		}
        	}
        }
    }
    
    
    /**
     * extractFileNames(List listFilesMetaData)
     * 
     * @param listFilesMetaData
     * @return List
     */
    public static List extractFileNames(List listFilesMetaData)
    {
    	Iterator itList = listFilesMetaData.iterator();
    	LinkedList listFilenames= new LinkedList();
    	
        while (itList.hasNext())
        {
            VoteAttachmentDTO mcAttachmentDTO=(VoteAttachmentDTO)itList.next();
        	String filename=mcAttachmentDTO.getFilename();
        	logger.debug("extracted filename: " + filename);
        	listFilenames.add(filename);
        }
    	logger.debug("final extracted listFilenames: " + listFilenames);
    	return listFilenames;
    }
    
    
    public static void removeRedundantOfflineFileItems(HttpServletRequest request, VoteContent mcContent)
    {
    	IVoteService mcService =VoteUtils.getToolService(request);
    	
    	List allOfflineFilenames=mcService.retrieveVoteUploadedOfflineFilesName(mcContent.getUid());
    	logger.debug("allOfflineFilenames:" + allOfflineFilenames);
    	
    	List listOfflineFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
 		logger.debug("listOfflineFilesMetaData:" + listOfflineFilesMetaData);
 		
 		List listUploadedOfflineFileNames=extractFileNames(listOfflineFilesMetaData);
		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
    	
		boolean matchFound=false;
    	Iterator itAllOfflineFiles = allOfflineFilenames.iterator();
        while (itAllOfflineFiles.hasNext())
        {
        	String filename =(String)itAllOfflineFiles.next();
        	logger.debug("filename: " + filename);
        
        	matchFound=false;
        	Iterator itFiles = listUploadedOfflineFileNames.iterator();
            while (itFiles.hasNext())
            {
            	matchFound=false;
            	String currentFilename =(String)itFiles.next();
            	logger.debug("currentFilename: " + currentFilename);
            	
            	if (filename.equals(currentFilename))
            	{
            		logger.debug("filename match found : " + currentFilename);
            		matchFound=true;
            		break;
            	}
    		}
            
            logger.debug("matchFound : " + matchFound);
            if (matchFound == false)
            {
            	logger.debug("matchFound is false for filename: " + filename);
            	mcService.removeOffLineFile(filename, mcContent.getUid()); 
            	logger.debug("filename removed: " + filename);
            }
		}
    }
    

    public static void removeRedundantOnlineFileItems(HttpServletRequest request, VoteContent mcContent)
    {
    	IVoteService mcService =VoteUtils.getToolService(request);
    	
    	List allOnlineFilenames=mcService.retrieveVoteUploadedOnlineFilesName(mcContent.getUid());
    	logger.debug("allOnlineFilenames:" + allOnlineFilenames);
    	
    	List listOnlineFilesMetaData =(List)request.getSession().getAttribute(LIST_ONLINEFILES_METADATA);
 		logger.debug("listOnlineFilesMetaData:" + listOnlineFilesMetaData);
 		
 		List listUploadedOnlineFileNames=extractFileNames(listOnlineFilesMetaData);
		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
    	
    	
		boolean matchFound=false;
    	Iterator itAllOnlineFiles = allOnlineFilenames.iterator();
        while (itAllOnlineFiles.hasNext())
        {
        	String filename =(String)itAllOnlineFiles.next();
        	logger.debug("filename: " + filename);
        
        	matchFound=false;
        	Iterator itFiles = listUploadedOnlineFileNames.iterator();
            while (itFiles.hasNext())
            {
            	matchFound=false;
            	String currentFilename =(String)itFiles.next();
            	logger.debug("currentFilename: " + currentFilename);
            	
            	if (filename.equals(currentFilename))
            	{
            		logger.debug("filename match found : " + currentFilename);
            		matchFound=true;
            		break;
            	}
    		}
            
            logger.debug("matchFound : " + matchFound);
            if (matchFound == false)
            {
            	logger.debug("matchFound is false for filename: " + filename);
            	mcService.removeOnLineFile(filename, mcContent.getUid()); 
            	logger.debug("filename removed: " + filename);
            }
		}
    }
    
    /**
     * simulatePropertyInspector_RunOffline(HttpServletRequest request)
     * @param request
     */
    public void simulatePropertyInspector_RunOffline(HttpServletRequest request)
    {
    	IVoteService mcService =VoteUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			mcService.setAsRunOffline(new Long(toolContentId));	
			}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-RunAsOffline");
		}
    	logger.debug("end of simulating RunOffline on content id: " + toolContentId);
    }
	
    /**
     * Normally, a request to set defineLaterproperty of the content comes directly from container through the property inspector.
     * What we do below is simulate that for development purposes.
     * @param request
     */
    public void simulatePropertyInspector_setAsDefineLater(HttpServletRequest request)
    {
    	IVoteService mcService =VoteUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			mcService.setAsDefineLater(new Long(toolContentId));	
    		}
    		catch(ToolException e)
			{
    			logger.debug("we should never come here");
			}
    		
    		logger.debug("post-setAsDefineLater");
		}
    	logger.debug("end of simulating setAsDefineLater on content id: " + toolContentId);
    }
    
    
    
    protected void reconstructOptionContentMapForAdd(Map mapOptionsContent, HttpServletRequest request)
    {
        logger.debug("doing reconstructOptionContentMapForAdd.");
    	logger.debug("pre-add Map content: " + mapOptionsContent);
    	logger.debug("pre-add Map size: " + mapOptionsContent.size());
    	
    	repopulateMap(mapOptionsContent, request);
    	
    	mapOptionsContent.put(new Long(mapOptionsContent.size()+1).toString(), "");
    	request.getSession().setAttribute("mapOptionsContent", mapOptionsContent);
	     
    	logger.debug("post-add Map is: " + mapOptionsContent);    	
	   	logger.debug("post-add count " + mapOptionsContent.size());
    }
    
    
    protected void reconstructOptionContentMapForRemove(Map mapOptionsContent, HttpServletRequest request, VoteAuthoringForm voteAuthoringForm)
    {
    		logger.debug("doing reconstructOptionContentMapForRemove.");
    	 	//String questionIndex =voteAuthoringForm.getQuestionIndex();
    		String optIndex =voteAuthoringForm.getOptIndex();
    	 	logger.debug("pre-delete map content:  " + mapOptionsContent);
    	 	logger.debug("optIndex: " + optIndex);
    	 	
    	 	String defLater=(String)request.getSession().getAttribute(ACTIVE_MODULE);
    	 	logger.debug("defLater: " + defLater);
    	 	
    	 	String removableOptIndex=null;
    	 	if (defLater.equals(MONITORING))
    	 	{
       	 		removableOptIndex=(String)request.getSession().getAttribute(REMOVABLE_QUESTION_INDEX);
        	 	logger.debug("removableOptIndex: " + removableOptIndex);
        	 	optIndex=removableOptIndex;
    	 	}
    	 	logger.debug("final removableOptIndex: " + optIndex);
    	 	
    	 	
    	 	long longOptIndex= new Long(optIndex).longValue();
    	 	logger.debug("pre-delete count: " + mapOptionsContent.size());
    	 	
        	repopulateMap(mapOptionsContent, request);
        	logger.debug("post-repopulateMap optIndex: " + optIndex);
        	
        	mapOptionsContent.remove(new Long(longOptIndex).toString());	
	 		logger.debug("removed the question content with index: " + longOptIndex);
	 		request.getSession().setAttribute("mapOptionsContent", mapOptionsContent);
	    	
	    	logger.debug("post-delete count " + mapOptionsContent.size());
	    	logger.debug("post-delete map content:  " + mapOptionsContent);
    }


    protected void repopulateMap(Map mapOptionsContent, HttpServletRequest request)
    {
    	logger.debug("optIndex: " + request.getSession().getAttribute("optIndex"));
    	long optIndex= new Long(request.getSession().getAttribute("optIndex").toString()).longValue();
    	logger.debug("optIndex: " + optIndex);

    	/* if there is data in the Map remaining from previous session remove those */
    	mapOptionsContent.clear();
		logger.debug("Map got initialized: " + mapOptionsContent);
		
		for (long i=0; i < optIndex ; i++)
		{
			String candidateOptionEntry =request.getParameter("optionContent" + i);
			if (i==0)
    		{
    			request.getSession().setAttribute("defaultOptionContent", candidateOptionEntry);
    			logger.debug("defaultQuestionContent set to: " + candidateOptionEntry);
    		}
			if ((candidateOptionEntry != null) && (candidateOptionEntry.length() > 0))
			{
				logger.debug("using key: " + i);
				mapOptionsContent.put(new Long(i+1).toString(), candidateOptionEntry);
				logger.debug("added new entry.");	
			}
		}
    }

    
    /**
     * cleans up authoring http session 
     * cleanupAuthoringSession(HttpServletRequest request)
     * @param request
     */
    public static void cleanupAuthoringSession(HttpServletRequest request)
    {

    }
}
