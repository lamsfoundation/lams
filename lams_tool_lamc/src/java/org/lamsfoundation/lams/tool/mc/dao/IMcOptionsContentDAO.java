/***************************************************************************
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.mc.dto.McOptionDTO;
import org.lamsfoundation.lams.tool.mc.model.McOptsContent;

/**
 * Interface for the McOptionsContent DAO, defines methods needed to access/modify mc options content
 *
 * @author Ozgur Demirtas
 */
public interface IMcOptionsContentDAO {

    /**
     * <p>
     * Return a list of a McOptsContents with the given identifier <code>mcQueContentId</code>, returns null if not
     * found.
     * </p>
     *
     * @param mcQueContentId
     * @return List
     */
    List findMcOptionsContentByQueId(Long mcQueContentId);

    List<McOptionDTO> getOptionDtos(Long mcQueContentId);

    /**
     * <p>
     * updates McOptsContent
     * </p>
     *
     * @param mcOptionsContent
     */
    void updateMcOptionsContent(McOptsContent mcOptionsContent);
}
