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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;

import org.apache.log4j.Logger;

@Entity
@Table(name = "tl_lawiki10_wiki")
public class Wiki implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 382787654329119829L;

    static Logger log = Logger.getLogger(Wiki.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "create_date")
    private Date createDate;

    @Column(name = "update_date")
    private Date updateDate;

    @Column(name = "create_by")
    private Long createBy;

    @Column
    private String title;

    @Column
    private String instructions;

    // BEGIN ADVANCED OPTIONS------------------

    // Lock the wiki after learner is finished
    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    // Allow learners to create their own pages
    @Column(name = "allow_learner_create_pages")
    private boolean allowLearnerCreatePages;

    // Allow learners to insert external links into wiki
    @Column(name = "allow_learner_insert_links")
    private boolean allowLearnerInsertLinks;

    // Allow learners to attach images to the wiki page
    @Column(name = "allow_learner_attach_images")
    private boolean allowLearnerAttachImages;

    // Add notification for wiki updates
    @Column(name = "notify_updates")
    private boolean notifyUpdates;

    // Minimum number of edits a learner must do before finishing activity
    @Column(name = "minimum_edits")
    private Integer minimumEdits;

    // Maximum number of edits a learner can do for an activity
    @Column(name = "maximum_edits")
    private Integer maximumEdits;

    // END ADVANCED OPTIONS------------------

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @OneToMany(mappedBy = "wiki")
    private Set<WikiSession> wikiSessions;

    @OneToMany(mappedBy = "parentWiki")
    @OrderBy("uid ASC")
    private Set<WikiPage> wikiPages;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_main_page_uid")
    private WikiPage mainPage;

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public boolean isAllowLearnerCreatePages() {
	return allowLearnerCreatePages;
    }

    public void setAllowLearnerCreatePages(boolean allowLearnerCreatePages) {
	this.allowLearnerCreatePages = allowLearnerCreatePages;
    }

    public boolean isAllowLearnerInsertLinks() {
	return allowLearnerInsertLinks;
    }

    public void setAllowLearnerInsertLinks(boolean allowLearnerInsertLinks) {
	this.allowLearnerInsertLinks = allowLearnerInsertLinks;
    }

    public boolean isAllowLearnerAttachImages() {
	return allowLearnerAttachImages;
    }

    public void setAllowLearnerAttachImages(boolean allowLearnerAttachImages) {
	this.allowLearnerAttachImages = allowLearnerAttachImages;
    }

    public boolean isNotifyUpdates() {
	return notifyUpdates;
    }

    public void setNotifyUpdates(boolean notifyUpdates) {
	this.notifyUpdates = notifyUpdates;
    }

    public Integer getMinimumEdits() {
	return minimumEdits;
    }

    public void setMinimumEdits(Integer minimumEdits) {
	this.minimumEdits = minimumEdits;
    }

    public Integer getMaximumEdits() {
	return maximumEdits;
    }

    public void setMaximumEdits(Integer maximumEdits) {
	this.maximumEdits = maximumEdits;
    }

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public Set<WikiSession> getWikiSessions() {
	return this.wikiSessions;
    }

    public void setWikiSessions(Set<WikiSession> wikiSessions) {
	this.wikiSessions = wikiSessions;
    }

    public Set<WikiPage> getWikiPages() {
	return wikiPages;
    }

    public void setWikiPages(Set<WikiPage> wikiPages) {
	this.wikiPages = wikiPages;
    }

    public WikiPage getMainPage() {
	return mainPage;
    }

    public void setMainPage(WikiPage mainPage) {
	this.mainPage = mainPage;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

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
