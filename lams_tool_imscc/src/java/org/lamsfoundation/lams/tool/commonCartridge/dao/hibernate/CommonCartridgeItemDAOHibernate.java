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

package org.lamsfoundation.lams.tool.commonCartridge.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.commonCartridge.dao.CommonCartridgeItemDAO;
import org.lamsfoundation.lams.tool.commonCartridge.model.CommonCartridgeItem;

public class CommonCartridgeItemDAOHibernate extends BaseDAOHibernate implements CommonCartridgeItemDAO {

    private static final String FIND_AUTHORING_ITEMS = "from " + CommonCartridgeItem.class.getName()
	    + " where commonCartridge_uid = ? order by create_date asc";

    @Override
    public List getAuthoringItems(Long commonCartridgeUid) {

	return this.getHibernateTemplate().find(FIND_AUTHORING_ITEMS, commonCartridgeUid);
    }

    @Override
    public CommonCartridgeItem getByUid(Long commonCartridgeItemUid) {
	return (CommonCartridgeItem) this.getObject(CommonCartridgeItem.class, commonCartridgeItemUid);
    }

}
