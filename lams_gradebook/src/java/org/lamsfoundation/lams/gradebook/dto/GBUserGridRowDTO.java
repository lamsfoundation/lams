/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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

package org.lamsfoundation.lams.gradebook.dto;

import java.util.ArrayList;

import org.lamsfoundation.lams.gradebook.util.GBGridView;
import org.lamsfoundation.lams.gradebook.util.GradebookUtil;
import org.lamsfoundation.lams.usermanagement.User;
import org.springframework.web.util.HtmlUtils;

public class GBUserGridRowDTO extends GradebookGridRowDTO {

    // For activity view
    // private Date startDate; defined in GradebookGridRowDTO
    private String activityUrl;

    // For excel export
    // private Date finishDate; defined in GradebookGridRowDTO
    private String firstName;
    private String lastName;
    private String login;
    private String currentActivity;
    private String portraitId;

    private boolean hasArchivedMarks;

    public GBUserGridRowDTO() {
    }

    public GBUserGridRowDTO(User user) {
	this.id = user.getUserId().toString();
	this.rowName = HtmlUtils.htmlEscape(user.getLastName() + ", " + user.getFirstName());
	this.firstName = user.getFirstName();
	this.lastName = user.getLastName();
	this.login = user.getLogin();
	this.setPortraitId(user.getPortraitUuid() == null ? null : user.getPortraitUuid().toString());
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<>();

	ret.add(id.toString());

	if (view == GBGridView.MON_USER) {

	    ret.add(rowName);
	    ret.add(status);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(startDate != null ? convertDateToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? convertDateToString(finishDate, null) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark, displayMarkAsPercent) : CELL_EMPTY);
	    ret.add(portraitId != null ? portraitId.toString() : "");
	    ret.add(String.valueOf(hasArchivedMarks));

	} else if (view == GBGridView.MON_ACTIVITY) {

	    ret.add(marksAvailable != null ? marksAvailable.toString() : "");
	    ret.add(rowName);
	    ret.add(status);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(startDate != null ? convertDateToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? convertDateToString(finishDate, null) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	    ret.add(portraitId != null ? portraitId.toString() : "");
	    if (activityUrl != null && activityUrl.length() != 0) {
		ret.add("javascript:launchPopup(\"" + activityUrl + "\",\"" + rowName + "\")'>");
	    }
	    ret.add(String.valueOf(hasArchivedMarks));

	} else if (view == GBGridView.MON_COURSE) {
	    ret.add(rowName);
	    ret.add(status);
	    ret.add((timeTaken != null) ? convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(startDate != null ? convertDateToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? convertDateToString(finishDate, null) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark, displayMarkAsPercent) : CELL_EMPTY);
	    ret.add(portraitId != null ? portraitId.toString() : "");

	} else if (view == GBGridView.LIST) {
	    ret.add(rowName);
	    ret.add(portraitId != null ? portraitId.toString() : "");

	}

	return ret;
    }

    public String getActivityUrl() {
	return activityUrl;
    }

    public void setActivityUrl(String activityUrl) {
	this.activityUrl = activityUrl;
    }

    public String getFirstName() {
	return firstName;
    }

    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    public String getLastName() {
	return lastName;
    }

    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    public String getLogin() {
	return login;
    }

    public void setLogin(String login) {
	this.login = login;
    }

    public String getCurrentActivity() {
	return currentActivity;
    }

    public void setCurrentActivity(String currentActivity) {
	this.currentActivity = currentActivity;
    }

    public String getPortraitId() {
	return portraitId;
    }

    public void setPortraitId(String portraitId) {
	this.portraitId = portraitId;
    }

    public boolean getHasArchivedMarks() {
	return hasArchivedMarks;
    }

    public void setHasArchivedMarks(boolean hasArchivedAttempts) {
	this.hasArchivedMarks = hasArchivedAttempts;
    }
}