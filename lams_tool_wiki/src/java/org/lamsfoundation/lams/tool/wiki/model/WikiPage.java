package org.lamsfoundation.lams.tool.wiki.model;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

/**
 * Each instance of WikiPage represents a page on the wiki (can be the main
 * page) Each WikiPage instance has 0 to many edits, and therefore has 0 to many
 * WikiContent instances
 *
 * @author lfoxton
 * @hibernate.class table="tl_lawiki10_wiki_page"
 */
public class WikiPage implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = 2980989002817635889L;

    private static Logger log = Logger.getLogger(WikiPage.class.getName());

    private Long uid;
    private Wiki parentWiki;
    private String title;
    private Boolean editable;
    private Boolean deleted;
    private Set<WikiPageContent> wikiContentVersions;
    private WikiPageContent currentWikiContent;
    private WikiSession wikiSession;
    private WikiUser addedBy;

    public WikiPage() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="wiki_uid"
     *
     */
    public Wiki getParentWiki() {
	return parentWiki;
    }

    public void setParentWiki(Wiki parentWiki) {
	this.parentWiki = parentWiki;
    }

    /**
     * @hibernate.property column="title" length="255"
     *
     */
    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @hibernate.property column="editable" length="1"
     *
     */
    public Boolean getEditable() {
	return editable;
    }

    public void setEditable(Boolean editable) {
	this.editable = editable;
    }

    /**
     * @hibernate.property column="deleted" length="1"
     *
     */
    public Boolean getDeleted() {
	return deleted;
    }

    public void setDeleted(Boolean deleted) {
	this.deleted = deleted;
    }

    /**
     * @hibernate.set lazy="true" inverse="false" cascade="all-delete-orphan"
     *                order-by="uid asc"
     * @hibernate.collection-key column="wiki_page_uid"
     * @hibernate.collection-one-to-many class="org.lamsfoundation.lams.tool.wiki.model.WikiPageContent"
     *
     */
    public Set<WikiPageContent> getWikiContentVersions() {
	return wikiContentVersions;
    }

    public void setWikiContentVersions(Set<WikiPageContent> wikiContentVersions) {
	this.wikiContentVersions = wikiContentVersions;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="wiki_current_content"
     *
     */
    public WikiPageContent getCurrentWikiContent() {
	return currentWikiContent;
    }

    public void setCurrentWikiContent(WikiPageContent currentWikiContent) {
	this.currentWikiContent = currentWikiContent;
    }

    /**
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="added_by"
     *
     */
    public WikiUser getAddedBy() {
	return addedBy;
    }

    public void setAddedBy(WikiUser addedBy) {
	this.addedBy = addedBy;
    }

    /**
     * Gets the toolSession
     *
     * @hibernate.many-to-one cascade="none"
     *                        class="org.lamsfoundation.lams.tool.wiki.model.WikiSession"
     *                        column="wiki_session_uid"
     *
     */
    public WikiSession getWikiSession() {
	return wikiSession;
    }

    public void setWikiSession(WikiSession wikiSession) {
	this.wikiSession = wikiSession;
    }

    @Override
    public Object clone() {

	WikiPage wikiPage = null;
	try {
	    wikiPage = (WikiPage) super.clone();
	    wikiPage.setUid(null);
	    wikiPage.setWikiSession(null);
	    wikiPage.setAddedBy(null);
	    WikiPageContent newContent = (WikiPageContent) currentWikiContent.clone();
	    newContent.setWikiPage(wikiPage);
	    wikiPage.setCurrentWikiContent(newContent);
	    wikiPage.setWikiContentVersions(new HashSet<WikiPageContent>());
	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + WikiPage.class);
	}
	return wikiPage;
    }
}
