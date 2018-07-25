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

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaItemVisitDAO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItemVisitLog;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>IKalturaItemVisitDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.kaltura.dao.IKalturaItemVisitDAO
 */
@Repository
public class KalturaItemVisitDAO extends LAMSBaseDAO implements IKalturaItemVisitDAO {

    private static final String FIND_BY_ITEM_AND_USER = "from " + KalturaItemVisitLog.class.getName()
	    + " as r where r.user.userId = ? and r.kalturaItem.uid=?";

    private static final String FIND_BY_ITEM_BYSESSION = "from " + KalturaItemVisitLog.class.getName()
	    + " as r where r.sessionId = ? and r.kalturaItem.uid=?";

    private static final String FIND_VIEW_COUNT_BY_USER = "select count(*) from " + KalturaItemVisitLog.class.getName()
	    + " as r where  r.sessionId=? and  r.user.userId =?";

    @Override
    public KalturaItemVisitLog getKalturaItemLog(Long itemUid, Long userId) {
	List list = doFind(FIND_BY_ITEM_AND_USER, new Object[] { userId, itemUid });
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (KalturaItemVisitLog) list.get(0);
    }

    @Override
    public int getUserViewLogCount(Long toolSessionId, Long userId) {
	List list = doFind(FIND_VIEW_COUNT_BY_USER, new Object[] { toolSessionId, userId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<KalturaItemVisitLog> getKalturaItemLogBySession(Long sessionId, Long itemUid) {

	return (List<KalturaItemVisitLog>) doFind(FIND_BY_ITEM_BYSESSION, new Object[] { sessionId, itemUid });
    }

}
