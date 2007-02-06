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
package org.lamsfoundation.lams.admin.web.form;

import org.apache.struts.action.ActionForm;

/**
 * @author jliew
 *
 * @struts.form name="ImportV1ContentsForm"
 */
public class ImportV1ContentsForm extends ActionForm {

	private String[] orgSids;
	private String[] sessSids;
	private boolean onlyMembers;
	
	public String[] getOrgSids() {
		return orgSids;
	}
	
	public void setOrgSids(String[] orgSids) {
		this.orgSids = orgSids;
	}
	
	public String[] getSessSids() {
		return sessSids;
	}
	
	public void setSessSids(String[] sessSids) {
		this.sessSids = sessSids;
	}
	
	public boolean getOnlyMembers() {
		return onlyMembers;
	}
	
	public void setOnlyMembers(boolean onlyMembers) {
		this.onlyMembers = onlyMembers;
	}

}
