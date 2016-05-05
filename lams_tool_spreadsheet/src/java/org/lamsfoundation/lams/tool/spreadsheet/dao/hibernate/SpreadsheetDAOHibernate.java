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
/* $$Id$$ */
package org.lamsfoundation.lams.tool.spreadsheet.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.spreadsheet.dao.SpreadsheetDAO;
import org.lamsfoundation.lams.tool.spreadsheet.model.Spreadsheet;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class SpreadsheetDAOHibernate extends BaseDAOHibernate implements SpreadsheetDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Spreadsheet.class.getName()
	    + " as r where r.contentId=?";

    @Override
    public Spreadsheet getByContentId(Long contentId) {
	List list = getHibernateTemplate().find(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return (Spreadsheet) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Spreadsheet getByUid(Long spreadsheetUid) {
	return (Spreadsheet) getObject(Spreadsheet.class, spreadsheetUid);
    }

    @Override
    public void delete(Spreadsheet spreadsheet) {
	this.getHibernateTemplate().delete(spreadsheet);
    }

}
