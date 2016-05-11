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

package org.lamsfoundation.lams.tool.assessment.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.assessment.dao.AssessmentDAO;
import org.lamsfoundation.lams.tool.assessment.model.Assessment;

/**
 *
 * @author Andrey Balan
 *
 * @version $Revision$
 */
public class AssessmentDAOHibernate extends BaseDAOHibernate implements AssessmentDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Assessment.class.getName()
	    + " as r where r.contentId=?";

    @Override
    public Assessment getByContentId(Long contentId) {
	List list = getHibernateTemplate().find(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return (Assessment) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Assessment getByUid(Long assessmentUid) {
	return (Assessment) getObject(Assessment.class, assessmentUid);
    }

    @Override
    public void delete(Assessment assessment) {
	this.getHibernateTemplate().delete(assessment);
    }

}
