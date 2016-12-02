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

import org.apache.log4j.Logger;

/**
 * AssessmentUnit
 *
 * @author Andrey Balan
 */
public class AssessmentUnit implements Cloneable, Sequencable {
    private static final Logger log = Logger.getLogger(AssessmentUnit.class);

    private Long uid;

    private Integer sequenceId;

    private String unit;

    private float multiplier;

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

    public void setUid(Long uuid) {
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
     * @return Returns multiplier.
     */
    public float getMultiplier() {
	return multiplier;
    }

    public void setMultiplier(float multiplier) {
	this.multiplier = multiplier;
    }

    /**
     *
     *
     * @return Returns unit.
     */
    public String getUnit() {
	return unit;
    }

    public void setUnit(String unit) {
	this.unit = unit;
    }

    @Override
    public Object clone() {
	AssessmentUnit obj = null;
	try {
	    obj = (AssessmentUnit) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    AssessmentUnit.log.error("When clone " + AssessmentUnit.class + " failed");
	}

	return obj;
    }
}
