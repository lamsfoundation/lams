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

/* $Id$ */
package org.lamsfoundation.lams.tool.imageGallery.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.imageGallery.dao.ImageCommentDAO;
import org.lamsfoundation.lams.tool.imageGallery.model.ImageComment;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation of <code>ImageCommentDAO</code>.
 * 
 * @author Andrey Balan
 * @see org.lamsfoundation.lams.tool.imageGallery.dao.ImageCommentDAO
 */
@Repository
public class ImageCommentDAOHibernate extends LAMSBaseDAO implements ImageCommentDAO {

    private static final String FIND_BY_UID = "from " + ImageComment.class.getName()
	    + " as r where r.uid = ?";

	public ImageComment getCommentByUid(Long commentUid) {
		List list = doFind(FIND_BY_UID, commentUid);
		if (list == null || list.size() == 0)
			return null;
		return (ImageComment) list.get(0);
	}

}
