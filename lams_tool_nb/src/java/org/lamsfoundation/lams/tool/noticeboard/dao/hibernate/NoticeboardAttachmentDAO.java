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
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * @author mtruong
 *
 * <p>Hibernate implementation for database access of noticeboard attachment,
 * which are generally the uploads, updates and removal of online/offline
 * instruction files </p>
 */
public class NoticeboardAttachmentDAO extends HibernateDaoSupport implements INoticeboardAttachmentDAO {
	
	private static final String FIND_NB_ATTACHMENT_BY_UUID = "from " + NoticeboardAttachment.class.getName() + " as nb where nb.uuid=?";
	private static final String FIND_NB_ATTACHMENT_BY_FILENAME = "from " + NoticeboardAttachment.class.getName() + " as nb where nb.filename=?";
	private static final String GET_ATTACHMENT_FROM_CONTENT = "select nb.attachmentId from " + NoticeboardAttachment.class.getName() + " as nb where nb.nbContent= :nbContent";
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachment(java.lang.Long) */
    public NoticeboardAttachment retrieveAttachment(Long attachmentId)
    {
        return (NoticeboardAttachment)this.getHibernateTemplate().get(NoticeboardAttachment.class, attachmentId);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachmentByUuid(java.lang.Long) */
    public NoticeboardAttachment retrieveAttachmentByUuid(Long uuid)
    {
        List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_UUID)
		.setLong(0,uuid.longValue())
		.list();

		if(attachments != null && attachments.size() > 0){
			NoticeboardAttachment nb = (NoticeboardAttachment) attachments.get(0);
			return nb;
		}
		else
			return null;
	    	
    	
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachmentByFilename(java.lang.String) */
    public NoticeboardAttachment retrieveAttachmentByFilename(String filename)
    {
    	List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_FILENAME)
		.setString(0, filename)
		.list();

		if(attachments != null && attachments.size() > 0){
			NoticeboardAttachment nb = (NoticeboardAttachment) attachments.get(0);
			return nb;
		}
		else
			return null;
    }
    
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#getAttachmentIdsFromContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    public List getAttachmentIdsFromContent(NoticeboardContent nbContent)
    {
        return (getHibernateTemplate().findByNamedParam(GET_ATTACHMENT_FROM_CONTENT,
	            "nbContent",
				nbContent));
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#saveAttachment(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    public void saveAttachment(NoticeboardAttachment attachment)
    {
        this.getHibernateTemplate().saveOrUpdate(attachment);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#removeAttachment(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    public void removeAttachment(NoticeboardAttachment attachment)
    {
    	//this.getHibernateTemplate().delete(attachment);
    	removeAttachment(attachment.getUuid());
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#removeAttachment(java.lang.Long) */
    public void removeAttachment(Long uuid)
    {
        //this.getHibernateTemplate().delete(retrieveAttachmentByUuid(uuid));
    	List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_UUID)
		.setLong(0,uuid.longValue())
		.list();

		if(attachments != null && attachments.size() > 0){
			NoticeboardAttachment nb = (NoticeboardAttachment) attachments.get(0);
			this.getSession().setFlushMode(FlushMode.AUTO);
			this.getHibernateTemplate().delete(nb);
			this.getHibernateTemplate().flush();
		}
    	
    }
}
