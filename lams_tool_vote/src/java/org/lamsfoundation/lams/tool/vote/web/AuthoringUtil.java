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
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
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

    protected static void setRadioboxes(VoteContent voteContent, VoteAuthoringForm voteAuthoringForm)
	{
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
    	String optionContent0=request.getParameter("optionContent0");
    	logger.debug("optionContent0: " + optionContent0);
		mapCounter++;
    	mapTempQuestionsContent.put(new Long(mapCounter).toString(), optionContent0);
    	
    	
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
	
	
    public static Map shiftMap(Map mapOptionsContent, String optIndex , String movableOptionEntry, String direction)
    {
    	logger.debug("movableOptionEntry: " +  movableOptionEntry);
    	/* map to be returned */
    	Map mapTempOptionsContent= new TreeMap(new VoteComparator());
    	
    	String shiftableEntry=null;
    	
    	int shiftableIndex=0;
    	if (direction.equals("down"))
        {
    		logger.debug("moving map down");
    		shiftableIndex=new Integer(optIndex).intValue() + 1;
        }
    	else
    	{
    		logger.debug("moving map up");
    		shiftableIndex=new Integer(optIndex).intValue() - 1;
    	}
    		
		logger.debug("shiftableIndex: " +  shiftableIndex);
    	shiftableEntry=(String)mapOptionsContent.get(new Integer(shiftableIndex).toString());
    	logger.debug("shiftable entry: " +  shiftableEntry);
    	
    	if (shiftableEntry != null) 
    	{
    		Iterator itQuestionsMap = mapOptionsContent.entrySet().iterator();
    		long mapCounter=0;
    		while (itQuestionsMap.hasNext()) {
            	Map.Entry pairs = (Map.Entry)itQuestionsMap.next();
                logger.debug("comparing the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
                mapCounter++;
                logger.debug("mapCounter: " +  mapCounter);
                
                if (!pairs.getKey().equals(optIndex) && !pairs.getKey().equals(new Integer(shiftableIndex).toString()))
                {
                	logger.debug("normal copy " +  optIndex);
                	mapTempOptionsContent.put(new Long(mapCounter).toString(), pairs.getValue());
                }
                else if (pairs.getKey().equals(optIndex))
                {
                	logger.debug("move type 1 " +  optIndex);
                	mapTempOptionsContent.put(new Long(mapCounter).toString(), shiftableEntry);
                }
                else if (pairs.getKey().equals(new Integer(shiftableIndex).toString()))
                {
                    mapTempOptionsContent.put(new Long(mapCounter).toString(), movableOptionEntry);
                }
            }
    	}
    	else
    	{
    	    mapTempOptionsContent=mapOptionsContent;
    	}
    		return mapTempOptionsContent;
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
    
    
    public static VoteAttachmentDTO uploadFile(HttpServletRequest request, VoteAuthoringForm voteAuthoringForm, boolean isOfflineFile) throws RepositoryCheckedException 
	{
    	logger.debug("doing uploadFile...");
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	InputStream stream=null; 
		String fileName=null; 
		String mimeType=null;
		String fileProperty=null;
    	
    	if (isOfflineFile)
    	{
    		FormFile theOfflineFile = voteAuthoringForm.getTheOfflineFile();
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
    		FormFile theOnlineFile = voteAuthoringForm.getTheOnlineFile();
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
    	
    	IVoteService voteService =VoteUtils.getToolService(request);
		logger.debug("calling uploadFile with:");
		logger.debug("istream:" + stream);
		logger.debug("filename:" + fileName);
		logger.debug("mimeType:" + mimeType);
		logger.debug("fileProperty:" + fileProperty);
		
		NodeKey nodeKey=null;
		try{
			nodeKey=voteService.uploadFile(stream, fileName, mimeType, fileProperty);
			logger.debug("nodeKey:" + nodeKey);
			logger.debug("nodeKey uuid:" + nodeKey.getUuid());	
		}
		catch(FileException e)
		{
			logger.debug("exception writing raw data:" + e);
			/* return a null dto*/
			return null;
		}
		
		VoteAttachmentDTO voteAttachmentDTO= new VoteAttachmentDTO();
		voteAttachmentDTO.setUid(null);
 		voteAttachmentDTO.setUuid(nodeKey.getUuid().toString());
 		voteAttachmentDTO.setFilename(fileName);
 		voteAttachmentDTO.setOfflineFile(isOfflineFile);
 		
		return voteAttachmentDTO;
	}
    

    public static List populateMetaDataAsAttachments(List listOfflineFilesMetaData)
    {
    	List listAttachments=new LinkedList();
    	
    	Iterator itList = listOfflineFilesMetaData.iterator();
    	while (itList.hasNext())
        {
        	VoteUploadedFile voteUploadedFile=(VoteUploadedFile)itList.next();
        	logger.debug("voteUploadedFile:" + voteUploadedFile);
        	logger.debug("voteUploadedFile details, uid" + voteUploadedFile.getUid().toString());
        	logger.debug("voteUploadedFile details, uuid" + voteUploadedFile.getUuid());
        	logger.debug("voteUploadedFile details, filename" + voteUploadedFile.getFilename());
        	logger.debug("voteUploadedFile details, isOfflineFile" + !voteUploadedFile.isFileOnline());
        	
        	VoteAttachmentDTO voteAttachmentDTO= new VoteAttachmentDTO();
        	voteAttachmentDTO.setUid(voteUploadedFile.getUid().toString());
     		voteAttachmentDTO.setUuid(voteUploadedFile.getUuid());
     		voteAttachmentDTO.setFilename(voteUploadedFile.getFilename());
     		voteAttachmentDTO.setOfflineFile(!voteUploadedFile.isFileOnline());
     		
     		listAttachments.add(voteAttachmentDTO);
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
    		VoteAttachmentDTO voteAttachmentDTO=(VoteAttachmentDTO)itList.next();
    		logger.debug("current filename" + voteAttachmentDTO.getFilename());
     		listFilenames.add(voteAttachmentDTO.getFilename());
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
    
    
    public static void persistFilesMetaData(HttpServletRequest request, boolean isOfflineFile, VoteContent voteContent)
    {
    	IVoteService voteService =VoteUtils.getToolService(request);

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
            VoteAttachmentDTO voteAttachmentDTO=(VoteAttachmentDTO)itListFilesMetaData.next();
        	logger.debug("voteAttachmentDTO:" + voteAttachmentDTO);
        	String uid=voteAttachmentDTO.getUid();
        	logger.debug("uid:" + uid);
        	
        	String uuid=voteAttachmentDTO.getUuid();
        	boolean isOnlineFile=!voteAttachmentDTO.isOfflineFile();
        	String fileName=voteAttachmentDTO.getFilename();
        	
        	if (uid == null)
        	{
        		logger.debug("persisting files metadata...");
        		if (!voteService.isUuidPersisted(uuid))
        		{
        			voteService.persistFile(uuid, isOnlineFile, fileName, voteContent);
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
            VoteAttachmentDTO voteAttachmentDTO=(VoteAttachmentDTO)itList.next();
        	String filename=voteAttachmentDTO.getFilename();
        	logger.debug("extracted filename: " + filename);
        	listFilenames.add(filename);
        }
    	logger.debug("final extracted listFilenames: " + listFilenames);
    	return listFilenames;
    }
    
    
    public static void removeRedundantOfflineFileItems(HttpServletRequest request, VoteContent voteContent)
    {
    	IVoteService voteService =VoteUtils.getToolService(request);
    	
    	List allOfflineFilenames=voteService.retrieveVoteUploadedOfflineFilesName(voteContent.getUid());
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
            	voteService.removeOffLineFile(filename, voteContent.getUid()); 
            	logger.debug("filename removed: " + filename);
            }
		}
    }
    

    public static void removeRedundantOnlineFileItems(HttpServletRequest request, VoteContent voteContent)
    {
    	IVoteService voteService =VoteUtils.getToolService(request);
    	
    	List allOnlineFilenames=voteService.retrieveVoteUploadedOnlineFilesName(voteContent.getUid());
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
            	voteService.removeOnLineFile(filename, voteContent.getUid()); 
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
    	IVoteService voteService =VoteUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			voteService.setAsRunOffline(new Long(toolContentId));	
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
    	IVoteService voteService =VoteUtils.getToolService(request);
    	
    	String toolContentId=(String)request.getSession().getAttribute(TOOL_CONTENT_ID);
    	if ((toolContentId != null) && (!toolContentId.equals("")))
    	{
    		logger.debug("passed TOOL_CONTENT_ID : " + new Long(toolContentId));
    		try
			{
    			voteService.setAsDefineLater(new Long(toolContentId));	
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

    protected  void reconstructOptionsContentMapForSubmit(Map mapOptionsContent, HttpServletRequest request)
    {
    	logger.debug("pre-submit Map:" + mapOptionsContent);
    	logger.debug("pre-submit Map size :" + mapOptionsContent.size());
    	
    	repopulateMap(mapOptionsContent, request);
    	Map mapFinalOptionsContent = new TreeMap(new VoteComparator());
    	
    	Iterator itMap = mapOptionsContent.entrySet().iterator();
	    while (itMap.hasNext()) {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        if ((pairs.getValue() != null) && (!pairs.getValue().equals("")))
    		{
	        	mapFinalOptionsContent.put(pairs.getKey(), pairs.getValue());
	        	logger.debug("adding the  pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		}
	    }
	    
	    mapOptionsContent=(TreeMap)mapFinalOptionsContent;
	    request.getSession().setAttribute("mapOptionsContent", mapOptionsContent);
	    logger.debug("final mapOptionsContent:" + mapOptionsContent);
    }

    
    public void removeRedundantOptions (Map mapOptionsContent, IVoteService voteService, VoteAuthoringForm voteAuthoringForm, HttpServletRequest request)
	{
    	logger.debug("removing unused entries... ");
    	logger.debug("mapOptionsContent:  " + mapOptionsContent);
    	
    	String toolContentId=voteAuthoringForm.getToolContentId();
    	logger.debug("toolContentId:  " + toolContentId);
     	if ((toolContentId == null) || toolContentId.equals(""))
     	{
     		logger.debug("getting toolContentId from session.");
     		Long longToolContentId =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
     		toolContentId=longToolContentId.toString();
     		logger.debug("toolContentId: " + toolContentId);
     	}
     	logger.debug("final toolContentId: " + toolContentId);
    	
    	
    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentId));
    	logger.debug("voteContent:  " + voteContent);
    	
    	if (voteContent != null)
    	{
        	logger.debug("voteContent uid: " + voteContent.getUid());
        	List allQuestions=voteService.getAllQuestionEntries(voteContent.getUid());
        	logger.debug("allQuestions: " + allQuestions);
        	
        	Iterator listIterator=allQuestions.iterator();
    		Long mapIndex=new Long(1);
    		boolean entryUsed=false;
    		while (listIterator.hasNext())
    		{
    			VoteQueContent queContent=(VoteQueContent)listIterator.next();
    			logger.debug("queContent data: " + queContent);
    			
    			entryUsed=false;
    	        Iterator itMap = mapOptionsContent.entrySet().iterator();
    	        while (itMap.hasNext()) 
    		    {
    	        	entryUsed=false;
    		        Map.Entry pairs = (Map.Entry)itMap.next();
    		        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
    		        if (pairs.getValue().toString().length() != 0)
    		        {
    		        	logger.debug("text from map:" + pairs.getValue().toString());
    		        	logger.debug("text from db:" + queContent.getQuestion());
    		        	if (pairs.getValue().toString().equals(queContent.getQuestion()))
    		        	{
    		        		logger.debug("used entry in db:" + queContent.getQuestion());
    		        		entryUsed=true;
    		        		break;
    		        	}
    		        }
    		    }
    	        
    	        if (entryUsed == false)
    	        {
    	        	logger.debug("removing unused entry in db:" + queContent.getQuestion());
    	        	
    	        	VoteQueContent removeableVoteQueContent=voteService.getQuestionContentByQuestionText(queContent.getQuestion(), voteContent.getUid());
        			logger.debug("removeableVoteQueContent"  + removeableVoteQueContent);
        			if (removeableVoteQueContent != null)
        			{
        				voteService.removeVoteQueContent(removeableVoteQueContent);
            			logger.debug("removed removeableVoteQueContent from the db: " + removeableVoteQueContent);	
        			}
    	        	
    	        }
    		}    		
    	}
	
	}

    
    public VoteContent saveOrUpdateVoteContent(Map mapOptionsContent, IVoteService voteService, VoteAuthoringForm voteAuthoringForm, HttpServletRequest request)
    {
        UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
        
        String richTextTitle = request.getParameter("title");
        String richTextInstructions = request.getParameter("instructions");
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        String voteChangable = voteAuthoringForm.getVoteChangable();
        logger.debug("voteChangable: " + voteChangable);
        
        String lockedOnFinish=voteAuthoringForm.getLockOnFinish();
        logger.debug("lockedOnFinish: " + lockedOnFinish);
        
        String allowText=voteAuthoringForm.getAllowText();
        logger.debug("allowText: " + allowText);

        String maxNomcount= voteAuthoringForm.getMaxNominationCount();
	    logger.debug("maxNomcount: " + maxNomcount);
	    
        String richTextOfflineInstructions=(String)request.getSession().getAttribute(RICHTEXT_OFFLINEINSTRUCTIONS);
        logger.debug("richTextOfflineInstructions: " + richTextOfflineInstructions);
		String richTextOnlineInstructions=(String)request.getSession().getAttribute(RICHTEXT_ONLINEINSTRUCTIONS);
		logger.debug("richTextOnlineInstructions: " + richTextOnlineInstructions);

        boolean setCommonContent=true; 
        if ((lockedOnFinish == null) || (voteChangable == null))
        {
        	setCommonContent=false;
        }
        logger.debug("setCommonContent: " + setCommonContent);
		
        String activeModule=(String)request.getSession().getAttribute(ACTIVE_MODULE);
        logger.debug("activeModule: " + activeModule);

        boolean voteChangableBoolean=false;
        boolean lockedOnFinishBoolean=false;
        boolean allowTextBoolean=false;
        
        if (setCommonContent)
        {
            if (voteChangable.equalsIgnoreCase(ON))
                voteChangableBoolean=true;
            
            if (lockedOnFinish.equalsIgnoreCase(ON))
                lockedOnFinishBoolean=true;

            if (allowText.equalsIgnoreCase(ON))
                allowTextBoolean=true;
        }
        
        
        long userId=0;
        if (toolUser != null)
        {
        	userId = toolUser.getUserID().longValue();	
        }
        else
        {
    		HttpSession ss = SessionManager.getSession();
    		logger.debug("ss: " + ss);
    		UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
    		logger.debug("user" + user);
    		if (user != null)
    		{
    			userId = user.getUserID().longValue();	
    		}
    		else
    		{
    			logger.debug("should not reach here");
    			userId=0;
    		}
        }
        logger.debug("userId: " + userId);
        
        
        Long toolContentIdLong =(Long) request.getSession().getAttribute(TOOL_CONTENT_ID);
        logger.debug("toolContentIdLong: " + toolContentIdLong);
        
        String toolContentId=toolContentIdLong.toString();
    	logger.debug("toolContentId:  " + toolContentId);
     	
     	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentId));
     	logger.debug("voteContent: " + voteContent);
     	
     	boolean newContent=false;
        if(voteContent == null)
        {
        	voteContent = new VoteContent();
        	newContent=true;
        }


    	logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
    	voteContent.setVoteContentId(new Long(toolContentId));
     	voteContent.setTitle(richTextTitle);
     	voteContent.setInstructions(richTextInstructions);
     	voteContent.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
     	logger.debug("userId: " + userId);
     	voteContent.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
     	logger.debug("end of setting common content values...");

     	
        if ((!activeModule.equals(DEFINE_LATER)) && (setCommonContent))
		{
        	logger.debug("setting other content values...");
         	voteContent.setVoteChangable(voteChangableBoolean);
         	voteContent.setLockOnFinish(lockedOnFinishBoolean);
         	voteContent.setAllowText(allowTextBoolean);
         	voteContent.setMaxNominationCount(maxNomcount);
         	voteContent.setOnlineInstructions(richTextOnlineInstructions);
         	voteContent.setOfflineInstructions(richTextOfflineInstructions);
		}
        
 
        if (newContent)
        {
        	logger.debug("will create: " + voteContent);
         	voteService.createVote(voteContent);
        }
        else
        {
        	logger.debug("will update: " + voteContent);
            voteService.updateVote(voteContent);
        }
        
        voteContent=voteService.retrieveVote(new Long(toolContentId));
     	logger.debug("voteContent: " + voteContent);
        
        voteContent=createOptiosContent(mapOptionsContent, voteService, voteContent);
        
        return voteContent;
    }
    
    protected VoteContent createOptiosContent(Map mapOptionsContent, IVoteService voteService, VoteContent voteContent)
    {    
        logger.debug("content uid is: " + voteContent.getUid());
        List questions=voteService.retrieveVoteQueContentsByToolContentId(voteContent.getUid().longValue());
        logger.debug("questions: " + questions);

        
        Iterator itMap = mapOptionsContent.entrySet().iterator();
        int diplayOrder=0;
        while (itMap.hasNext()) 
	    {
	        Map.Entry pairs = (Map.Entry)itMap.next();
	        logger.debug("using the pair: " +  pairs.getKey() + " = " + pairs.getValue());
	        
	        if (pairs.getValue().toString().length() != 0)
	        {
	        	logger.debug("starting createQuestionContent: pairs.getValue().toString():" + pairs.getValue().toString());
	        	logger.debug("starting createQuestionContent: voteContent: " + voteContent);
	        	logger.debug("starting createQuestionContent: diplayOrder: " + diplayOrder);
	        	diplayOrder=new Integer(pairs.getKey().toString()).intValue();
	        	logger.debug("int diplayOrder: " + diplayOrder);
	        	
	        	
	        	VoteQueContent queContent=  new VoteQueContent(pairs.getValue().toString(),
	        	        									diplayOrder,
		        											voteContent,
															null);
		        
		        
			       /* checks if the question is already recorded*/
			       logger.debug("question text is: " + pairs.getValue().toString());
			       logger.debug("content uid is: " + voteContent.getUid());
			       logger.debug("question display order is: " + diplayOrder);
			       
			       VoteQueContent existingVoteQueContent=voteService.getQuestionContentByQuestionText(pairs.getValue().toString(), voteContent.getUid());
			       logger.debug("existingVoteQueContent: " + existingVoteQueContent);
			       if (existingVoteQueContent == null)
			       {
			       	/*make sure a question with the same question text is not already saved*/
			    	VoteQueContent duplicateVoteQueContent=voteService.getQuestionContentByQuestionText(pairs.getValue().toString(), voteContent.getUid());
			    	logger.debug("duplicateVoteQueContent: " + duplicateVoteQueContent);
			       	if (duplicateVoteQueContent == null)
			       	{
			       		logger.debug("adding a new question to content: " + queContent);
			       		voteContent.getVoteQueContents().add(queContent);
			       		queContent.setVoteContent(voteContent);
	
			       		voteService.createVoteQue(queContent);
			       	}
			       }
			       else
			       {
			       		existingVoteQueContent.setQuestion(pairs.getValue().toString());
			       		existingVoteQueContent.setDisplayOrder(diplayOrder);
			       		logger.debug("updating the existing question content: " + existingVoteQueContent);
			       		voteService.updateVoteQueContent(existingVoteQueContent);
			       }
	        }      
	    }
        return voteContent;
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
