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


package org.lamsfoundation.lams.tool.videoRecorder.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRecordingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;

/**
 * DAO for accessing the VideoRecorderRecording objects - Hibernate specific code.
 */
public class VideoRecorderRecordingDAO extends BaseDAO implements IVideoRecorderRecordingDAO {

    private static final String SQL_QUERY_FIND_BY_RECORDING_ID = "from " + VideoRecorderRecording.class.getName()
	    + " as f" + " where uid=?";

    private static final String SQL_QUERY_BY_SESSION_ID = "from " + VideoRecorderRecording.class.getName() + " as vrr "
	    + "where vrr.videoRecorderSession.sessionId=?";

    private static final String SQL_QUERY_BY_TOOL_CONTENT_ID = "from " + VideoRecorderRecording.class.getName()
	    + " as vrr " + "where vrr.toolContentId=?";

    private static final String SQL_QUERY_BY_SESSION_ID_AND_USER_UID = "from " + VideoRecorderRecording.class.getName()
	    + " as vrr " + "where vrr.videoRecorderSession.sessionId=? and vrr.createBy.uid=?";

    private static final String SQL_QUERY_BY_SESSION_ID_AND_USER_ID = "from " + VideoRecorderRecording.class.getName()
	    + " as vrr " + "where vrr.videoRecorderSession.sessionId=? and vrr.createBy.userId=?";

    private static final String SQL_QUERY_NB_RECORDINGS_BY_USER = "select count(*) from "
	    + VideoRecorderRecording.class.getName() + " vrr "
	    + " where vrr.createBy.userId=? and vrr.videoRecorderSession.sessionId=?";

    @Override
    public VideoRecorderRecording getRecordingById(Long recordingId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_FIND_BY_RECORDING_ID, recordingId);

	if (list == null || list.isEmpty()) {
	    return null;
	}

	return (VideoRecorderRecording) list.get(0);
    }

    @Override
    public List<VideoRecorderRecording> getByToolSessionId(Long toolSessionId) {
	return (this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION_ID, toolSessionId));
    }

    @Override
    public List<VideoRecorderRecording> getBySessionAndUserUid(Long toolSessionId, Long userUid) {
	return (this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION_ID_AND_USER_UID,
		new Object[] { toolSessionId, userUid }));
    }

    @Override
    public List<VideoRecorderRecording> getBySessionAndUserId(Long toolSessionId, Long userId) {
	return (this.getHibernateTemplate().find(SQL_QUERY_BY_SESSION_ID_AND_USER_ID,
		new Object[] { toolSessionId, userId }));
    }

    @Override
    public List<VideoRecorderRecording> getByToolContentId(Long toolContentId) {
	return (this.getHibernateTemplate().find(SQL_QUERY_BY_TOOL_CONTENT_ID, toolContentId));
    }

    @Override
    public void saveOrUpdate(VideoRecorderRecording videoRecorderRecording) {
	this.getHibernateTemplate().saveOrUpdate(videoRecorderRecording);
	this.getHibernateTemplate().flush();
    }

    @Override
    public Long getNbRecordings(Long userID, Long sessionId) {
	List list = this.getHibernateTemplate().find(SQL_QUERY_NB_RECORDINGS_BY_USER,
		new Object[] { userID, sessionId });
	if (list != null && list.size() > 0) {
	    return ((Number) list.get(0)).longValue();
	} else {
	    return new Long(0);
	}
    }
}
