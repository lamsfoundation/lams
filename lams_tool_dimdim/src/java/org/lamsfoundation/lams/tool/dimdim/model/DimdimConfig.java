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
/* $Id$ */

package org.lamsfoundation.lams.tool.dimdim.model;

/**
 * @hibernate.class table="tl_laddim10_dimdim_config"
 */
public class DimdimConfig {

	private Long uid;

	private String key;

	private String value;

	/**
	 * @hibernate.id generator-class="native" type="java.lang.Long" column="uid"
	 */
	public Long getUid() {
		return uid;
	}

	public void setUid(Long uid) {
		this.uid = uid;
	}

	/**
	 * @hibernate.property column="config_key"
	 * @return
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	/**
	 * @hibernate.property column="config_value"
	 * @return
	 */
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
