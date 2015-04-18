/****************************************************************
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
 * ****************************************************************
 */

/* $Id$ */

package org.lamsfoundation.lams.rating.service;

import java.util.List;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

public class RatingService implements IRatingService {

    private static Logger log = Logger.getLogger(RatingService.class);

    private IRatingDAO ratingDAO;
    
    private IRatingCriteriaDAO ratingCriteriaDAO;

    protected IUserManagementService userManagementService;

    protected MessageService messageService;

//    public TreeMap<Long, List<Rating>> getEntryByLesson(Integer userID, Integer idType) {
//	TreeMap<Long, List<Rating>> entryMap = new TreeMap<Long, List<Rating>>();
//	List<Rating> list = getEntry(userID, idType);
//
//	for (Rating entry : list) {
//	    if (entryMap.containsKey(entry.getItemId())) {
//		String lessonName = (String) entryMap.get(entry.getItemId()).get(0).getRating();
//		entry.setRating(lessonName);
//		entryMap.get(entry.getItemId()).add(entry);
//	    } else {
//		Lesson lesson = (Lesson) baseDAO.find(Lesson.class, entry.getItemId());
//		List<Rating> newEntryList = new ArrayList<Rating>();
//
//		entry.setRating(lesson.getLessonName());
//		newEntryList.add(entry);
//
//		entryMap.put(entry.getItemId(), newEntryList);
//	    }
//	}
//
//	return entryMap;
//    }
//
//    public List<Rating> getEntry(Long id, Integer idType, String signature, Integer userID) {
//	return ratingDAO.get(id, idType, signature, userID);
//    }
//
//    public List<Rating> getEntry(Long id, Integer idType, String signature) {
//	return ratingDAO.get(id, idType, signature);
//    }
//
//    public List<Rating> getEntry(Long id, Integer idType, Integer userID) {
//	return ratingDAO.get(id, idType, userID);
//    }
//
//    public List<Rating> getEntry(Integer userID) {
//	return ratingDAO.get(userID);
//    }
//
//    public List<Rating> getEntry(Integer userID, Integer idType) {
//	return ratingDAO.get(userID, idType);
//    }
//
//    public List<Rating> getEntry(Integer userID, Long lessonID) {
//	return ratingDAO.get(userID, lessonID);
//    }
//
//    public Rating getEntry(Long uid) {
//	return ratingDAO.get(uid);
//    }
//
//    public void updateEntry(Long uid, String title, String entry) {
//	Rating ne = getEntry(uid);
//	if (ne != null) {
//	    ne.setTitle(title);
//	    ne.setEntry(entry);
//	    ne.setLastModified(new Date());
//	    saveOrUpdateRating(ne);
//	} else {
//	    log.debug("updateEntry: uid " + uid + "does not exist");
//	}
//    }
//
//    public void updateEntry(Rating rating) {
//	rating.setLastModified(new Date());
//	saveOrUpdateRating(rating);
//    }

    @Override
    public Rating getRatingByItemAndUser(Long itemId, Integer userId) {
	return null;
    }

    @Override
    public List<Rating> getRatingsByItem(Long itemId) {
	return null;
    }
    
    @Override
    public int getCountItemsRatedByActivityAndUser(Long toolContentId, Integer userId) {
	return ratingDAO.getCountItemsRatedByActivityAndUser(toolContentId, userId);
    }
    
    @Override
    public void saveOrUpdateRating(Rating rating) {
	ratingDAO.saveOrUpdate(rating);
    }
    
    @Override
    public RatingDTO rateItem(Long ratingCriteriaId, Integer userId, Long itemId, float ratingFloat) {
	Rating rating = ratingDAO.getRating(ratingCriteriaId, userId, itemId);

	//persist MessageRating changes in DB
	if (rating == null) { // add
	    rating = new Rating();
	    rating.setItemId(itemId);
	    
	    User learner = (User) userManagementService.findById(User.class, userId);
	    rating.setLearner(learner);
	    
	    RatingCriteria ratingCriteria = (RatingCriteria) userManagementService.findById(RatingCriteria.class, ratingCriteriaId);
	    rating.setRatingCriteria(ratingCriteria);
	}
	rating.setRating(ratingFloat);
	ratingDAO.saveOrUpdate(rating);
	
	//to make available new changes be visible on a jsp page
	return ratingDAO.getRatingAverageDTOByItem(ratingCriteriaId, itemId);
    }
    
    @Override
    public RatingDTO getRatingDTOByUser(Long ratingCriteriaId, Long itemId, Integer userId) {
	return ratingDAO.getRatingAverageDTOByUser(ratingCriteriaId, itemId, userId);
    }
    
    @Override
    public List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId) {
	List<RatingCriteria> criterias = ratingCriteriaDAO.getByToolContentId(toolContentId);
	return criterias;
    }
    
    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId, Class clasz) {
	return ratingCriteriaDAO.getByRatingCriteriaId(ratingCriteriaId, clasz);
    }
    
    @Override
    public void saveOrUpdateRatingCriteria(RatingCriteria criteria) {
	ratingCriteriaDAO.saveOrUpdate(criteria);
    }
    
    @Override
    public void deleteRatingCriteria(Long ratingCriteriaId) {
	ratingCriteriaDAO.deleteRatingCriteria(ratingCriteriaId);
    }

    /* ********** Used by Spring to "inject" the linked objects ************* */

    public void setRatingDAO(IRatingDAO ratingDAO) {
	this.ratingDAO = ratingDAO;
    }
    
    public void setRatingCriteriaDAO(IRatingCriteriaDAO ratingCriteriaDAO) {
	this.ratingCriteriaDAO = ratingCriteriaDAO;
    }

    /**
     * 
     * @param IUserManagementService
     *            The userManagementService to set.
     */
    public void setUserManagementService(IUserManagementService userManagementService) {
	this.userManagementService = userManagementService;
    }

    /**
     * Set i18n MessageService
     */
    public void setMessageService(MessageService messageService) {
	this.messageService = messageService;
    }
}
