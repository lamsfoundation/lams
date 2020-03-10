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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.apache.log4j.Logger;

/**
 * Tool may contain several questions. Which in turn contain answers.
 *
 * @author Andrey Balan
 */
@Entity
@Table(name = "tl_lascrt11_scratchie_item")
public class ScratchieItem implements Cloneable {
    private static final Logger log = Logger.getLogger(ScratchieItem.class);

    @Id
    @Column
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long uid;

    @Column
    private String title;

    @Column
    private String description;

    @Column(name = "order_id")
    private Integer orderId;

    @Column(name = "create_by_author")
    private boolean isCreateByAuthor;

    @Column(name = "create_date")
    private Date createDate;

    @OneToMany(cascade = CascadeType.ALL)
    @OrderBy("order_id ASC")
    @JoinColumn(name = "scratchie_item_uid")
    private Set<ScratchieAnswer> answers = new HashSet<>();

    // ************************ DTO fields ***********************
    @Transient
    private boolean isUnraveled;
    @Transient
    private String burningQuestion;
    @Transient
    private int mark;

    @Override
    public Object clone() {
	ScratchieItem item = null;
	try {
	    item = (ScratchieItem) super.clone();

	    item.setUid(null);

	    if (answers != null) {
		Iterator<ScratchieAnswer> iter = answers.iterator();
		Set<ScratchieAnswer> set = new HashSet<>();
		while (iter.hasNext()) {
		    ScratchieAnswer answer = iter.next();
		    ScratchieAnswer newAnswer = (ScratchieAnswer) answer.clone();
		    // just clone old file without duplicate it in repository
		    set.add(newAnswer);
		}
		item.answers = set;
	    }

	} catch (CloneNotSupportedException e) {
	    log.error("When clone " + ScratchieItem.class + " failed");
	}

	return item;
    }

    // **********************************************************
    // Get/Set methods
    // **********************************************************

    public Long getUid() {
	return uid;
    }

    public void setUid(Long userID) {
	this.uid = userID;
    }

    public String getTitle() {
	return title;
    }

    public void setTitle(String title) {
	this.title = title;
    }

    public String getDescription() {
	return description;
    }

    public void setDescription(String description) {
	this.description = description;
    }

    public Date getCreateDate() {
	return createDate;
    }

    public void setCreateDate(Date createDate) {
	this.createDate = createDate;
    }

    public boolean isCreateByAuthor() {
	return isCreateByAuthor;
    }

    public void setCreateByAuthor(boolean isCreateByAuthor) {
	this.isCreateByAuthor = isCreateByAuthor;
    }

    public Integer getOrderId() {
	return orderId;
    }

    public void setOrderId(Integer orderId) {
	this.orderId = orderId;
    }

    public Set<ScratchieAnswer> getAnswers() {
	return answers;
    }

    public void setAnswers(Set<ScratchieAnswer> answers) {
	this.answers = answers;
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

    public int getMark() {
	return mark;
    }

    public void setMark(int mark) {
	this.mark = mark;
    }

}
