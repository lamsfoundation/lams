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

package org.lamsfoundation.lams.tool.notebook.model;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.log4j.Logger;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.lamsfoundation.lams.learningdesign.TextSearchConditionComparator;
import org.lamsfoundation.lams.tool.notebook.service.NotebookService;

@Entity
@Table(name = "tl_lantbk11_notebook")
public class Notebook implements java.io.Serializable, Cloneable {

    private static final long serialVersionUID = 579733009969321015L;

    static Logger log = Logger.getLogger(NotebookService.class.getName());

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

    @Column(name = "force_response")
    private boolean forceResponse;

    @Column(name = "lock_on_finished")
    private boolean lockOnFinished;

    @Column(name = "allow_rich_editor")
    private boolean allowRichEditor;

    @Column(name = "content_in_use")
    private boolean contentInUse;

    @Column(name = "define_later")
    private boolean defineLater;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @Column(name = "tool_content_id")
    private Long toolContentId;

    @OneToMany(mappedBy = "notebook")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<NotebookSession> notebookSessions = new HashSet<>();

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "content_uid")
    @Cache(usage = CacheConcurrencyStrategy.TRANSACTIONAL)
    private Set<NotebookCondition> conditions = new TreeSet<>(new TextSearchConditionComparator());

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public Date getUpdateDate() {
	return updateDate;
    }

    public void setUpdateDate(Date updateDate) {
	this.updateDate = updateDate;
    }

    public Long getCreateBy() {
	return createBy;
    }

    public void setCreateBy(Long createBy) {
	this.createBy = createBy;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public boolean isLockOnFinished() {
	return lockOnFinished;
    }

    public void setLockOnFinished(boolean lockOnFinished) {
	this.lockOnFinished = lockOnFinished;
    }

    public boolean isForceResponse() {
	return forceResponse;
    }

    public void setForceResponse(boolean forceResponse) {
	this.forceResponse = forceResponse;
    }

    public boolean isAllowRichEditor() {
	return allowRichEditor;
    }

    public void setAllowRichEditor(boolean allowRichEditor) {
	this.allowRichEditor = allowRichEditor;
    }

    public boolean isContentInUse() {
	return contentInUse;
    }

    public void setContentInUse(boolean contentInUse) {
	this.contentInUse = contentInUse;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    public Long getToolContentId() {
	return toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    public Set<NotebookSession> getNotebookSessions() {
	return notebookSessions;
    }

    public void setNotebookSessions(Set<NotebookSession> notebookSessions) {
	this.notebookSessions = notebookSessions;
    }

    public Set<NotebookCondition> getConditions() {
	return conditions;
    }

    public void setConditions(Set<NotebookCondition> conditions) {
	this.conditions = conditions;
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
	if (this == other) {
	    return true;
	}
	if ((other == null) || !(other instanceof Notebook)) {
	    return false;
	}
	Notebook castOther = (Notebook) other;

	return this.getUid() == castOther.getUid()
		|| this.getUid() != null && castOther.getUid() != null && this.getUid().equals(castOther.getUid());
    }

    @Override
    public int hashCode() {
	int result = 17;
	result = 37 * result + (getUid() == null ? 0 : this.getUid().hashCode());
	return result;
    }

    public static Notebook newInstance(Notebook fromContent, Long toContentId) {
	Notebook toContent = new Notebook();
	toContent = (Notebook) fromContent.clone();
	toContent.setToolContentId(toContentId);
	toContent.setCreateDate(new Date());
	return toContent;
    }

    @Override
    protected Object clone() {

	Notebook notebook = null;
	try {
	    notebook = (Notebook) super.clone();
	    notebook.setUid(null);

	    // create an empty set for the notebookSession
	    notebook.notebookSessions = new HashSet<>();

	    if (conditions != null) {
		Set<NotebookCondition> set = new TreeSet<>(new TextSearchConditionComparator());
		for (NotebookCondition condition : conditions) {
		    set.add((NotebookCondition) condition.clone());
		}
		notebook.setConditions(set);
	    }

	} catch (CloneNotSupportedException cnse) {
	    Notebook.log.error("Error cloning " + Notebook.class);
	}
	return notebook;
    }
}