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
import org.lamsfoundation.lams.util.DateUtil;
import org.springframework.web.util.HtmlUtils;

public class GBLessonGridRowDTO extends GradebookGridRowDTO {

    public static final String VIEW_MONITOR = "monitorView";
    public static final String VIEW_LEARNER = "learnerView";

    private String subGroup;
    // private String startDate;  defined in GradebookGridRowDTO

    // Only for monitor view
    private String gradebookMonitorURL;

    // Only for learner view
    private String gradebookLearnerURL;
    // private String finishDate;  defined in GradebookGridRowDTO

    public GBLessonGridRowDTO() {
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<>();

	ret.add(id.toString());

	rowName = HtmlUtils.htmlEscape(rowName);

	if (view == GBGridView.MON_COURSE) {
	    if (gradebookMonitorURL != null && gradebookMonitorURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradebookMonitorURL + "\",\"" + rowName + "\")'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(subGroup);
	    ret.add(startDate != null ? DateUtil.convertToString(startDate, null) : CELL_EMPTY);
	    ret.add((medianTimeTaken != null && medianTimeTaken != 0) ? DateUtil.convertTimeToString(medianTimeTaken)
		    : CELL_EMPTY);
	    ret.add((averageMark != null) ? GradebookUtil.niceFormatting(averageMark, displayMarkAsPercent)
		    : CELL_EMPTY);

	} else if ((view == GBGridView.LRN_COURSE) || (view == GBGridView.MON_USER)) {
	    if (gradebookLearnerURL != null && gradebookLearnerURL.length() != 0) {
		ret.add("<a href='javascript:launchPopup(\"" + gradebookLearnerURL + "\",\"" + rowName + "\")'>"
			+ rowName + "</a>");
	    } else {
		ret.add(rowName);
	    }
	    ret.add(subGroup);
	    ret.add((status != null) ? status : CELL_EMPTY);
	    ret.add(startDate != null ? DateUtil.convertToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? DateUtil.convertToString(finishDate, null) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add((medianTimeTaken != null && medianTimeTaken != 0)
		    ? toItalic(DateUtil.convertTimeToString(medianTimeTaken))
		    : CELL_EMPTY);
	    ret.add((timeTaken != null) ? DateUtil.convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add((averageMark != null) ? toItalic(GradebookUtil.niceFormatting(averageMark, displayMarkAsPercent))
		    : CELL_EMPTY);
	    ret.add((mark != null) ? GradebookUtil.niceFormatting(mark, displayMarkAsPercent) : CELL_EMPTY);

	    //plain lesson list case
	} else if (view == GBGridView.LIST) {
	    ret.add(rowName);
	}
	return ret;
    }

    public String getLessonName() {
	return rowName;
    }

    public void setLessonName(String rowName) {
	this.rowName = rowName;
    }

    public String getGradebookMonitorURL() {
	return gradebookMonitorURL;
    }

    public void setGradebookMonitorURL(String gradebookMonitorURL) {
	this.gradebookMonitorURL = gradebookMonitorURL;
    }

    public String getGradebookLearnerURL() {
	return gradebookLearnerURL;
    }

    public void setGradebookLearnerURL(String gradebookLearnerURL) {
	this.gradebookLearnerURL = gradebookLearnerURL;
    }

    public String getSubGroup() {
	return subGroup;
    }

    public void setSubGroup(String subGroup) {
	this.subGroup = subGroup;
    }

}
