/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.tool.daco.model;

import org.apache.log4j.Logger;

/**
 * DacoAnswerOption
 *
 * @author Marcin Cieslak
 *
 *
 *
 */
public class DacoAnswerOption implements Cloneable {
    private static final Logger log = Logger.getLogger(DacoQuestion.class);

    private Long uid;

    private Integer sequenceNumber;

    private String answerOption;

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
     *
     * @return Returns the sequence number.
     */
    public Integer getSequenceNumber() {
	return sequenceNumber;
    }

    public void setSequenceNumber(Integer sequenceNumber) {
	this.sequenceNumber = sequenceNumber;
    }

    /**
     *
     * @return Returns the possible answer.
     */
    public String getAnswerOption() {
	return answerOption;
    }

    public void setAnswerOption(String answerOption) {
	this.answerOption = answerOption;
    }

    @Override
    public Object clone() {
	DacoAnswerOption obj = null;
	try {
	    obj = (DacoAnswerOption) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    DacoAnswerOption.log.error("When clone " + DacoAnswerOption.class + " failed");
	}

	return obj;
    }
}