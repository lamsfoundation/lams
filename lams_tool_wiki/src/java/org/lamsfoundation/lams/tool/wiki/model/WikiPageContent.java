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

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * An instance of WikiContent represents 1 version of the Wiki Page. Each time
 * you edit, a new instance of WikiContent will be created, so we will be able
 * to search the history and do reverts.
 *
 * @author lfoxton
 *
 */
@Entity
@Table(name = "tl_lawiki10_wiki_page_content")
public class WikiPageContent implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 98276541234892314L;

    private static Logger log = Logger.getLogger(WikiPageContent.class.getName());

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wiki_page_uid")
    private WikiPage wikiPage;

    @Column
    private String body;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "editor")
    private WikiUser editor;

    @Column(name = "edit_date")
    private Date editDate;

    @Column
    private Long version;

    public WikiPageContent() {
    }

    /**
     *
     *
     */
    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     *
     *
     *
     */
    public WikiPage getWikiPage() {
	return wikiPage;
    }

    public void setWikiPage(WikiPage wikiPage) {
	this.wikiPage = wikiPage;
    }

    /**
     *
     *
     */
    public String getBody() {
	return body;
    }

    public void setBody(String body) {
	this.body = body;
    }

    /**
     *
     *
     *
     */
    public WikiUser getEditor() {
	return editor;
    }

    public void setEditor(WikiUser editor) {
	this.editor = editor;
    }

    /**
     *
     *
     */
    public Date getEditDate() {
	return editDate;
    }

    public void setEditDate(Date editDate) {
	this.editDate = editDate;
    }

    /**
     *
     *
     */
    public Long getVersion() {
	return version;
    }

    public void setVersion(Long version) {
	this.version = version;
    }

    @Override
    public Object clone() {

	WikiPageContent wikiContent = null;
	try {
	    wikiContent = (WikiPageContent) super.clone();
	    wikiContent.setUid(null);
	    wikiContent.setEditor(null);
	    wikiContent.setEditDate(new Date());
	} catch (CloneNotSupportedException cnse) {
	    log.error("Error cloning " + WikiPageContent.class);
	}
	return wikiContent;
    }

}
