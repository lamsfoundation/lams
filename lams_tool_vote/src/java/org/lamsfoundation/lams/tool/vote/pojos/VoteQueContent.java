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

package org.lamsfoundation.lams.tool.vote.pojos;

import java.io.Serializable;

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Persistent object/bean that defines the question content for the Voting tool. Provides accessors and mutators to
 * get/set attributes It maps to database table: tl_lavote11_nomination_content
 *
 * @author Ozgur Demirtas
 */
public class VoteQueContent implements Serializable, Comparable<VoteQueContent> {

    /**
     *
     */
    private static final long serialVersionUID = 1598466582549757720L;

    /** identifier field */
    private Long uid;

    /** nullable persistent field */
    private String question;

    private int displayOrder;

    /** non persistent field */
    private Long voteContentId;

    /** persistent field */
    private org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent;

    public VoteQueContent(String question, VoteContent voteContent) {
	this.question = question;
	this.voteContent = voteContent;
    }

    public VoteQueContent(String question, int displayOrder, VoteContent voteContent) {
	this.question = question;
	this.displayOrder = displayOrder;
	this.voteContent = voteContent;
    }

    /** default constructor */
    public VoteQueContent() {
    }

    /**
     * gets called by copyToolContent
     *
     * Copy constructor
     *
     * @param queContent
     *            the original qa question content
     * @return the new qa question content object
     */
    public static VoteQueContent newInstance(VoteQueContent queContent, int displayOrder, VoteContent newMcContent) {
	VoteQueContent newQueContent = new VoteQueContent(queContent.getQuestion(), displayOrder, newMcContent);

	return newQueContent;
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public String getQuestion() {
	return this.question;
    }

    public void setQuestion(String question) {
	this.question = question;
    }

    public org.lamsfoundation.lams.tool.vote.pojos.VoteContent getMcContent() {
	return this.voteContent;
    }

    public void setMcContent(org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent) {
	this.voteContent = voteContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    @Override
    public int compareTo(VoteQueContent queContent) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - queContent.uid.longValue());
	}
    }

    /**
     * @return Returns the voteContentId.
     */
    public Long getVoteContentId() {
	return voteContentId;
    }

    /**
     * @param voteContentId
     *            The voteContentId to set.
     */
    public void setVoteContentId(Long voteContentId) {
	this.voteContentId = voteContentId;
    }

    /**
     * @return Returns the voteContent.
     */
    public org.lamsfoundation.lams.tool.vote.pojos.VoteContent getVoteContent() {
	return voteContent;
    }

    /**
     * @param voteContent
     *            The voteContent to set.
     */
    public void setVoteContent(org.lamsfoundation.lams.tool.vote.pojos.VoteContent voteContent) {
	this.voteContent = voteContent;
    }

    /**
     * @return Returns the displayOrder.
     */
    public int getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(int displayOrder) {
	this.displayOrder = displayOrder;
    }
}
