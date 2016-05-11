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


package org.lamsfoundation.lams.rest;

import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.tomcat.util.json.JSONObject;
import org.lamsfoundation.lams.learningdesign.LearningDesign;
import org.lamsfoundation.lams.usermanagement.dto.UserDTO;

public class LearningDesignRestServlet extends RestServlet {
    private static final Logger log = Logger.getLogger(LearningDesignRestServlet.class);

    @Override
    protected void doPostInternal(JSONObject requestJSON, UserDTO userDTO, HttpServletResponse response)
	    throws Exception {
	JSONObject learningDesignJSON = requestJSON.getJSONObject("ld");
	LearningDesign learningDesign = getAuthoringService().saveLearningDesignDetails(learningDesignJSON);

	JSONObject JSONObject = new JSONObject();
	JSONObject.put("learningDesignID", learningDesign.getLearningDesignId());
	JSONObject.put("title", learningDesign.getTitle());
	response.setContentType("application/json;charset=utf-8");
	response.getWriter().print(JSONObject);
    }
}