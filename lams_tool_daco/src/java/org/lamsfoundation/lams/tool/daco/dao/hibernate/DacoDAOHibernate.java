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

package org.lamsfoundation.lams.tool.daco.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.daco.dao.DacoDAO;
import org.lamsfoundation.lams.tool.daco.model.Daco;
import org.springframework.stereotype.Repository;

/**
 *
 * @author Marcin Cieslak
 *
 * @version $Revision$
 */
@Repository
public class DacoDAOHibernate extends LAMSBaseDAO implements DacoDAO {
    private static final String FIND_BY_CONTENT_ID = "from " + Daco.class.getName() + " as r where r.contentId=?";

    @Override
    public Daco getByContentId(Long contentId) {
	List<?> list = doFindCacheable(DacoDAOHibernate.FIND_BY_CONTENT_ID, contentId);
	if (list.size() > 0) {
	    return (Daco) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Daco getByUid(Long dacoUid) {
	return (Daco) getObject(Daco.class, dacoUid);
    }
}