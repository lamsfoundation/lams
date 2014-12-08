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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.ToolImportSupport;
import org.lamsfoundation.lams.tool.dao.IToolImportSupportDAO;
import org.springframework.stereotype.Repository;

@Repository
public class ToolImportSupportDAO extends LAMSBaseDAO implements IToolImportSupportDAO
{
	private static final String LOAD_BY_OLD_SIG = "from tis in class ToolImportSupport where tis.supportsToolSignature=:supportsToolSignature";
	private static final String FIND_ALL = "from obj in class " + ToolImportSupport.class.getName();
	
	/** Get all the ToolImportSupport objects which record support for the given old tool signature */
	public List getToolSignatureWhichSupports(final String oldToolSignature) {
		return (List) getSession().createQuery(LOAD_BY_OLD_SIG).setString("supportsToolSignature", oldToolSignature).list();

	}

    public List getAllToolImportSupport(){    	
    	return doFind(FIND_ALL);
    }

}
