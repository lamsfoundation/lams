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
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.RatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;

public class RatingService implements IRatingService {

    private static Logger log = Logger.getLogger(RatingService.class);

    private IRatingDAO ratingDAO;

    private IRatingCommentDAO ratingCommentDAO;

    private IRatingCriteriaDAO ratingCriteriaDAO;

    protected IUserManagementService userManagementService;

    protected MessageService messageService;

    @Override
    public Rating getRatingByItemAndUser(Long itemId, Integer userId) {
	return null;
    }

    @Override
    public List<Rating> getRatingsByItem(Long itemId) {
	return null;
    }

    @Override
    public int getCountItemsRatedByUser(final Long toolContentId, final Integer userId) {
	return ratingDAO.getCountItemsRatedByUser(toolContentId, userId);
    }

    @Override
    public void saveOrUpdateRating(Rating rating) {
	ratingDAO.saveOrUpdate(rating);
    }

    @Override
    public RatingCriteriaDTO rateItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, float ratingFloat) {
	Long ratingCriteriaId = ratingCriteria.getRatingCriteriaId();
	Rating rating = ratingDAO.getRating(ratingCriteriaId, userId, itemId);

	// persist MessageRating changes in DB
	if (rating == null) { // add
	    rating = new Rating();
	    rating.setItemId(itemId);

	    User learner = (User) userManagementService.findById(User.class, userId);
	    rating.setLearner(learner);

	    rating.setRatingCriteria(ratingCriteria);
	}

	rating.setRating(ratingFloat);
	ratingDAO.saveOrUpdate(rating);

	// to make available new changes be visible on a jsp page
	return ratingDAO.getRatingAverageDTOByItem(ratingCriteriaId, itemId);
    }

    @Override
    public void commentItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, String comment) {
	RatingComment ratingComment = ratingCommentDAO.getRatingComment(ratingCriteria.getRatingCriteriaId(), userId,
		itemId);

	// persist MessageRating changes in DB
	if (ratingComment == null) { // add
	    ratingComment = new RatingComment();
	    ratingComment.setItemId(itemId);

	    User learner = (User) userManagementService.findById(User.class, userId);
	    ratingComment.setLearner(learner);

	    ratingComment.setRatingCriteria(ratingCriteria);
	}

	ratingComment.setComment(comment);
	ratingDAO.saveOrUpdate(ratingComment);
    }

    @Override
    public RatingCriteriaDTO getCriteriaDTOByUser(RatingCriteria criteria, Long itemId, Integer userId) {
	Long criteriaId = criteria.getRatingCriteriaId();

	RatingCriteriaDTO criteriaDto;
	if (criteria.isCommentsEnabled()) {
	    criteriaDto = ratingCommentDAO.getCommentsRatingDTO(criteriaId, itemId, userId);
	    criteriaDto.setCommentsMinWordsLimit(criteria.getCommentsMinWordsLimit());

	} else {
	    criteriaDto = ratingDAO.getRatingAverageDTOByUser(criteriaId, itemId, userId);
	}

	criteriaDto.setRatingCriteria(criteria);
	return criteriaDto;
    }

    @Override
    public List<RatingCriteria> getCriteriasByToolContentId(Long toolContentId) {
	List<RatingCriteria> criterias = ratingCriteriaDAO.getByToolContentId(toolContentId);
	return criterias;
    }

    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId) {
	return ratingCriteriaDAO.getByRatingCriteriaId(ratingCriteriaId);
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

    public void setRatingCommentDAO(IRatingCommentDAO ratingCommentDAO) {
	this.ratingCommentDAO = ratingCommentDAO;
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
