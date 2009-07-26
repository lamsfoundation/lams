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
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.VoteAttachmentDTO;
import org.lamsfoundation.lams.tool.vote.VoteComparator;
import org.lamsfoundation.lams.tool.vote.VoteNominationContentDTO;
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
 * <p>
 * Keeps all operations needed for Authoring mode.
 * </p>
 * 
 * @author Ozgur Demirtas
 * 
 */
public class AuthoringUtil implements VoteAppConstants {
    static Logger logger = Logger.getLogger(AuthoringUtil.class.getName());

    /**
     * checks if there are any duplicate entries
     * 
     * @param mapOptionsContent
     * @returnboolean
     */
    public static boolean verifyDuplicateNominations(Map mapOptionsContent) {
	Map originalMapOptionsContent = mapOptionsContent;
	Map backupMapOptionsContent = mapOptionsContent;

	int optionCount = 0;
	for (long i = 1; i <= VoteAppConstants.MAX_OPTION_COUNT; i++) {
	    String currentOption = (String) originalMapOptionsContent.get(new Long(i).toString());
	    AuthoringUtil.logger.debug("verified currentOption  " + currentOption);

	    optionCount = 0;
	    for (long j = 1; j <= VoteAppConstants.MAX_OPTION_COUNT; j++) {
		String backedOption = (String) backupMapOptionsContent.get(new Long(j).toString());

		if (currentOption != null && backedOption != null) {
		    if (currentOption.equals(backedOption)) {
			optionCount++;
			AuthoringUtil.logger.debug("optionCount for  " + currentOption + " is: " + optionCount);
		    }

		    if (optionCount > 1) {
			return true;
		    }
		}
	    }
	}
	return false;
    }

    /**
     * checks if the map is empty or not
     * 
     * @param map
     * @return boolean
     */
    public static boolean verifyMapNoEmptyString(Map map) {
	Iterator itMap = map.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    AuthoringUtil.logger.debug("using the  pair: " + pairs.getKey() + " = " + pairs.getValue());

	    if (pairs.getValue() != null && pairs.getValue().toString().length() == 0) {
		return false;
	    }

	}
	return true;
    }

    public static boolean validateNominationsNotEmpty(Map mapNominationsContent) {
	Iterator itMap = mapNominationsContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    AuthoringUtil.logger.debug("using the  pair: " + pairs.getKey() + " = " + pairs.getValue());

	    if (pairs.getValue() != null && pairs.getValue().toString().length() == 0) {
		return false;
	    }

	}
	return true;
    }

    public static Map repopulateMap(HttpServletRequest request, String parameterType) {
	Map mapTempNominationsContent = new TreeMap(new VoteComparator());
	AuthoringUtil.logger.debug("parameterType: " + parameterType);

	long mapCounter = 0;
	String optionContent0 = request.getParameter("optionContent0");
	AuthoringUtil.logger.debug("optionContent0: " + optionContent0);
	mapCounter++;
	mapTempNominationsContent.put(new Long(mapCounter).toString(), optionContent0);

	for (long i = 1; i <= VoteAppConstants.MAX_QUESTION_COUNT; i++) {
	    String candidateEntry = request.getParameter(parameterType + i);
	    if (candidateEntry != null && candidateEntry.length() > 0) {
		mapCounter++;
		mapTempNominationsContent.put(new Long(mapCounter).toString(), candidateEntry);
	    }
	}
	AuthoringUtil.logger.debug("return repopulated Map: " + mapTempNominationsContent);
	return mapTempNominationsContent;
    }

    public static Map shiftMap(Map mapOptionsContent, String optIndex, String movableOptionEntry, String direction) {
	AuthoringUtil.logger.debug("movableOptionEntry: " + movableOptionEntry);
	Map mapTempOptionsContent = new TreeMap(new VoteComparator());

	String shiftableEntry = null;

	int shiftableIndex = 0;
	if (direction.equals("down")) {
	    AuthoringUtil.logger.debug("moving map down");
	    shiftableIndex = new Integer(optIndex).intValue() + 1;
	} else {
	    AuthoringUtil.logger.debug("moving map up");
	    shiftableIndex = new Integer(optIndex).intValue() - 1;
	}

	AuthoringUtil.logger.debug("shiftableIndex: " + shiftableIndex);
	shiftableEntry = (String) mapOptionsContent.get(new Integer(shiftableIndex).toString());
	AuthoringUtil.logger.debug("shiftable entry: " + shiftableEntry);

	if (shiftableEntry != null) {
	    Iterator itNominationsMap = mapOptionsContent.entrySet().iterator();
	    long mapCounter = 0;
	    while (itNominationsMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itNominationsMap.next();
		AuthoringUtil.logger.debug("comparing the  pair: " + pairs.getKey() + " = " + pairs.getValue());
		mapCounter++;
		AuthoringUtil.logger.debug("mapCounter: " + mapCounter);

		if (!pairs.getKey().equals(optIndex) && !pairs.getKey().equals(new Integer(shiftableIndex).toString())) {
		    AuthoringUtil.logger.debug("normal copy " + optIndex);
		    mapTempOptionsContent.put(new Long(mapCounter).toString(), pairs.getValue());
		} else if (pairs.getKey().equals(optIndex)) {
		    AuthoringUtil.logger.debug("move type 1 " + optIndex);
		    mapTempOptionsContent.put(new Long(mapCounter).toString(), shiftableEntry);
		} else if (pairs.getKey().equals(new Integer(shiftableIndex).toString())) {
		    mapTempOptionsContent.put(new Long(mapCounter).toString(), movableOptionEntry);
		}
	    }
	} else {
	    mapTempOptionsContent = mapOptionsContent;
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
    public static VoteAttachmentDTO uploadFile(HttpServletRequest request, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, boolean isOfflineFile, SessionMap sessionMap)
	    throws RepositoryCheckedException {
	AuthoringUtil.logger.debug("doing uploadFile...: " + sessionMap);
	AuthoringUtil.logger.debug("isOfflineFile:" + isOfflineFile);

	InputStream stream = null;
	String fileName = null;
	String mimeType = null;
	String fileProperty = null;

	if (isOfflineFile) {
	    FormFile theOfflineFile = voteAuthoringForm.getTheOfflineFile();
	    AuthoringUtil.logger.debug("retrieved theOfflineFile: " + theOfflineFile);

	    try {
		stream = theOfflineFile.getInputStream();
		fileName = theOfflineFile.getFileName();
		if (fileName.length() == 0) {
		    return null;
		}
		AuthoringUtil.logger.debug("retrieved fileName: " + fileName);
		fileProperty = "OFFLINE";

	    } catch (FileNotFoundException e) {
		AuthoringUtil.logger
			.debug("filenotfound exception occured in accessing the repository server for the offline file : "
				+ e.getMessage());
	    } catch (IOException e) {
		AuthoringUtil.logger
			.debug("io exception occured in accessing the repository server for the offline file : "
				+ e.getMessage());
	    }

	    if (fileName.length() > 0) {
		List listUploadedOfflineFileNames = (List) sessionMap
			.get(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY);
		AuthoringUtil.logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);
		int index = findFileNameIndex(listUploadedOfflineFileNames, fileName);
		AuthoringUtil.logger.debug("index:" + index);
		if (index == 0) {
		    listUploadedOfflineFileNames.add(fileName);
		    AuthoringUtil.logger.debug("listUploadedOfflineFileNames after add :"
			    + listUploadedOfflineFileNames);
		    sessionMap.put(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
		}
	    }

	} else {
	    FormFile theOnlineFile = voteAuthoringForm.getTheOnlineFile();
	    AuthoringUtil.logger.debug("retrieved theOnlineFile: " + theOnlineFile);

	    try {
		stream = theOnlineFile.getInputStream();
		fileName = theOnlineFile.getFileName();

		if (fileName.length() == 0) {
		    return null;
		}

		AuthoringUtil.logger.debug("retrieved fileName: " + fileName);
		fileProperty = "ONLINE";

	    } catch (FileNotFoundException e) {
		AuthoringUtil.logger
			.debug("filenotfound exception occured in accessing the repository server for the online file : "
				+ e.getMessage());
	    } catch (IOException e) {
		AuthoringUtil.logger
			.debug("io exception occured in accessing the repository server for the online file : "
				+ e.getMessage());
	    }

	    if (fileName.length() > 0) {
		List listUploadedOnlineFileNames = (List) sessionMap
			.get(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY);
		AuthoringUtil.logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);
		int index = findFileNameIndex(listUploadedOnlineFileNames, fileName);
		AuthoringUtil.logger.debug("index:" + index);
		if (index == 0) {
		    listUploadedOnlineFileNames.add(fileName);
		    AuthoringUtil.logger.debug("listUploadedOnlineFileNames after add :" + listUploadedOnlineFileNames);
		    sessionMap.put(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY, listUploadedOnlineFileNames);
		}
	    }
	}

	AuthoringUtil.logger.debug("calling uploadFile with:");
	AuthoringUtil.logger.debug("istream:" + stream);
	AuthoringUtil.logger.debug("filename:" + fileName);
	AuthoringUtil.logger.debug("mimeType:" + mimeType);
	AuthoringUtil.logger.debug("fileProperty:" + fileProperty);

	NodeKey nodeKey = null;
	try {
	    nodeKey = voteService.uploadFile(stream, fileName, mimeType, fileProperty);
	    AuthoringUtil.logger.debug("nodeKey:" + nodeKey);
	    AuthoringUtil.logger.debug("nodeKey uuid:" + nodeKey.getUuid());
	} catch (FileException e) {
	    AuthoringUtil.logger.debug("exception writing raw data:" + e);
	    /* return a null dto */
	    return null;
	}

	VoteAttachmentDTO voteAttachmentDTO = new VoteAttachmentDTO();
	voteAttachmentDTO.setUid(null);
	voteAttachmentDTO.setUuid(nodeKey.getUuid().toString());
	voteAttachmentDTO.setFilename(fileName);
	voteAttachmentDTO.setOfflineFile(isOfflineFile);

	AuthoringUtil.logger.debug("uploadFile ends with sessionMap:" + sessionMap);
	return voteAttachmentDTO;
    }

    /**
     * returns a list of Vote attachements for listing of online and offline file information
     * 
     * @param listOfflineFilesMetaData
     * @return
     */
    public static List populateMetaDataAsAttachments(List listOfflineFilesMetaData) {
	List listAttachments = new LinkedList();

	Iterator itList = listOfflineFilesMetaData.iterator();
	while (itList.hasNext()) {
	    VoteUploadedFile voteUploadedFile = (VoteUploadedFile) itList.next();
	    AuthoringUtil.logger.debug("voteUploadedFile:" + voteUploadedFile);
	    AuthoringUtil.logger.debug("voteUploadedFile details, uid" + voteUploadedFile.getSubmissionId().toString());
	    AuthoringUtil.logger.debug("voteUploadedFile details, uuid" + voteUploadedFile.getUuid());
	    AuthoringUtil.logger.debug("voteUploadedFile details, filename" + voteUploadedFile.getFileName());
	    AuthoringUtil.logger.debug("voteUploadedFile details, isOfflineFile" + !voteUploadedFile.isFileOnline());

	    VoteAttachmentDTO voteAttachmentDTO = new VoteAttachmentDTO();
	    voteAttachmentDTO.setUid(voteUploadedFile.getSubmissionId().toString());
	    voteAttachmentDTO.setUuid(voteUploadedFile.getUuid());
	    voteAttachmentDTO.setFilename(voteUploadedFile.getFileName());
	    voteAttachmentDTO.setOfflineFile(!voteUploadedFile.isFileOnline());

	    listAttachments.add(voteAttachmentDTO);
	    AuthoringUtil.logger.debug("listAttachments after add" + listAttachments);
	}
	AuthoringUtil.logger.debug("final listAttachments after populating all: " + listAttachments);
	return listAttachments;
    }

    /**
     * @param listFilesMetaData
     * @return
     */
    public static List populateMetaDataAsFilenames(List listFilesMetaData) {
	List listFilenames = new LinkedList();

	Iterator itList = listFilesMetaData.iterator();
	while (itList.hasNext()) {
	    VoteAttachmentDTO voteAttachmentDTO = (VoteAttachmentDTO) itList.next();
	    AuthoringUtil.logger.debug("current filename" + voteAttachmentDTO.getFilename());
	    listFilenames.add(voteAttachmentDTO.getFilename());
	    AuthoringUtil.logger.debug("listFilenames after add" + listFilenames);
	}
	AuthoringUtil.logger.debug("final listFilenames after populating all: " + listFilenames);
	return listFilenames;
    }

    /**
     * used in removing a file item listed in the jsp
     * 
     * @param request
     * @param filename
     * @param offlineFile
     */
    public static void removeFileItem(HttpServletRequest request, String filename, String offlineFile,
	    SessionMap sessionMap) {
	AuthoringUtil.logger.debug("starting removeFileItem, sessionMap:" + sessionMap);
	AuthoringUtil.logger.debug("offlineFile:" + offlineFile);
	if (offlineFile.equals("1")) {
	    AuthoringUtil.logger.debug("will remove an offline file");
	    List listUploadedOfflineFileNames = (List) sessionMap
		    .get(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY);
	    AuthoringUtil.logger.debug("listUploadedOfflineFileNames:" + listUploadedOfflineFileNames);

	    listUploadedOfflineFileNames.remove(filename);
	    AuthoringUtil.logger.debug("removed offline filename:" + filename);

	    AuthoringUtil.logger.debug("listUploadedOfflineFileNames after remove :" + listUploadedOfflineFileNames);
	    sessionMap.put(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
	} else {
	    AuthoringUtil.logger.debug("will remove an online file");
	    List listUploadedOnlineFileNames = (List) sessionMap
		    .get(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY);
	    AuthoringUtil.logger.debug("listUploadedOnlineFileNames:" + listUploadedOnlineFileNames);

	    listUploadedOnlineFileNames.remove(filename);
	    AuthoringUtil.logger.debug("removed online filename:" + filename);

	    AuthoringUtil.logger.debug("listUploadedOnlineFileNames after remove :" + listUploadedOnlineFileNames);
	    sessionMap.put(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY, listUploadedOnlineFileNames);
	}
    }

    /**
     * @param listUploadedFileNames
     * @param filename
     * @return int
     */
    public static int findFileNameIndex(List listUploadedFileNames, String filename) {
	Iterator itListUploadedFileNames = listUploadedFileNames.iterator();
	int mainIndex = 0;
	while (itListUploadedFileNames.hasNext()) {
	    mainIndex++;
	    String currentFilename = (String) itListUploadedFileNames.next();
	    AuthoringUtil.logger.debug("currentFilename :" + currentFilename);
	    if (currentFilename.equals(filename)) {
		AuthoringUtil.logger.debug("currentFilename found in the list at mainIndex :" + mainIndex);
		return mainIndex;
	    }
	}
	return 0;
    }

    public static List removeFileItem(List listFilesMetaData, String uuid) {
	VoteAttachmentDTO deletableAttachmentDTO = null;

	Iterator itList = listFilesMetaData.iterator();
	int mainIndex = 0;
	while (itList.hasNext()) {
	    mainIndex++;
	    VoteAttachmentDTO currentAttachmentDTO = (VoteAttachmentDTO) itList.next();
	    AuthoringUtil.logger.debug("currentAttachmentDTO :" + currentAttachmentDTO);
	    AuthoringUtil.logger.debug("currentAttachmentDTO uuid :" + currentAttachmentDTO.getUuid());

	    if (currentAttachmentDTO.getUuid().equals(uuid)) {
		AuthoringUtil.logger.debug("equal uuid found uuid :" + uuid);
		deletableAttachmentDTO = currentAttachmentDTO;
		break;
	    }
	}

	AuthoringUtil.logger.debug("equal uuid found at index :" + mainIndex);
	AuthoringUtil.logger.debug("deletable attachment is:" + deletableAttachmentDTO);

	listFilesMetaData.remove(deletableAttachmentDTO);
	AuthoringUtil.logger.debug("listOfflineFilesMetaData after remove:" + listFilesMetaData);

	return listFilesMetaData;
    }

    public static List extractFileNames(List listFilesMetaData) {
	Iterator itList = listFilesMetaData.iterator();
	LinkedList listFilenames = new LinkedList();

	while (itList.hasNext()) {
	    VoteAttachmentDTO voteAttachmentDTO = (VoteAttachmentDTO) itList.next();
	    String filename = voteAttachmentDTO.getFilename();
	    AuthoringUtil.logger.debug("extracted filename: " + filename);
	    listFilenames.add(filename);
	}
	AuthoringUtil.logger.debug("final extracted listFilenames: " + listFilenames);
	return listFilenames;
    }

    protected Map reconstructOptionContentMapForAdd(Map mapOptionsContent, HttpServletRequest request) {
	AuthoringUtil.logger.debug("doing reconstructOptionContentMapForAdd.");
	AuthoringUtil.logger.debug("pre-add Map content: " + mapOptionsContent);
	AuthoringUtil.logger.debug("pre-add Map size: " + mapOptionsContent.size());

	mapOptionsContent = repopulateMap(mapOptionsContent, request);
	AuthoringUtil.logger.debug("mapOptionsContent: " + mapOptionsContent);
	mapOptionsContent.put(new Long(mapOptionsContent.size() + 1).toString(), "");

	AuthoringUtil.logger.debug("post-add Map is: " + mapOptionsContent);
	AuthoringUtil.logger.debug("post-add count " + mapOptionsContent.size());

	return mapOptionsContent;
    }

    protected void reconstructOptionContentMapForRemove(Map mapOptionsContent, HttpServletRequest request,
	    VoteAuthoringForm voteAuthoringForm) {
	AuthoringUtil.logger.debug("doing reconstructOptionContentMapForRemove.");
	String optIndex = voteAuthoringForm.getOptIndex();
	AuthoringUtil.logger.debug("pre-delete map content:  " + mapOptionsContent);
	AuthoringUtil.logger.debug("optIndex: " + optIndex);

	String defLater = voteAuthoringForm.getActiveModule();
	AuthoringUtil.logger.debug("defLater: " + defLater);

	String removableOptIndex = null;
	if (defLater != null && defLater.equals(VoteAppConstants.MONITORING)) {
	    removableOptIndex = (String) request.getSession().getAttribute(VoteAppConstants.REMOVABLE_QUESTION_INDEX);
	    AuthoringUtil.logger.debug("removableOptIndex: " + removableOptIndex);
	    optIndex = removableOptIndex;
	}
	AuthoringUtil.logger.debug("final removableOptIndex: " + optIndex);

	long longOptIndex = new Long(optIndex).longValue();
	AuthoringUtil.logger.debug("pre-delete count: " + mapOptionsContent.size());

	repopulateMap(mapOptionsContent, request);
	AuthoringUtil.logger.debug("post-repopulateMap optIndex: " + optIndex);

	mapOptionsContent.remove(new Long(longOptIndex).toString());
	AuthoringUtil.logger.debug("removed the question content with index: " + longOptIndex);
	AuthoringUtil.logger.debug("post-delete count " + mapOptionsContent.size());
	AuthoringUtil.logger.debug("post-delete map content:  " + mapOptionsContent);
    }

    protected Map repopulateMap(Map mapOptionsContent, HttpServletRequest request) {
	AuthoringUtil.logger.debug("starting repopulateMap");
	int intOptionIndex = mapOptionsContent.size();
	AuthoringUtil.logger.debug("intOptionIndex: " + intOptionIndex);

	/* if there is data in the Map remaining from previous session remove those */
	mapOptionsContent.clear();
	AuthoringUtil.logger.debug("Map got initialized: " + mapOptionsContent);

	for (long i = 0; i < intOptionIndex; i++) {
	    String candidateOptionEntry = request.getParameter("optionContent" + i);
	    if (i == 0) {
		request.getSession().setAttribute("defaultOptionContent", candidateOptionEntry);
		AuthoringUtil.logger.debug("defaultNominationContent set to: " + candidateOptionEntry);
	    }
	    if (candidateOptionEntry != null && candidateOptionEntry.length() > 0) {
		AuthoringUtil.logger.debug("using key: " + i);
		mapOptionsContent.put(new Long(i + 1).toString(), candidateOptionEntry);
		AuthoringUtil.logger.debug("added new entry.");
	    }
	}
	return mapOptionsContent;
    }

    /**
     * 
     * @param mapOptionsContent
     * @param request
     */
    protected Map reconstructOptionsContentMapForSubmit(Map mapOptionsContent, HttpServletRequest request) {
	AuthoringUtil.logger.debug("pre-submit Map:" + mapOptionsContent);
	AuthoringUtil.logger.debug("pre-submit Map size :" + mapOptionsContent.size());

	repopulateMap(mapOptionsContent, request);
	Map mapFinalOptionsContent = new TreeMap(new VoteComparator());

	Iterator itMap = mapOptionsContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		mapFinalOptionsContent.put(pairs.getKey(), pairs.getValue());
		AuthoringUtil.logger.debug("adding the  pair: " + pairs.getKey() + " = " + pairs.getValue());
	    }
	}

	mapOptionsContent = mapFinalOptionsContent;
	AuthoringUtil.logger.debug("final mapOptionsContent:" + mapOptionsContent);
	return mapOptionsContent;
    }

    public void removeRedundantOptions(Map mapOptionsContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {
	AuthoringUtil.logger.debug("removing unused entries... ");
	AuthoringUtil.logger.debug("mapOptionsContent:  " + mapOptionsContent);

	String toolContentID = voteAuthoringForm.getToolContentID();
	AuthoringUtil.logger.debug("toolContentID:  " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	AuthoringUtil.logger.debug("voteContent:  " + voteContent);

	if (voteContent != null) {
	    AuthoringUtil.logger.debug("voteContent uid: " + voteContent.getUid());
	    List allNominations = voteService.getAllQuestionEntries(voteContent.getUid());
	    AuthoringUtil.logger.debug("allNominations: " + allNominations);

	    Iterator listIterator = allNominations.iterator();
	    Long mapIndex = new Long(1);
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		VoteQueContent queContent = (VoteQueContent) listIterator.next();
		AuthoringUtil.logger.debug("queContent data: " + queContent);

		entryUsed = false;
		Iterator itMap = mapOptionsContent.entrySet().iterator();
		while (itMap.hasNext()) {
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();
		    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());
		    if (pairs.getValue().toString().length() != 0) {
			AuthoringUtil.logger.debug("text from map:" + pairs.getValue().toString());
			AuthoringUtil.logger.debug("text from db:" + queContent.getQuestion());
			if (pairs.getValue().toString().equals(queContent.getQuestion())) {
			    AuthoringUtil.logger.debug("used entry in db:" + queContent.getQuestion());
			    entryUsed = true;
			    break;
			}
		    }
		}

		if (entryUsed == false) {
		    AuthoringUtil.logger.debug("removing unused entry in db:" + queContent.getQuestion());

		    VoteQueContent removeableVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), voteContent.getUid());
		    AuthoringUtil.logger.debug("removeableVoteQueContent" + removeableVoteQueContent);

		    if (removeableVoteQueContent != null) {
			AuthoringUtil.logger.debug("doing association removal for nomination: "
				+ removeableVoteQueContent);
			AuthoringUtil.logger.debug("doing association removal, for question: "
				+ removeableVoteQueContent.getQuestion());
			AuthoringUtil.logger.debug("doing association removal for nomination list: "
				+ voteContent.getVoteQueContents());
			voteContent.getVoteQueContents().remove(removeableVoteQueContent);

			voteService.removeVoteQueContent(removeableVoteQueContent);

			AuthoringUtil.logger.debug("removed removeableVoteQueContent from the db: "
				+ removeableVoteQueContent);
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
    public VoteContent saveOrUpdateVoteContent(Map mapOptionsContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request, SessionMap sessionMap,
	    DataFlowObject assignedDataFlowObject) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	AuthoringUtil.logger.debug("toolUser: " + toolUser);

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	AuthoringUtil.logger.debug("richTextTitle: " + richTextTitle);
	AuthoringUtil.logger.debug("richTextInstructions: " + richTextInstructions);

	String lockOnFinish = request.getParameter("lockOnFinish");
	AuthoringUtil.logger.debug("lockOnFinish: " + lockOnFinish);

	String allowTextEntry = request.getParameter("allowText");
	AuthoringUtil.logger.debug("allowTextEntry: " + allowTextEntry);

	String showResults = request.getParameter("showResults");

	String maxInputs = request.getParameter("maxInputs");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);
	AuthoringUtil.logger.debug("reflect: " + reflect);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();
	AuthoringUtil.logger.debug("reflectionSubject: " + reflectionSubject);

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();
	AuthoringUtil.logger.debug("maxNomcount: " + maxNomcount);

	String richTextOfflineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);
	AuthoringUtil.logger.debug("richTextOfflineInstructions: " + richTextOfflineInstructions);

	String richTextOnlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);
	AuthoringUtil.logger.debug("richTextOnlineInstructions: " + richTextOnlineInstructions);

	boolean setCommonContent = true;
	if (lockOnFinish == null) {
	    setCommonContent = false;
	}
	AuthoringUtil.logger.debug("setCommonContent: " + setCommonContent);

	String activeModule = voteAuthoringForm.getActiveModule();
	AuthoringUtil.logger.debug("activeModule: " + activeModule);

	boolean lockedOnFinishBoolean = false;
	boolean allowTextBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;
	short maxInputsShort = 0;

	if (lockOnFinish != null && lockOnFinish.equalsIgnoreCase("1")) {
	    lockedOnFinishBoolean = true;
	}

	if (allowTextEntry != null && allowTextEntry.equalsIgnoreCase("1")) {
	    allowTextBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	if (showResults != null && showResults.equalsIgnoreCase("1")) {
	    showResultsBoolean = true;
	}

	if (!"0".equals(maxInputs)) {
	    maxInputsShort = Short.parseShort(maxInputs);
	}

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    AuthoringUtil.logger.debug("ss: " + ss);
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    AuthoringUtil.logger.debug("user" + user);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		AuthoringUtil.logger.debug("should not reach here");
		userId = 0;
	    }
	}
	AuthoringUtil.logger.debug("userId: " + userId);

	String toolContentID = voteAuthoringForm.getToolContentID();
	AuthoringUtil.logger.debug("toolContentID: " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	AuthoringUtil.logger.debug("voteContent: " + voteContent);

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	AuthoringUtil.logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
	voteContent.setVoteContentId(new Long(toolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	AuthoringUtil.logger.debug("userId: " + userId);
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */
	AuthoringUtil.logger.debug("end of setting common content values...");

	AuthoringUtil.logger.debug("activeModule: " + activeModule);

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    AuthoringUtil.logger.debug("setting other content values...");
	    voteContent.setLockOnFinish(lockedOnFinishBoolean);
	    voteContent.setAllowText(allowTextBoolean);
	    voteContent.setShowResults(showResultsBoolean);
	    voteContent.setReflect(reflectBoolean);
	    voteContent.setReflectionSubject(reflectionSubject);
	    voteContent.setMaxNominationCount(maxNomcount);
	    voteContent.setOnlineInstructions(richTextOnlineInstructions);
	    voteContent.setOfflineInstructions(richTextOfflineInstructions);
	    voteContent.setMaxInputs(maxInputsShort);
	}

	voteContent.setAssignedDataFlowObject(assignedDataFlowObject != null);

	if (newContent) {
	    AuthoringUtil.logger.debug("will create: " + voteContent);
	    voteService.createVote(voteContent);
	} else {
	    AuthoringUtil.logger.debug("will update: " + voteContent);
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.retrieveVote(new Long(toolContentID));
	AuthoringUtil.logger.debug("voteContent: " + voteContent);

	voteContent = createOptionsContent(mapOptionsContent, voteService, voteContent);

	voteService.saveDataFlowObjectAssigment(assignedDataFlowObject);

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
    protected VoteContent createOptionsContent(Map mapOptionsContent, IVoteService voteService, VoteContent voteContent) {
	AuthoringUtil.logger.debug("starting createOptiosContent: " + voteContent);
	AuthoringUtil.logger.debug("content uid is: " + voteContent.getUid());
	List questions = voteService.retrieveVoteQueContentsByToolContentId(voteContent.getUid().longValue());
	AuthoringUtil.logger.debug("questions: " + questions);

	Iterator itMap = mapOptionsContent.entrySet().iterator();
	int diplayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());

	    if (pairs.getValue().toString().length() != 0) {
		AuthoringUtil.logger.debug("starting createNominationContent: pairs.getValue().toString():"
			+ pairs.getValue().toString());
		AuthoringUtil.logger.debug("starting createNominationContent: voteContent: " + voteContent);
		AuthoringUtil.logger.debug("starting createNominationContent: diplayOrder: " + diplayOrder);
		diplayOrder = new Integer(pairs.getKey().toString()).intValue();
		AuthoringUtil.logger.debug("int diplayOrder: " + diplayOrder);

		VoteQueContent queContent = new VoteQueContent(pairs.getValue().toString(), diplayOrder, voteContent,
			null);

		/* checks if the question is already recorded */
		AuthoringUtil.logger.debug("question text is: " + pairs.getValue().toString());
		AuthoringUtil.logger.debug("content uid is: " + voteContent.getUid());
		AuthoringUtil.logger.debug("question display order is: " + diplayOrder);

		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByQuestionText(pairs.getValue()
			.toString(), voteContent.getUid());
		AuthoringUtil.logger.debug("existingVoteQueContent: " + existingVoteQueContent);
		if (existingVoteQueContent == null) {
		    /* make sure a question with the same question text is not already saved */
		    VoteQueContent duplicateVoteQueContent = voteService.getQuestionContentByQuestionText(pairs
			    .getValue().toString(), voteContent.getUid());
		    AuthoringUtil.logger.debug("duplicateVoteQueContent: " + duplicateVoteQueContent);
		    if (duplicateVoteQueContent == null) {
			AuthoringUtil.logger.debug("adding a new question to content: " + queContent);
			voteContent.getVoteQueContents().add(queContent);
			queContent.setVoteContent(voteContent);

			voteService.createVoteQue(queContent);
		    }
		} else {
		    existingVoteQueContent.setQuestion(pairs.getValue().toString());
		    existingVoteQueContent.setDisplayOrder(diplayOrder);
		    AuthoringUtil.logger.debug("updating the existing question content: " + existingVoteQueContent);
		    voteService.updateVoteQueContent(existingVoteQueContent);
		}
	    }
	}
	return voteContent;
    }

    protected static List swapNodes(List listNominationContentDTO, String questionIndex, String direction) {
	AuthoringUtil.logger.debug("swapNodes:");
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	AuthoringUtil.logger.debug("questionIndex:" + questionIndex);
	AuthoringUtil.logger.debug("direction:" + direction);

	int intNominationIndex = new Integer(questionIndex).intValue();
	int intOriginalNominationIndex = intNominationIndex;
	AuthoringUtil.logger.debug("intNominationIndex:" + intNominationIndex);

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    AuthoringUtil.logger.debug("direction down:");
	    replacedNodeIndex = ++intNominationIndex;
	} else {
	    AuthoringUtil.logger.debug("direction up:");
	    replacedNodeIndex = --intNominationIndex;

	}
	AuthoringUtil.logger.debug("replacedNodeIndex:" + replacedNodeIndex);
	AuthoringUtil.logger.debug("replacing nodes:" + intOriginalNominationIndex + " and " + replacedNodeIndex);

	VoteNominationContentDTO mainNode = extractNodeAtDisplayOrder(listNominationContentDTO,
		intOriginalNominationIndex);
	AuthoringUtil.logger.debug("mainNode:" + mainNode);

	VoteNominationContentDTO replacedNode = extractNodeAtDisplayOrder(listNominationContentDTO, replacedNodeIndex);
	AuthoringUtil.logger.debug("replacedNode:" + replacedNode);

	List listFinalNominationContentDTO = new LinkedList();

	listFinalNominationContentDTO = reorderSwappedListNominationContentDTO(listNominationContentDTO,
		intOriginalNominationIndex, replacedNodeIndex, mainNode, replacedNode);

	AuthoringUtil.logger.debug("listFinalNominationContentDTO:" + listFinalNominationContentDTO);
	return listFinalNominationContentDTO;
    }

    protected static VoteNominationContentDTO extractNodeAtDisplayOrder(List listNominationContentDTO,
	    int intOriginalNominationIndex) {
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	AuthoringUtil.logger.debug("intOriginalNominationIndex:" + intOriginalNominationIndex);

	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());

	    AuthoringUtil.logger.debug("intOriginalNominationIndex versus displayOrder:"
		    + new Integer(intOriginalNominationIndex).toString() + " versus "
		    + voteNominationContentDTO.getDisplayOrder());
	    if (new Integer(intOriginalNominationIndex).toString().equals(voteNominationContentDTO.getDisplayOrder())) {
		AuthoringUtil.logger.debug("node found:" + voteNominationContentDTO);
		return voteNominationContentDTO;
	    }
	}
	return null;
    }

    protected static List reorderSwappedListNominationContentDTO(List listNominationContentDTO,
	    int intOriginalNominationIndex, int replacedNodeIndex, VoteNominationContentDTO mainNode,
	    VoteNominationContentDTO replacedNode) {
	AuthoringUtil.logger.debug("reorderSwappedListNominationContentDTO: intOriginalNominationIndex:"
		+ intOriginalNominationIndex);
	AuthoringUtil.logger.debug("reorderSwappedListNominationContentDTO: replacedNodeIndex:" + replacedNodeIndex);
	AuthoringUtil.logger.debug("mainNode: " + mainNode);
	AuthoringUtil.logger.debug("replacedNode: " + replacedNode);

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());
	    queIndex++;
	    VoteNominationContentDTO tempNode = new VoteNominationContentDTO();

	    if (!voteNominationContentDTO.getDisplayOrder().equals(new Integer(intOriginalNominationIndex).toString())
		    && !voteNominationContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		AuthoringUtil.logger.debug("normal copy ");
		tempNode.setNomination(voteNominationContentDTO.getNomination());
		tempNode.setDisplayOrder(voteNominationContentDTO.getDisplayOrder());
		tempNode.setFeedback(voteNominationContentDTO.getFeedback());
	    } else if (voteNominationContentDTO.getDisplayOrder().equals(
		    new Integer(intOriginalNominationIndex).toString())) {
		AuthoringUtil.logger.debug("move type 1 ");
		tempNode.setNomination(replacedNode.getNomination());
		tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
		tempNode.setFeedback(replacedNode.getFeedback());
	    } else if (voteNominationContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		AuthoringUtil.logger.debug("move type 1 ");
		tempNode.setNomination(mainNode.getNomination());
		tempNode.setDisplayOrder(mainNode.getDisplayOrder());
		tempNode.setFeedback(mainNode.getFeedback());
	    }

	    listFinalNominationContentDTO.add(tempNode);
	}

	AuthoringUtil.logger.debug("final listFinalNominationContentDTO:" + listFinalNominationContentDTO);
	return listFinalNominationContentDTO;
    }

    protected static List reorderSimpleListNominationContentDTO(List listNominationContentDTO) {
	AuthoringUtil.logger.debug("reorderListNominationContentDTO");
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());

	    String question = voteNominationContentDTO.getNomination();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = voteNominationContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    if (question != null && !question.equals("")) {
		++queIndex;
		AuthoringUtil.logger.debug("using queIndex:" + queIndex);

		voteNominationContentDTO.setNomination(question);
		voteNominationContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		voteNominationContentDTO.setFeedback(feedback);

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    }
	}

	AuthoringUtil.logger.debug("final listFinalNominationContentDTO:" + listFinalNominationContentDTO);
	return listFinalNominationContentDTO;
    }

    protected static List reorderListNominationContentDTO(List listNominationContentDTO, String excludeNominationIndex) {
	AuthoringUtil.logger.debug("reorderListNominationContentDTO");
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	AuthoringUtil.logger.debug("excludeNominationIndex:" + excludeNominationIndex);

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());

	    String question = voteNominationContentDTO.getNomination();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = voteNominationContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    AuthoringUtil.logger.debug("displayOrder versus excludeNominationIndex :" + displayOrder + " versus "
		    + excludeNominationIndex);

	    if (question != null && !question.equals("")) {
		if (!displayOrder.equals(excludeNominationIndex)) {
		    ++queIndex;
		    AuthoringUtil.logger.debug("using queIndex:" + queIndex);

		    voteNominationContentDTO.setNomination(question);
		    voteNominationContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		    voteNominationContentDTO.setFeedback(feedback);

		    listFinalNominationContentDTO.add(voteNominationContentDTO);
		}
	    }
	}

	AuthoringUtil.logger.debug("final listFinalNominationContentDTO:" + listFinalNominationContentDTO);
	return listFinalNominationContentDTO;
    }

    public static boolean checkDuplicateNominations(List listNominationContentDTO, String newNomination) {
	AuthoringUtil.logger.debug("checkDuplicateNominations: " + listNominationContentDTO);
	AuthoringUtil.logger.debug("newNomination: " + newNomination);

	Map mapNominationContent = extractMapNominationContent(listNominationContentDTO);
	AuthoringUtil.logger.debug("mapNominationContent: " + mapNominationContent);

	Iterator itMap = mapNominationContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		AuthoringUtil.logger.debug("checking the  pair: " + pairs.getKey() + " = " + pairs.getValue());

		if (pairs.getValue().equals(newNomination)) {
		    AuthoringUtil.logger.debug("entry found: " + newNomination);
		    return true;
		}
	    }
	}
	return false;
    }

    protected static Map extractMapNominationContent(List listNominationContentDTO) {
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	Map mapNominationContent = new TreeMap(new VoteComparator());

	Iterator listIterator = listNominationContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());

	    queIndex++;
	    AuthoringUtil.logger.debug("queIndex:" + queIndex);
	    mapNominationContent.put(new Integer(queIndex).toString(), voteNominationContentDTO.getNomination());
	}
	AuthoringUtil.logger.debug("mapNominationContent:" + mapNominationContent);
	return mapNominationContent;
    }

    protected static List reorderUpdateListNominationContentDTO(List listNominationContentDTO,
	    VoteNominationContentDTO voteNominationContentDTONew, String editableNominationIndex) {
	AuthoringUtil.logger.debug("reorderUpdateListNominationContentDTO");
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	AuthoringUtil.logger.debug("voteNominationContentDTONew:" + voteNominationContentDTONew);
	AuthoringUtil.logger.debug("editableNominationIndex:" + editableNominationIndex);

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO question:" + voteNominationContentDTO.getNomination());

	    ++queIndex;
	    AuthoringUtil.logger.debug("using queIndex:" + queIndex);
	    String question = voteNominationContentDTO.getNomination();
	    AuthoringUtil.logger.debug("question:" + question);

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();
	    AuthoringUtil.logger.debug("displayOrder:" + displayOrder);

	    String feedback = voteNominationContentDTO.getFeedback();
	    AuthoringUtil.logger.debug("feedback:" + feedback);

	    if (displayOrder.equals(editableNominationIndex)) {
		AuthoringUtil.logger.debug("displayOrder equals editableNominationIndex:" + editableNominationIndex);
		voteNominationContentDTO.setNomination(voteNominationContentDTONew.getNomination());
		voteNominationContentDTO.setDisplayOrder(voteNominationContentDTONew.getDisplayOrder());
		voteNominationContentDTO.setFeedback(voteNominationContentDTONew.getFeedback());

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    } else {
		AuthoringUtil.logger.debug("displayOrder does not equal editableNominationIndex:"
			+ editableNominationIndex);
		voteNominationContentDTO.setNomination(question);
		voteNominationContentDTO.setDisplayOrder(displayOrder);
		voteNominationContentDTO.setFeedback(feedback);

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    }
	}

	AuthoringUtil.logger.debug("listFinalNominationContentDTO:" + listFinalNominationContentDTO);
	return listFinalNominationContentDTO;
    }

    protected static Map extractMapFeedback(List listNominationContentDTO) {
	AuthoringUtil.logger.debug("listNominationContentDTO:" + listNominationContentDTO);
	Map mapFeedbackContent = new TreeMap(new VoteComparator());

	Iterator listIterator = listNominationContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    AuthoringUtil.logger.debug("voteNominationContentDTO:" + voteNominationContentDTO);
	    AuthoringUtil.logger.debug("voteNominationContentDTO feedback:" + voteNominationContentDTO.getFeedback());

	    queIndex++;
	    AuthoringUtil.logger.debug("queIndex:" + queIndex);
	    mapFeedbackContent.put(new Integer(queIndex).toString(), voteNominationContentDTO.getFeedback());
	}
	AuthoringUtil.logger.debug("mapFeedbackContent:" + mapFeedbackContent);
	return mapFeedbackContent;
    }

    public void removeRedundantNominations(Map mapNominationContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request, String toolContentID) {
	AuthoringUtil.logger.debug("removing unused entries... ");
	AuthoringUtil.logger.debug("mapNominationContent:  " + mapNominationContent);
	AuthoringUtil.logger.debug("toolContentID:  " + toolContentID);

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));
	AuthoringUtil.logger.debug("voteContent:  " + voteContent);

	if (voteContent != null) {
	    AuthoringUtil.logger.debug("voteContent uid: " + voteContent.getUid());
	    List allNominations = voteService.getAllQuestionEntries(voteContent.getUid());
	    AuthoringUtil.logger.debug("allNominations: " + allNominations);

	    Iterator listIterator = allNominations.iterator();
	    int mapIndex = 0;
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		++mapIndex;
		AuthoringUtil.logger.debug("current mapIndex: " + mapIndex);

		VoteQueContent queContent = (VoteQueContent) listIterator.next();
		AuthoringUtil.logger.debug("queContent data: " + queContent);
		AuthoringUtil.logger.debug("queContent: " + queContent.getQuestion() + " "
			+ queContent.getDisplayOrder());

		entryUsed = false;
		Iterator itMap = mapNominationContent.entrySet().iterator();
		int displayOrder = 0;
		while (itMap.hasNext()) {
		    ++displayOrder;
		    AuthoringUtil.logger.debug("current displayOrder: " + displayOrder);
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();
		    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());

		    if (pairs.getValue().toString().length() != 0) {
			AuthoringUtil.logger.debug("text from map:" + pairs.getValue().toString());
			AuthoringUtil.logger.debug("text from db:" + queContent.getQuestion());

			AuthoringUtil.logger.debug("mapIndex versus displayOrder:" + mapIndex + " versus "
				+ displayOrder);
			if (mapIndex == displayOrder) {
			    AuthoringUtil.logger.debug("used displayOrder position:" + displayOrder);
			    entryUsed = true;
			    break;
			}

		    }
		}

		if (entryUsed == false) {
		    AuthoringUtil.logger.debug("removing unused entry in db:" + queContent.getQuestion());

		    VoteQueContent removeableVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), voteContent.getUid());
		    AuthoringUtil.logger.debug("removeableVoteQueContent" + removeableVoteQueContent);
		    if (removeableVoteQueContent != null) {
			voteService.removeVoteQueContent(removeableVoteQueContent);
			AuthoringUtil.logger.debug("removed removeableVoteQueContent from the db: "
				+ removeableVoteQueContent);
		    }

		}
	    }
	}

    }

    public VoteContent saveOrUpdateVoteContent(Map mapQuestionContent, Map mapFeedback, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request, VoteContent voteContent,
	    String strToolContentID, DataFlowObject assignedDataFlowObject) {
	UserDTO toolUser = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);

	boolean isLockOnFinish = false;
	boolean isAllowTextEntry = false;
	boolean isReflect = false;

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	AuthoringUtil.logger.debug("richTextTitle: " + richTextTitle);
	AuthoringUtil.logger.debug("richTextInstructions: " + richTextInstructions);

	String lockOnFinish = request.getParameter("lockOnFinish");
	AuthoringUtil.logger.debug("lockOnFinish: " + lockOnFinish);

	String allowTextEntry = request.getParameter("allowText");
	AuthoringUtil.logger.debug("allowTextEntry: " + allowTextEntry);

	String showResults = request.getParameter("showResults");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);
	AuthoringUtil.logger.debug("reflect: " + reflect);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();
	AuthoringUtil.logger.debug("reflectionSubject: " + reflectionSubject);

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();
	AuthoringUtil.logger.debug("maxNomcount: " + maxNomcount);

	String richTextOfflineInstructions = request.getParameter(VoteAppConstants.OFFLINE_INSTRUCTIONS);
	String richTextOnlineInstructions = request.getParameter(VoteAppConstants.ONLINE_INSTRUCTIONS);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);
	AuthoringUtil.logger.debug("activeModule: " + activeModule);

	boolean setCommonContent = true;
	if (lockOnFinish == null || allowTextEntry == null || showResults == null || reflect == null
		|| reflectionSubject == null || maxNomcount == null)

	{
	    setCommonContent = false;
	}
	AuthoringUtil.logger.debug("setCommonContent: " + setCommonContent);

	boolean lockOnFinishBoolean = false;
	boolean allowTextEntryBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;

	if (lockOnFinish != null && lockOnFinish.equalsIgnoreCase("1")) {
	    lockOnFinishBoolean = true;
	}

	if (allowTextEntry != null && allowTextEntry.equalsIgnoreCase("1")) {
	    allowTextEntryBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	if (showResults != null && showResults.equalsIgnoreCase("1")) {
	    showResultsBoolean = true;
	}

	AuthoringUtil.logger.debug("lockOnFinishBoolean: " + lockOnFinishBoolean);
	AuthoringUtil.logger.debug("allowTextEntryBoolean: " + allowTextEntryBoolean);
	AuthoringUtil.logger.debug("reflectBoolean: " + reflectBoolean);

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    AuthoringUtil.logger.debug("ss: " + ss);
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    AuthoringUtil.logger.debug("user" + user);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		AuthoringUtil.logger.debug("should not reach here");
		userId = 0;
	    }
	}
	AuthoringUtil.logger.debug("userId: " + userId);
	AuthoringUtil.logger.debug("voteContent: " + voteContent);

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	AuthoringUtil.logger.debug("setting common content values..." + richTextTitle + " " + richTextInstructions);
	voteContent.setVoteContentId(new Long(strToolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	AuthoringUtil.logger.debug("userId: " + userId);
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */
	AuthoringUtil.logger.debug("end of setting common content values...");

	AuthoringUtil.logger.debug("activeModule:" + activeModule);
	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    AuthoringUtil.logger.debug("setting other content values...");
	    voteContent.setLockOnFinish(lockOnFinishBoolean);
	    voteContent.setAllowText(allowTextEntryBoolean);
	    voteContent.setShowResults(showResultsBoolean);
	    voteContent.setReflect(reflectBoolean);
	    voteContent.setMaxNominationCount(maxNomcount);

	    voteContent.setOnlineInstructions(richTextOnlineInstructions);
	    voteContent.setOfflineInstructions(richTextOfflineInstructions);

	    voteContent.setReflectionSubject(reflectionSubject);
	}

	if (newContent) {
	    AuthoringUtil.logger.debug("will create: " + voteContent);
	    voteService.createVote(voteContent);
	} else {
	    AuthoringUtil.logger.debug("will update: " + voteContent);
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.retrieveVote(new Long(strToolContentID));

	AuthoringUtil.logger.debug("voteContent: " + voteContent);

	voteContent = createQuestionContent(mapQuestionContent, mapFeedback, voteService, voteContent);

	voteService.saveDataFlowObjectAssigment(assignedDataFlowObject);

	return voteContent;
    }

    protected VoteContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, IVoteService voteService,
	    VoteContent voteContent) {
	AuthoringUtil.logger.debug("createQuestionContent: ");
	AuthoringUtil.logger.debug("content uid is: " + voteContent.getUid());
	List questions = voteService.retrieveVoteQueContentsByToolContentId(voteContent.getUid().longValue());
	AuthoringUtil.logger.debug("questions: " + questions);

	AuthoringUtil.logger.debug("mapQuestionContent: " + mapQuestionContent);
	AuthoringUtil.logger.debug("mapFeedback: " + mapFeedback);

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	int displayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    AuthoringUtil.logger.debug("using the pair: " + pairs.getKey() + " = " + pairs.getValue());

	    if (pairs.getValue().toString().length() != 0) {
		AuthoringUtil.logger.debug("starting createQuestionContent: pairs.getValue().toString():"
			+ pairs.getValue().toString());
		AuthoringUtil.logger.debug("starting createQuestionContent: voteContent: " + voteContent);

		++displayOrder;
		AuthoringUtil.logger.debug("starting createQuestionContent: displayOrder: " + displayOrder);
		String currentFeedback = (String) mapFeedback.get(new Integer(displayOrder).toString());
		AuthoringUtil.logger.debug("currentFeedback: " + currentFeedback);

		VoteQueContent queContent = new VoteQueContent(pairs.getValue().toString(), displayOrder, voteContent,
			null);

		AuthoringUtil.logger.debug("queContent: " + queContent);

		/* checks if the question is already recorded */
		AuthoringUtil.logger.debug("question text is: " + pairs.getValue().toString());
		AuthoringUtil.logger.debug("content uid is: " + voteContent.getUid());
		AuthoringUtil.logger.debug("question display order is: " + displayOrder);
		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByDisplayOrder(new Long(
			displayOrder), voteContent.getUid());
		AuthoringUtil.logger.debug("existingVoteQueContent: " + existingVoteQueContent);

		if (existingVoteQueContent == null) {
		    /* make sure a question with the same question text is not already saved */
		    VoteQueContent duplicateVoteQueContent = voteService.getQuestionContentByQuestionText(pairs
			    .getValue().toString(), voteContent.getUid());
		    AuthoringUtil.logger.debug("duplicateVoteQueContent: " + duplicateVoteQueContent);
		    AuthoringUtil.logger.debug("adding a new question to content: " + queContent);
		    voteContent.getVoteQueContents().add(queContent);
		    queContent.setVoteContent(voteContent);

		    voteService.createVoteQue(queContent);
		} else {

		    String existingQuestion = existingVoteQueContent.getQuestion();
		    AuthoringUtil.logger.debug("existingQuestion: " + existingQuestion);

		    AuthoringUtil.logger.debug("map question versus existingQuestion: " + pairs.getValue().toString()
			    + " versus db question value: " + existingQuestion);

		    existingVoteQueContent.setQuestion(pairs.getValue().toString());
		    // existingVoteQueContent.setFeedback(currentFeedback);
		    existingVoteQueContent.setDisplayOrder(displayOrder);

		    AuthoringUtil.logger.debug("updating the existing question content: " + existingVoteQueContent);
		    voteService.updateVoteQueContent(existingVoteQueContent);
		}
	    }
	}
	return voteContent;
    }

    public void reOrganizeDisplayOrder(Map mapQuestionContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, VoteContent voteContent) {
	AuthoringUtil.logger.debug("voteContent: " + voteContent);
	if (voteContent != null) {
	    AuthoringUtil.logger.debug("content uid: " + voteContent.getUid());
	    List sortedQuestions = voteService.getAllQuestionEntriesSorted(voteContent.getUid().longValue());
	    AuthoringUtil.logger.debug("sortedQuestions: " + sortedQuestions);

	    Iterator listIterator = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (listIterator.hasNext()) {
		VoteQueContent queContent = (VoteQueContent) listIterator.next();
		AuthoringUtil.logger.debug("queContent data: " + queContent);
		AuthoringUtil.logger.debug("queContent: " + queContent.getQuestion() + " "
			+ queContent.getDisplayOrder());

		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			.getQuestion(), voteContent.getUid());
		AuthoringUtil.logger.debug("existingVoteQueContent: " + existingVoteQueContent);
		existingVoteQueContent.setDisplayOrder(displayOrder);
		AuthoringUtil.logger.debug("updating the existing question content for displayOrder: "
			+ existingVoteQueContent);
		voteService.updateVoteQueContent(existingVoteQueContent);
		displayOrder++;
	    }
	}
	AuthoringUtil.logger.debug("done with reOrganizeDisplayOrder...");
    }

}
