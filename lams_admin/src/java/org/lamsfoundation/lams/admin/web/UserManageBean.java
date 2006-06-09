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
 * @version
 *
 * <p>
 * <a href="UserManageBean.java.html"><i>View Source</i></a>
 * </p>
 *
 * @author <a href="mailto:jliew@melcoe.mq.edu.au">Jun-Dir Liew</a>
 *
 * Created at 13:34:33 on 9/06/2006
 */
public class UserManageBean {

	/**
	 * UserManageBean Constructor
	 *
	 * @param 
	 */
	public UserManageBean() {
		super();

	}
    private Integer userId;
    private String login;
    private String title;
    private String firstName;
    private String lastName;
    
    public Integer getUserId() {
    	return this.userId;
    }
    
    public void setUserId(Integer userId) {
    	this.userId = userId;
    }
    
    public String getLogin() {
    	return this.login;
    }
    
    public void setLogin(String login) {
    	this.login = login;
    }
    
    public String getTitle() {
    	return this.title;
    }
	
    public void setTitle(String title) {
    	this.title = title;
    }
    
    public String getFirstName() {
    	return this.firstName;
    }
    
    public void setFirstName(String firstName) {
    	this.firstName = firstName;
    }
    
    public String getLastName() {
    	return this.lastName;
    }
    
    public void setLastName(String lastName) {
    	this.lastName = lastName;
    }
}
