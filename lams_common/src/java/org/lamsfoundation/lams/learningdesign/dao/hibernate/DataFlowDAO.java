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
package org.lamsfoundation.lams.learningdesign.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;
import org.lamsfoundation.lams.learningdesign.DataTransition;
import org.lamsfoundation.lams.learningdesign.ToolActivity;
import org.lamsfoundation.lams.learningdesign.dao.IDataFlowDAO;

/**
 * @author Marcin Cieslak
 */
public class DataFlowDAO extends BaseDAO implements IDataFlowDAO {

    private static final String FIND_DATA_FLOW_OBJECTS_BY_TOOL_CONTENT_ID = "SELECT obj FROM "
	    + DataFlowObject.class.getName() + " AS obj, " + DataTransition.class.getName() + " AS transition, "
	    + ToolActivity.class.getName()
	    + " AS activity WHERE activity.toolContentId=? AND activity=transition.toActivity AND transition=obj.dataTransition ORDER BY transition.transitionUIID ASC, obj.orderId ASC";
    private static final String FIND_ASSIGNED_DATA_FLOW_OBJECTS = "SELECT obj FROM " + DataFlowObject.class.getName()
	    + " AS obj, " + DataTransition.class.getName() + " AS transition, " + ToolActivity.class.getName()
	    + " AS activity WHERE activity.toolContentId=:toolContentId AND activity=transition.toActivity AND transition=obj.dataTransition AND obj.toolAssigmentId=:toolAssigmentId";

    @Override
    public List<DataFlowObject> getDataFlowObjectsByToolContentId(Long toolContentId) {
	List<DataFlowObject> result = this.getHibernateTemplate()
		.find(DataFlowDAO.FIND_DATA_FLOW_OBJECTS_BY_TOOL_CONTENT_ID, toolContentId);
	if (result == null || result.isEmpty()) {
	    return null;
	}
	return result;
    }

    @Override
    public DataFlowObject getAssignedDataFlowObject(Long toolContentId, Integer assigmentId) {
	List<DataFlowObject> result = this.getHibernateTemplate().findByNamedParam(
		DataFlowDAO.FIND_ASSIGNED_DATA_FLOW_OBJECTS, new String[] { "toolContentId", "toolAssigmentId" },
		new Object[] { toolContentId, assigmentId });
	// There must be exactly one result
	if (result == null || result.isEmpty() || result.size() > 1) {
	    return null;
	}
	return result.get(0);
    }
}