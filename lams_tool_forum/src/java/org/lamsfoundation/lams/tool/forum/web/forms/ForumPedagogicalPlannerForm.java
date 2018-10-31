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

package org.lamsfoundation.lams.tool.forum.web.forms;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang.StringUtils;
import org.lamsfoundation.lams.planner.PedagogicalPlannerActivitySpringForm;
import org.lamsfoundation.lams.tool.forum.model.Forum;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.util.MessageComparator;
import org.lamsfoundation.lams.util.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

/**
 *
 */
public class ForumPedagogicalPlannerForm extends PedagogicalPlannerActivitySpringForm {

    @Autowired
    @Qualifier("forumMessageService")
    private MessageService messageService;

    private List<String> topic;
    private String contentFolderID;
    private String instructions;

    public MultiValueMap<String, String> validate() {
	MultiValueMap<String, String> errorMap = new LinkedMultiValueMap<>();
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
	    errorMap.add("GLOBAL", messageService.getMessage("error.must.have.topic"));
	    valid = false;
	    topic = null;
	}

	setValid(valid);
	return errorMap;
    }

    public void fillForm(Forum forum) {
	if (forum != null) {
	    setToolContentID(forum.getContentId());
	    setInstructions(forum.getInstructions());

	    topic = new ArrayList<>();
	    Set<Message> messages = new TreeSet<>(new MessageComparator());
	    messages.addAll(forum.getMessages());
	    if (messages != null) {
		int topicIndex = 0;
		for (Message message : messages) {
		    if (message.getIsAuthored() && message.getToolSession() == null) {
			setTopic(topicIndex++, message.getBody());
		    }
		}
	    }
	}
    }

    public void setTopic(int number, String Topics) {
	if (topic == null) {
	    topic = new ArrayList<>();
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

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }
}