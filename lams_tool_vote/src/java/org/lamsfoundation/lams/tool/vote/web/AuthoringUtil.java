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

    static private Logger logger = Logger.getLogger(AuthoringUtil.class.getName());
    
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

	    optionCount = 0;
	    for (long j = 1; j <= VoteAppConstants.MAX_OPTION_COUNT; j++) {
		String backedOption = (String) backupMapOptionsContent.get(new Long(j).toString());

		if (currentOption != null && backedOption != null) {
		    if (currentOption.equals(backedOption)) {
			optionCount++;
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

	    if (pairs.getValue() != null && pairs.getValue().toString().length() == 0) {
		return false;
	    }

	}
	return true;
    }

    public static Map repopulateMap(HttpServletRequest request, String parameterType) {
	Map mapTempNominationsContent = new TreeMap(new VoteComparator());

	long mapCounter = 0;
	String optionContent0 = request.getParameter("optionContent0");
	mapCounter++;
	mapTempNominationsContent.put(new Long(mapCounter).toString(), optionContent0);

	for (long i = 1; i <= VoteAppConstants.MAX_QUESTION_COUNT; i++) {
	    String candidateEntry = request.getParameter(parameterType + i);
	    if (candidateEntry != null && candidateEntry.length() > 0) {
		mapCounter++;
		mapTempNominationsContent.put(new Long(mapCounter).toString(), candidateEntry);
	    }
	}
	return mapTempNominationsContent;
    }

    public static Map shiftMap(Map mapOptionsContent, String optIndex, String movableOptionEntry, String direction) {
	Map mapTempOptionsContent = new TreeMap(new VoteComparator());

	String shiftableEntry = null;

	int shiftableIndex = 0;
	if (direction.equals("down")) {
	    //moving map down");
	    shiftableIndex = new Integer(optIndex).intValue() + 1;
	} else {
	    //moving map up
	    shiftableIndex = new Integer(optIndex).intValue() - 1;
	}

	shiftableEntry = (String) mapOptionsContent.get(new Integer(shiftableIndex).toString());

	if (shiftableEntry != null) {
	    Iterator itNominationsMap = mapOptionsContent.entrySet().iterator();
	    long mapCounter = 0;
	    while (itNominationsMap.hasNext()) {
		Map.Entry pairs = (Map.Entry) itNominationsMap.next();
		mapCounter++;

		if (!pairs.getKey().equals(optIndex) && !pairs.getKey().equals(new Integer(shiftableIndex).toString())) {
		    //normal copy 
		    mapTempOptionsContent.put(new Long(mapCounter).toString(), pairs.getValue());
		} else if (pairs.getKey().equals(optIndex)) {
		   //move type 1
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

	InputStream stream = null;
	String fileName = null;
	String mimeType = null;
	String fileProperty = null;

	if (isOfflineFile) {
	    FormFile theOfflineFile = voteAuthoringForm.getTheOfflineFile();

	    try {
		stream = theOfflineFile.getInputStream();
		fileName = theOfflineFile.getFileName();
		if (fileName.length() == 0) {
		    return null;
		}
		fileProperty = "OFFLINE";

	    } catch (FileNotFoundException e) {
		AuthoringUtil.logger
			.error("filenotfound exception occured in accessing the repository server for the offline file : "
				+ e.getMessage());
	    } catch (IOException e) {
		AuthoringUtil.logger
			.error("io exception occured in accessing the repository server for the offline file : "
				+ e.getMessage());
	    }

	    if (fileName.length() > 0) {
		List listUploadedOfflineFileNames = (List) sessionMap
			.get(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY);
		int index = findFileNameIndex(listUploadedOfflineFileNames, fileName);
		if (index == 0) {
		    listUploadedOfflineFileNames.add(fileName);
		    sessionMap.put(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
		}
	    }

	} else {
	    FormFile theOnlineFile = voteAuthoringForm.getTheOnlineFile();

	    try {
		stream = theOnlineFile.getInputStream();
		fileName = theOnlineFile.getFileName();

		if (fileName.length() == 0) {
		    return null;
		}

		fileProperty = "ONLINE";

	    } catch (FileNotFoundException e) {
		AuthoringUtil.logger
			.error("filenotfound exception occured in accessing the repository server for the online file : "
				+ e.getMessage());
	    } catch (IOException e) {
		AuthoringUtil.logger
			.error("io exception occured in accessing the repository server for the online file : "
				+ e.getMessage());
	    }

	    if (fileName.length() > 0) {
		List listUploadedOnlineFileNames = (List) sessionMap
			.get(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY);
		int index = findFileNameIndex(listUploadedOnlineFileNames, fileName);
		if (index == 0) {
		    listUploadedOnlineFileNames.add(fileName);
		    sessionMap.put(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY, listUploadedOnlineFileNames);
		}
	    }
	}

	NodeKey nodeKey = null;
	try {
	    nodeKey = voteService.uploadFile(stream, fileName, mimeType, fileProperty);
	} catch (FileException e) {
	    AuthoringUtil.logger.error("exception writing raw data:" + e);
	    /* return a null dto */
	    return null;
	}

	VoteAttachmentDTO voteAttachmentDTO = new VoteAttachmentDTO();
	voteAttachmentDTO.setUid(null);
	voteAttachmentDTO.setUuid(nodeKey.getUuid().toString());
	voteAttachmentDTO.setFilename(fileName);
	voteAttachmentDTO.setOfflineFile(isOfflineFile);

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

	    VoteAttachmentDTO voteAttachmentDTO = new VoteAttachmentDTO();
	    voteAttachmentDTO.setUid(voteUploadedFile.getSubmissionId().toString());
	    voteAttachmentDTO.setUuid(voteUploadedFile.getUuid());
	    voteAttachmentDTO.setFilename(voteUploadedFile.getFileName());
	    voteAttachmentDTO.setOfflineFile(!voteUploadedFile.isFileOnline());

	    listAttachments.add(voteAttachmentDTO);
	}
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
	    listFilenames.add(voteAttachmentDTO.getFilename());
	}
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
	if (offlineFile.equals("1")) {
	    List listUploadedOfflineFileNames = (List) sessionMap
		    .get(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY);

	    listUploadedOfflineFileNames.remove(filename);

	    sessionMap.put(VoteAppConstants.LIST_UPLOADED_OFFLINE_FILENAMES_KEY, listUploadedOfflineFileNames);
	} else {
	    List listUploadedOnlineFileNames = (List) sessionMap
		    .get(VoteAppConstants.LIST_UPLOADED_ONLINE_FILENAMES_KEY);

	    listUploadedOnlineFileNames.remove(filename);

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
	    if (currentFilename.equals(filename)) {
		return mainIndex;
	    }
	}
	return 0;
    }

    public static List removeFileItem(List listFilesMetaData, String uuid) {
	VoteAttachmentDTO deletableAttachmentDTO = null;

	Iterator itList = listFilesMetaData.iterator();
	while (itList.hasNext()) {
	    VoteAttachmentDTO currentAttachmentDTO = (VoteAttachmentDTO) itList.next();

	    if (currentAttachmentDTO.getUuid().equals(uuid)) {
		deletableAttachmentDTO = currentAttachmentDTO;
		break;
	    }
	}

	listFilesMetaData.remove(deletableAttachmentDTO);

	return listFilesMetaData;
    }

    public static List extractFileNames(List listFilesMetaData) {
	Iterator itList = listFilesMetaData.iterator();
	LinkedList listFilenames = new LinkedList();

	while (itList.hasNext()) {
	    VoteAttachmentDTO voteAttachmentDTO = (VoteAttachmentDTO) itList.next();
	    String filename = voteAttachmentDTO.getFilename();
	    listFilenames.add(filename);
	}
	return listFilenames;
    }

    protected Map reconstructOptionContentMapForAdd(Map mapOptionsContent, HttpServletRequest request) {

	mapOptionsContent = repopulateMap(mapOptionsContent, request);
	mapOptionsContent.put(new Long(mapOptionsContent.size() + 1).toString(), "");

	return mapOptionsContent;
    }

    protected void reconstructOptionContentMapForRemove(Map mapOptionsContent, HttpServletRequest request,
	    VoteAuthoringForm voteAuthoringForm) {
	String optIndex = voteAuthoringForm.getOptIndex();

	String defLater = voteAuthoringForm.getActiveModule();

	String removableOptIndex = null;
	if (defLater != null && defLater.equals(VoteAppConstants.MONITORING)) {
	    removableOptIndex = (String) request.getSession().getAttribute(VoteAppConstants.REMOVABLE_QUESTION_INDEX);
	    optIndex = removableOptIndex;
	}

	long longOptIndex = new Long(optIndex).longValue();

	repopulateMap(mapOptionsContent, request);

	mapOptionsContent.remove(new Long(longOptIndex).toString());
    }

    protected Map repopulateMap(Map mapOptionsContent, HttpServletRequest request) {
	int intOptionIndex = mapOptionsContent.size();

	/* if there is data in the Map remaining from previous session remove those */
	mapOptionsContent.clear();

	for (long i = 0; i < intOptionIndex; i++) {
	    String candidateOptionEntry = request.getParameter("optionContent" + i);
	    if (i == 0) {
		request.getSession().setAttribute("defaultOptionContent", candidateOptionEntry);
	    }
	    if (candidateOptionEntry != null && candidateOptionEntry.length() > 0) {
		mapOptionsContent.put(new Long(i + 1).toString(), candidateOptionEntry);
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

	repopulateMap(mapOptionsContent, request);
	Map mapFinalOptionsContent = new TreeMap(new VoteComparator());

	Iterator itMap = mapOptionsContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {
		mapFinalOptionsContent.put(pairs.getKey(), pairs.getValue());
	    }
	}

	mapOptionsContent = mapFinalOptionsContent;
	return mapOptionsContent;
    }

    public void removeRedundantOptions(Map mapOptionsContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request) {

	String toolContentID = voteAuthoringForm.getToolContentID();

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	if (voteContent != null) {
	    List allNominations = voteService.getAllQuestionEntries(voteContent.getUid());

	    Iterator listIterator = allNominations.iterator();
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		VoteQueContent queContent = (VoteQueContent) listIterator.next();

		entryUsed = false;
		Iterator itMap = mapOptionsContent.entrySet().iterator();
		while (itMap.hasNext()) {
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();
		    if (pairs.getValue().toString().length() != 0) {
			if (pairs.getValue().toString().equals(queContent.getQuestion())) {
			    entryUsed = true;
			    break;
			}
		    }
		}

		if (entryUsed == false) {

		    VoteQueContent removeableVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), voteContent.getUid());

		    if (removeableVoteQueContent != null) {
			voteContent.getVoteQueContents().remove(removeableVoteQueContent);

			voteService.removeVoteQueContent(removeableVoteQueContent);
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

	String richTextTitle = request.getParameter(VoteAppConstants.TITLE);
	String richTextInstructions = request.getParameter(VoteAppConstants.INSTRUCTIONS);

	String lockOnFinish = request.getParameter("lockOnFinish");

	String allowTextEntry = request.getParameter("allowText");

	String showResults = request.getParameter("showResults");

	String maxInputs = request.getParameter("maxInputs");
	
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();

        String minNomcount= voteAuthoringForm.getMinNominationCount();
	    
	String richTextOfflineInstructions = (String) sessionMap.get(VoteAppConstants.OFFLINE_INSTRUCTIONS_KEY);

	String richTextOnlineInstructions = (String) sessionMap.get(VoteAppConstants.ONLINE_INSTRUCTIONS_KEY);

	String activeModule = voteAuthoringForm.getActiveModule();

	boolean lockedOnFinishBoolean = false;
	boolean allowTextBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
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
	
	if (useSelectLeaderToolOuput != null && useSelectLeaderToolOuput.equalsIgnoreCase("1")) {
	    useSelectLeaderToolOuputBoolean = true;
	}

	if (maxInputs != null && !"0".equals(maxInputs)) {
	    maxInputsShort = Short.parseShort(maxInputs);
	}

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}

	String toolContentID = voteAuthoringForm.getToolContentID();

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	voteContent.setVoteContentId(new Long(toolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    voteContent.setLockOnFinish(lockedOnFinishBoolean);
	    voteContent.setAllowText(allowTextBoolean);
	    voteContent.setShowResults(showResultsBoolean);
	    voteContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	    voteContent.setReflect(reflectBoolean);
	    voteContent.setReflectionSubject(reflectionSubject);
	    voteContent.setMaxNominationCount(maxNomcount);
	    voteContent.setMinNominationCount(minNomcount);	    
	    voteContent.setOnlineInstructions(richTextOnlineInstructions);
	    voteContent.setOfflineInstructions(richTextOfflineInstructions);
	    voteContent.setMaxExternalInputs(maxInputsShort);
	}

	voteContent.setAssignedDataFlowObject(assignedDataFlowObject != null);

	if (newContent) {
	    voteService.createVote(voteContent);
	} else {
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.retrieveVote(new Long(toolContentID));

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

	Iterator itMap = mapOptionsContent.entrySet().iterator();
	int diplayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    if (pairs.getValue().toString().length() != 0) {
		diplayOrder = new Integer(pairs.getKey().toString()).intValue();

		VoteQueContent queContent = new VoteQueContent(pairs.getValue().toString(), diplayOrder, voteContent,
			null);

		/* checks if the question is already recorded */

		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByQuestionText(pairs.getValue()
			.toString(), voteContent.getUid());
		if (existingVoteQueContent == null) {
		    /* make sure a question with the same question text is not already saved */
		    VoteQueContent duplicateVoteQueContent = voteService.getQuestionContentByQuestionText(pairs
			    .getValue().toString(), voteContent.getUid());
		    if (duplicateVoteQueContent == null) {
			voteContent.getVoteQueContents().add(queContent);
			queContent.setVoteContent(voteContent);

			voteService.createVoteQue(queContent);
		    }
		} else {
		    existingVoteQueContent.setQuestion(pairs.getValue().toString());
		    existingVoteQueContent.setDisplayOrder(diplayOrder);
		    voteService.updateVoteQueContent(existingVoteQueContent);
		}
	    }
	}
	return voteContent;
    }

    protected static List swapNodes(List listNominationContentDTO, String questionIndex, String direction) {

	int intNominationIndex = new Integer(questionIndex).intValue();
	int intOriginalNominationIndex = intNominationIndex;

	int replacedNodeIndex = 0;
	if (direction.equals("down")) {
	    replacedNodeIndex = ++intNominationIndex;
	} else {
	    replacedNodeIndex = --intNominationIndex;

	}

	VoteNominationContentDTO mainNode = extractNodeAtDisplayOrder(listNominationContentDTO,
		intOriginalNominationIndex);

	VoteNominationContentDTO replacedNode = extractNodeAtDisplayOrder(listNominationContentDTO, replacedNodeIndex);

	List listFinalNominationContentDTO = new LinkedList();

	listFinalNominationContentDTO = reorderSwappedListNominationContentDTO(listNominationContentDTO,
		intOriginalNominationIndex, replacedNodeIndex, mainNode, replacedNode);

	return listFinalNominationContentDTO;
    }

    protected static VoteNominationContentDTO extractNodeAtDisplayOrder(List listNominationContentDTO,
	    int intOriginalNominationIndex) {
	
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    if (new Integer(intOriginalNominationIndex).toString().equals(voteNominationContentDTO.getDisplayOrder())) {
		return voteNominationContentDTO;
	    }
	}
	return null;
    }

    protected static List reorderSwappedListNominationContentDTO(List listNominationContentDTO,
	    int intOriginalNominationIndex, int replacedNodeIndex, VoteNominationContentDTO mainNode,
	    VoteNominationContentDTO replacedNode) {

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();
	    queIndex++;
	    VoteNominationContentDTO tempNode = new VoteNominationContentDTO();

	    if (!voteNominationContentDTO.getDisplayOrder().equals(new Integer(intOriginalNominationIndex).toString())
		    && !voteNominationContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		//normal copy
		tempNode.setNomination(voteNominationContentDTO.getNomination());
		tempNode.setDisplayOrder(voteNominationContentDTO.getDisplayOrder());
		tempNode.setFeedback(voteNominationContentDTO.getFeedback());
	    } else if (voteNominationContentDTO.getDisplayOrder().equals(
		    new Integer(intOriginalNominationIndex).toString())) {
		//move type 1
		tempNode.setNomination(replacedNode.getNomination());
		tempNode.setDisplayOrder(replacedNode.getDisplayOrder());
		tempNode.setFeedback(replacedNode.getFeedback());
	    } else if (voteNominationContentDTO.getDisplayOrder().equals(new Integer(replacedNodeIndex).toString())) {
		//move type 1
		tempNode.setNomination(mainNode.getNomination());
		tempNode.setDisplayOrder(mainNode.getDisplayOrder());
		tempNode.setFeedback(mainNode.getFeedback());
	    }

	    listFinalNominationContentDTO.add(tempNode);
	}

	return listFinalNominationContentDTO;
    }

    protected static List reorderSimpleListNominationContentDTO(List listNominationContentDTO) {
	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    String question = voteNominationContentDTO.getNomination();

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    String feedback = voteNominationContentDTO.getFeedback();

	    if (question != null && !question.equals("")) {
		++queIndex;

		voteNominationContentDTO.setNomination(question);
		voteNominationContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		voteNominationContentDTO.setFeedback(feedback);

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    }
	}

	return listFinalNominationContentDTO;
    }

    protected static List reorderListNominationContentDTO(List listNominationContentDTO, String excludeNominationIndex) {

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    String question = voteNominationContentDTO.getNomination();

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    String feedback = voteNominationContentDTO.getFeedback();

	    if (question != null && !question.equals("")) {
		if (!displayOrder.equals(excludeNominationIndex)) {
		    ++queIndex;

		    voteNominationContentDTO.setNomination(question);
		    voteNominationContentDTO.setDisplayOrder(new Integer(queIndex).toString());
		    voteNominationContentDTO.setFeedback(feedback);

		    listFinalNominationContentDTO.add(voteNominationContentDTO);
		}
	    }
	}

	return listFinalNominationContentDTO;
    }

    public static boolean checkDuplicateNominations(List listNominationContentDTO, String newNomination) {

	Map mapNominationContent = extractMapNominationContent(listNominationContentDTO);

	Iterator itMap = mapNominationContent.entrySet().iterator();
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();
	    if (pairs.getValue() != null && !pairs.getValue().equals("")) {

		if (pairs.getValue().equals(newNomination)) {
		    return true;
		}
	    }
	}
	return false;
    }

    protected static Map extractMapNominationContent(List listNominationContentDTO) {
	Map mapNominationContent = new TreeMap(new VoteComparator());

	Iterator listIterator = listNominationContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    queIndex++;
	    mapNominationContent.put(new Integer(queIndex).toString(), voteNominationContentDTO.getNomination());
	}
	return mapNominationContent;
    }

    protected static List reorderUpdateListNominationContentDTO(List listNominationContentDTO,
	    VoteNominationContentDTO voteNominationContentDTONew, String editableNominationIndex) {

	List listFinalNominationContentDTO = new LinkedList();

	int queIndex = 0;
	Iterator listIterator = listNominationContentDTO.iterator();
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    ++queIndex;
	    String question = voteNominationContentDTO.getNomination();

	    String displayOrder = voteNominationContentDTO.getDisplayOrder();

	    String feedback = voteNominationContentDTO.getFeedback();

	    if (displayOrder.equals(editableNominationIndex)) {
		voteNominationContentDTO.setNomination(voteNominationContentDTONew.getNomination());
		voteNominationContentDTO.setDisplayOrder(voteNominationContentDTONew.getDisplayOrder());
		voteNominationContentDTO.setFeedback(voteNominationContentDTONew.getFeedback());

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    } else {
		voteNominationContentDTO.setNomination(question);
		voteNominationContentDTO.setDisplayOrder(displayOrder);
		voteNominationContentDTO.setFeedback(feedback);

		listFinalNominationContentDTO.add(voteNominationContentDTO);
	    }
	}

	return listFinalNominationContentDTO;
    }

    protected static Map extractMapFeedback(List listNominationContentDTO) {
	Map mapFeedbackContent = new TreeMap(new VoteComparator());

	Iterator listIterator = listNominationContentDTO.iterator();
	int queIndex = 0;
	while (listIterator.hasNext()) {
	    VoteNominationContentDTO voteNominationContentDTO = (VoteNominationContentDTO) listIterator.next();

	    queIndex++;
	    mapFeedbackContent.put(new Integer(queIndex).toString(), voteNominationContentDTO.getFeedback());
	}
	return mapFeedbackContent;
    }

    public void removeRedundantNominations(Map mapNominationContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, HttpServletRequest request, String toolContentID) {

	VoteContent voteContent = voteService.retrieveVote(new Long(toolContentID));

	if (voteContent != null) {
	    List allNominations = voteService.getAllQuestionEntries(voteContent.getUid());

	    Iterator listIterator = allNominations.iterator();
	    int mapIndex = 0;
	    boolean entryUsed = false;
	    while (listIterator.hasNext()) {
		++mapIndex;

		VoteQueContent queContent = (VoteQueContent) listIterator.next();

		entryUsed = false;
		Iterator itMap = mapNominationContent.entrySet().iterator();
		int displayOrder = 0;
		while (itMap.hasNext()) {
		    ++displayOrder;
		    entryUsed = false;
		    Map.Entry pairs = (Map.Entry) itMap.next();

		    if (pairs.getValue().toString().length() != 0) {

			if (mapIndex == displayOrder) {
			    entryUsed = true;
			    break;
			}

		    }
		}

		if (entryUsed == false) {

		    VoteQueContent removeableVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			    .getQuestion(), voteContent.getUid());
		    if (removeableVoteQueContent != null) {
			voteService.removeVoteQueContent(removeableVoteQueContent);
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

	String lockOnFinish = request.getParameter("lockOnFinish");

	String allowTextEntry = request.getParameter("allowText");

	String showResults = request.getParameter("showResults");

	String maxInputs = request.getParameter("maxInputs");
	
	String useSelectLeaderToolOuput = request.getParameter("useSelectLeaderToolOuput");

	String reflect = request.getParameter(VoteAppConstants.REFLECT);

	String reflectionSubject = voteAuthoringForm.getReflectionSubject();

	String maxNomcount = voteAuthoringForm.getMaxNominationCount();
	
        String minNomcount= voteAuthoringForm.getMinNominationCount();

	String richTextOfflineInstructions = request.getParameter(VoteAppConstants.OFFLINE_INSTRUCTIONS);
	String richTextOnlineInstructions = request.getParameter(VoteAppConstants.ONLINE_INSTRUCTIONS);

	String activeModule = request.getParameter(VoteAppConstants.ACTIVE_MODULE);

	boolean setCommonContent = true;
	if (lockOnFinish == null || allowTextEntry == null || showResults == null || reflect == null
		|| reflectionSubject == null || maxNomcount == null || minNomcount == null)

	{
	    setCommonContent = false;
	}

	boolean lockOnFinishBoolean = false;
	boolean allowTextEntryBoolean = false;
	boolean useSelectLeaderToolOuputBoolean = false;
	boolean reflectBoolean = false;
	boolean showResultsBoolean = false;
	short maxInputsShort = 0;

	if (lockOnFinish != null && lockOnFinish.equalsIgnoreCase("1")) {
	    lockOnFinishBoolean = true;
	}

	if (allowTextEntry != null && allowTextEntry.equalsIgnoreCase("1")) {
	    allowTextEntryBoolean = true;
	}
	
	if (useSelectLeaderToolOuput != null && useSelectLeaderToolOuput.equalsIgnoreCase("1")) {
	    useSelectLeaderToolOuputBoolean = true;
	}

	if (reflect != null && reflect.equalsIgnoreCase("1")) {
	    reflectBoolean = true;
	}

	if (showResults != null && showResults.equalsIgnoreCase("1")) {
	    showResultsBoolean = true;
	}

	if (maxInputs != null && !"0".equals(maxInputs)) {
	    maxInputsShort = Short.parseShort(maxInputs);
	}

	long userId = 0;
	if (toolUser != null) {
	    userId = toolUser.getUserID().longValue();
	} else {
	    HttpSession ss = SessionManager.getSession();
	    UserDTO user = (UserDTO) ss.getAttribute(AttributeNames.USER);
	    if (user != null) {
		userId = user.getUserID().longValue();
	    } else {
		userId = 0;
	    }
	}

	boolean newContent = false;
	if (voteContent == null) {
	    voteContent = new VoteContent();
	    newContent = true;
	}

	voteContent.setVoteContentId(new Long(strToolContentID));
	voteContent.setTitle(richTextTitle);
	voteContent.setInstructions(richTextInstructions);
	voteContent.setUpdateDate(new Date(System.currentTimeMillis()));
	/** keep updating this one */
	voteContent.setCreatedBy(userId);
	/** make sure we are setting the userId from the User object above */

	if (activeModule.equals(VoteAppConstants.AUTHORING)) {
	    voteContent.setLockOnFinish(lockOnFinishBoolean);
	    voteContent.setAllowText(allowTextEntryBoolean);
	    voteContent.setShowResults(showResultsBoolean);
	    voteContent.setUseSelectLeaderToolOuput(useSelectLeaderToolOuputBoolean);
	    voteContent.setReflect(reflectBoolean);
	    voteContent.setMaxNominationCount(maxNomcount);
	    voteContent.setMinNominationCount(minNomcount);

	    voteContent.setOnlineInstructions(richTextOnlineInstructions);
	    voteContent.setOfflineInstructions(richTextOfflineInstructions);

	    voteContent.setReflectionSubject(reflectionSubject);

	    voteContent.setMaxExternalInputs(maxInputsShort);
	}

	if (newContent) {
	    voteService.createVote(voteContent);
	} else {
	    voteService.updateVote(voteContent);
	}

	voteContent = voteService.retrieveVote(new Long(strToolContentID));

	voteContent = createQuestionContent(mapQuestionContent, mapFeedback, voteService, voteContent);

	voteService.saveDataFlowObjectAssigment(assignedDataFlowObject);

	return voteContent;
    }

    protected VoteContent createQuestionContent(Map mapQuestionContent, Map mapFeedback, IVoteService voteService,
	    VoteContent voteContent) {
	List questions = voteService.retrieveVoteQueContentsByToolContentId(voteContent.getUid().longValue());

	Iterator itMap = mapQuestionContent.entrySet().iterator();
	int displayOrder = 0;
	while (itMap.hasNext()) {
	    Map.Entry pairs = (Map.Entry) itMap.next();

	    if (pairs.getValue().toString().length() != 0) {

		++displayOrder;
		String currentFeedback = (String) mapFeedback.get(new Integer(displayOrder).toString());

		VoteQueContent queContent = new VoteQueContent(pairs.getValue().toString(), displayOrder, voteContent,
			null);

		/* checks if the question is already recorded */
		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByDisplayOrder(new Long(
			displayOrder), voteContent.getUid());

		if (existingVoteQueContent == null) {
		    /* make sure a question with the same question text is not already saved */
		    VoteQueContent duplicateVoteQueContent = voteService.getQuestionContentByQuestionText(pairs
			    .getValue().toString(), voteContent.getUid());
		    voteContent.getVoteQueContents().add(queContent);
		    queContent.setVoteContent(voteContent);

		    voteService.createVoteQue(queContent);
		} else {

		    String existingQuestion = existingVoteQueContent.getQuestion();

		    existingVoteQueContent.setQuestion(pairs.getValue().toString());
		    // existingVoteQueContent.setFeedback(currentFeedback);
		    existingVoteQueContent.setDisplayOrder(displayOrder);
		    voteService.updateVoteQueContent(existingVoteQueContent);
		}
	    }
	}
	return voteContent;
    }

    public void reOrganizeDisplayOrder(Map mapQuestionContent, IVoteService voteService,
	    VoteAuthoringForm voteAuthoringForm, VoteContent voteContent) {
	if (voteContent != null) {
	    List sortedQuestions = voteService.getAllQuestionEntriesSorted(voteContent.getUid().longValue());

	    Iterator listIterator = sortedQuestions.iterator();
	    int displayOrder = 1;
	    while (listIterator.hasNext()) {
		VoteQueContent queContent = (VoteQueContent) listIterator.next();

		VoteQueContent existingVoteQueContent = voteService.getQuestionContentByQuestionText(queContent
			.getQuestion(), voteContent.getUid());
		voteService.updateVoteQueContent(existingVoteQueContent);
		displayOrder++;
	    }
	}
    }

}
