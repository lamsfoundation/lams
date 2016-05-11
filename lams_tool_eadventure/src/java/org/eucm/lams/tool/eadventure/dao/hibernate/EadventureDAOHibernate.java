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

package org.eucm.lams.tool.eadventure.dao.hibernate;

import java.util.List;

import org.eucm.lams.tool.eadventure.dao.EadventureDAO;
import org.eucm.lams.tool.eadventure.model.Eadventure;

/**
 *
 * @author Steve.Ni
 *
 * @version $Revision$
 */
public class EadventureDAOHibernate extends BaseDAOHibernate implements EadventureDAO {
    private static final String GET_RESOURCE_BY_CONTENTID = "from " + Eadventure.class.getName()
	    + " as r where r.contentId=?";

    @Override
    public Eadventure getByContentId(Long contentId) {
	List list = getHibernateTemplate().find(GET_RESOURCE_BY_CONTENTID, contentId);
	if (list.size() > 0) {
	    return (Eadventure) list.get(0);
	} else {
	    return null;
	}
    }

    @Override
    public Eadventure getByUid(Long eadventureUid) {
	return (Eadventure) getObject(Eadventure.class, eadventureUid);
    }

    @Override
    public void delete(Eadventure eadventure) {
	this.getHibernateTemplate().delete(eadventure);
    }

}
