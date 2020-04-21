/***************************************************************************
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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.learning.kumalive.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "lams_kumalive_log")
public class KumaliveLog implements Serializable {
    private static final long serialVersionUID = -2294366826356383997L;

    public static final short TYPE_HAND_UP = 1;
    public static final short TYPE_HAND_DOWN = 2;

    @Id
    @Column(name = "log_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long logId;
    
    @Column(name = "kumalive_id")
    private Long kumaliveId;
    
    @Column(name = "user_id")
    private Integer userId;
    
    @Column(name = "log_date")
    private Date date;
    
    @Column(name = "log_type")
    private Short type;

    public KumaliveLog() {
    }

    public KumaliveLog(Long kumaliveId, Integer userId, Date date, Short type) {
	this.kumaliveId = kumaliveId;
	this.userId = userId;
	this.date = date;
	this.type = type;
    }

    public Long getLogId() {
	return logId;
    }

    public void setLogId(Long logId) {
	this.logId = logId;
    }

    public Long getKumaliveId() {
	return kumaliveId;
    }

    public void setKumaliveId(Long kumaliveId) {
	this.kumaliveId = kumaliveId;
    }

    public Integer getUserId() {
	return userId;
    }

    public void setUserId(Integer userId) {
	this.userId = userId;
    }

    public Date getDate() {
	return date;
    }

    public void setDate(Date date) {
	this.date = date;
    }

    public Short getType() {
	return type;
    }

    public void setType(Short type) {
	this.type = type;
    }

}