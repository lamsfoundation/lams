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
import org.lamsfoundation.lams.tool.vote.model.VoteContent;
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
			if (index == srcEntry.length() - 1) {
			    index = srcEntry.lastIndexOf("/", index);
			}
			if (index > -1) {
			    srcEntry = srcEntry.substring(index + 1);
			}
			return srcEntry;
		    }
		}
	    }
	    if (htmlText.length() > 50) {
		return htmlText.substring(0, 51);
	    } else {
		return htmlText;
	    }
	}

	if (noHtmlNoNewLineStr.length() > 50) {
	    return noHtmlNoNewLineStr.substring(0, 51);
	}

	return noHtmlNoNewLineStr;
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
