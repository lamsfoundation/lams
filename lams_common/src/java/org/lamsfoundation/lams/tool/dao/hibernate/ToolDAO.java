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

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Jacky Fang 8/02/2005
 * @author Ozgur Demirtas 24/06/2005
 *
 */
@Repository
public class ToolDAO extends LAMSBaseDAO implements IToolDAO {
    private static final String LOAD_TOOL_BY_SIG = "from tool in class Tool where tool.toolSignature=:toolSignature";

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolDAO#getToolByID(java.lang.Long)
     */
    @Override
    public Tool getToolByID(Long toolID) {
	return getSession().get(Tool.class, toolID);
    }

    @Override
    public List getAllTools() {
	return findAll(Tool.class);
    }

    @Override
    public Tool getToolBySignature(final String toolSignature) {
	return (Tool) getSession().createQuery(LOAD_TOOL_BY_SIG).setString("toolSignature", toolSignature)
		.setCacheable(true).uniqueResult();
    }

    @Override
    public long getToolDefaultContentIdBySignature(final String toolSignature) {
	Tool tool = getToolBySignature(toolSignature);

	if (tool != null) {
	    return tool.getDefaultToolContentId();
	} else {
	    return 0;
	}
    }

    @Override
    public void saveOrUpdateTool(Tool tool) {
	this.getSession().saveOrUpdate(tool);
    }
}