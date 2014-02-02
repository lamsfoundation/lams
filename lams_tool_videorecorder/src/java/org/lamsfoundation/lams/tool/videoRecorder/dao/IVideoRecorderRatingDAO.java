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

package org.lamsfoundation.lams.tool.videoRecorder.dao;

import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.videoRecorder.model.VideoRecorderRating;

/**
 * DAO for accessing the VideoRecorderRating objects - interface defining
 * methods to be implemented by the Hibernate or other implementation.
 */
public interface IVideoRecorderRatingDAO extends IBaseDAO{
	/**
	 * @param recordingId
	 * @return
	 */
	VideoRecorderRating getRatingById(Long ratingId);
	
	List<VideoRecorderRating> getRatingsByUserId(Long userId);
	
	List<VideoRecorderRating> getRatingsByToolSessionId(Long toolSessionId);
	
	void saveOrUpdate(VideoRecorderRating videoRecorderRating);
	
	Long getNbRatings(Long userID, Long sessionId);
}
