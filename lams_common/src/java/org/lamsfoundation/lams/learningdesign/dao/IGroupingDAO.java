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
import org.lamsfoundation.lams.learningdesign.Grouping;

/**
 * @author Manpreet Minhas
 * @author Fiona Malikoff
 */
public interface IGroupingDAO extends IBaseDAO {

    /**
     * Get a grouping record from the database. Must return a "real" grouping object, not
     * a CGLIB proxy object.
     * 
     * @param groupingID
     * @return Grouping populated Grouping object
     */
    public Grouping getGroupingById(Long groupingID);

    /**
     * Returns the list of groupings applicable for the given learning design.
     * 
     * @param learningDesignId
     * @return list of Grouping objects
     */
    public List getGroupingsByLearningDesign(Long learningDesignId);

}
