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


package org.lamsfoundation.lams.tool.kaltura.dao.hibernate;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaRatingDAO;
import org.lamsfoundation.lams.tool.kaltura.dto.AverageRatingDTO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaRating;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>IKalturaRatingDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.kaltura.dao.IKalturaRatingDAO
 */
@Repository
public class KalturaRatingDAO extends LAMSBaseDAO implements IKalturaRatingDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + KalturaRating.class.getName()
	    + " as r where r.createBy.userId = ? and r.kalturaItem.uid=?";

    private static final String FIND_BY_ITEM_UID = "from " + KalturaRating.class.getName()
	    + " as r where r.kalturaItem.uid=?";

    private static final String FIND_AVERAGE_RATING_BY_MESSAGE = "SELECT AVG(r.rating), COUNT(*) from "
	    + KalturaRating.class.getName() + " as r where r.kalturaItem.uid=? and r.createBy.session.sessionId=?";

    @Override
    public KalturaRating getKalturaRatingByItemAndUser(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (KalturaRating) list.get(0);
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<KalturaRating> getKalturaRatingsByItemUid(Long itemUid) {
	return (List<KalturaRating>) doFind(FIND_BY_ITEM_UID, itemUid);
    }

    @SuppressWarnings("unchecked")
    @Override
    public AverageRatingDTO getAverageRatingDtoByItem(Long itemUid, Long sessionId) {
	List<Object[]> list = (List<Object[]>) doFind(FIND_AVERAGE_RATING_BY_MESSAGE,
		new Object[] { itemUid, sessionId });
	Object[] results = list.get(0);

	Object averageRatingObj = (results[0] == null) ? 0 : results[0];
	NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
	numberFormat.setMaximumFractionDigits(1);
	String averageRating = numberFormat.format(averageRatingObj);

	String numberOfVotes = (results[1] == null) ? "0" : String.valueOf(results[1]);
	return new AverageRatingDTO(averageRating, numberOfVotes);
    }

}
