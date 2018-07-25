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

package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.tool.dao.IToolContentDAO;

/**
 * <p>
 * This generator is designed to create new content id for a particular tool.
 * </p>
 * <p>
 * It is configured as a Spring singleton bean. Transaction demarcation has
 * been set to <code>REQUIRED_NEW</code> to ensure it is always runing within
 * its own transaction. And THE isolation level is set to
 * <code>READ_COMMITTED</code>.
 *
 * We are using MySql auto-increment generator to ensure the data correctness
 * under concurrency contention.
 *
 * @author Jacky Fang 9/02/2005
 *
 */
public class ToolContentIDGenerator {
    private IToolContentDAO toolContentDao;

    /**
     * Method injection for Spring configuration.
     * 
     * @param toolContentDao
     *            The toolContentDao to set.
     */
    public void setToolContentDao(IToolContentDAO toolContentDao) {
	this.toolContentDao = toolContentDao;
    }

    /**
     * Get the next tool content id for a tool.
     * 
     * @param tool
     *            the target tool.
     * @return the next id object.
     */
    public Long getNextToolContentIDFor(Tool tool) {
	ToolContent newContent = new ToolContent(tool);
	toolContentDao.saveToolContent(newContent);
	return newContent.getToolContentId();
    }
}
