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
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaItemDAO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaItem;
import org.springframework.stereotype.Repository;

@Repository
public class KalturaItemDAO extends LAMSBaseDAO implements IKalturaItemDAO {

    private static final String FIND_AUTHORING_ITEMS = "from " + KalturaItem.class.getName()
	    + " where kaltura_uid = ? and create_by_author = 1 order by create_date asc";

    private static final String FIND_ITEMS_COUNT_BY_USER = "select count(*) from " + KalturaItem.class.getName()
	    + " as r where  r.createdBy.session.sessionId=? and  r.createdBy.userId =?";

    @Override
    public List getAuthoringItems(Long kalturaUid) {
	return this.doFind(FIND_AUTHORING_ITEMS, kalturaUid);
    }

    @Override
    public KalturaItem getByUid(Long kalturaItemUid) {
	return (KalturaItem) getSession().get(KalturaItem.class, kalturaItemUid);
    }

    @Override
    public int getItemsCountByUser(Long toolSessionId, Long userId) {
	List list = doFind(FIND_ITEMS_COUNT_BY_USER, new Object[] { toolSessionId, userId });
	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
}
