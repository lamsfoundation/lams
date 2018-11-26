/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.model;

import java.io.Serializable;

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

import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Persistent object/bean that defines the content for the MCQ tool. Provides accessors and mutators to get/set
 * attributes It maps to database table: tl_lamc11_options_content
 *
 * @author Ozgur Demirtas
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tl_lamc11_options_content")
public class McOptsContent implements Serializable, Comparable<McOptsContent> {

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column(name = "correct_option")
    private boolean correctOption;

    @Column(name = "mc_que_option_text")
    private String mcQueOptionText;

    @Column
    private Integer displayOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mc_que_content_id")
    private McQueContent mcQueContent;

    @Transient
    private boolean selected;

    @Transient
    private String escapedOptionText;

    public McOptsContent(Integer displayOrder, boolean correctOption, String mcQueOptionText,
	    McQueContent mcQueContent) {
	this.displayOrder = displayOrder;
	this.correctOption = correctOption;
	this.mcQueOptionText = mcQueOptionText;
	this.mcQueContent = mcQueContent;
    }

    public static McOptsContent newInstance(McOptsContent mcOptsContent, McQueContent newMcQueContent) {
	McOptsContent newMcOptsContent = new McOptsContent(mcOptsContent.getDisplayOrder(),
		mcOptsContent.isCorrectOption(), mcOptsContent.getMcQueOptionText(), newMcQueContent);
	return newMcOptsContent;
    }

    /** default constructor */
    public McOptsContent() {
    }

    public Long getUid() {
	return this.uid;
    }

    public void setUid(Long uid) {
	this.uid = uid;
    }

    public boolean isCorrectOption() {
	return this.correctOption;
    }

    public void setCorrectOption(boolean correctOption) {
	this.correctOption = correctOption;
    }

    public String getMcQueOptionText() {
	return this.mcQueOptionText;
    }

    public void setMcQueOptionText(String mcQueOptionText) {
	this.mcQueOptionText = mcQueOptionText;
    }

    public McQueContent getMcQueContent() {
	return this.mcQueContent;
    }

    public void setMcQueContent(McQueContent mcQueContent) {
	this.mcQueContent = mcQueContent;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("uid", getUid()).toString();
    }

    @Override
    public int compareTo(McOptsContent optContent) {
	// if the object does not exist yet, then just return any one of 0, -1, 1. Should not make a difference.
	if (uid == null) {
	    return 1;
	} else {
	    return (int) (uid.longValue() - optContent.uid.longValue());
	}
    }

    /**
     * @return Returns the displayOrder.
     */
    public Integer getDisplayOrder() {
	return displayOrder;
    }

    /**
     * @param displayOrder
     *            The displayOrder to set.
     */
    public void setDisplayOrder(Integer displayOrder) {
	this.displayOrder = displayOrder;
    }

    public boolean isSelected() {
	return this.selected;
    }

    public void setSelected(boolean selected) {
	this.selected = selected;
    }

    public String getEscapedOptionText() {
	return this.escapedOptionText;
    }

    public void setEscapedOptionText(String escapedOptionText) {
	this.escapedOptionText = escapedOptionText;
    }

    public String formatPrefixLetter(int index) {
	return new String(Character.toChars(97 + index)) + ")";
    }
}
