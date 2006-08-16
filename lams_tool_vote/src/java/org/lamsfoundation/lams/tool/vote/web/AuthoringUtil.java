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
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteQueContent;
import org.lamsfoundation.lams.tool.vote.pojos.VoteUploadedFile;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.lamsfoundation.lams.web.util.SessionMap;

/**
 * 
 * <p> Keeps all operations needed for Authoring mode. </p>
 *  
 * @author Ozgur Demirtas
 *
 */
public class AuthoringUtil implements VoteAppConstants {
	static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

	/**
	 * checks if there are any duplicate entries
	 * @param mapOptionsContent
	 * @returnboolean
	 */
    public static boolean verifyDuplicateNominations(Map mapOptionsContent)
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
            			return true;	
        		}
    		}	
		}
    	return false;
	}
    
    /**
     * checks if the map is empty or not
     * @param map
     * @return boolean
     */
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

    public static Map shiftMap(Map mapOptionsContent, String optIndex , String movableOptionEntry, String direction)
    {
    	logger.debug("movableOptionEntry: " +  movableOptionEntry);
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


    /**
     * 
     * Used in uploading offline and online files
     * 
     * @param request
     * @param voteAuthoringForm
     * @param isOfflineFile
     * @return VoteAttachmentDTO
     * @throws RepositoryCheckedException
     */
    public static VoteAttachmentDTO uploadFile(HttpServletRequest request, IVoteService voteService, VoteAuthoringForm voteAuthoringForm, 
            boolean isOfflineFile, SessionMap sessionMap) throws RepositoryCheckedException 
	{
    	logger.debug("doing uploadFile...: " + sessionMap);
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
    		    List listUploadedOfflineFileNames=(List)sessionMap.get(LIST_UPLOADED_OFFLINE_FILENAMES_KEY);
        		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
        		int index=findFileNameIndex(listUploadedOfflineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOfflineFileNames.add(fileName);
            		logger.debug("listUploadedOfflineFileNames after add :" + listUploadedOfflineFileNames);
            		sessionMap.put(LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
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
    		    List listUploadedOnlineFileNames=(List)sessionMap.get(LIST_UPLOADED_ONLINE_FILENAMES_KEY);
        		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
        		int index=findFileNameIndex(listUploadedOnlineFileNames, fileName);
        		logger.debug("index:" + index);
        		if (index == 0)
        		{
        			listUploadedOnlineFileNames.add(fileName);
            		logger.debug("listUploadedOnlineFileNames after add :" + listUploadedOnlineFileNames);
            		sessionMap.put(LIST_UPLOADED_ONLINE_FILENAMES_KEY, listUploadedOnlineFileNames);
        		}
    		}
    	}
    	
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
 		
 		logger.debug("uploadFile ends with sessionMap:" + sessionMap);
		return voteAttachmentDTO;
	}
    
    
	/**
	 * returns a list of Vote attachements for listing of online and offline file information
	 * @param listOfflineFilesMetaData
	 * @return
	 */
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
    
    /**
     * @param listFilesMetaData
     * @return
     */
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
    

    /**
     * used in removing a file item listed in the jsp
     * @param request
     * @param filename
     * @param offlineFile
     */
    public static void removeFileItem(HttpServletRequest request, String filename, String offlineFile, SessionMap sessionMap)
	{
        logger.debug("starting removeFileItem, sessionMap:" + sessionMap);
    	logger.debug("offlineFile:" + offlineFile);
    	if (offlineFile.equals("1"))
    	{
    		logger.debug("will remove an offline file");
    		List listUploadedOfflineFileNames=(List)sessionMap.get(LIST_UPLOADED_OFFLINE_FILENAMES_KEY);
    		logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
    		
    		listUploadedOfflineFileNames.remove(filename);
    		logger.debug("removed offline filename:" + filename);
    		
    		logger.debug("listUploadedOfflineFileNames after remove :" + listUploadedOfflineFileNames);
    		sessionMap.put(LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
    	}
    	else
    	{
    		logger.debug("will remove an online file");
    		List listUploadedOnlineFileNames=(List)sessionMap.get(LIST_UPLOADED_ONLINE_FILENAMES_KEY);
    		logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
    		
    		listUploadedOnlineFileNames.remove(filename);
    		logger.debug("removed online filename:" + filename);
    		
    		logger.debug("listUploadedOnlineFileNames after remove :" + listUploadedOnlineFileNames);
    		sessionMap.put(LIST_UPLOADED_ONLINE_FILENAMES_KEY, listUploadedOnlineFileNames);
    	}
	}
    
    
    /**
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

    
    /**
     * Online and offline files metadata is stored into the database 
     * @param request
     * @param isOfflineFile
     * @param voteContent
     */
    public static void persistFilesMetaData(HttpServletRequest request, IVoteService voteService, 
            boolean isOfflineFile, VoteContent voteContent, SessionMap sessionMap)
    {
    	logger.debug("doing persistFilesMetaData... sessionMap" + sessionMap);
       	List listFilesMetaData=null;
    	logger.debug("isOfflineFile:" + isOfflineFile);
    	
    	if (isOfflineFile)
    	{
    	    listFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
    	}
    	else
    	{
    		listFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
    		
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
    
    /**
     * used if an offline file item is no more referenced
     * @param request
     * @param voteContent
     */
    public static void removeRedundantOfflineFileItems(HttpServletRequest request, IVoteService voteService, 
            VoteContent voteContent, SessionMap sessionMap)
    {
        logger.debug("starting removeRedundantOfflineFileItems:" + sessionMap);
    	List allOfflineFilenames=voteService.retrieveVoteUploadedOfflineFilesName(voteContent.getUid());
    	logger.debug("allOfflineFilenames:" + allOfflineFilenames);
    	
    	//List listOfflineFilesMetaData =(List)request.getSession().getAttribute(LIST_OFFLINEFILES_METADATA);
    	List listOfflineFilesMetaData =(List)sessionMap.get(LIST_OFFLINEFILES_METADATA_KEY);
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
    
    /**
     * used if an online file item is no more referenced
     * @param request
     * @param voteContent
     */
    public static void removeRedundantOnlineFileItems(HttpServletRequest request, IVoteService voteService, 
            VoteContent voteContent, SessionMap sessionMap)
    {
        logger.debug("starting removeRedundantOnlineFileItems:" + sessionMap);
        
    	List allOnlineFilenames=voteService.retrieveVoteUploadedOnlineFilesName(voteContent.getUid());
    	logger.debug("allOnlineFilenames:" + allOnlineFilenames);

    	List listOnlineFilesMetaData =(List)sessionMap.get(LIST_ONLINEFILES_METADATA_KEY);
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

    
    protected Map reconstructOptionContentMapForAdd(Map mapOptionsContent, HttpServletRequest request)
    {
        logger.debug("doing reconstructOptionContentMapForAdd.");
    	logger.debug("pre-add Map content: " + mapOptionsContent);
    	logger.debug("pre-add Map size: " + mapOptionsContent.size());
    	
    	mapOptionsContent=repopulateMap(mapOptionsContent, request);
    	logger.debug("mapOptionsContent: " + mapOptionsContent);
    	mapOptionsContent.put(new Long(mapOptionsContent.size()+1).toString(), "");
    	//request.getSession().setAttribute("mapOptionsContent", mapOptionsContent);
	     
    	logger.debug("post-add Map is: " + mapOptionsContent);    	
	   	logger.debug("post-add count " + mapOptionsContent.size());
	   	
	   	return mapOptionsContent;
    }
        
    
    
    protected void reconstructOptionContentMapForRemove(Map mapOptionsContent, HttpServletRequest request, VoteAuthoringForm voteAuthoringForm)
    {
    		logger.debug("doing reconstructOptionContentMapForRemove.");
    		String optIndex =voteAuthoringForm.getOptIndex();
    	 	logger.debug("pre-delete map content:  " + mapOptionsContent);
    	 	logger.debug("optIndex: " + optIndex);
    	 	
    	 	String defLater=voteAuthoringForm.getActiveModule();
    	 	logger.debug("defLater: " + defLater);
    	 	
    	 	String removableOptIndex=null;
    	 	if ((defLater != null) && (defLater.equals(MONITORING)))
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
	    	logger.debug("post-delete count " + mapOptionsContent.size());
	    	logger.debug("post-delete map content:  " + mapOptionsContent);
    }


    protected Map repopulateMap(Map mapOptionsContent, HttpServletRequest request)
    {
    	logger.debug("starting repopulateMap");
        int intOptionIndex= mapOptionsContent.size();
        logger.debug("intOptionIndex: " + intOptionIndex);

    	/* if there is data in the Map remaining from previous session remove those */
    	mapOptionsContent.clear();
		logger.debug("Map got initialized: " + mapOptionsContent);
		
		for (long i=0; i < intOptionIndex ; i++)
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
		return mapOptionsContent; 
    }

    /**
     * 
     * @param mapOptionsContent
     * @param request
     */
    protected  Map reconstructOptionsContentMapForSubmit(Map mapOptionsContent, HttpServletRequest request)
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
	    logger.debug("final mapOptionsContent:" + mapOptionsContent);
	    return mapOptionsContent;
    }

    
    public void removeRedundantOptions (Map mapOptionsContent, IVoteService voteService, VoteAuthoringForm voteAuthoringForm, HttpServletRequest request)
	{
    	logger.debug("removing unused entries... ");
    	logger.debug("mapOptionsContent:  " + mapOptionsContent);
    	
    	String toolContentID=voteAuthoringForm.getToolContentID();
    	logger.debug("toolContentID:  " + toolContentID);
    	
    	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
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
        			    logger.debug("doing association removal for nomination: "  + removeableVoteQueContent);
        			    logger.debug("doing association removal, for question: "  + removeableVoteQueContent.getQuestion());
        			    logger.debug("doing association removal for nomination list: "  + voteContent.getVoteQueContents());
        			    voteContent.getVoteQueContents().remove(removeableVoteQueContent);
        			    
        				voteService.removeVoteQueContent(removeableVoteQueContent);
        				
            			logger.debug("removed removeableVoteQueContent from the db: " + removeableVoteQueContent);	
        			}
    	        	
    	        }
    		}    		
    	}
	
	}

    /**
     * persists the vote content
     * 
     * @param mapOptionsContent
     * @param voteService
     * @param voteAuthoringForm
     * @param request
     * @return
     */
    public VoteContent saveOrUpdateVoteContent(Map mapOptionsContent, IVoteService voteService, VoteAuthoringForm voteAuthoringForm, 
            HttpServletRequest request, SessionMap sessionMap)
    {
        UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
        logger.debug("toolUser: " + toolUser);
        
        String richTextTitle = request.getParameter(TITLE);
        String richTextInstructions = request.getParameter(INSTRUCTIONS);
        
        logger.debug("richTextTitle: " + richTextTitle);
        logger.debug("richTextInstructions: " + richTextInstructions);
        
        String voteChangable = voteAuthoringForm.getVoteChangable();
        logger.debug("voteChangable: " + voteChangable);
        
        String lockOnFinish=voteAuthoringForm.getLockOnFinish();
        logger.debug("lockOnFinish: " + lockOnFinish);
        
        String allowTextEntry=voteAuthoringForm.getAllowText();
        logger.debug("allowTextEntry: " + allowTextEntry);

        String maxNomcount= voteAuthoringForm.getMaxNominationCount();
	    logger.debug("maxNomcount: " + maxNomcount);

	    String richTextOfflineInstructions=(String)sessionMap.get(OFFLINE_INSTRUCTIONS_KEY);
        logger.debug("richTextOfflineInstructions: " + richTextOfflineInstructions);
        
        String richTextOnlineInstructions=(String)sessionMap.get(ONLINE_INSTRUCTIONS_KEY);
		logger.debug("richTextOnlineInstructions: " + richTextOnlineInstructions);

        boolean setCommonContent=true; 
        if ((lockOnFinish == null) || (voteChangable == null))
        {
        	setCommonContent=false;
        }
        logger.debug("setCommonContent: " + setCommonContent);
		
        String activeModule=voteAuthoringForm.getActiveModule();
        logger.debug("activeModule: " + activeModule);

        boolean voteChangableBoolean=false;
        boolean lockedOnFinishBoolean=false;
        boolean allowTextBoolean=false;
        
        if (setCommonContent)
        {
            if (voteChangable.equalsIgnoreCase(ON))
                voteChangableBoolean=true;
            
            if (lockOnFinish.equalsIgnoreCase(ON))
                lockedOnFinishBoolean=true;

            if (allowTextEntry.equalsIgnoreCase(ON))
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
        
        
        String toolContentID =voteAuthoringForm.getToolContentID();
        logger.debug("toolContentID: " + toolContentID);
        
     	VoteContent voteContent=voteService.retrieveVote(new Long(toolContentID));
     	logger.debug("voteContent: " + voteContent);
     	
     	boolean newContent=false;
        if(voteContent == null)
        {
        	voteContent = new VoteContent();
        	newContent=true;
        }


    	logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
    	voteContent.setVoteContentId(new Long(toolContentID));
     	voteContent.setTitle(richTextTitle);
     	voteContent.setInstructions(richTextInstructions);
     	voteContent.setUpdateDate(new Date(System.currentTimeMillis())); /**keep updating this one*/
     	logger.debug("userId: " + userId);
     	voteContent.setCreatedBy(userId); /**make sure we are setting the userId from the User object above*/
     	logger.debug("end of setting common content values...");

     	logger.debug("activeModule: " + activeModule);
     	
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
        
        voteContent=voteService.retrieveVote(new Long(toolContentID));
     	logger.debug("voteContent: " + voteContent);
        
        voteContent=createOptiosContent(mapOptionsContent, voteService, voteContent);
        
        return voteContent;
    }
    
    /**
     * creates a new vote content
     * 
     * @param mapOptionsContent
     * @param voteService
     * @param voteContent
     * @return
     */
    protected VoteContent createOptiosContent(Map mapOptionsContent, IVoteService voteService, VoteContent voteContent)
    {    
        logger.debug("starting createOptiosContent: " + voteContent);
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
}
