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

/* $Id$ */
package org.lamsfoundation.lams.web;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;

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
 * @web:servlet name="ratingServlet"
 * @web:servlet-mapping url-pattern="/servlet/rateItem"
 */
public class RatingServlet extends HttpServlet {

    private static Logger log = Logger.getLogger(RatingServlet.class);
    private static RatingService ratingService;

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
	    if (criteria.isCommentsEnabled()) {
		String comment = WebUtil.readStrParam(request, "comment");
		ratingService.commentItem(criteria, userId, itemId, comment);
		JSONObject.put("comment", StringEscapeUtils.escapeCsv(comment));

	    } else {
		float rating = Float.parseFloat((String) request.getParameter("rate"));


		ItemRatingCriteriaDTO averageRatingDTO = ratingService.rateItem(criteria, userId, itemId, rating);

		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		numberFormat.setMaximumFractionDigits(1);
		JSONObject.put("userRating", numberFormat.format(rating));
		JSONObject.put("averageRating", averageRatingDTO.getAverageRating());
		JSONObject.put("numberOfVotes", averageRatingDTO.getNumberOfVotes());
	    }
	    

	    boolean hasRatingLimits = WebUtil.readBooleanParam(request, "hasRatingLimits", false);

	    // refresh countRatedItems in case there is rating limit set
	    if (hasRatingLimits) {
		// as long as this can be requested only for LEARNER_ITEM_CRITERIA_TYPE type, cast Criteria
		LearnerItemRatingCriteria learnerItemRatingCriteria = (LearnerItemRatingCriteria) criteria;
		Long toolContentId = learnerItemRatingCriteria.getToolContentId();

		int countRatedItems = ratingService.getCountItemsRatedByUser(toolContentId, userId);
		JSONObject.put("countRatedItems", countRatedItems);
	    }
	    
	} catch (JSONException e) {
	    throw new ServletException(e);
	}

	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
	    IOException {
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
