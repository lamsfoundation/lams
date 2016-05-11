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
import org.lamsfoundation.lams.tool.kaltura.dao.IKalturaCommentDAO;
import org.lamsfoundation.lams.tool.kaltura.model.KalturaComment;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>IKalturaCommentDAO</code>.
 *
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.kaltura.dao.IKalturaCommentDAO
 */
@Repository
public class KalturaCommentDAO extends LAMSBaseDAO implements IKalturaCommentDAO {

    private static final String FIND_BY_UID = "from " + KalturaComment.class.getName() + " as r where r.uid = ?";

    @Override
    public KalturaComment getCommentByUid(Long commentUid) {
	List list = doFind(FIND_BY_UID, commentUid);
	if (list == null || list.size() == 0) {
	    return null;
	}
	return (KalturaComment) list.get(0);
    }

}
