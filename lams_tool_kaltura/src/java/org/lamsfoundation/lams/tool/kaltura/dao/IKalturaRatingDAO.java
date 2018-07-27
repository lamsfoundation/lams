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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.kaltura.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaRating;

/**
 * DAO interface for <code>KalturaRating</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.kaltura.model.KalturaRating
 */
public interface IKalturaRatingDAO extends IBaseDAO {

    /**
     * Return KalturaRating by the given itemUid and userId.
     *
     * @param itemUid
     * @param userId
     * @return
     */
    KalturaRating getKalturaRatingByItemAndUser(Long itemUid, Long userId);

    /**
     * Return list of KalturaRating by the the given itemUid.
     *
     * @param itemUid
     * @param userId
     * @return
     */
    List<KalturaRating> getKalturaRatingsByItemUid(Long itemUid);

    /**
     * Returns rating statistics by particular item
     *
     * @param itemUid
     * @param sessionId
     * @return
     */
    AverageRatingDTO getAverageRatingDtoByItem(Long itemUid, Long sessionId);

}
