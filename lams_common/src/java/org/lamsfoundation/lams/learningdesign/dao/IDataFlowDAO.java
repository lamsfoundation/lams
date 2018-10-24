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

package org.lamsfoundation.lams.learningdesign.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.learningdesign.DataFlowObject;

/**
 *
 *
 * @author Marcin Cieslak
 *
 */
public interface IDataFlowDAO extends IBaseDAO {
    /**
     * Finds data flow objects (definitions of tool inputs) based on the tool's ID.
     *
     * @param toolContentId
     * @return
     */
    public List<DataFlowObject> getDataFlowObjectsByToolContentId(Long toolContentId);

    /**
     * Finds the data flow object assigned somewhere within the tool. It's up to tool to manage assigment ID, for core
     * it's meaningless.
     *
     * @param toolContentId
     * @param assigmentId
     * @return
     */
    public DataFlowObject getAssignedDataFlowObject(Long toolContentId, Integer assigmentId);
}