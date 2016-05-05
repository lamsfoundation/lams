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

/* $Id$ */
package org.lamsfoundation.lams.tool.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.springframework.stereotype.Repository;

@Repository
public class SystemToolDAO extends LAMSBaseDAO implements ISystemToolDAO {

    private static final String LOAD_TOOL_BY_ACT_TYPE = "from tool in class SystemTool where tool.activityTypeId=:activityTypeId";

    @Override
    public SystemTool getSystemToolByID(final Long systemToolID) {
	return (SystemTool) getSession().get(SystemTool.class, systemToolID);
    }

    @Override
    public SystemTool getSystemToolByActivityTypeId(final Integer activityTypeId) {
	return (SystemTool) getSession().createQuery(LOAD_TOOL_BY_ACT_TYPE).setInteger("activityTypeId", activityTypeId)
		.uniqueResult();
    }

}
