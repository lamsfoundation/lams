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
/* $$Id$$ */

package org.lamsfoundation.lams.tool.wiki.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.contentrepository.client.IToolContentHandler;

/**
 * @hibernate.class table="tl_lawiki10_wiki"
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

    private boolean runOffline;

    // BEGIN ADVANCED OPTIONS------------------

    // Lock the wiki after learner is finished
    private boolean lockOnFinished;

    // Allow learners to create their own pages
    private boolean allowLearnerCreatePages;

    // Allow learners to insert external links into wiki
    private boolean allowLearnerInsertLinks;

    // Allow learners to attach images to the wiki page
    private boolean allowLearnerAttachImages;

    // Add notebook at the end of activity
    private boolean reflectOnActivity;

    // instructions for notebook
    private String reflectInstructions;

    // Minimum number of edits a learner must do before finishing activity
    private Integer minimumEdits;

    // Maximum number of edits a learner can do for an activity
    private Integer maximumEdits;

    // END ADVANCED OPTIONS------------------

    private String onlineInstructions;

    private String offlineInstructions;

    private boolean contentInUse;

    private boolean defineLater;

    private Long toolContentId;

    private Set<WikiAttachment> wikiAttachments;

    private Set<WikiSession> wikiSessions;

    private Set<WikiPage> wikiPages;

    private WikiPage mainPage;

    // *********** NON Persist fields
    private IToolContentHandler toolContentHandler;

    // Constructors

    /** default constructor */
    public Wiki() {
    }

    /** full constructor */
    public Wiki(Date createDate, Date updateDate, Long createBy, String title, String instructions, boolean runOffline,
	    boolean lockOnFinished, boolean filteringEnabled, String filterKeywords, String onlineInstructions,
	    String offlineInstructions, boolean contentInUse, boolean defineLater, Long toolContentId,
	    Set<WikiAttachment> wikiAttachments, Set<WikiSession> wikiSessions, Set<WikiPage> wikiPages) {
	this.createDate = createDate;
	this.updateDate = updateDate;
	this.createBy = createBy;
	this.title = title;
	this.instructions = instructions;
	this.runOffline = runOffline;
	this.lockOnFinished = lockOnFinished;
	this.onlineInstructions = onlineInstructions;
	this.offlineInstructions = offlineInstructions;
	this.contentInUse = contentInUse;
	this.defineLater = defineLater;
	this.toolContentId = toolContentId;
	this.wikiAttachments = wikiAttachments;
	this.wikiSessions = wikiSessions;
	this.wikiPages = wikiPages;
    }

    // Property accessors
    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     * 
     */

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.property column="create_date"
     * 
     */

    public Date getCreateDate() {
	return this.createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    /**
     * @hibernate.property column="update_date"
     * 
     */

    public Date getUpdateDate() {
	return this.updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    /**
     * @hibernate.property column="create_by" length="20"
     * 
     */

    public Long getCreateBy() {
	return this.createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    /**
     * @hibernate.property column="title" length="255"
     * 
     */

    public String getTitle() {
	return this.title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="instructions" length="65535"
     * 
     */

    public String getInstructions() {
	return this.instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    /**
     * @hibernate.property column="run_offline" length="1"
     * 
     */

    public boolean isRunOffline() {
	return this.runOffline;
    }

    public void setRunOffline(boolean runOffline) {
	this.runOffline = runOffline;
    }

    /**
     * @hibernate.property column="lock_on_finished" length="1"
     * 
     */

    public boolean isLockOnFinished() {
	return this.lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    /**
     * @hibernate.property column="allow_learner_create_pages" length="1"
     * 
     */
    public boolean isAllowLearnerCreatePages() {
	return allowLearnerCreatePages;
    }

    public void setAllowLearnerCreatePages(boolean allowLearnerCreatePages) {
	this.allowLearnerCreatePages = allowLearnerCreatePages;
    }

    /**
     * @hibernate.property column="allow_learner_insert_links" length="1"
     * 
     */
    public boolean isAllowLearnerInsertLinks() {
	return allowLearnerInsertLinks;
    }

    public void setAllowLearnerInsertLinks(boolean allowLearnerInsertLinks) {
	this.allowLearnerInsertLinks = allowLearnerInsertLinks;
    }

    /**
     * @hibernate.property column="allow_learner_attach_images" length="1"
     * 
     */
    public boolean isAllowLearnerAttachImages() {
	return allowLearnerAttachImages;
    }

    public void setAllowLearnerAttachImages(boolean allowLearnerAttachImages) {
	this.allowLearnerAttachImages = allowLearnerAttachImages;
    }

    /**
     * @hibernate.property column="reflect_on_activity" length="1"
     */
    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    /**
     * @hibernate.property column="reflect_instructions" length="65535"
     */
    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    /**
     * @hibernate.property column="minimum_edits"
     */
    public Integer getMinimumEdits() {
	return minimumEdits;
    }

    public void setMinimumEdits(Integer minimumEdits) {
	this.minimumEdits = minimumEdits;
    }

    /**
     * @hibernate.property column="maximum_edits"
     */
    public Integer getMaximumEdits() {
	return maximumEdits;
    }

    public void setMaximumEdits(Integer maximumEdits) {
	this.maximumEdits = maximumEdits;
    }

    /**
     * @hibernate.property column="online_instructions" length="65535"
     * 
     */

    public String getOnlineInstructions() {
	return this.onlineInstructions;
    }

    public void setOnlineInstructions(String onlineInstructions) {
	this.onlineInstructions = onlineInstructions;
    }

    /**
     * @hibernate.property column="offline_instructions" length="65535"
     * 
     */

    public String getOfflineInstructions() {
	return this.offlineInstructions;
    }

    public void setOfflineInstructions(String offlineInstructions) {
	this.offlineInstructions = offlineInstructions;
    }

    /**
     * @hibernate.property column="content_in_use" length="1"
     * 
     */

    public boolean isContentInUse() {
	return this.contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    /**
     * @hibernate.property column="define_later" length="1"
     * 
     */

    public boolean isDefineLater() {
	return this.defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    /**
     * @hibernate.property column="tool_content_id" length="20"
     * 
     */

    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     * @hibernate.set lazy="true" inverse="false" cascade="all-delete-orphan"
     * @hibernate.collection-key column="wiki_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wiki.model.WikiAttachment"
     * 
     */

    public Set<WikiAttachment> getWikiAttachments() {
	return this.wikiAttachments;
    }

    public void setWikiAttachments(Set<WikiAttachment> wikiAttachments) {
	this.wikiAttachments = wikiAttachments;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none"
     * @hibernate.collection-key column="wiki_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wiki.model.WikiSession"
     * 
     */

    public Set<WikiSession> getWikiSessions() {
	return this.wikiSessions;
    }

    public void setWikiSessions(Set<WikiSession> wikiSessions) {
	this.wikiSessions = wikiSessions;
    }

    /**
     * @hibernate.set lazy="true" inverse="true" cascade="none" order-by="uid
     *                asc"
     * @hibernate.collection-key column="wiki_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wiki.model.WikiPage"
     * 
     */

    public Set<WikiPage> getWikiPages() {
	return wikiPages;
    }

    public void setWikiPages(Set<WikiPage> wikiPages) {
	this.wikiPages = wikiPages;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="wiki_main_page_uid"
     * 
     */
    public WikiPage getMainPage() {
	return mainPage;
    }

    public void setMainPage(WikiPage mainPage) {
	this.mainPage = mainPage;
    }

    /**
     * toString
     * 
     * @return String
     */
    public String toString() {
	StringBuffer buffer = new StringBuffer();

	buffer.append(getClass().getName()).append("@").append(Integer.toHexString(hashCode())).append(" [");
	buffer.append("title").append("='").append(getTitle()).append("' ");
	buffer.append("instructions").append("='").append(getInstructions()).append("' ");
	buffer.append("toolContentId").append("='").append(getToolContentId()).append("' ");
	buffer.append("]");

	return buffer.toString();
    }

    public boolean equals(Object other) {
	if ((this == other))
	    return true;
	if ((other == null))
	    return false;
	if (!(other instanceof Wiki))
	    return false;
	Wiki castOther = (Wiki) other;

	return ((this.getUid() == castOther.getUid()) || (this.getUid() != null && castOther.getUid() != null && this
		.getUid().equals(castOther.getUid())));
    }

    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Wiki newInstance(Wiki fromContent, Long toContentId, IToolContentHandler wikiToolContentHandler) {
	Wiki toContent = new Wiki();
	fromContent.toolContentHandler = wikiToolContentHandler;
	toContent = (Wiki) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    protected Object clone() {

	Wiki wiki = null;
	try {
	    wiki = (Wiki) super.clone();
	    wiki.setUid(null);

	    if (wikiAttachments != null) {
		// create a copy of the attachments
		Iterator<WikiAttachment> iter = wikiAttachments.iterator();
		Set<WikiAttachment> set = new HashSet<WikiAttachment>();
		while (iter.hasNext()) {
		    WikiAttachment originalFile = (WikiAttachment) iter.next();
		    WikiAttachment newFile = (WikiAttachment) originalFile.clone();
		    set.add(newFile);
		}
		wiki.wikiAttachments = set;
	    }

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
	    
	    wiki.setMainPage((WikiPage)wiki.getMainPage().clone());
	    

	    // create an empty set for the wikiSession
	    wiki.wikiSessions = new HashSet<WikiSession>();

	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + Wiki.class);
	}
	return wiki;
    }

    public IToolContentHandler getToolContentHandler() {
	return toolContentHandler;
    }

    public void setToolContentHandler(IToolContentHandler toolContentHandler) {
	this.toolContentHandler = toolContentHandler;
    }
}
