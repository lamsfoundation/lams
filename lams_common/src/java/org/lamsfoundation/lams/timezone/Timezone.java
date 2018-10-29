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


package org.lamsfoundation.lams.timezone;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Contains timezone id.
 *
 * @author Andrey Balan
 * @see java.util.TimeZone
 *
 *
 */
@Entity
@Table(name = "lams_timezone")
public class Timezone implements Serializable {

    /**
     * Link on Lams wiki timezone help page.
     */
    public static String TIMEZONE_HELP_PAGE = "LAMS+Configuration";

    private static final long serialVersionUID = 6736816209131888523L;

    /** identifier field */
    @Id 
    @Column 
    @GeneratedValue(strategy = GenerationType.IDENTITY) 
    private Long id;

    @Column(name="timezone_id")
    private String timezoneId;

    @Column(name="server_timezone")
    private boolean serverTimezone;

    /** default constructor */
    public Timezone() {
	this.serverTimezone = false;
    }

    /** full constructor */
    public Timezone(String timezoneId) {
	this.timezoneId = timezoneId;
	this.serverTimezone = false;
    }

    /**
     *
     */
    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * Timezone id. This value is a copy of <code>java.util.TimeZone<code> ID.
     *
     *
     */
    public String getTimezoneId() {
	return this.timezoneId;
    }

    public void setTimezoneId(String timezoneId) {
	this.timezoneId = timezoneId;
    }

    /**
     * If this timezone is server's default one.
     *
     *
     */
    public boolean isServerTimezone() {
	return this.serverTimezone;
    }

    public void setServerTimezone(boolean serverTimezone) {
	this.serverTimezone = serverTimezone;
    }

    @Override
    public int hashCode() {
	final int prime = 31;
	int result = 1;
	result = prime * result + (serverTimezone ? 1231 : 1237);
	result = prime * result + ((timezoneId == null) ? 0 : timezoneId.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj) {
	if (this == obj) {
	    return true;
	}
	if (obj == null) {
	    return false;
	}
	if (getClass() != obj.getClass()) {
	    return false;
	}
	Timezone other = (Timezone) obj;
	if (serverTimezone != other.serverTimezone) {
	    return false;
	}
	if (timezoneId == null) {
	    if (other.timezoneId != null) {
		return false;
	    }
	} else if (!timezoneId.equals(other.timezoneId)) {
	    return false;
	}
	return true;
    }

}
