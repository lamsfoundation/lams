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

package org.lamsfoundation.lams.tool.videoRecorder.dao.hibernate;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderCommentDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRatingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.dao.IVideoRecorderRecordingDAO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderComment;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRecording;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * DAO for accessing the VideoRecorderRating objects - Hibernate specific code.
 */
public class VideoRecorderRatingDAO extends BaseDAO implements IVideoRecorderRatingDAO {

	private static final String SQL_QUERY_FIND_BY_RATING_ID = "from "
		+ VideoRecorderRating.class.getName() + " as r"
		+ " where uid=?";
		
	private static final String SQL_QUERY_BY_USER_ID = "from " + VideoRecorderRating.class.getName() + " as r "
	+ "where r.createBy.userId=?";
	
	private static final String SQL_QUERY_BY_TOOL_SESSION_ID = "from " + VideoRecorderComment.class.getName() + " as r "
	+ "where r.videoRecorderSession.sessionId=?";
	
	private static final String SQL_QUERY_NB_RATINGS_BY_USER = "select count(*) from " + VideoRecorderRecording.class.getName() + " r "
	+ " where r.createBy.userId=? and r.videoRecorderSession.sessionId=?";
	
	public VideoRecorderRating getRatingById(Long ratingId) {
		List list = this.getHibernateTemplate().find(
				SQL_QUERY_FIND_BY_RATING_ID ,
				ratingId);
	
		if (list == null || list.isEmpty())
			return null;
	
		return (VideoRecorderRating) list.get(0);
	}
	
	public List<VideoRecorderRating> getRatingsByUserId(Long userId){
		return (List<VideoRecorderRating>)(this.getHibernateTemplate().find(SQL_QUERY_BY_USER_ID, userId));
	}
	
	public List<VideoRecorderRating> getRatingsByToolSessionId(Long toolSessionId){
		return (List<VideoRecorderRating>)(this.getHibernateTemplate().find(SQL_QUERY_BY_TOOL_SESSION_ID, toolSessionId));
	}

    public void saveOrUpdate(VideoRecorderRating videoRecorderRating) {
		this.getHibernateTemplate().saveOrUpdate(videoRecorderRating);
		this.getHibernateTemplate().flush();
    }
    
	public Long getNbRatings(Long userID, Long sessionId) {
		List list = this.getHibernateTemplate().find(SQL_QUERY_NB_RATINGS_BY_USER, new Object[]{userID,sessionId});
		if(list != null && list.size() > 0)
			return ((Number)list.get(0)).longValue();
		else
			return new Long(0);
	}
}
