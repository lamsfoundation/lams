/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
/* $$Id$$ */
package org.eucm.lams.tool.eadventure.service;

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.struts.upload.FormFile;
import org.eucm.lams.tool.eadventure.dto.ReflectDTO;
import org.eucm.lams.tool.eadventure.dto.Summary;
import org.eucm.lams.tool.eadventure.model.Eadventure;
import org.eucm.lams.tool.eadventure.model.EadventureCondition;
import org.eucm.lams.tool.eadventure.model.EadventureExpression;
import org.eucm.lams.tool.eadventure.model.EadventureItemVisitLog;
import org.eucm.lams.tool.eadventure.model.EadventureParam;
import org.eucm.lams.tool.eadventure.model.EadventureSession;
import org.eucm.lams.tool.eadventure.model.EadventureUser;
import org.eucm.lams.tool.eadventure.model.EadventureVars;
import org.lamsfoundation.lams.contentrepository.IVersionedNode;
import org.lamsfoundation.lams.events.IEventNotificationService;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author Dapeng.Ni
 *
 *         Interface that defines the contract that all ShareEadventure service provider must follow.
 */
public interface IEadventureService {

    public void removeParams(Long toolContentId);

    public List<EadventureParam> getEadventureParamByContentId(Long contentId);

    public EadventureVars getEadventureVars(Long itemVisitLogID, String name);

    public EadventureItemVisitLog getEadventureItemLog(Long itemUid, Long userId);

    /**
     * Recibir report del applet
     */
    void setReportInput(String name, String value, String userId, String toolSessionID);

    /**
     * Get applet params and set it values in the associated eadventure vars.
     * 
     * @return true if the eadventure is not completed and it was defined as defineCompleted
     *         in order to indicate VarsExchangeServlet to send or not javascript order.
     */
    boolean setAppletInput(String name, String value, String userId, String toolSessionID);

    /**
     * Get file <code>IVersiondNode</code> by given package id and path.
     *
     * @param packageId
     * @param relPathString
     * @return
     * @throws EadventureApplicationException
     */
    IVersionedNode getFileNode(Long packageId, String relPathString) throws EadventureApplicationException;

    /**
     * Get <code>Eadventure</code> by toolContentID.
     *
     * @param contentId
     * @return
     */
    Eadventure getEadventureByContentId(Long contentId);

    /**
     * Get a cloned copy of tool default tool content (Eadventure) and assign the toolContentId of that copy as the
     * given
     * <code>contentId</code>
     *
     * @param contentId
     * @return
     * @throws EadventureApplicationException
     */
    Eadventure getDefaultContent(Long contentId) throws EadventureApplicationException;

    /**
     * Get list of eadventure items by given eadventureUid. These eadventure items must be created by author.
     *
     * @param eadventureUid
     * @return
     */
    // List getAuthoredItems(Long eadventureUid);

    /**
     * Upload eadventure file to repository.
     *
     * @param eAdventure
     * @param file
     * @throws UploadEadventureFileException
     */
    void uploadEadventureFile(Eadventure item, FormFile file) throws UploadEadventureFileException;

    // ********** for user methods *************
    /**
     * Create a new user in database.
     */
    void createUser(EadventureUser eadventureUser);

    /**
     * Get user by given userID and toolContentID.
     *
     * @param long1
     * @return
     */
    EadventureUser getUserByIDAndContent(Long userID, Long contentId);

    /**
     * Get user by sessionID and UserID
     *
     * @param long1
     * @param sessionId
     * @return
     */
    EadventureUser getUserByIDAndSession(Long long1, Long sessionId);

    // ********** Repository methods ***********************
    /**
     * Delete file from repository.
     */
    void deleteFromRepository(Long fileUuid, Long fileVersionId) throws EadventureApplicationException;

    /**
     * Save or update eadventure into database.
     *
     * @param Eadventure
     */
    void saveOrUpdateEadventure(Eadventure Eadventure);

    public void saveOrUpdateEadventureConditions(Set<EadventureCondition> eadConditions);

    public void saveOrUpdateEadventureExpressions(Set<EadventureExpression> eadExpressions, Long condUID);

    public void saveOrUpdateEadventureCondition(EadventureCondition eadCondition);

    public void saveOrUpdateEadventureExpression(EadventureExpression eadExpression);

    public void deleteEadventureCondition(Long conditionUid);

    public void deleteEadventureExpression(Long expressionUid);

    /**
     * Delete resoruce item from database.
     *
     * @param uid
     */
    //void deleteEadventureItem(Long uid);

    /**
     * Return all reource items within the given toolSessionID.
     *
     * @param sessionId
     * @return
     */
    //List<EadventureItem> getEadventureItemsBySessionId(Long sessionId);

    /**
     * Get eadventure which is relative with the special toolSession.
     *
     * @param sessionId
     * @return
     */
    Eadventure getEadventureBySessionId(Long sessionId);

    /**
     * Get eadventure toolSession by toolSessionId
     *
     * @param sessionId
     * @return
     */
    EadventureSession getEadventureSessionBySessionId(Long sessionId);

    /**
     * Save or update eadventure session.
     *
     * @param resSession
     */
    void saveOrUpdateEadventureSession(EadventureSession resSession);

    //void retrieveComplete(SortedSet<EadventureItem> eadventureItemList, EadventureUser user);

    void setItemComplete(Long eadventureItemUid, Long userId, Long sessionId);

    void setItemAccess(Long eadventureItemUid, Long userId, Long sessionId);

    /**
     * the reqired number minus the count of view of the given user.
     *
     * @param userUid
     * @return
     */
    // int checkMiniView(Long toolSessionId, Long userId);

    /**
     * If success return next activity's url, otherwise return null.
     *
     * @param toolSessionId
     * @param userId
     * @return
     */
    String finishToolSession(Long toolSessionId, Long userId) throws EadventureApplicationException;

    //EadventureItem getEadventureItemByUid(Long itemUid);

    void saveOrUpdateEadventureParams(Set<EadventureParam> eadParams);

    /**
     * Return monitoring summary list. The return value is list of eadventure summaries for each groups.
     *
     * @param contentId
     * @return
     */
    List<Summary> getSummary(Long contentId);

    List<EadventureUser> getUserListBySessionItem(Long sessionId, Long itemUid);

    /**
     * Set a eadventure item visible or not.
     *
     * @param itemUid
     * @param visible
     *            true, item is visible. False, item is invisible.
     */
    void setItemVisible(Long itemUid, boolean visible);

    /**
     * Get eadventure item <code>Summary</code> list according to sessionId
     *
     * @return
     */
    public List<Summary> exportBySessionId(Long sessionId, Long userId);

    public List<Summary> exportByContentId(Long contentId);

    /**
     * Create refection entry into notebook tool.
     *
     * @param sessionId
     * @param notebook_tool
     * @param tool_signature
     * @param userId
     * @param entryText
     */
    public Long createNotebookEntry(Long sessionId, Integer notebookToolType, String toolSignature, Integer userId,
	    String entryText);

    /**
     * Get reflection entry from notebook tool.
     *
     * @param sessionId
     * @param idType
     * @param signature
     * @param userID
     * @return
     */
    public NotebookEntry getEntry(Long sessionId, Integer idType, String signature, Integer userID);

    /**
     * @param notebookEntry
     */
    public void updateEntry(NotebookEntry notebookEntry);

    /**
     * Get Reflect DTO list grouped by sessionID.
     *
     * @param contentId
     * @return
     */
    Map<Long, Set<ReflectDTO>> getReflectList(Long contentId, boolean setEntry);

    /**
     * Get user by UID
     *
     * @param uid
     * @return
     */
    EadventureUser getUser(Long uid);

    public IEventNotificationService getEventNotificationService();

    /**
     * Gets a message from eadventure bundle. Same as <code><fmt:message></code> in JSP pages.
     *
     * @param key
     *            key of the message
     * @param args
     *            arguments for the message
     * @return message content
     */
    String getLocalisedMessage(String key, Object[] args);

    /**
     * Finds out which lesson the given tool content belongs to and returns its monitoring users.
     *
     * @param sessionId
     *            tool session ID
     * @return list of teachers that monitor the lesson which contains the tool with given session ID
     */
    public List<User> getMonitorsByToolSessionId(Long sessionId);

    public boolean checkCondition(String conditionName, Long toolSessionId, Long userUid);

    public void removeParam(EadventureParam eadParams);
}
