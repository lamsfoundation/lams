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


package org.lamsfoundation.lams.tool.forum.dto;

import java.util.Set;
import java.util.TreeSet;

import org.lamsfoundation.lams.learningdesign.dto.TextSearchConditionDTO;
import org.lamsfoundation.lams.tool.forum.model.ForumCondition;
import org.lamsfoundation.lams.tool.forum.model.Message;
import org.lamsfoundation.lams.tool.forum.util.ConditionTopicComparator;

public class ForumConditionDTO extends TextSearchConditionDTO {
    private Set<Message> topics = new TreeSet<Message>(new ConditionTopicComparator());

    public ForumConditionDTO(ForumCondition condition, Integer toolActivityUIID) {
	super(condition, toolActivityUIID);
	for (Message topic : condition.getTopics()) {
	    Message topicCopy = new Message();
	    topicCopy.setCreated(topic.getCreated());
	    topicCopy.setSubject(topic.getSubject());
	    topics.add(topicCopy);
	}
    }

    @Override
    public ForumCondition getCondition() {
	return new ForumCondition(this);
    }

    public Set<Message> getTopics() {
	return topics;
    }

    public void setTopics(Set<Message> topics) {
	this.topics = topics;
    }
}