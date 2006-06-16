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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.admin.web;

/**
 * @author Jun-Dir Liew
 *
 * Created at 12:10:10 on 16/06/2006
 */
public class UserOrgRolesDTO {

	private Boolean learner = false;
	private Boolean author = false;
	private Boolean staff = false;
	private Boolean admin = false;
	private Boolean manager = false;
	
	public Boolean getLearner() {
		return learner;
	}
	
	public void setLearner(Boolean learner) {
		this.learner = learner;
	}
	
	public Boolean getAuthor() {
		return author;
	}
	
	public void setAuthor(Boolean author) {
		this.author = author;
	}
	
	public Boolean getStaff() {
		return staff;
	}
	
	public void setStaff(Boolean staff) {
		this.staff = staff;
	}
	
	public Boolean getAdmin() {
		return admin;
	}
	
	public void setAdmin(Boolean admin) {
		this.admin = admin;
	}
	
	public Boolean getManager() {
		return manager;
	}
	
	public void setManager(Boolean manager) {
		this.manager = manager;
	}
}
