package org.lamsfoundation.lams.tool.wiki.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
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

/**
 * Each instance of WikiPage represents a page on the wiki (can be the main
 * page) Each WikiPage instance has 0 to many edits, and therefore has 0 to many
 * WikiContent instances
 *
 * @author lfoxton
 *
 */
@Entity
@Table(name = "tl_lawiki10_wiki_page")
public class WikiPage implements java.io.Serializable, Cloneable {
    private static final long serialVersionUID = 2980989002817635889L;

    private static Logger log = Logger.getLogger(WikiPage.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_uid")
    private Wiki parentWiki;

    @Column
    private String title;

    @Column
    private Boolean editable;

    @Column
    private Boolean deleted;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "wiki_page_uid")
    @OrderBy("uid ASC")
    private Set<WikiPageContent> wikiContentVersions;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_current_content")
    private WikiPageContent currentWikiContent;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_session_uid")
    private WikiSession wikiSession;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "added_by")
    private WikiUser addedBy;

    public WikiPage() {
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Wiki getParentWiki() {
	return parentWiki;
    }

    public void setParentWiki(Wiki parentWiki) {
	this.parentWiki = parentWiki;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public Boolean getEditable() {
	return editable;
    }

    public void setEditable(Boolean editable) {
	this.editable = editable;
    }

    public Boolean getDeleted() {
	return deleted;
    }

    public void setDeleted(Boolean deleted) {
	this.deleted = deleted;
    }

    public Set<WikiPageContent> getWikiContentVersions() {
	return wikiContentVersions;
    }

    public void setWikiContentVersions(Set<WikiPageContent> wikiContentVersions) {
	this.wikiContentVersions = wikiContentVersions;
    }

    public WikiPageContent getCurrentWikiContent() {
	return currentWikiContent;
    }

    public void setCurrentWikiContent(WikiPageContent currentWikiContent) {
	this.currentWikiContent = currentWikiContent;
    }

    public WikiUser getAddedBy() {
	return addedBy;
    }

    public void setAddedBy(WikiUser addedBy) {
	this.addedBy = addedBy;
    }

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
