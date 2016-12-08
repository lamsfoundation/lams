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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONArray;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.rating.dao.IRatingCommentDAO;
import org.lamsfoundation.lams.rating.dao.IRatingCriteriaDAO;
import org.lamsfoundation.lams.rating.dao.IRatingDAO;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.dto.ItemRatingDTO;
import org.lamsfoundation.lams.rating.dto.RatingCommentDTO;
import org.lamsfoundation.lams.rating.dto.RatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledCriteriaRatingDTO;
import org.lamsfoundation.lams.rating.dto.StyledRatingDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.Rating;
import org.lamsfoundation.lams.rating.model.RatingComment;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
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
    public int getCountItemsRatedByUserByCriteria(final Long criteriaId, final Integer userId) {
	return ratingDAO.getCountItemsRatedByUserByCriteria(criteriaId, userId);
    }

    @Override
    public Map<Long, Long> countUsersRatedEachItem(final Long contentId, final Collection<Long> itemIds,
	    Integer excludeUserId) {
	return ratingDAO.countUsersRatedEachItem(contentId, itemIds, excludeUserId);
    }

    @Override
    public Map<Long, Long> countUsersRatedEachItemByCriteria(final Long criteriaId, final Collection<Long> itemIds,
	    Integer excludeUserId) {
	return ratingDAO.countUsersRatedEachItemByCriteria(criteriaId, itemIds, excludeUserId);
    }
    
    @Override
    public void saveOrUpdateRating(Rating rating) {
	ratingDAO.saveOrUpdate(rating);
    }

    @Override
    public ItemRatingCriteriaDTO rateItem(RatingCriteria ratingCriteria, Integer userId, Long itemId,
	    float ratingFloat) {

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
    public int rateItems(RatingCriteria ratingCriteria, Integer userId, Map<Long, Float> newRatings) {

	User learner = (User) userManagementService.findById(User.class, userId);
	int numRatings = 0;
	
	List<Rating> dbRatings = ratingDAO.getRatingsByUserCriteria(ratingCriteria.getRatingCriteriaId(), userId);
	for ( Rating rating: dbRatings ) {
	    Long itemId = rating.getItemId();
	    Float newRating = newRatings.get(itemId);
	    if ( newRating != null ) {
		rating.setRating(newRating);
		newRatings.remove(itemId);
		numRatings++;
		ratingDAO.saveOrUpdate(rating);
	    } else {
		rating.setRating((Float)null);
	    }
	}
	for ( Map.Entry<Long, Float> entry : newRatings.entrySet() ) {
	    Rating rating = new Rating();
	    rating.setItemId(entry.getKey());
	    rating.setLearner(learner);
	    rating.setRatingCriteria(ratingCriteria);
	    rating.setRating(entry.getValue());
	    ratingDAO.saveOrUpdate(rating);
	    numRatings++;
	}
	    
	return numRatings;
    }
    
    @Override
    public void commentItem(RatingCriteria ratingCriteria, Integer userId, Long itemId, String comment) {
	RatingComment ratingComment = ratingCommentDAO.getComment(ratingCriteria.getRatingCriteriaId(), userId, itemId);

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
    public List<ItemRatingDTO> getRatingCriteriaDtos(Long contentId, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {

	// fail safe - if there are no items, don't try to look anything up! LDEV-4094
	if ( itemIds == null || itemIds.isEmpty() ) {
	    return new LinkedList<ItemRatingDTO>();
	}

	//initial preparations
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	List<RatingCriteria> criterias = getCriteriasByToolContentId(contentId);
	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;

	//handle comments criteria
	List<ItemRatingDTO> itemDtos = handleCommentsCriteria(criterias, itemIds, isCommentsByOtherUsersRequired,
		userId);

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
		if (criteria.getRatingStyle() == 0) {
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

		if ( itemStatistics != null ) {
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
    private List<ItemRatingDTO> handleCommentsCriteria(List<RatingCriteria> criterias, Collection<Long> itemIds,
	    boolean isCommentsByOtherUsersRequired, Long userId) {

	boolean isSingleItem = itemIds.size() == 1;
	Long singleItemId = isSingleItem ? itemIds.iterator().next() : null;

	List<ItemRatingDTO> itemDtos = initializeItemDtos(itemIds);

	//handle comments criteria
	for (RatingCriteria criteria : criterias) {
	    if (criteria.isCommentRating()) {
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

		for (ItemRatingDTO itemDto : itemDtos) {
		    itemDto.setCommentsEnabled(true);
		    itemDto.setCommentsCriteriaId(commentCriteriaId);
		    itemDto.setCommentsMinWordsLimit(criteria.getCommentsMinWordsLimit());

		    //assign commentDtos by the appropriate items
		    List<RatingCommentDTO> commentDtosPerItem = new LinkedList<RatingCommentDTO>();
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
	List<ItemRatingDTO> itemDtos = new LinkedList<ItemRatingDTO>();
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

    @Override
    public RatingCriteria getCriteriaByCriteriaId(Long ratingCriteriaId, Class clasz) {
	return ratingCriteriaDAO.getByRatingCriteriaId(ratingCriteriaId, clasz);
    }

    @Override
    public void saveRatingCriterias(HttpServletRequest request, Collection<RatingCriteria> oldCriterias,
	    Long toolContentId) {
	
	// different handling for comments - simple tag sets the isCommentsEnabled flag,
	// the complex tag sends explicit comment type entry.
	boolean explicitCommentTypeFound = false;
	
	// create orderId to RatingCriteria map
	Map<Integer, RatingCriteria> mapOrderIdToRatingCriteria = new HashMap<Integer, RatingCriteria>();
	for (RatingCriteria ratingCriteriaIter : oldCriterias) {
	    mapOrderIdToRatingCriteria.put(ratingCriteriaIter.getOrderId(), ratingCriteriaIter);
	}
	
	for ( Map.Entry entry : request.getParameterMap().entrySet()) {
	    log.debug("entry: "+entry.getKey()+" "+entry.getValue());
	}
	
	int criteriaMaxOrderId = WebUtil.readIntParam(request, "criteriaMaxOrderId");
	// i is equal to an old orderId
	for (int i = 1; i <= criteriaMaxOrderId; i++) {

	    String criteriaTitle = WebUtil.readStrParam(request, "criteriaTitle" + i, true);

	    Integer ratingStyle = WebUtil.readIntParam(request,  "ratingStyle" + i, true);
	    if ( ratingStyle == null )
		ratingStyle = RatingCriteria.RATING_STYLE_STAR;
	    
	    Integer maxRating = WebUtil.readIntParam(request, "maxRating" + i, true);
	    if (maxRating == null) {
		switch (ratingStyle) {
		    case RatingCriteria.RATING_STYLE_STAR:
			maxRating = RatingCriteria.RATING_STYLE_STAR_DEFAULT_MAX;
			break;
		    case RatingCriteria.RATING_STYLE_RANKING:
			maxRating = RatingCriteria.RATING_STYLE_RANKING_DEFAULT_MAX;
			break;
		    case RatingCriteria.RATING_STYLE_HEDGING:
		    case RatingCriteria.RATING_STYLE_COMMENT:
			maxRating = 0;
			break;
		}
	    }

	    Integer minRatings = 0;
	    Integer maxRatings = 0;
	    if ( ratingStyle == RatingCriteria.RATING_STYLE_STAR || ratingStyle == RatingCriteria.RATING_STYLE_COMMENT ) {
		minRatings = WebUtil.readIntParam(request, "minimumRates" + i, true);
		maxRatings = WebUtil.readIntParam(request, "maximumRates" + i, true);
	    } 
	    
	    if ( ratingStyle == RatingCriteria.RATING_STYLE_COMMENT ) {
		explicitCommentTypeFound = true;
	    }

	    boolean commentsEnabled = ( ratingStyle != RatingCriteria.RATING_STYLE_COMMENT ? WebUtil.readBooleanParam(request,  "enableComments" + i, false) : true );
	    
	    RatingCriteria ratingCriteria = mapOrderIdToRatingCriteria.get(i);
	    if (StringUtils.isNotBlank(criteriaTitle)) {
		int newCriteriaOrderId = WebUtil.readIntParam(request, "criteriaOrderId" + i);

		// modify existing one if it exists. add otherwise
		if (ratingCriteria == null) {
		    ratingCriteria = new LearnerItemRatingCriteria();
		    ratingCriteria.setRatingCriteriaTypeId(RatingCriteria.LEARNER_ITEM_CRITERIA_TYPE);
		    ((LearnerItemRatingCriteria) ratingCriteria).setToolContentId(toolContentId);
		}

		ratingCriteria.setOrderId(newCriteriaOrderId);
		ratingCriteria.setTitle(criteriaTitle);
		ratingCriteria.setRatingStyle(ratingStyle);
		ratingCriteria.setMaxRating(maxRating);
		ratingCriteria.setCommentsEnabled(commentsEnabled);
		if ( commentsEnabled ) {
		    Integer commentsMinWordsLimit = WebUtil.readIntParam(request, "commentsMinWordsLimit" + i, true);
		    ratingCriteria.setCommentsMinWordsLimit(commentsMinWordsLimit != null ? commentsMinWordsLimit : 0);
		} else {
		    ratingCriteria.setCommentsMinWordsLimit(0);
		}

		ratingCriteria.setMinimumRates( minRatings );
		ratingCriteria.setMaximumRates( maxRatings );
		
    		ratingCriteriaDAO.saveOrUpdate(ratingCriteria);
		// !!updatedCriterias.add(ratingCriteria);

		// delete
	    } else if (ratingCriteria != null) {
		ratingCriteriaDAO.deleteRatingCriteria(ratingCriteria.getRatingCriteriaId());
	    }

	}

	// ==== handle comments criteria - simple tag support ====
	if ( ! explicitCommentTypeFound ) {
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
     *   tool item id (usually user id), rating.item_id, rating_comment.comment, (tool fields)+
     * Entries in Object array for other styles: 
     *   tool item id (usually user id), rating.item_id, rating_comment.comment,
     *   rating.rating, calculated.average_rating, calculated.count_vote, (tool fields)+
     */
    @Override
    public StyledCriteriaRatingDTO convertToStyledDTO(RatingCriteria ratingCriteria, Long currentUserId, boolean includeCurrentUser, 
	    List<Object[]> rawDataRows) {

	StyledCriteriaRatingDTO criteriaDto = new StyledCriteriaRatingDTO();
	criteriaDto.setRatingCriteria(ratingCriteria);

	if (ratingCriteria.isHedgeStyleRating() && ratingCriteria.isCommentsEnabled() ) {
	    RatingComment justification = ratingCommentDAO.getComment(ratingCriteria.getRatingCriteriaId(),
		    currentUserId.intValue(), ratingCriteria.getRatingCriteriaId());
	    if (justification != null)
		criteriaDto.setJustificationComment(justification.getComment());
	} else if ( ratingCriteria.isStarStyleRating() ) {
	    int ratedCount = getCountItemsRatedByUserByCriteria(ratingCriteria.getRatingCriteriaId(), currentUserId.intValue());
	    criteriaDto.setCountRatedItems(ratedCount);
	}

	boolean isComment = ratingCriteria.isCommentRating();
	if ( rawDataRows != null ) {

	    List<StyledRatingDTO> ratingDtos = new ArrayList<StyledRatingDTO>();
	    criteriaDto.setRatingDtos(ratingDtos);


	    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	    numberFormat.setMaximumFractionDigits(1);

	    for (Object[] row : rawDataRows) {
		int numColumns = row.length;
		if ( ( isComment && numColumns < 4 ) || ( !isComment && numColumns < 7) ) {
		    log.error("convertToStyledDTO: ratingCriteria" + ratingCriteria.getRatingCriteriaId() + " UserId: "
			    + currentUserId + "  Skipping data row as there are not enough columns. Only " + numColumns
			    + " columns. Data:" + row);
		    break;
		}

		long itemId = ((BigInteger) row[0]).longValue();
		if ( includeCurrentUser || itemId != currentUserId) {

		    if (row[1] != null && row[0] != row[1]) {
			log.error("convertToStyledDTO: ratingCriteria" + ratingCriteria.getRatingCriteriaId() + " UserId: "
				+ currentUserId + "  Potential issue: expected item id " + row[0] + " does match real item id"
				+ row[1] + ". Data: " + row);
		    }
		    
		    StyledRatingDTO dto = new StyledRatingDTO(((BigInteger) row[0]).longValue());
		    dto.setComment((String) row[2]);
		    dto.setItemDescription((String) row[numColumns - 1]);
		    if ( ! isComment ) {
			dto.setUserRating(row[3] == null ? "" : numberFormat.format((Float) row[3]));
			dto.setAverageRating(row[4] == null ? "" : numberFormat.format((Double) row[4]));
			dto.setNumberOfVotes(row[5] == null ? "" : numberFormat.format((BigInteger) row[5]));
		    }
		    ratingDtos.add(dto);
		}
	    }
	}

	return criteriaDto;
    }

    /**
     * Convert the raw data from the database to JSON, similar on StyledCriteriaRatingDTO and StyleRatingDTO. 
     * The rating service expects the potential itemId followed by rating.* (its own fields) and the last item 
     * in the array to be an item description (eg formatted user's name) Will go back to the database for the 
     * justification comment that would apply to hedging. 
     * 
     * If includeCurrentUser == true will include the current users' records (used for SelfReview and Monitoring)
     * otherwise skips the current user, so they do not rate themselves!
     * 
     * In JSON for use with tablesorter.
     * Entries in Object array for comment style: 
     *   tool item id (usually user id), rating.item_id, rating_comment.comment, (tool fields)+
     * Entries in Object array for other styles: 
     *   tool item id (usually user id), rating.item_id, rating_comment.comment,
     *   rating.rating, calculated.average_rating, calculated.count_vote, (tool fields)+
     * @throws JSONException 
     */
    @Override
    public JSONArray convertToStyledJSON(RatingCriteria ratingCriteria, Long currentUserId, boolean includeCurrentUser, 
	    List<Object[]> rawDataRows, boolean needRatesPerUser) throws JSONException {

	JSONArray rows = new JSONArray();
	boolean isComment = ratingCriteria.isCommentRating();

	if ( rawDataRows != null ) {

	    List<Long> itemIds = needRatesPerUser ? new ArrayList<Long>(rawDataRows.size()) : null;

	    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	    numberFormat.setMaximumFractionDigits(1);

	    for (Object[] row : rawDataRows) {
		int numColumns = row.length;
		if ( ( isComment && numColumns < 4 ) || ( !isComment && numColumns < 7) ) {
		    log.error("convertToStyledDTO: ratingCriteria" + ratingCriteria.getRatingCriteriaId() + " UserId: "
			    + currentUserId + "  Skipping data row as there are not enough columns. Only " + numColumns
			    + " columns. Data:" + row);
		    break;
		}
		
		Long itemId = ((BigInteger) row[0]).longValue();
		if ( includeCurrentUser || itemId != currentUserId) {

		    if (row[1] != null && row[0] != row[1]) {
			log.error("convertToStyledDTO: ratingCriteria" + ratingCriteria.getRatingCriteriaId() + " UserId: "
				+ currentUserId + "  Potential issue: expected item id " + row[0] + " does match real item id"
				+ row[1] + ". Data: " + row);
		    }
		    
		    JSONObject userRow = new JSONObject();
		    userRow.put("itemId", itemId);
		    userRow.put("comment", row[2] == null ? "" : (String) row[2]);
    		    userRow.put("itemDescription", row[numColumns - 1] == null ? "" : (String) row[numColumns - 1]);
		    if ( ! isComment ) {
        		    userRow.put("userRating", row[3] == null ? "" : numberFormat.format((Float) row[3]));
        		    userRow.put("averageRating",  row[4] == null ? "" : numberFormat.format((Double) row[4]));
        		    userRow.put("numberOfVotes", row[5] == null ? "" : numberFormat.format((BigInteger) row[5]));
		    } else {
			// don't have missing entries in JSON or an exception can occur if you try to access them
        		    userRow.put("userRating", "");
        		    userRow.put("averageRating", "");
        		    userRow.put("numberOfVotes", "");
		    }

		    rows.put(userRow);
		    
		    if ( needRatesPerUser )
			itemIds.add(itemId);
		}
	    }
	    
	    if ( needRatesPerUser ) {
		Map<Long, Long> countUsersRatedEachItemMap = ratingDAO.countUsersRatedEachItemByCriteria(ratingCriteria.getRatingCriteriaId(), itemIds, -1);
		for ( int i=0; i < rows.length(); i++ ) {
		    JSONObject row = rows.getJSONObject(i);
		    Long count = countUsersRatedEachItemMap.get(row.get("itemId"));
		    row.put("ratesPerUser", count != null ? count : 0);
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
