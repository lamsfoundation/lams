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

package org.lamsfoundation.lams.tool.wiki.web.forms;

import org.lamsfoundation.lams.planner.PedagogicalPlannerActivitySpringForm;
import org.lamsfoundation.lams.tool.wiki.model.Wiki;
import org.lamsfoundation.lams.tool.wiki.model.WikiPage;

/**
 *
 */
public class WikiPedagogicalPlannerForm extends PedagogicalPlannerActivitySpringForm {
    String title;
    String wikiBody;
    String contentFolderID;

    public String getWikiBody() {
	return wikiBody;
    }

    public void setWikiBody(String body) {
	this.wikiBody = body;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public void fillForm(Wiki wiki) {
	if (wiki != null) {
	    WikiPage page = wiki.getWikiPages().iterator().next();
	    setWikiBody(page.getCurrentWikiContent().getBody());
	    setTitle(page.getTitle());
	    setToolContentID(wiki.getToolContentId());
	}
    }

//    @Override
//    public ActionMessages validate() {
//	ActionMessages errors = new ActionMessages();
//	boolean valid = true;
//	if (StringUtils.isEmpty(getTitle())) {
//	    ActionMessage error = new ActionMessage("label.wiki.add.title.required");
//	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
//	    valid = false;
//	}
//	setValid(valid);
//	return errors;
//    }
}