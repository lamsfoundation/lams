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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

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
    public ItemRatingCriteriaDTO rateItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, float ratingFloat) {
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
	RatingComment ratingComment = ratingCommentDAO.getComment(ratingCriteria.getRatingCriteriaId(), userId,
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
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds, boolean isCommentsByOtherUsersRequired, Long userId) {

	//initial preparations
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	List<RatingCriteria> criterias = getCriteriasByToolContentId(contentId);
	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;
	
	//handle comments criteria
	List<ItemRatingDTO> itemDtos = handleCommentsCriteria(criterias, itemIds, isCommentsByOtherUsersRequired, userId);
	
	//get all data from DB
	List<Rating> userRatings = ratingDAO.getRatingsByUser(contentId, userId.intValue());
	List<Object[]> itemsStatistics;
	if (isSingleItem) {
	    itemsStatistics = ratingDAO.getRatingAverageByContentAndItem(contentId, singleItemId);
	    
	// query DB using itemIds
	} else {
	    itemsStatistics = ratingDAO.getRatingAverageByContentAndItems(contentId, itemIds);
	}

	//handle all criterias except for comments' one
	for (ItemRatingDTO itemDto : itemDtos) {
	    Long itemId = itemDto.getItemId();
	    List<ItemRatingCriteriaDTO> criteriaDtos = new LinkedList<ItemRatingCriteriaDTO>();
	    itemDto.setCriteriaDtos(criteriaDtos);

	    for (RatingCriteria criteria : criterias) {
		Long criteriaId = criteria.getRatingCriteriaId();
		
		//comments' criteria are handled earlier, at the beginning of this function
		if (criteria.isCommentsEnabled()) {
		    continue;
		}
		    
		ItemRatingCriteriaDTO criteriaDto = new ItemRatingCriteriaDTO();
		criteriaDto.setRatingCriteria(criteria);

		// set user's rating
		Rating userRating = null;
		for (Rating userRatingIter : userRatings) {
		    if (userRatingIter.getItemId().equals(itemId)
			    && userRatingIter.getRatingCriteria().getRatingCriteriaId().equals(criteriaId)) {
			userRating = userRatingIter;
		    }
		}
		String userRatingStr = userRating == null ? "" : numberFormat.format(userRating.getRating());
		criteriaDto.setUserRating(userRatingStr);

		// check if there is any data returned from DB regarding this item and criteria
		Object[] itemStatistics = null;
		for (Object[] itemStatisticsIter : itemsStatistics) {
		    Long itemIdIter = (Long) itemStatisticsIter[0];
		    Long ratingCriteriaIdIter = (Long) itemStatisticsIter[1];

		    if (itemIdIter.equals(itemId) && ratingCriteriaIdIter.equals(criteriaId)) {
			itemStatistics = itemStatisticsIter;
		    }
		}

		String averageRating = itemStatistics == null ? "0" : numberFormat.format(itemStatistics[2]);
		String numberOfVotes = itemStatistics == null ? "0" : String.valueOf(itemStatistics[3]);
		criteriaDto.setAverageRating(averageRating);
		criteriaDto.setNumberOfVotes(numberOfVotes);

		criteriaDtos.add(criteriaDto);

	    }

	}

	return itemDtos;
    }
    
    /*
     * Fetches all required comments from the DB.
     */
    private List<ItemRatingDTO> handleCommentsCriteria(List<RatingCriteria> criterias, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {
	
	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;

	// initialize itemDtos
	List<ItemRatingDTO> itemDtos = new LinkedList<ItemRatingDTO>();
	for (Long itemId : itemIds) {
	    ItemRatingDTO itemDto = new ItemRatingDTO();
	    itemDto.setItemId(itemId);
	    itemDtos.add(itemDto);
	}
	
	//handle comments criteria
	for (RatingCriteria criteria : criterias) {
	    if (criteria.isCommentsEnabled()) {
		Long commentCriteriaId = criteria.getRatingCriteriaId();
		
		List<RatingCommentDTO> commentDtos;
		if (isSingleItem) {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItem(commentCriteriaId, singleItemId);
		
		//query DB using itemIds
		} else if (isCommentsByOtherUsersRequired) {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItems(commentCriteriaId, itemIds);
		    
		// get only comments done by the specified user
		} else {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItemsAndUser(commentCriteriaId, itemIds,
			    userId.intValue());
		}
		
		for (ItemRatingDTO itemDto: itemDtos) {
		    itemDto.setCommentsEnabled(true);
		    itemDto.setCommentsCriteriaId(commentCriteriaId);
		    itemDto.setCommentsMinWordsLimit(criteria.getCommentsMinWordsLimit());
		    
		    //assign commentDtos by the appropriate items
		    List<RatingCommentDTO> commentDtosPerItem = new LinkedList<RatingCommentDTO>();
		    for (RatingCommentDTO commentDto: commentDtos) {
			if (commentDto.getItemId().equals(itemDto.getItemId())) {
			    commentDtosPerItem.add(commentDto);
			    
			    //fill in commentPostedByUser field
			    if (commentDto.getUserId().equals(userId)) {
				itemDto.setCommentPostedByUser(commentDto);
			    }
			}
		    }
		    
		    itemDto.setCommentDtos(commentDtosPerItem);
		}
		
		break;
	    }
	}
	
	return itemDtos;
    }
    
    @Override
    public ItemRatingDTO getRatingCriteriaDtoWithActualRatings(Long contentId, Long itemId) {

	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	List<RatingCriteria> criterias = getCriteriasByToolContentId(contentId);
	
	// handle comments criteria
	List<Long> itemIds = Collections.singletonList(itemId);
	boolean isCommentsByOtherUsersRequired = false;// not important as it's not used
	Long userId = -1L; // passing impossible user id as there is no need in this info
	List<ItemRatingDTO> itemDtos = handleCommentsCriteria(criterias, itemIds, isCommentsByOtherUsersRequired,
		userId);
	ItemRatingDTO itemDto = itemDtos.get(0);
	
	//get all data from DB
	List<Rating> itemRatings = ratingDAO.getRatingsByItem(contentId, itemId);

	// handle all criterias except for comments' one
	List<ItemRatingCriteriaDTO> criteriaDtos = new LinkedList<ItemRatingCriteriaDTO>();
	for (RatingCriteria criteria : criterias) {
	    Long criteriaId = criteria.getRatingCriteriaId();

	    // comments' criteria are handled earlier, at the beginning of this function
	    if (criteria.isCommentsEnabled()) {
		continue;
	    }

	    ItemRatingCriteriaDTO criteriaDto = new ItemRatingCriteriaDTO();
	    criteriaDto.setRatingCriteria(criteria);
	    List<RatingDTO> ratingDtos = new ArrayList<RatingDTO>();
	    
	    //find according to that criteria itemRatings
	    for (Rating itemRating : itemRatings) {
		if (itemRating.getRatingCriteria().getRatingCriteriaId().equals(criteria.getRatingCriteriaId())) {
		    RatingDTO ratingDto = new RatingDTO();
		    String ratingStr = numberFormat.format(itemRating.getRating());
		    ratingDto.setRating(ratingStr);
		    ratingDto.setLearner(itemRating.getLearner());
		    ratingDtos.add(ratingDto);
		}
	    }
	    criteriaDto.setRatingDtos(ratingDtos);

	    criteriaDtos.add(criteriaDto);
	}
	itemDto.setCriteriaDtos(criteriaDtos);
	
	return itemDto;
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
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {
	// create orderId to RatingCriteria map
	Map<Integer, RatingCriteria> mapOrderIdToRatingCriteria = new HashMap<Integer, RatingCriteria>();
	for (RatingCriteria ratingCriteriaIter : oldCriterias) {
	    mapOrderIdToRatingCriteria.put(ratingCriteriaIter.getOrderId(), ratingCriteriaIter);
	}

	int criteriaMaxOrderId = WebUtil.readIntParam(request, "criteriaMaxOrderId");
	// i is equal to an old orderId
	for (int i = 1; i <= criteriaMaxOrderId; i++) {

	    String criteriaTitle = WebUtil.readStrParam(request, "criteriaTitle" + i, true);

	    RatingCriteria ratingCriteria = mapOrderIdToRatingCriteria.get(i);
	    if (StringUtils.isNotBlank(criteriaTitle)) {
		int newCriteriaOrderId = WebUtil.readIntParam(request, "criteriaOrderId" + i);

		// modify existing one if it exists. add otherwise
		if (ratingCriteria == null) {
		    ratingCriteria = new LearnerItemRatingCriteria();
		    ratingCriteria.setRatingCriteriaTypeId(LearnerItemRatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
		    ((LearnerItemRatingCriteria) ratingCriteria).setToolContentId(toolContentId);
		}

		ratingCriteria.setOrderId(newCriteriaOrderId);
		ratingCriteria.setTitle(criteriaTitle);
		ratingCriteriaDAO.saveOrUpdate(ratingCriteria);
		// !!updatedCriterias.add(ratingCriteria);

		// delete
	    } else if (ratingCriteria != null) {
		ratingCriteriaDAO.deleteRatingCriteria(ratingCriteria.getRatingCriteriaId());
	    }

	}

	// ==== handle comments criteria ====

	boolean isCommentsEnabled = WebUtil.readBooleanParam(request, "isCommentsEnabled", false);
	// find comments' responsible RatingCriteria
	RatingCriteria commentsResponsibleCriteria = null;
	for (RatingCriteria ratingCriteriaIter : oldCriterias) {
	    if (ratingCriteriaIter.isCommentsEnabled()) {
		commentsResponsibleCriteria = ratingCriteriaIter;
		break;
	    }
	}
	// create commentsRatingCriteria if it's required
	if (isCommentsEnabled) {
	    if (commentsResponsibleCriteria == null) {
		commentsResponsibleCriteria = new LearnerItemRatingCriteria();
		commentsResponsibleCriteria
			.setRatingCriteriaTypeId(LearnerItemRatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
		((LearnerItemRatingCriteria) commentsResponsibleCriteria).setToolContentId(toolContentId);
		commentsResponsibleCriteria.setOrderId(0);
		commentsResponsibleCriteria.setCommentsEnabled(true);
	    }

	    int commentsMinWordsLimit = WebUtil.readIntParam(request, "commentsMinWordsLimit");
	    commentsResponsibleCriteria.setCommentsMinWordsLimit(commentsMinWordsLimit);

	    ratingCriteriaDAO.saveOrUpdate(commentsResponsibleCriteria);

	    // delete commentsRatingCriteria if it's not required
	} else {
	    if (commentsResponsibleCriteria != null) {
		ratingCriteriaDAO.deleteRatingCriteria(commentsResponsibleCriteria.getRatingCriteriaId());
	    }
	}
    }
    
    @Override
    public boolean isCommentsEnabled(Long toolContentId) {	
	return ratingCriteriaDAO.isCommentsEnabledForToolContent(toolContentId);
    }
    
    @Override
    public int getCommentsMinWordsLimit(Long toolContentId) {
	return ratingCriteriaDAO.getCommentsMinWordsLimitForToolContent(toolContentId);
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
