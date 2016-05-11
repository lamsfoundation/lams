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


package org.eucm.lams.tool.eadventure.dao.hibernate;

import java.util.List;

import org.eucm.lams.tool.eadventure.dao.EadventureParamDAO;
import org.eucm.lams.tool.eadventure.model.EadventureParam;

public class EadventureParamDAOHibernate extends BaseDAOHibernate implements EadventureParamDAO {

    private static final String FIND_BY_PARAM_NAME = "from " + EadventureParam.class.getName()
	    + " as r where r.eadventure_uid = ? and r.name =?";

    private static final String FIND_BY_EAD = "from " + EadventureParam.class.getName()
	    + " as r where r.eadventure_uid = ?";

    @Override
    public List<EadventureParam> getEadventureParamByEadContentId(Long contentId) {
	List list = getHibernateTemplate().find(FIND_BY_EAD, contentId);
	if (list == null || list.size() == 0) {
	    return null;
	}

	return list;
    }

    @Override
    public void delete(EadventureParam param) {
	this.getHibernateTemplate().delete(param);
    }

    @Override
    public String getEadventureParamTypeByNameAndEadContentID(String name, Long contentId) {

	List list = getHibernateTemplate().find(FIND_BY_PARAM_NAME, new Object[] { contentId, name });
	if (list == null || list.size() == 0) {
	    return null;
	}

	return ((EadventureParam) list.get(0)).getType();
    }

}
