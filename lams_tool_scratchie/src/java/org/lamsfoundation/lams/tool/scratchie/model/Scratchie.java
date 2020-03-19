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

package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.Date;
import java.util.Iterator;
import java.util.Set;
import java.util.TreeSet;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.log4j.Logger;

/**
 * Scratchie
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_scratchie")
public class Scratchie implements Cloneable {
    private static final Logger log = Logger.getLogger(Scratchie.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "content_id")
    private Long contentId;

    @Column
    private String title;

    @Column
    private String instructions;

    // advance

    @Column(name = "define_later")
    private boolean defineLater;

    // general infomation

    @Column(name = "create_date")
    private Date created;

    @Column(name = "update_date")
    private Date updated;

    @Column(name = "submission_deadline")
    private Date submissionDeadline;

    @OneToMany
    @JoinColumn(name = "scratchie_uid")
    private Set<ScratchieItem> scratchieItems = new TreeSet<>();

    @Column(name = "extra_point")
    private boolean extraPoint;

    @Column(name = "burning_questions_enabled")
    private boolean burningQuestionsEnabled;

    @Column(name = "shuffle_items")
    private boolean shuffleItems;

    @Column(name = "time_limit")
    private int timeLimit;

    @Column(name = "confidence_levels_activity_uiid")
    private Integer confidenceLevelsActivityUiid;

    @Column(name = "activity_uuid_providing_vsa_answers")
    private Integer activityUiidProvidingVsaAnswers;

    //overwrites default preset marks stored as admin config setting
    @Column(name = "preset_marks")
    private String presetMarks;

    @Column(name = "reflect_on_activity")
    private boolean reflectOnActivity;

    @Column(name = "reflect_instructions")
    private String reflectInstructions;

    @Column(name = "show_scratchies_in_results")
    private boolean showScrachiesInResults;

    // **********************************************************
    // Function method for Scratchie
    // **********************************************************
    public static Scratchie newInstance(Scratchie defaultContent, Long contentId) {
	Scratchie toContent = new Scratchie();
	toContent = (Scratchie) defaultContent.clone();
	toContent.setContentId(contentId);

	return toContent;
    }

    @Override
    public Object clone() {
	Scratchie scratchie = null;
	try {
	    scratchie = (Scratchie) super.clone();
	    scratchie.setUid(null);
	    if (scratchieItems != null) {
		Iterator<ScratchieItem> iter = scratchieItems.iterator();
		Set<ScratchieItem> set = new TreeSet<>();
		while (iter.hasNext()) {
		    ScratchieItem item = iter.next();
		    ScratchieItem newItem = (ScratchieItem) item.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newItem);
		}
		scratchie.scratchieItems = set;
	    }
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + Scratchie.class + " failed");
	}

	return scratchie;
    }

    @Override
    public boolean equals(Object o) {
	if (this == o) {
	    return true;
	}
	if (!(o instanceof Scratchie)) {
	    return false;
	}

	final Scratchie genericEntity = (Scratchie) o;

	return new EqualsBuilder().append(uid, genericEntity.uid).append(title, genericEntity.title)
		.append(instructions, genericEntity.instructions).append(created, genericEntity.created)
		.append(updated, genericEntity.updated).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(uid).append(title).append(instructions).append(created).append(updated)
		.toHashCode();
    }

    /**
     * Updates the modification data for this entity.
     */
    public void updateModificationData() {

	long now = System.currentTimeMillis();
	if (created == null) {
	    this.setCreated(new Date(now));
	}
	this.setUpdated(new Date(now));
    }

    // **********************************************************
    // get/set methods
    // **********************************************************
    /**
     * Returns the object's creation date
     *
     * @return date
     *
     */
    public Date getCreated() {
	return created;
    }

    /**
     * Sets the object's creation date
     *
     * @param created
     */
    public void setCreated(Date created) {
	this.created = created;
    }

    /**
     * Returns the object's date of last update
     *
     * @return date updated
     *
     */
    public Date getUpdated() {
	return updated;
    }

    /**
     * Sets the object's date of last update
     *
     * @param updated
     */
    public void setUpdated(Date updated) {
	this.updated = updated;
    }

    /**
     * Returns deadline for learner's submission
     *
     * @return submissionDeadline
     */
    public Date getSubmissionDeadline() {
	return submissionDeadline;
    }

    /**
     * Sets deadline for learner's submission
     *
     * @param submissionDeadline
     */
    public void setSubmissionDeadline(Date submissionDeadline) {
	this.submissionDeadline = submissionDeadline;
    }

    public Long getUid() {
	return uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    /**
     * @return Returns the title.
     */
    public String getTitle() {
	return title;
    }

    /**
     * @param title
     *            The title to set.
     */
    public void setTitle(String title) {
	this.title = title;
    }

    /**
     * @return Returns the instructions set by the teacher.
     */
    public String getInstructions() {
	return instructions;
    }

    public void setInstructions(String instructions) {
	this.instructions = instructions;
    }

    public Set<ScratchieItem> getScratchieItems() {
	return scratchieItems;
    }

    public void setScratchieItems(Set<ScratchieItem> scratchieItems) {
	this.scratchieItems = scratchieItems;
    }

    public boolean isDefineLater() {
	return defineLater;
    }

    public void setDefineLater(boolean defineLater) {
	this.defineLater = defineLater;
    }

    public Long getContentId() {
	return contentId;
    }

    public void setContentId(Long contentId) {
	this.contentId = contentId;
	for (ScratchieItem item : scratchieItems) {
	    item.setToolContentId(contentId);
	}
    }

    public String getReflectInstructions() {
	return reflectInstructions;
    }

    public void setReflectInstructions(String reflectInstructions) {
	this.reflectInstructions = reflectInstructions;
    }

    public boolean isReflectOnActivity() {
	return reflectOnActivity;
    }

    public void setReflectOnActivity(boolean reflectOnActivity) {
	this.reflectOnActivity = reflectOnActivity;
    }

    public boolean isExtraPoint() {
	return extraPoint;
    }

    public void setExtraPoint(boolean extraPoint) {
	this.extraPoint = extraPoint;
    }

    public boolean isBurningQuestionsEnabled() {
	return burningQuestionsEnabled;
    }

    public void setBurningQuestionsEnabled(boolean burningQuestionsEnabled) {
	this.burningQuestionsEnabled = burningQuestionsEnabled;
    }

    public boolean isShuffleItems() {
	return shuffleItems;
    }

    public void setShuffleItems(boolean shuffleItems) {
	this.shuffleItems = shuffleItems;
    }

    /**
     * @return Returns the time limitation, that students have to complete an attempt.
     */
    public int getTimeLimit() {
	return timeLimit;
    }

    /**
     * @param timeLimit
     *            the time limitation, that students have to complete an attempt.
     */
    public void setTimeLimit(int timeLimit) {
	this.timeLimit = timeLimit;
    }

    /**
     * @return which preceding activity should be queried for confidence levels
     */
    public boolean isConfidenceLevelsEnabled() {
	return confidenceLevelsActivityUiid != null;
    }

    /**
     * @return which preceding activity should be queried for confidence levels
     */
    public Integer getConfidenceLevelsActivityUiid() {
	return confidenceLevelsActivityUiid;
    }

    /**
     * @param confidenceLevelsActivityUiid
     *            preceding activity that should be queried for confidence levels
     */
    public void setConfidenceLevelsActivityUiid(Integer confidenceLevelsActivityUiid) {
	this.confidenceLevelsActivityUiid = confidenceLevelsActivityUiid;
    }

    public boolean isAnswersFetchingEnabled() {
	return activityUiidProvidingVsaAnswers != null;
    }

    /**
     * @return which preceding activity should be queried for VSA answers
     */
    public Integer getActivityUiidProvidingVsaAnswers() {
	return activityUiidProvidingVsaAnswers;
    }

    /**
     * @param activityUiidProvidingVsaAnswers
     *            preceding activity that should be queried for VSA answers
     */
    public void setActivityUiidProvidingVsaAnswers(Integer activityUiidProvidingVsaAnswers) {
	this.activityUiidProvidingVsaAnswers = activityUiidProvidingVsaAnswers;
    }

    /**
     * This property holds value that can overwrite default preset marks stored in admin config setting. It can be null
     * and therefore ScratchieService.getPresetMarks() method should be used instead when calculating actual marks.
     *
     * @return
     */
    public String getPresetMarks() {
	return presetMarks;
    }

    public void setPresetMarks(String presetMarks) {
	this.presetMarks = presetMarks;
    }

    public boolean isShowScrachiesInResults() {
	return showScrachiesInResults;
    }

    public void setShowScrachiesInResults(boolean showScrachiesInResults) {
	this.showScrachiesInResults = showScrachiesInResults;
    }
}
