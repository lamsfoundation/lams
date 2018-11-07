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


package org.lamsfoundation.lams.tool.assessment.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.apache.log4j.Logger;

/**
 * AssessmentOverallFeedback
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_laasse10_assessment_overall_feedback")
public class AssessmentOverallFeedback implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(AssessmentOverallFeedback.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "sequence_id")
    private Integer sequenceId;

    @Column(name = "grade_boundary")
    private Integer gradeBoundary;

    @Column
    private String feedback;

    @Override
    public Object clone() {
	AssessmentOverallFeedback obj = null;
	try {
	    obj = (AssessmentOverallFeedback) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    AssessmentOverallFeedback.log.error("When clone " + AssessmentOverallFeedback.class + " failed");
	}

	return obj;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    /**
     *
     * @return Returns the answer ID.
     */
    public Long getUid() {
	return uid;
    }

    private void setUid(Long uuid) {
	uid = uuid;
    }

    /**
     * Returns image sequence number.
     *
     * @return image sequence number
     *
     *
     */
    @Override
    public int getSequenceId() {
	return sequenceId;
    }

    /**
     * Sets image sequence number.
     *
     * @param sequenceId
     *            image sequence number
     */
    @Override
    public void setSequenceId(int sequenceId) {
	this.sequenceId = sequenceId;
    }

    /**
     *
     *
     * @return Returns grade Boundary.
     */
    public Integer getGradeBoundary() {
	return gradeBoundary;
    }

    public void setGradeBoundary(Integer gradeBoundary) {
	this.gradeBoundary = gradeBoundary;
    }

    /**
     *
     *
     * @return Returns feedback on this answer option.
     */
    public String getFeedback() {
	return feedback;
    }

    public void setFeedback(String feedback) {
	this.feedback = feedback;
    }
}
