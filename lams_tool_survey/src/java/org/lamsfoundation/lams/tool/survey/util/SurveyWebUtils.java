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

package org.lamsfoundation.lams.tool.survey.util;

import org.lamsfoundation.lams.tool.survey.model.Survey;
import org.lamsfoundation.lams.tool.survey.web.controller.MonitoringController;

/**
 * Contains helper methods used by the Action Servlets
 *
 * @author Anthony Sukkar
 */
public class SurveyWebUtils {

    public static boolean isSurveyEditable(Survey survey) {
	if ((survey.isDefineLater() == true) && (survey.isContentInUse() == true)) {
	    // throw new
	    // SurveyApplicationException("An exception has occurred: There is a bug in this tool, conflicting flags are set");
	    MonitoringController.log
		    .error("An exception has occurred: There is a bug in this tool, conflicting flags are set");
	    return false;
	} else if ((survey.isDefineLater() == true) && (survey.isContentInUse() == false)) {
	    return true;
	} else if ((survey.isDefineLater() == false) && (survey.isContentInUse() == false)) {
	    return true;
	} else {
	    // (content.isContentInUse()==true && content.isDefineLater() == false)
	    return false;
	}
    }

    public static String getChoicesStr(String[] choiceList) {
	String choices = "";
	if (choiceList == null) {
	    return choices;
	}

	for (String c : choiceList) {
	    choices = choices + c + "&";
	}
	return choices;
    }

    public static String[] getChoiceList(String choiceList) {
	if (choiceList == null) {
	    return new String[] {};
	}

	return choiceList.split("&");
    }
}
