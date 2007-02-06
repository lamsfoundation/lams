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

/* $Id$ */
package org.lamsfoundation.lams.admin.web.dto;

/**
 * @author jliew
 *
 */
public class V1OrgRightDTO {

	private String orgSid;
	private String user_right;
	
	public static final String MEMBERSHIP = "0";
	public static final String SELECTION = "1";
	public static final String ADMIN = "2";
	
	public V1OrgRightDTO(String orgSid, String user_right) {
		this.orgSid = orgSid;
		this.user_right = user_right;
	}
	
	public String getOrgSid() {
		return orgSid;
	}
	
	public void setOrgSid(String orgSid) {
		this.orgSid = orgSid;
	}
	
	public String getUserRight() {
		return user_right;
	}
	
	public void setUserRight(String user_right) {
		this.user_right = user_right;
	}
}
