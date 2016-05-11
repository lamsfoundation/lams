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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */



package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesContentDAO extends BaseDAO implements ISubmitFilesContentDAO {

    /**
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#getContentByID(java.lang.Long)
     */
    @Override
    public SubmitFilesContent getContentByID(Long contentID) {
	return (SubmitFilesContent) super.find(SubmitFilesContent.class, contentID);
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#save(org.lamsfoundation.lams.tool.sbmt.
     * SubmitFilesContent)
     */
    @Override
    public void saveOrUpdate(SubmitFilesContent content) {
	this.getHibernateTemplate().saveOrUpdate(content);
    }

}
