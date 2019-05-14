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

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;

/**
 * Interface for the McQueContent DAO, defines methods needed to access/modify mc questionDescription content
 *
 * @author Ozgur Demirtas
 */
public interface IMcQueContentDAO extends IBaseDAO {

    /**
     * <p>
     * Return the persistent instance of a McQueContent with the given identifier <code>displayOrder</code> and
     * <code>mcContentUid</code>, returns null if not found.
     * </p>
     *
     * @param displayOrder
     * @param mcContentUid
     * @return McQueContent
     */
    McQueContent getQuestionContentByDisplayOrder(final Integer displayOrder, final Long mcContentUid);

    /**
     * <p>
     * Return a list of McQueContent with the given identifier <code>questionDescription</code> and <code>mcContentUid</code>,
     * returns null if not found.
     * </p>
     *
     * @param mcContentUid
     * @return List
     */
    List<McQueContent> getQuestionsByContentUid(final long contentUid);

    /**
     * <p>
     * saves McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     *
     * @param mcQueContent
     */
    void saveOrUpdateMcQueContent(McQueContent mcQueContent);

    /**
     * <p>
     * removes McQueContent with the given identifier <code>mcQueContent</code>
     * </p>
     *
     * @param mcQueContent
     * @return
     */
    void removeMcQueContent(McQueContent mcQueContent);

    McQueContent findMcQuestionContentByUid(Long uid);

    List getAllQuestionEntriesSorted(final long qaContentId);

    void releaseQuestionFromCache(McQueContent question);
}