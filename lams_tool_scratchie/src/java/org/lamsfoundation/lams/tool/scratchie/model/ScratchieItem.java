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

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.qb.model.QbOption;
import org.lamsfoundation.lams.qb.model.QbQuestion;
import org.lamsfoundation.lams.qb.model.QbToolQuestion;
import org.lamsfoundation.lams.tool.scratchie.dto.OptionDTO;

/**
 * Tool may contain several questions. Which in turn contain optionDtos.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_scratchie_item")
//in this entity's table primary key is "uid", but it references "tool_question_uid" in lams_qb_tool_question
@PrimaryKeyJoinColumn(name = "uid")
public class ScratchieItem extends QbToolQuestion implements Serializable, Cloneable {
    private static final long serialVersionUID = -2824051249870361117L;

    private static final Logger log = Logger.getLogger(ScratchieItem.class);

    // ************************ DTO fields ***********************
    @Transient
    private boolean isUnraveled;
    @Transient
    private String burningQuestion;
    @Transient
    private List<OptionDTO> optionDtos = null;

    @Override
    public Object clone() {
	ScratchieItem item = null;
	try {
	    item = (ScratchieItem) super.clone();
	    item.uid = null;
	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ScratchieItem.class + " failed");
	}
	return item;
    }

    public List<OptionDTO> getOptionDtos() {
	if (optionDtos == null) {
	    optionDtos = new LinkedList<>();
	    
	    if (QbQuestion.TYPE_MULTIPLE_CHOICE == this.qbQuestion.getType()) {
		for (QbOption option : qbQuestion.getQbOptions()) {
		    OptionDTO answer = new OptionDTO();
		    answer.setQbOption(option);
		    optionDtos.add(answer);
		}
	    }
	}
	return optionDtos;
    }

    public void setOptionDtos(List<OptionDTO> optionDtos) {
	this.optionDtos = optionDtos;
    }

    public boolean isUnraveled() {
	return isUnraveled;
    }

    public void setUnraveled(boolean isUnraveled) {
	this.isUnraveled = isUnraveled;
    }

    public String getBurningQuestion() {
	return burningQuestion;
    }

    public void setBurningQuestion(String burningQuestion) {
	this.burningQuestion = burningQuestion;
    }

}