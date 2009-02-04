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
package org.lamsfoundation.lams.tool.forum.web.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;
import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.Message;
import org.lamsfoundation.lams.web.planner.PedagogicalPlannerForm;

/**
 * @struts.form name="pedagogicalPlannerForm"
 */
public class ForumPedagogicalPlannerForm extends PedagogicalPlannerForm {
    private List<String> topic;
    private String contentFolderID;

    @Override
    public ActionMessages validate() {
	ActionMessages errors = new ActionMessages();
	boolean valid = true;
	boolean allEmpty = true;
	if (topic != null && !topic.isEmpty()) {
	    for (String item : topic) {
		if (!StringUtils.isEmpty(item)) {
		    allEmpty = false;
		    break;
		}
	    }
	}
	if (allEmpty) {
	    ActionMessage error = new ActionMessage("error.must.have.topic");
	    errors.add(ActionMessages.GLOBAL_MESSAGE, error);
	    valid = false;
	    topic = null;
	}

	setValid(valid);
	return errors;
    }

    public void fillForm(Forum forum) {
	if (forum != null) {
	    setToolContentID(forum.getContentId());

	    topic = new ArrayList<String>();
	    Set messages = forum.getMessages();
	    if (messages != null) {
		int topicIndex = 0;
		for (Message message : (Set<Message>) messages) {
		    setTopic(topicIndex++, message.getSubject());
		}
	    }
	}
    }

    public void setTopic(int number, String Topics) {
	if (topic == null) {
	    topic = new ArrayList<String>();
	}
	while (number >= topic.size()) {
	    topic.add(null);
	}
	topic.set(number, Topics);
    }

    public String getTopic(int number) {
	if (topic == null || number >= topic.size()) {
	    return null;
	}
	return topic.get(number);
    }

    public Integer getTopicCount() {
	return topic == null ? 0 : topic.size();
    }

    public boolean removeTopic(int number) {
	if (topic == null || number >= topic.size()) {
	    return false;
	}
	topic.remove(number);
	return true;
    }

    public String getContentFolderID() {
	return contentFolderID;
    }

    public void setContentFolderID(String contentFolderID) {
	this.contentFolderID = contentFolderID;
    }

    public List<String> getTopicList() {
	return topic;
    }
}