/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.imageGallery.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageGalleryItemVisitDAO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageGalleryItemVisitLog;
import org.springframework.stereotype.Repository;

@Repository
public class ImageGalleryItemVisitDAOHibernate extends LAMSBaseDAO implements ImageGalleryItemVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + ImageGalleryItemVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.imageGalleryItem.uid=?";

    private static final String FIND_BY_ITEM_BYSESSION = "from " + ImageGalleryItemVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.imageGalleryItem.uid=?";

    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from "
	    + ImageGalleryItemVisitLog.class.getName() + " as r where  r.sessionId=? and  r.user.userId =?";

    @Override
    public ImageGalleryItemVisitLog getImageGalleryItemLog(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (ImageGalleryItemVisitLog) list.get(0);
    }

    @Override
    public int getUserViewLogCount(Long toolSessionId, Long userUid) {
	List list = doFind(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userUid });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<ImageGalleryItemVisitLog> getImageGalleryItemLogBySession(Long sessionId, Long itemUid) {
	return (List<ImageGalleryItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

}
