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

package org.lamsfoundation.lams.tool.videoRecorder.service;

import java.util.List;
import java.util.Set;

import org.apache.struts.upload.FormFile;
import org.lamsfoundation.lams.notebook.model.NotebookEntry;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderCommentDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRatingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.dto.VideoRecorderRecordingDTO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorder;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderCondition;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderSession;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderUser;
import org.lamsfoundation.lams.tool.videoRecorder.util.VideoRecorderException;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

/**
 * Defines the services available to the web layer from the VideoRecorder Service
 */
public interface IVideoRecorderService {
    /**
     * Makes a copy of the default content and assigns it a newContentID
     * 
     * @params newContentID
     * @return
     */
    public VideoRecorder copyDefaultContent(Long newContentID);

    /**
     * Returns an instance of the VideoRecorder tools default content.
     * 
     * @return
     */
    public VideoRecorder getDefaultContent();

    /**
     * @param toolSignature
     * @return
     */
    public Long getDefaultContentIdBySignature(String toolSignature);

    /**
     * @param toolContentID
     * @return
     */
    public VideoRecorder getVideoRecorderByContentId(Long toolContentID);

    /**
     * @param videoRecorder
     */
    public void saveOrUpdateVideoRecorder(VideoRecorder videoRecorder);

    /**
     * @param toolSessionId
     * @return
     */
    public VideoRecorderSession getSessionBySessionId(Long toolSessionId);

    /**
     * @param videoRecorderSession
     */
    public void saveOrUpdateVideoRecorderSession(VideoRecorderSession videoRecorderSession);

    /**
     * 
     * @param userId
     * @param toolSessionId
     * @return
     */
    public VideoRecorderUser getUserByUserIdAndSessionId(Long userId, Long toolSessionId);

    /**
     * 
     * @param uid
     * @return
     */
    public VideoRecorderUser getUserByUID(Long uid);

    /**
     * 
     * @param videoRecorderUser
     */
    public void saveOrUpdateVideoRecorderUser(VideoRecorderUser videoRecorderUser);

    /**
     * 
     * @param commentId
     * @return
     */
    public VideoRecorderComment getCommentById(Long commentId);
    
    /**
     * 
     * @param userId
     * @return
     */
    public Set<VideoRecorderComment> getCommentsByUserId(Long userId);
    
    /**
     * 
     * @param toolSessionId
     * @return
     */
    public Set<VideoRecorderCommentDTO> getCommentsByToolSessionId(Long toolSessionId);

    /**
     * 
     * @param videoRecorderComment
     */
    public void saveOrUpdateVideoRecorderComment(VideoRecorderComment videoRecorderComment);    
    
    /**
     * 
     * @param ratingId
     * @return
     */
    public VideoRecorderRating getRatingById(Long ratingId);
    
    /**
     * 
     * @param userId
     * @return
     */
    public Set<VideoRecorderRating> getRatingsByUserId(Long userId);
    
    /**
     * 
     * @param toolSessionId
     * @return
     */
    public Set<VideoRecorderRatingDTO> getRatingsByToolSessionId(Long toolSessionId); 

    /**
     * 
     * @param videoRecorderComment
     */
    public void saveOrUpdateVideoRecorderRating(VideoRecorderRating videoRecorderRating);        
    
    /**
     * 
     * @param user
     * @param videoRecorderSession
     * @return
     */
    public VideoRecorderUser createVideoRecorderUser(UserDTO user, VideoRecorderSession videoRecorderSession);
    
    /**
     * 
     * @param recordingId
     * @return
     */
    public VideoRecorderRecording getRecordingById(Long recordingId);

    /**
     * 
     * @param toolSessionId
     * @param toolContentId
     * @return
     */
    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionId(Long toolSessionId, Long toolContentId);
    
    /**
     * 
     * @param toolSessionId
     * @param userId
     * @param toolContentId
     * @return
     */
    public List<VideoRecorderRecordingDTO> getRecordingsByToolSessionIdAndUserId(Long toolSessionId, Long userId, Long toolContentId);

    /**
     * 
     * @param toolContentId
     * @return
     */
    public List<VideoRecorderRecordingDTO> getRecordingsByToolContentId(Long toolContentId);
    
    /**
     * 
     * @param toolContentId
     * @return
     */
    public VideoRecorderRecording getFirstRecordingByToolContentId(Long toolContentId);
    
    /**
     * 
     * @param videoRecorderRecording
     */
    public void saveOrUpdateVideoRecorderRecording(VideoRecorderRecording videoRecorderRecording); 

    /**
     * 
     * @param videoRecorderRecording
     */
    public void deleteVideoRecorderRecording(VideoRecorderRecording videoRecorderRecording);
    
    /**
     * 
     * @param id
     * @param idType
     * @param signature
     * @param userID
     * @param title
     * @param entry
     * @return
     */
    Long createNotebookEntry(Long id, Integer idType, String signature, Integer userID, String entry);

    /**
     * 
     * @param uid
     * @return
     */
    NotebookEntry getEntry(Long uid);

    /**
     * 
     * @param uid
     * @param title
     * @param entry
     */
    void updateEntry(Long uid, String entry);

    void releaseConditionsFromCache(VideoRecorder videoRecorder);

    void deleteCondition(VideoRecorderCondition condition);
    
    public Long getNbRecordings(Long userID, Long sessionId);
    
    public Long getNbComments(Long userID, Long sessionId);

    public Long getNbRatings(Long userID, Long sessionId);
    
    public String getLanguageXML();
    
    public String getLanguageXMLForFCK();
    
    public String getMessage(String key);
    
    public boolean isGroupedActivity(long toolContentID);
}
