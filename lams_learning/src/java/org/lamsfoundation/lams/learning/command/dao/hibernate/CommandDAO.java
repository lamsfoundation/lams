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

package org.lamsfoundation.lams.learning.command.dao.hibernate;

import java.util.Date;
import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.learning.command.dao.ICommandDAO;
import org.lamsfoundation.lams.learning.command.model.Command;
import org.springframework.stereotype.Repository;

@Repository
public class CommandDAO extends LAMSBaseDAO implements ICommandDAO {
    private static final String COMMAND_BY_LESSON_AND_DATE = "FROM Command WHERE lessonId = :lessonId AND createDate >= :lastCheck";

    @Override
    @SuppressWarnings("unchecked")
    public List<Command> getNewCommands(Long lessonId, Date lastCheck) {
	return (List<Command>) (doFindByNamedParam(CommandDAO.COMMAND_BY_LESSON_AND_DATE,
		new String[] { "lessonId", "lastCheck" }, new Object[] { lessonId, lastCheck }));
    }
}