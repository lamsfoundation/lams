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
package org.lamsfoundation.lams.tool.commonCartridge.web.form;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

/**
 *
 * 
 * @author Andrey Balan
 */
public class AdminForm extends ActionForm {
    private static final long serialVersionUID = 414425664356226L;

    private boolean allowExposeUserName;

    private boolean allowExposeUserEmail;

    @Override
    public ActionErrors validate(ActionMapping arg0, HttpServletRequest arg1) {
	ActionErrors ac = new ActionErrors();
	ac.add(ActionMessages.GLOBAL_MESSAGE, new ActionMessage("this is an error"));
	return ac;
    }

    public boolean isAllowExposeUserName() {
	return allowExposeUserName;
    }

    public void setAllowExposeUserName(boolean allowExposeUserName) {
	this.allowExposeUserName = allowExposeUserName;
    }

    public boolean isAllowExposeUserEmail() {
	return allowExposeUserEmail;
    }

    public void setAllowExposeUserEmail(boolean allowExposeUserEmail) {
	this.allowExposeUserEmail = allowExposeUserEmail;
    }
}
