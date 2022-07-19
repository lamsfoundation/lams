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

public class GBActivityArchiveGridRowDTO extends GradebookGridRowDTO {

    private Double lessonMark;

    public GBActivityArchiveGridRowDTO(int attemptNumber, Double lessonMark) {
	this.id = String.valueOf(attemptNumber);
	this.lessonMark = lessonMark;
    }

    @Override
    public ArrayList<String> toStringArray(GBGridView view) {
	ArrayList<String> ret = new ArrayList<>();
	if (view == GBGridView.MON_ACTIVITY) {
	    ret.add(id);
	    ret.add(feedback);
	    ret.add(lessonMark.toString());
	    ret.add(status);
	    ret.add(timeTaken != null ? DateUtil.convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(startDate != null ? DateUtil.convertToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? DateUtil.convertToString(finishDate, null) : CELL_EMPTY);
	    ret.add(mark != null ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	} else {
	    ret.add(id);
	    ret.add(status);
	    ret.add(timeTaken != null ? DateUtil.convertTimeToString(timeTaken) : CELL_EMPTY);
	    ret.add(startDate != null ? DateUtil.convertToString(startDate, null) : CELL_EMPTY);
	    ret.add(finishDate != null ? DateUtil.convertToString(finishDate, null) : CELL_EMPTY);
	    ret.add(feedback);
	    ret.add(lessonMark != null ? GradebookUtil.niceFormatting(lessonMark) : CELL_EMPTY);
	    ret.add(mark != null ? GradebookUtil.niceFormatting(mark) : CELL_EMPTY);
	}

	return ret;
    }
}