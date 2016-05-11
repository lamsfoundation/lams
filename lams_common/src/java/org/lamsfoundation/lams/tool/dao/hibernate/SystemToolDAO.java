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


package org.lamsfoundation.lams.tool.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.SystemTool;
import org.lamsfoundation.lams.tool.dao.ISystemToolDAO;
import org.springframework.orm.hibernate3.HibernateCallback;

public class SystemToolDAO extends BaseDAO implements ISystemToolDAO {

    private static final String LOAD_TOOL_BY_ACT_TYPE = "from tool in class SystemTool where tool.activityTypeId=:activityTypeId";

    @Override
    public SystemTool getSystemToolByID(final Long systemToolID) {
	return (SystemTool) getHibernateTemplate().get(SystemTool.class, systemToolID);
    }

    @Override
    public SystemTool getSystemToolByActivityTypeId(final Integer activityTypeId) {
	return (SystemTool) getHibernateTemplate().execute(new HibernateCallback() {
	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_TOOL_BY_ACT_TYPE).setInteger("activityTypeId", activityTypeId)
			.uniqueResult();
	    }
	});
    }

}
