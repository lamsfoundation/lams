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

package org.lamsfoundation.lams.tool.scratchie.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.confidencelevel.ConfidenceLevelDTO;

/**
 * Tool may contain several questions. Which in turn contain answers.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_scratchie_answer")
public class ScratchieAnswer implements Cloneable {
    private static final Logger log = Logger.getLogger(ScratchieAnswer.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String description;

    @Column
    private boolean correct;

    @Column(name = "order_id")
    private Integer orderId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "scratchie_item_uid")
    private ScratchieItem scratchieItem;

    // ******************** DTO fields ***************************
    @Transient
    private boolean scratched;
    @Transient
    private int attemptOrder;
    @Transient
    private int[] attempts;
    @Transient
    private List<ConfidenceLevelDTO> confidenceLevelDtos;

    // **********************************************************
    // Get/Set methods
    // **********************************************************
    /**
     *
     * @return Returns the uid.
     */
    public Long getUid() {
	return uid;
    }

    /**
     * @param uid
     *            The uid to set.
     */
    public void setUid(Long userID) {
	this.uid = userID;
    }

    /**
     *
     * @return
     */
    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    /**
     *
     * @return
     */
    public boolean isCorrect() {
	return correct;
    }

    public void setCorrect(boolean correctScratchie) {
	this.correct = correctScratchie;
    }

    /**
     *
     * @return
     */
    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    /**
     *
     */
    public ScratchieItem getScratchieItem() {
	return scratchieItem;
    }

    public void setScratchieItem(ScratchieItem scratchieItem) {
	this.scratchieItem = scratchieItem;
    }

    public void setScratched(boolean complete) {
	this.scratched = complete;
    }

    public boolean isScratched() {
	return scratched;
    }

    /**
     * @return in which order the student selected it
     */
    public int getAttemptOrder() {
	return attemptOrder;
    }

    public void setAttemptOrder(int attemptOrder) {
	this.attemptOrder = attemptOrder;
    }

    /**
     * Used for item summary page in monitor
     *
     * @return
     */
    public int[] getAttempts() {
	return attempts;
    }

    public void setAttempts(int[] attempts) {
	this.attempts = attempts;
    }

    public List<ConfidenceLevelDTO> getConfidenceLevelDtos() {
	return confidenceLevelDtos;
    }

    public void setConfidenceLevelDtos(List<ConfidenceLevelDTO> confidenceLevelDtos) {
	this.confidenceLevelDtos = confidenceLevelDtos;
    }

    @Override
    public Object clone() {
	ScratchieAnswer obj = null;
	try {
	    obj = (ScratchieAnswer) super.clone();
	    obj.setUid(null);
	} catch (CloneNotSupportedException e) {
	    ScratchieAnswer.log.error("When clone " + ScratchieAnswer.class + " failed");
	}

	return obj;
    }
}
