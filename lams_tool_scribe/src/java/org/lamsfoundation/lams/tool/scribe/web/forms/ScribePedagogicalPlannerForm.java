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

package org.lamsfoundation.lams.tool.scribe.web.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.lamsfoundation.lams.planner.PedagogicalPlannerActivitySpringForm;
import org.lamsfoundation.lams.tool.scribe.model.Scribe;
import org.lamsfoundation.lams.tool.scribe.model.ScribeHeading;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 */
public class ScribePedagogicalPlannerForm extends PedagogicalPlannerActivitySpringForm {
    private List<String> headings;
    private String contentFolderID;

    public MultiValueMap<String, String> validate() {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
	boolean valid = true;

	setValid(valid);
	return errorMap;
    }

    public void fillForm(Scribe scribe) {
	if (scribe != null) {
	    setToolContentID(scribe.getToolContentId());
	    headings = new ArrayList<>();
	    Set<ScribeHeading> scribeHeadings = scribe.getScribeHeadings();

	    if (scribeHeadings != null) {
		int headingIndex = 0;
		for (ScribeHeading heading : scribeHeadings) {
		    setHeading(headingIndex++, heading.getHeadingText());
		}
	    }
	}
    }

    public void setHeading(int number, String heading) {
	if (headings == null) {
	    headings = new ArrayList<>();
	}
	while (number >= headings.size()) {
	    headings.add(null);
	}
	headings.set(number, heading);
    }

    public String getHeading(int number) {
	if (headings == null || number >= headings.size()) {
	    return null;
	}
	return headings.get(number);
    }

    public Integer getHeadingCount() {
	return headings == null ? 0 : headings.size();
    }

    public boolean removeHeading(int number) {
	if (headings == null || number >= headings.size()) {
	    return false;
	}
	headings.remove(number);
	return true;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public List<String> getHeadingList() {
	return headings;
    }
}