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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.dao.hibernate.BaseDAO;
import org.lamsfoundation.lams.tool.sbmt.InstructionFiles;
import org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent;
import org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;

/**
 * @author Manpreet Minhas
 */
public class SubmitFilesContentDAO extends BaseDAO implements ISubmitFilesContentDAO {

	private static final String FIND_INSTRUCTION_FILE = "from " + InstructionFiles.class.getName() 
													+ " as i where content_id=? and i.uuID=? and i.versionID=? and i.type=?";
	/**
	 * (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#getContentByID(java.lang.Long)
	 */
	public SubmitFilesContent getContentByID(Long contentID) {
		return (SubmitFilesContent) super.find(SubmitFilesContent.class,contentID);
	}

	/* (non-Javadoc)
	 * @see org.lamsfoundation.lams.tool.sbmt.dao.ISubmitFilesContentDAO#save(org.lamsfoundation.lams.tool.sbmt.SubmitFilesContent)
	 */
	public void saveOrUpdate(SubmitFilesContent content) {
		this.getSession().setFlushMode(FlushMode.COMMIT);
		this.getHibernateTemplate().saveOrUpdate(content);
		this.getHibernateTemplate().flush();
	}

	public void deleteInstructionFile(Long contentID, Long uuid, Long versionID, String type) {
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( contentID != null && uuid != null && versionID != null ) {
			List list = getSession().createQuery(FIND_INSTRUCTION_FILE)
				.setLong(0,contentID.longValue())
				.setLong(1,uuid.longValue())
				.setLong(2,versionID.longValue())
				.setString(3,type)
				.list();
			if(list != null && list.size() > 0){
				InstructionFiles file = (InstructionFiles) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(file);
				templ.flush();
			}
		}
		
	}
		
}
