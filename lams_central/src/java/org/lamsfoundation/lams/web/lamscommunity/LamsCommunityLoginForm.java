/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.web.lamscommunity; 

import org.apache.struts.action.ActionForm;
 
/**
 * 
 * @author lfoxton
 * 
 *
 */
public class LamsCommunityLoginForm extends ActionForm {
    
    public static final long serialVersionUID = 82738272773582375L;
    
    public String lcUserName;
    public String lcPassword;
    public String dest;
    public String dispatch;
    
    public LamsCommunityLoginForm() {}

    public String getLcUserName() {
        return lcUserName;
    }

    public void setLcUserName(String lcUserName) {
        this.lcUserName = lcUserName;
    }

    public String getLcPassword() {
        return lcPassword;
    }

    public void setLcPassword(String lcPassword) {
        this.lcPassword = lcPassword;
    }

    public String getDispatch() {
        return dispatch;
    }

    public void setDispatch(String dispatch) {
        this.dispatch = dispatch;
    }

    public String getDest() {
        return dest;
    }

    public void setDest(String dest) {
        this.dest = dest;
    }
}
 