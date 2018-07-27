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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.scribe.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.scribe.dao.IScribeHeadingDAO;
import org.lamsfoundation.lams.tool.scribe.model.ScribeReportEntry;
import org.springframework.stereotype.Repository;

@Repository
public class ScribeHeadingDAO extends LAMSBaseDAO implements IScribeHeadingDAO {

    private static final String SQL_DELETE_REPORT = "from " + ScribeReportEntry.class.getName()
	    + " as r where r.scribeHeading.uid=?";

    @Override
    @SuppressWarnings("unchecked")
    public boolean deleteReport(Long headingUid) {
	List<ScribeReportEntry> reports = (List<ScribeReportEntry>) doFind(SQL_DELETE_REPORT,
		new Object[] { headingUid });
	if (reports.isEmpty()) {
	    return false;
	}

	for (ScribeReportEntry report : reports) {
	    getSession().delete(report);
	}

	return true;
    }

}
