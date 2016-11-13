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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONException;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.rating.dto.ItemRatingCriteriaDTO;
import org.lamsfoundation.lams.rating.model.LearnerItemRatingCriteria;
import org.lamsfoundation.lams.rating.model.RatingCriteria;
import org.lamsfoundation.lams.rating.model.ToolActivityRatingCriteria;
import org.lamsfoundation.lams.rating.service.RatingService;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;
import org.lamsfoundation.lams.util.WebUtil;
import org.lamsfoundation.lams.web.session.SessionManager;
import org.lamsfoundation.lams.web.util.AttributeNames;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

/**
 * Stores rating.
 *
 * @author Andrey Balan
 *
 *
 *
 */
public class RatingServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RatingServlet.class);
    private static RatingService ratingService;

    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	JSONObject JSONObject = new JSONObject();
	getRatingService();

	String objectId = WebUtil.readStrParam(request, "idBox");
	Long ratingCriteriaId = Long.parseLong(objectId.split("-")[0]);
	Long itemId = Long.parseLong(objectId.split("-")[1]);
	RatingCriteria criteria = ratingService.getCriteriaByCriteriaId(ratingCriteriaId);

	UserDTO user = (UserDTO) SessionManager.getSession().getAttribute(AttributeNames.USER);
	Integer userId = user.getUserID();

	// get rating value as either float or comment String
	try {
	    boolean doSave = true;
	    boolean ratingLimitsByCriteria = WebUtil.readBooleanParam(request, "ratingLimitsByCriteria", false);

	    Long maxRatingsForItem = WebUtil.readLongParam(request, "maxRatingsForItem", true);
//	    log.debug("RatingServlet: Check Max rates for an item reached. Item " + itemId + " criteria id "
//		    + criteria.getRatingCriteriaId() + " maxRatingsForItem " + maxRatingsForItem);
	    if (maxRatingsForItem != null && maxRatingsForItem > 0) {
		if (!ToolActivityRatingCriteria.class.isInstance(criteria)) {
		    log.error(
			    "Unable to enforce max ratings on a non ToolActivityRatingCritera class. Need tool content id to do the db lookup!");
		} else {
		    ToolActivityRatingCriteria toolCriteria = (ToolActivityRatingCriteria) criteria;
		    List<Long> itemIds = new LinkedList<Long>();
		    itemIds.add(itemId);
		    Map<Long, Long> itemIdToRatedUsersCountMap = ratingLimitsByCriteria ? 
			ratingService.countUsersRatedEachItemByCriteria(ratingCriteriaId, itemIds, userId) :
			ratingService.countUsersRatedEachItem(toolCriteria.getToolContentId(), itemIds, userId);

		    Long currentRatings = itemIdToRatedUsersCountMap.get(itemId);
		    if (currentRatings != null && maxRatingsForItem.compareTo(currentRatings) <= 0) {
			JSONObject.put("error", true);
			JSONObject.put("message",
				"Maximum number of ratings for this item has been reached. No more may be saved.");
//			log.debug("RatingServlet: Max rates for an item reached. Item " + itemId + " criteria id "
//				+ criteria.getRatingCriteriaId() + " count " + currentRatings);
			doSave = false;
		    }
		}
	    }

	    if (doSave) {
		if (criteria.isCommentsEnabled()) {
		    // can have but do not have to have comment
		    String comment = WebUtil.readStrParam(request, "comment", true);
		    if ( comment != null ) {
        		    ratingService.commentItem(criteria, userId, itemId, comment);
        		    JSONObject.put("comment", StringEscapeUtils.escapeCsv(comment));
		    }
		} 
	
		String floatString = request.getParameter("rate");
		if ( floatString != null && floatString.length() > 0 ) {
		    float rating = Float.parseFloat(request.getParameter("rate"));

		    ItemRatingCriteriaDTO averageRatingDTO = ratingService.rateItem(criteria, userId, itemId, rating);

		    NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		    numberFormat.setMaximumFractionDigits(1);
		    JSONObject.put("userRating", numberFormat.format(rating));
		    JSONObject.put("averageRating", averageRatingDTO.getAverageRating());
		    JSONObject.put("numberOfVotes", averageRatingDTO.getNumberOfVotes());
		}

		boolean hasRatingLimits = WebUtil.readBooleanParam(request, "hasRatingLimits", false);

		// refresh countRatedItems in case there is rating limit set
		// Preview tool counts rated items on a criteria basis, other tools on a set of criteria basis!
		if (hasRatingLimits) {
		    // as long as this can be requested only for LEARNER_ITEM_CRITERIA_TYPE type, cast Criteria
		    LearnerItemRatingCriteria learnerItemRatingCriteria = (LearnerItemRatingCriteria) criteria;
		    Long toolContentId = learnerItemRatingCriteria.getToolContentId();

		    int countRatedItems = ratingLimitsByCriteria ? 
			ratingService.getCountItemsRatedByUserByCriteria(ratingCriteriaId, userId) :
			ratingService.getCountItemsRatedByUser(toolContentId, userId);
		    JSONObject.put("countRatedItems", countRatedItems);
		}
	    }
	} catch (JSONException e) {
	    throw new ServletException(e);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
	    throws ServletException, IOException {
	doGet(request, response);
    }

    private RatingService getRatingService() {
	if (ratingService == null) {
	    WebApplicationContext ctx = WebApplicationContextUtils
		    .getRequiredWebApplicationContext(getServletContext());
	    ratingService = (RatingService) ctx.getBean("ratingService");
	}
	return ratingService;
    }
}
