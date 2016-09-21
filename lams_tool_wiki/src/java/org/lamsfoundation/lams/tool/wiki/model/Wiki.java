/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.wiki.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 *
 */

public class Wiki implements java.io.Serializable, Cloneable {

    /**
     *
     */
    private static final long serialVersionUID = 382787654329119829L;

    static Logger log = Logger.getLogger(Wiki.class.getName());

    // Fields
    /**
     *
     */
    private Long uid;

    private Date createDate;

    private Date updateDate;

    private Long createBy;

    private String title;

    private String instructions;

    // BEGIN ADVANCED OPTIONS------------------

    // Lock the wiki after learner is finished
    private boolean lockOnFinished;

    // Allow learners to create their own pages
    private boolean allowLearnerCreatePages;

    // Allow learners to insert external links into wiki
    private boolean allowLearnerInsertLinks;

    // Allow learners to attach images to the wiki page
    private boolean allowLearnerAttachImages;

    // Add notification for wiki updates
    private boolean notifyUpdates;

    // Add notebook at the end of activity
    private boolean reflectOnActivity;

    // instructions for notebook
    private String reflectInstructions;

    // Minimum number of edits a learner must do before finishing activity
    private Integer minimumEdits;

    // Maximum number of edits a learner can do for an activity
    private Integer maximumEdits;

    // END ADVANCED OPTIONS------------------

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;
    
	private Date submissionDeadline;

    private Set<WikiSession> wikiSessions;

    private Set<WikiPage> wikiPages;

    private WikiPage mainPage;

    // Property accessors
    /**
     *
     *
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     *
     *
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     *
     *
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     *
     *
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     *
     *
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     *
     *
     */

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     *
     *
     */
    public boolean isAllowLearnerCreatePages() {
	return allowLearnerCreatePages;
    }

    public void setAllowLearnerCreatePages(boolean allowLearnerCreatePages) {
	this.allowLearnerCreatePages = allowLearnerCreatePages;
    }

    /**
     *
     *
     */
    public boolean isAllowLearnerInsertLinks() {
	return allowLearnerInsertLinks;
    }

    public void setAllowLearnerInsertLinks(boolean allowLearnerInsertLinks) {
	this.allowLearnerInsertLinks = allowLearnerInsertLinks;
    }

    /**
     *
     *
     */
    public boolean isAllowLearnerAttachImages() {
	return allowLearnerAttachImages;
    }

    public void setAllowLearnerAttachImages(boolean allowLearnerAttachImages) {
	this.allowLearnerAttachImages = allowLearnerAttachImages;
    }

    /**
     *
     *
     */
    public boolean isNotifyUpdates() {
	return notifyUpdates;
    }

    public void setNotifyUpdates(boolean notifyUpdates) {
	this.notifyUpdates = notifyUpdates;
    }

    /**
     *
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     *
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     *
     */
    public Integer getMinimumEdits() {
	return minimumEdits;
    }

    public void setMinimumEdits(Integer minimumEdits) {
	this.minimumEdits = minimumEdits;
    }

    /**
     *
     */
    public Integer getMaximumEdits() {
	return maximumEdits;
    }

    public void setMaximumEdits(Integer maximumEdits) {
	this.maximumEdits = maximumEdits;
    }

    /**
     *
     *
     */

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     *
     *
     */

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     *
     *
     */

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     *
     *
     *
     */

    public Set<WikiSession> getWikiSessions() {
	return this.wikiSessions;
    }

    public void setWikiSessions(Set<WikiSession> wikiSessions) {
	this.wikiSessions = wikiSessions;
    }

    /**
     *
     * asc"
     *
     *
     *
     */

    public Set<WikiPage> getWikiPages() {
	return wikiPages;
    }

    public void setWikiPages(Set<WikiPage> wikiPages) {
	this.wikiPages = wikiPages;
    }

    /**
     *
     *
     *
     */
    public WikiPage getMainPage() {
	return mainPage;
    }

    public void setMainPage(WikiPage mainPage) {
	this.mainPage = mainPage;
    }
    
    
    /**
    *
    * @return
    */
    public Date getSubmissionDeadline() {
		return submissionDeadline;
	}
    

	public void setSubmissionDeadline(Date submissionDeadline) {
		this.submissionDeadline = submissionDeadline;
	}
    
    /**
     * toString
     *
     * @return String
     */
    @Override
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if ((other == null)) {
	    return false;
	}
	if (!(other instanceof Wiki)) {
	    return false;
	}
	Wiki castOther = (Wiki) other;

	return ((this.getUid() == castOther.getUid())
		|| (this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid())));
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Wiki newInstance(Wiki fromContent, Long toContentId) {
	Wiki toContent = new Wiki();
	toContent = (Wiki) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Wiki wiki = null;
	try {
	    wiki = (Wiki) super.clone();
	    wiki.setUid(null);

	    if (wikiPages != null) {
		Set<WikiPage> newPages = new HashSet<WikiPage>();
		for (WikiPage page : wikiPages) {
		    WikiPage newPage = (WikiPage) page.clone();
		    newPage.setParentWiki(wiki);
		    if (page.getTitle().equals(mainPage.getTitle())) {
			wiki.setMainPage(page);
		    }
		    newPages.add(newPage);
		}
		wiki.setWikiPages(newPages);
	    }

	    wiki.setMainPage((WikiPage) wiki.getMainPage().clone());

	    // create an empty set for the wikiSession
	    wiki.wikiSessions = new HashSet<WikiSession>();

	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + Wiki.class);
	}
	return wiki;
    }
}
