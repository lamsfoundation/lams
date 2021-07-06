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

package org.lamsfoundation.lams.rating.service;

import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.lamsfoundation.lams.rating.RatingException;
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingRubricsColumn;
import org.lamsfoundation.lams.usermanagement.User;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;
import org.lamsfoundation.lams.util.JsonUtil;
import org.lamsfoundation.lams.util.MessageService;
import org.lamsfoundation.lams.util.WebUtil;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

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
    public int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId) {
	return ratingDAO.getCountItemsRatedByUserByCriteria(criteriaId, userId);
    }

    @Override
    public void removeUserCommitsByContent(final Long contentId, final Integer userId) {
	List<Rating> ratings = ratingDAO.getRatingsByUser(contentId, userId);
	for (Rating rating : ratings) {
	    ratingDAO.delete(rating);
	}

	List<RatingComment> comments = ratingCommentDAO.getCommentsByContentAndUser(contentId, userId);
	for (RatingComment comment : comments) {
	    ratingDAO.delete(comment);
	}
    }

    @Override
    public Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId) {
	return ratingDAO.countUsersRatedEachItem(contentId, toolSessionId, itemIds, excludeUserId);
    }

    @Override
    public Map<Long, Long> countUsersRatedEachItemByCriteria(final Long criteriaId, final Long toolSessionId,
	    final Collection<Long> itemIds, Integer excludeUserId) {
	return ratingDAO.countUsersRatedEachItemByCriteria(criteriaId, toolSessionId, itemIds, excludeUserId);
    }

    @Override
    public void saveOrUpdateRating(Rating rating) {
	ratingDAO.saveOrUpdate(rating);
    }

    @Override
    public ItemRatingCriteriaDTO rateItem(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId,
	    Long itemId, float ratingFloat) {

	Long ratingCriteriaId = ratingCriteria.getRatingCriteriaId();
	Rating rating = ratingDAO.getRating(ratingCriteriaId, userId, itemId);

	// persist MessageRating changes in DB
	if (rating == null) { // add
	    rating = new Rating();
	    rating.setItemId(itemId);

	    User learner = (User) userManagementService.findById(User.class, userId);
	    rating.setLearner(learner);

	    rating.setRatingCriteria(ratingCriteria);
	    rating.setToolSessionId(toolSessionId);
	}

	// LDEV-4590 Star Rating can never be more than 5 stars
	if (ratingCriteria.isStarStyleRating()
		&& Float.compare(ratingFloat, RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT) > 0) {
	    ratingFloat = RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT;
	}

	rating.setRating(ratingFloat);
	ratingDAO.saveOrUpdate(rating);

	// to make available new changes be visible on a jsp page
	ItemRatingCriteriaDTO averageDTO = ratingDAO.getRatingAverageDTOByItem(ratingCriteriaId, toolSessionId, itemId);
	averageDTO.setUserRating(Float.toString(ratingFloat));
	return averageDTO;
    }

    @Override
    public int rateItems(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId,
	    Map<Long, Float> newRatings) {

	User learner = (User) userManagementService.findById(User.class, userId);
	int numRatings = 0;

	List<Rating> dbRatings = ratingDAO.getRatingsByUserCriteria(ratingCriteria.getRatingCriteriaId(), userId);
	for (Rating rating : dbRatings) {
	    Long itemId = rating.getItemId();
	    Float newRating = newRatings.get(itemId);
	    if (newRating != null) {
		rating.setRating(newRating);
		newRatings.remove(itemId);
		numRatings++;
		ratingDAO.saveOrUpdate(rating);
	    } else {
		rating.setRating((Float) null);
	    }
	}
	for (Map.Entry<Long, Float> entry : newRatings.entrySet()) {
	    Rating rating = new Rating();
	    rating.setItemId(entry.getKey());
	    rating.setLearner(learner);
	    rating.setRatingCriteria(ratingCriteria);
	    rating.setToolSessionId(toolSessionId);

	    // LDEV-4590 Star Rating can never be more than 5 stars
	    float rawRating = entry.getValue();
	    if (ratingCriteria.isStarStyleRating()
		    && Float.compare(rawRating, RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT) > 0) {
		rawRating = RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX_AS_FLOAT;
	    }
	    rating.setRating(rawRating);

	    ratingDAO.saveOrUpdate(rating);
	    numRatings++;
	}

	return numRatings;
    }

    @Override
    public void commentItem(RatingCriteria ratingCriteria, Long toolSessionId, Integer userId, Long itemId,
	    String comment) {
	RatingComment ratingComment = ratingCommentDAO.getComment(ratingCriteria.getRatingCriteriaId(), userId, itemId);

	// persist MessageRating changes in DB
	if (ratingComment == null) { // add
	    ratingComment = new RatingComment();
	    ratingComment.setItemId(itemId);

	    User learner = (User) userManagementService.findById(User.class, userId);
	    ratingComment.setLearner(learner);

	    ratingComment.setRatingCriteria(ratingCriteria);
	    ratingComment.setToolSessionId(toolSessionId);
	}

	ratingComment.setComment(comment);
	ratingDAO.saveOrUpdate(ratingComment);
    }

    @Override
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Long toolSessionId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {

	// fail safe - if there are no items, don't try to look anything up! LDEV-4094
	if (itemIds == null || itemIds.isEmpty()) {
	    return new LinkedList<>();
	}

	//initial preparations
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	List<RatingCriteria> criterias = getCriteriasByToolContentId(contentId);
	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;

	//handle comments criteria
	List<ItemRatingDTO> itemDtos = handleCommentsCriteria(criterias, toolSessionId, itemIds,
		isCommentsByOtherUsersRequired, userId);

	//get all data from DB
	List<Rating> userRatings = userId == null ? null : ratingDAO.getRatingsByUser(contentId, userId.intValue());
	List<Object[]> itemsStatistics;
	if (isSingleItem) {
	    itemsStatistics = toolSessionId == null
		    ? ratingDAO.getRatingAverageByContentAndItem(contentId, singleItemId)
		    : ratingDAO.getRatingAverageByContentAndItem(contentId, toolSessionId, singleItemId);

	    // query DB using itemIds
	} else {
	    itemsStatistics = toolSessionId == null ? ratingDAO.getRatingAverageByContentAndItems(contentId, itemIds)
		    : ratingDAO.getRatingAverageByContentAndItems(contentId, itemIds);
	}

	//handle all criterias except for comments' one
	for (ItemRatingDTO itemDto : itemDtos) {
	    Long itemId = itemDto.getItemId();
	    List<ItemRatingCriteriaDTO> criteriaDtos = new LinkedList<>();
	    itemDto.setCriteriaDtos(criteriaDtos);

	    for (RatingCriteria criteria : criterias) {
		Long criteriaId = criteria.getRatingCriteriaId();

		//comments' criteria are handled earlier, at the beginning of this function
		if (criteria.getRatingStyle() == 0) {
		    continue;
		}

		ItemRatingCriteriaDTO criteriaDto = new ItemRatingCriteriaDTO();
		criteriaDto.setRatingCriteria(criteria);

		if (userId != null) {
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
		}

		// check if there is any data returned from DB regarding this item and criteria
		Object[] itemStatistics = null;
		for (Object[] itemStatisticsIter : itemsStatistics) {
		    Long itemIdIter = (Long) itemStatisticsIter[0];
		    Long ratingCriteriaIdIter = (Long) itemStatisticsIter[1];

		    if (itemIdIter.equals(itemId) && ratingCriteriaIdIter.equals(criteriaId)) {
			itemStatistics = itemStatisticsIter;
		    }
		}

		if (itemStatistics != null) {
		    Number averageRating = (Number) itemStatistics[2];
		    criteriaDto.setAverageRating(numberFormat.format(averageRating));
		    criteriaDto.setAverageRatingAsNumber(averageRating);
		    criteriaDto.setNumberOfVotes(String.valueOf(itemStatistics[3]));
		} else {
		    criteriaDto.setAverageRating("0");
		    criteriaDto.setAverageRatingAsNumber(0);
		    criteriaDto.setNumberOfVotes("0");
		}

		criteriaDtos.add(criteriaDto);

	    }

	}

	return itemDtos;
    }

    /*
     * Fetches all required comments from the DB.
     */
    private List<ItemRatingDTO> handleCommentsCriteria(List<RatingCriteria> criterias, Long toolSessionId,
	    Collection<Long> itemIds, boolean isCommentsByOtherUsersRequired, Long userId) {

	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;

	List<ItemRatingDTO> itemDtos = initializeItemDtos(itemIds);

	//handle comments criteria
	for (RatingCriteria criteria : criterias) {
	    if (criteria.isCommentRating() || criteria.isCommentsEnabled()) {
		Long commentCriteriaId = criteria.getRatingCriteriaId();

		List<RatingCommentDTO> commentDtos;
		if (isSingleItem) {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItem(commentCriteriaId, toolSessionId,
			    singleItemId);

		    //query DB using itemIds
		} else if (isCommentsByOtherUsersRequired) {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItems(commentCriteriaId, toolSessionId,
			    itemIds);

		    // get only comments done by the specified user
		} else {
		    commentDtos = ratingCommentDAO.getCommentsByCriteriaAndItemsAndUser(commentCriteriaId, itemIds,
			    userId.intValue());
		}

		for (ItemRatingDTO itemDto : itemDtos) {
		    itemDto.setCommentsEnabled(true);
		    itemDto.setCommentsCriteriaId(commentCriteriaId);
		    itemDto.setCommentsMinWordsLimit(criteria.getCommentsMinWordsLimit());

		    //assign commentDtos by the appropriate items
		    List<RatingCommentDTO> commentDtosPerItem = new LinkedList<>();
		    for (RatingCommentDTO commentDto : commentDtos) {
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

    private List<ItemRatingDTO> initializeItemDtos(Collection<Long> itemIds) {
	// initialize itemDtos
	List<ItemRatingDTO> itemDtos = new LinkedList<>();
	for (Long itemId : itemIds) {
	    ItemRatingDTO itemDto = new ItemRatingDTO();
	    itemDto.setItemId(itemId);
	    itemDtos.add(itemDto);
	}
	return itemDtos;
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

    @SuppressWarnings("rawtypes")
    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId, Class clasz) {
	return ratingCriteriaDAO.getByRatingCriteriaId(ratingCriteriaId, clasz);
    }

    private LearnerItemRatingCriteria updateLearnerItemRatingCriteria(LearnerItemRatingCriteria oldCriteria,
	    Long toolContentId, String title, Integer orderId, int ratingStyle, boolean withComments,
	    int minWordsInComment) {

	LearnerItemRatingCriteria criteria = oldCriteria != null ? oldCriteria : new LearnerItemRatingCriteria();
	criteria.setRatingCriteriaTypeId(RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
	criteria.setToolContentId(toolContentId);
	criteria.setRatingStyle(ratingStyle);
	criteria.setOrderId(orderId);
	criteria.setTitle(title);
	criteria.setCommentsEnabled(withComments);
	criteria.setCommentsMinWordsLimit(minWordsInComment);
	criteria.setMinimumRates(0);
	criteria.setMaximumRates(0);
	criteria.setMaxRating(RatingCriteria.getDefaultMaxRating(ratingStyle));
	return criteria;
    }

    @Override
    public LearnerItemRatingCriteria saveLearnerItemRatingCriteria(Long toolContentId, String title, Integer orderId,
	    int ratingStyle, boolean withComments, int minWordsInComment) throws RatingException {
	List<RatingCriteria> criterias = ratingCriteriaDAO.getByToolContentId(toolContentId);
	LearnerItemRatingCriteria criteria = null;
	if (criterias.size() > 0) {
	    for (RatingCriteria c : criterias) {
		if (c.getOrderId().equals(orderId)) {
		    if (!c.isLearnerItemRatingCriteria()) {
			String msg = new StringBuilder(
				"Wrong type of criteria found - needs to be a AuthoredItemRatingCriteria for toolContentId ")
					.append(toolContentId).append("criteriaId ").append(c.getRatingCriteriaId())
					.toString();
			log.error(msg);
			throw new RatingException(msg);
		    }
		    criteria = (LearnerItemRatingCriteria) c;
		    break;
		}
	    }
	}
	criteria = updateLearnerItemRatingCriteria(criteria, toolContentId, title, orderId, ratingStyle, withComments,
		minWordsInComment);
	ratingCriteriaDAO.saveOrUpdate(criteria);
	return criteria;
    }

    @Override
    public int deleteAllRatingCriterias(Long toolContentId) {
	List<RatingCriteria> criterias = ratingCriteriaDAO.getByToolContentId(toolContentId);
	int count = 0;
	for (RatingCriteria criteria : criterias) {
	    ratingCriteriaDAO.deleteRatingCriteria(criteria.getRatingCriteriaId());
	    count++;
	}
	return count;
    }

    @Override
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {

	// different handling for comments - simple tag sets the isCommentsEnabled flag,
	// the complex tag sends explicit comment type entry.
	boolean explicitCommentTypeFound = false;

	// create orderId to RatingCriteria map
	Map<Integer, RatingCriteria> mapOrderIdToRatingCriteria = new HashMap<>();
	for (RatingCriteria ratingCriteriaIter : oldCriterias) {
	    mapOrderIdToRatingCriteria.put(ratingCriteriaIter.getOrderId(), ratingCriteriaIter);
	}

//	for ( Map.Entry entry : request.getParameterMap().entrySet()) {
//	    log.debug("entry: "+entry.getKey()+" "+entry.getValue());
//	}

	int criteriaMaxOrderId = WebUtil.readIntParam(request, "criteriaMaxOrderId");
	Map<Integer, Integer> groupIdMap = new HashMap<>();
	// i is equal to an old orderId
	for (int i = 1; i <= criteriaMaxOrderId; i++) {

	    String criteriaTitle = WebUtil.readStrParam(request, "criteriaTitle" + i, true);

	    Integer ratingStyle = WebUtil.readIntParam(request, "ratingStyle" + i, true);
	    if (ratingStyle == null) {
		ratingStyle = RatingCriteria.RATING_STYLE_STAR;
	    }

	    Integer groupId = WebUtil.readIntParam(request, "groupId" + i, true);

	    Integer maxRating = WebUtil.readIntParam(request, "maxRating" + i, true);
	    if (maxRating == null) {
		maxRating = RatingCriteria.getDefaultMaxRating(ratingStyle);
	    }

	    Integer minRatings = 0;
	    Integer maxRatings = 0;
	    if (ratingStyle == RatingCriteria.RATING_STYLE_STAR || ratingStyle == RatingCriteria.RATING_STYLE_COMMENT) {
		minRatings = WebUtil.readIntParam(request, "minimumRates" + i, true);
		maxRatings = WebUtil.readIntParam(request, "maximumRates" + i, true);
	    }

	    boolean commentsEnabled = (ratingStyle != RatingCriteria.RATING_STYLE_COMMENT
		    ? WebUtil.readBooleanParam(request, "enableComments" + i, false)
		    : true);

	    RatingCriteria ratingCriteria = mapOrderIdToRatingCriteria.get(i);
	    if (ratingStyle == RatingCriteria.RATING_STYLE_COMMENT
		    || (ratingCriteria != null && ratingCriteria.isCommentRating())) {
		explicitCommentTypeFound = true;
	    }

	    if (StringUtils.isNotBlank(criteriaTitle)) {
		int newCriteriaOrderId = WebUtil.readIntParam(request, "criteriaOrderId" + i);

		// modify existing one if it exists. add otherwise
		if (ratingCriteria == null) {
		    ratingCriteria = new LearnerItemRatingCriteria();
		    ratingCriteria.setRatingCriteriaTypeId(RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
		    ((LearnerItemRatingCriteria) ratingCriteria).setToolContentId(toolContentId);
		}

		ratingCriteria.setOrderId(newCriteriaOrderId);
		ratingCriteria.setTitle(criteriaTitle.strip().replaceAll("\r\n", "<BR/>"));

		ratingCriteria.setRatingStyle(ratingStyle);
		ratingCriteria.setMaxRating(maxRating);
		ratingCriteria.setCommentsEnabled(commentsEnabled);
		if (commentsEnabled) {
		    Integer commentsMinWordsLimit = WebUtil.readIntParam(request, "commentsMinWordsLimit" + i, true);
		    ratingCriteria.setCommentsMinWordsLimit(commentsMinWordsLimit != null ? commentsMinWordsLimit : 0);
		} else {
		    ratingCriteria.setCommentsMinWordsLimit(0);
		}

		ratingCriteria.setMinimumRates(minRatings);
		ratingCriteria.setMaximumRates(maxRatings);

		ratingCriteriaDAO.saveOrUpdate(ratingCriteria);

		if (ratingStyle.equals(RatingCriteria.RATING_STYLE_RUBRICS)) {
		    if (groupId == null) {
			log.error("No group ID found for rubrics rating criterion with order ID " + newCriteriaOrderId);
			continue;
		    }

		    Integer newGroupId = groupIdMap.get(groupId);

		    if (newGroupId == null) {
			if (groupId < 1) {
			    newGroupId = getNextRatingCriteriaGroupId();
			} else {
			    newGroupId = groupId;
			    ratingCriteriaDAO.deleteByProperty(RatingRubricsColumn.class, "ratingCriteriaGroupId",
				    groupId);
			}

			for (int columnIndex = 1; columnIndex <= RatingCriteria.RATING_STYLE_RUBRICS_DEFAULT_MAX; columnIndex++) {
			    String columnHeaderString = WebUtil.readStrParam(request,
				    "rubrics" + groupId + "column" + columnIndex, true);
			    if (columnHeaderString != null) {
				RatingRubricsColumn columnHeader = new RatingRubricsColumn(columnIndex,
					columnHeaderString);
				columnHeader.setRatingCriteriaGroupId(newGroupId);
				columnHeader.setName(columnHeaderString.strip().replaceAll("\r\n", "<BR/>"));
				ratingCriteriaDAO.insert(columnHeader);
			    }
			}

			groupIdMap.put(groupId, newGroupId);
		    }

		    ratingCriteria.setRatingCriteriaGroupId(newGroupId);

		    ratingCriteria.getRubricsColumns().clear();
		    for (int columnIndex = 1; columnIndex <= RatingCriteria.RATING_STYLE_RUBRICS_DEFAULT_MAX; columnIndex++) {
			String columnString = WebUtil.readStrParam(request, "rubrics" + i + "cell" + columnIndex, true);
			if (columnString != null) {
			    RatingRubricsColumn column = new RatingRubricsColumn(columnIndex, columnString);
			    column.setName(columnString.strip().replaceAll("\r\n", "<BR/>"));
			    ratingCriteria.getRubricsColumns().add(column);
			}
		    }
		}

		// delete
	    } else if (ratingCriteria != null) {
		ratingCriteriaDAO.deleteRatingCriteria(ratingCriteria.getRatingCriteriaId());
	    }

	}

	// ==== handle comments criteria - simple tag support ====
	if (!explicitCommentTypeFound) {
	    boolean isCommentsEnabled = WebUtil.readBooleanParam(request, "isCommentsEnabled", false);

	    // find comments' responsible RatingCriteria
	    RatingCriteria commentsResponsibleCriteria = null;
	    for (RatingCriteria ratingCriteriaIter : oldCriterias) {
		if (ratingCriteriaIter.isCommentRating()) {
		    commentsResponsibleCriteria = ratingCriteriaIter;
		    break;
		}
	    }
	    // create commentsRatingCriteria if it's required
	    if (isCommentsEnabled) {
		if (commentsResponsibleCriteria == null) {
		    commentsResponsibleCriteria = new LearnerItemRatingCriteria();
		    commentsResponsibleCriteria.setRatingCriteriaTypeId(RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
		    ((LearnerItemRatingCriteria) commentsResponsibleCriteria).setToolContentId(toolContentId);
		    commentsResponsibleCriteria.setOrderId(0);
		    commentsResponsibleCriteria.setCommentsEnabled(true);
		    commentsResponsibleCriteria.setRatingStyle(RatingCriteria.RATING_STYLE_COMMENT);
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
    }

    /**
     * Convert the raw data from the database to StyledCriteriaRatingDTO and StyleRatingDTO. The rating service expects
     * the potential itemId followed by rating.* (its own fields) and the last item in the array to be an item
     * description (eg formatted user's name) Will go back to the database for the justification comment that would
     * apply to hedging.
     *
     * If includeCurrentUser == true will include the current users' records (used for SelfReview and Monitoring)
     * otherwise skips the current user, so they do not rate themselves!
     *
     * Entries in Object array for comment style:
     * tool item id (usually user id), rating.item_id, rating_comment.comment, (tool fields)+
     * Entries in Object array for other styles:
     * tool item id (usually user id), rating.item_id, rating_comment.comment,
     * rating.rating, calculated.average_rating, calculated.count_vote, (tool fields)+
     */
    @Override
    public StyledCriteriaRatingDTO convertToStyledDTO(RatingCriteria ratingCriteria, Long currentUserId,
	    boolean includeCurrentUser, List<Object[]> rawDataRows) {

	StyledCriteriaRatingDTO criteriaDto = new StyledCriteriaRatingDTO();
	criteriaDto.setRatingCriteria(ratingCriteria);

	if (ratingCriteria.isHedgeStyleRating() && ratingCriteria.isCommentsEnabled()) {
	    RatingComment justification = ratingCommentDAO.getComment(ratingCriteria.getRatingCriteriaId(),
		    currentUserId.intValue(), ratingCriteria.getRatingCriteriaId());
	    if (justification != null) {
		criteriaDto.setJustificationComment(justification.getComment());
	    }
	} else if (ratingCriteria.isStarStyleRating()) {
	    int ratedCount = getCountItemsRatedByUserByCriteria(ratingCriteria.getRatingCriteriaId(),
		    currentUserId.intValue());
	    criteriaDto.setCountRatedItems(ratedCount);
	}

	boolean isComment = ratingCriteria.isCommentRating();
	if (rawDataRows != null) {

	    List<StyledRatingDTO> ratingDtos = new ArrayList<>();
	    criteriaDto.setRatingDtos(ratingDtos);

	    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	    numberFormat.setMaximumFractionDigits(1);

	    for (Object[] row : rawDataRows) {
		int numColumns = row.length;
		if ((isComment && numColumns < 4) || (!isComment && numColumns < 7)) {
		    StringBuffer buf = new StringBuffer("convertToStyledDTO: ratingCriteria")
			    .append(ratingCriteria.getRatingCriteriaId()).append(" UserId: ").append(currentUserId)
			    .append("  Skipping data row as there are not enough columns. Only ").append(numColumns)
			    .append(" columns. numColumns: ").append(numColumns);
		    if (numColumns > 0) {
			buf.append(" Data: 0:").append(row[0]);
		    }
		    if (numColumns > 1) {
			buf.append(" 1:").append(row[1]);
		    }
		    log.error(buf.toString());
		    break;
		}

		long itemId = ((BigInteger) row[0]).longValue();
		if (includeCurrentUser || itemId != currentUserId) {

		    if (row[1] != null && itemId != ((BigInteger) row[0]).longValue()) {
			log.error("convertToStyledDTO: ratingCriteria" + ratingCriteria.getRatingCriteriaId()
				+ " UserId: " + currentUserId + "  Potential issue: expected item id " + row[0]
				+ " does match real item id " + row[1] + ". Data: 0:" + row[0] + " 1:" + row[1]);
		    }

		    StyledRatingDTO dto = new StyledRatingDTO(((BigInteger) row[0]).longValue());
		    dto.setComment((String) row[2]);
		    dto.setItemDescription(row[numColumns - 2] != null ? row[numColumns - 2].toString() : null);
		    dto.setItemDescription2(row[numColumns - 1] != null ? row[numColumns - 1].toString() : null);
		    if (!isComment) {
			dto.setUserRating(row[3] == null ? "" : numberFormat.format(row[3]));
			dto.setAverageRating(row[4] == null ? "" : numberFormat.format(row[4]));
			dto.setNumberOfVotes(row[5] == null ? "" : numberFormat.format(row[5]));
		    }
		    ratingDtos.add(dto);
		}
	    }
	}

	return criteriaDto;
    }

    /**
     * Convert the raw data from the database to JSON, similar on StyledCriteriaRatingDTO and StyleRatingDTO.
     * The rating service expects the potential itemId followed by rating.* (its own fields) and the last two items
     * in the array will be two item descriptions fields (eg formatted user's name, portrait id). Will go back to
     * the database for the justification comment that would apply to hedging.
     *
     * If includeCurrentUser == true will include the current users' records (used for SelfReview and Monitoring)
     * otherwise skips the current user, so they do not rate themselves!
     *
     * toolSessionId is only used to calculate the number of users who have rated an item, so it may be null
     * if the tool doesn't need to seperate session (Peer Review doesn't need to separate by sessions).
     *
     * In JSON for use with tablesorter.
     * Entries in Object array for comment style:
     * tool item id (usually user id), rating.item_id, rating_comment.comment, (tool fields)+
     * Entries in Object array for other styles:
     * tool item id (usually user id), rating.item_id, rating_comment.comment,
     * rating.rating, calculated.average_rating, calculated.count_vote, (tool fields)+
     *
     * @throws JSONException
     */
    @Override
    public ArrayNode convertToStyledJSON(RatingCriteria ratingCriteria, Long toolSessionId, Long currentUserId,
	    boolean includeCurrentUser, List<Object[]> rawDataRows, boolean needRatesPerUser) {

	ArrayNode rows = JsonNodeFactory.instance.arrayNode();
	boolean isComment = ratingCriteria.isCommentRating();

	if (rawDataRows != null) {

	    List<Long> itemIds = needRatesPerUser ? new ArrayList<>(rawDataRows.size()) : null;

	    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	    numberFormat.setMaximumFractionDigits(1);

	    for (Object[] row : rawDataRows) {
		int numColumns = row.length;
		if ((isComment && numColumns < 4) || (!isComment && numColumns < 7)) {
		    StringBuffer buf = new StringBuffer("convertToStyledJSON: ratingCriteria")
			    .append(ratingCriteria.getRatingCriteriaId()).append(" UserId: ").append(currentUserId)
			    .append("  Skipping data row as there are not enough columns. Only ").append(numColumns)
			    .append(" columns. numColumns: ").append(numColumns);
		    if (numColumns > 0) {
			buf.append(" Data: 0:").append(row[0]);
		    }
		    if (numColumns > 1) {
			buf.append(" 1:").append(row[1]);
		    }
		    log.error(buf.toString());
		    break;
		}

		Long itemId = ((BigInteger) row[0]).longValue();
		if (includeCurrentUser || itemId != currentUserId) {

		    if (row[1] != null && itemId.longValue() != ((BigInteger) row[0]).longValue()) {
			log.error("convertToStyledJSON: ratingCriteria" + ratingCriteria.getRatingCriteriaId()
				+ " UserId: " + currentUserId + "  Potential issue: expected item id " + row[0]
				+ " does match real item id " + row[1] + ". Data: 0:" + row[0] + " 1:" + row[1]);
		    }

		    ObjectNode userRow = JsonNodeFactory.instance.objectNode();
		    userRow.put("itemId", itemId);
		    userRow.put("comment", row[2] == null ? "" : (String) row[2]);
		    userRow.put("itemDescription", row[numColumns - 2] == null ? "" : row[numColumns - 2].toString());
		    userRow.put("itemDescription2", row[numColumns - 1] == null ? "" : row[numColumns - 1].toString());
		    if (!isComment) {
			userRow.put("userRating", row[3] == null ? "" : numberFormat.format(row[3]));
			userRow.put("averageRating", row[4] == null ? "" : numberFormat.format(row[4]));
			userRow.put("numberOfVotes", row[5] == null ? "" : numberFormat.format(row[5]));
		    } else {
			// don't have missing entries in JSON or an exception can occur if you try to access them
			userRow.put("userRating", "");
			userRow.put("averageRating", "");
			userRow.put("numberOfVotes", "");
		    }

		    rows.add(userRow);

		    if (needRatesPerUser) {
			itemIds.add(itemId);
		    }
		}
	    }

	    if (needRatesPerUser) {
		Map<Long, Long> countUsersRatedEachItemMap = ratingDAO.countUsersRatedEachItemByCriteria(
			ratingCriteria.getRatingCriteriaId(), toolSessionId, itemIds, -1);
		for (JsonNode row : rows) {
		    Long count = countUsersRatedEachItemMap.get(JsonUtil.optLong(row, "itemId"));
		    ((ObjectNode) row).put("ratesPerUser", count == null ? 0 : count);
		}
	    }
	}

	return rows;

    }

    @Override
    public boolean isCommentsEnabled(Long toolContentId) {
	return ratingCriteriaDAO.isCommentsEnabledForToolContent(toolContentId);
    }

    @Override
    public int getCommentsMinWordsLimit(Long toolContentId) {
	return ratingCriteriaDAO.getCommentsMinWordsLimitForToolContent(toolContentId);
    }

    @Override
    public String getRatingSelectJoinSQL(Integer ratingStyle, boolean getByUser) {
	return ratingDAO.getRatingSelectJoinSQL(ratingStyle, getByUser);
    }

    @Override
    public List<String> getRubricsColumnHeaders(int groupId) {
	return ratingCriteriaDAO.getRubricsColumnHeaders(groupId);
    }

    @Override
    public int getNextRatingCriteriaGroupId() {
	return ratingCriteriaDAO.getNextRatingCriteriaGroupId();
    }

    @Override
    public List<RatingCommentDTO> getCommentsByCriteriaAndItem(Long ratingCriteriaId, Long toolSessionId, Long itemId) {
	return ratingCommentDAO.getCommentsByCriteriaAndItem(ratingCriteriaId, toolSessionId, itemId);
    }

    /**
     * Get all the raw ratings for a combination of criteria and item ids. Used by Peer Review to do SPA analysis.
     */
    @Override
    public List<Rating> getRatingsByCriteriasAndItems(Collection<Long> ratingCriteriaIds, Collection<Long> itemIds) {
	return ratingDAO.getRatingsByCriteriasAndItems(ratingCriteriaIds, itemIds);
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
