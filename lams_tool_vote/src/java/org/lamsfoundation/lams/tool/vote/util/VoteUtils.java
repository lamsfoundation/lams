/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.util;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.pojos.VoteContent;
import org.lamsfoundation.lams.tool.vote.service.IVoteService;

/**
 * Common Voting utility functions live here.
 * 
 * @author Ozgur Demirtas
 */
public abstract class VoteUtils implements VoteAppConstants {

    public static String stripHTML(String htmlText) {
	String noHTMLText = htmlText.replaceAll("\\<.*?\\>", "").replaceAll("&nbsp;", "")
		.replaceAll("&#[0-9][0-9][0-9][0-9];", "");
	String[] htmlTokens = noHTMLText.split("\n");
	String noHtmlNoNewLineStr = "";
	for (int i = 0; i < htmlTokens.length; i++) {
	    if (!htmlTokens[i].trim().equals("")) {
		noHtmlNoNewLineStr = noHtmlNoNewLineStr.length() > 0 ? noHtmlNoNewLineStr + " " + htmlTokens[i]
			: htmlTokens[i];
	    }
	}

	if (noHtmlNoNewLineStr.trim().length() == 0) {
	    // nomination text is just composed of html markup, try getting just a src entry for a picture otherwise
	    // give up
	    htmlText = htmlText.toLowerCase();
	    int index = htmlText.indexOf("src");
	    if (index > -1) {
		index = htmlText.indexOf("\"", index);
		if (index > -1 && index < htmlText.length()) {
		    int indexStop = htmlText.indexOf("\"", index + 1);
		    if (indexStop > -1) {
			String srcEntry = htmlText.substring(index + 1, indexStop);
			// get rid of any leading path and just get the filename;
			index = srcEntry.lastIndexOf("/");
			if (index == srcEntry.length() - 1)
			    index = srcEntry.lastIndexOf("/", index);
			if (index > -1) {
			    srcEntry = srcEntry.substring(index + 1);
			}
			return srcEntry;
		    }
		}
	    }
	    if (htmlText.length() > 50)
		return htmlText.substring(0, 51);
	    else
		return htmlText;
	}

	if (noHtmlNoNewLineStr.length() > 50)
	    return noHtmlNoNewLineStr.substring(0, 51);

	return noHtmlNoNewLineStr;
    }

    /**
     * removes attributes except USER_EXCEPTION_NO_STUDENT_ACTIVITY
     */
    public static void cleanUpUserExceptions(HttpServletRequest request) {
	request.getSession().removeAttribute(USER_EXCEPTION_WRONG_FORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_INCOMPATIBLE_IDS);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSION_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLCONTENT_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_LEARNER_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_TOOLSESSIONID_INCONSISTENT);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTAVAILABLE);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_NOTNUMERIC);
	request.getSession().removeAttribute(USER_EXCEPTION_ONLYCONTENT_ANDNOSESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_USERID_EXISTING);
	request.getSession().removeAttribute(USER_EXCEPTION_USER_DOESNOTEXIST);
	request.getSession().removeAttribute(USER_EXCEPTION_MONITORINGTAB_CONTENTID_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_DEFAULTCONTENT_NOTSETUP);
	request.getSession().removeAttribute(USER_EXCEPTION_NO_TOOL_SESSIONS);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_REQUIRED);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_IN_USE);
	request.getSession().removeAttribute(USER_EXCEPTION_CONTENT_BEING_MODIFIED);
	request.getSession().removeAttribute(USER_EXCEPTION_MODE_INVALID);
	request.getSession().removeAttribute(USER_EXCEPTION_QUESTION_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_ANSWER_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_ANSWERS_DUPLICATE);
	request.getSession().removeAttribute(USER_EXCEPTION_OPTIONS_COUNT_ZERO);
	request.getSession().removeAttribute(USER_EXCEPTION_CHKBOXES_EMPTY);
	request.getSession().removeAttribute(USER_EXCEPTION_SUBMIT_NONE);
	request.getSession().removeAttribute(USER_EXCEPTION_NUMBERFORMAT);
	request.getSession().removeAttribute(USER_EXCEPTION_WEIGHT_MUST_EQUAL100);
	request.getSession().removeAttribute(USER_EXCEPTION_SINGLE_OPTION);
    }

    public static void setDefineLater(HttpServletRequest request, boolean value, String strToolContentID,
	    IVoteService voteService) {

	VoteContent voteContent = voteService.getVoteContent(new Long(strToolContentID));

	if (voteContent != null) {
	    voteContent.setDefineLater(value);
	    voteService.updateVote(voteContent);
	}
    }

}
