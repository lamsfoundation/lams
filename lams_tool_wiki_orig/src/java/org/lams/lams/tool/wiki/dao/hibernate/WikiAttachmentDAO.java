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

/* $$Id$$ */
package org.lams.lams.tool.wiki.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lams.lams.tool.wiki.WikiAttachment;
import org.lams.lams.tool.wiki.WikiContent;
import org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
/**
 * @author mtruong
 *
 * <p>Hibernate implementation for database access of wiki attachment,
 * which are generally the uploads, updates and removal of online/offline
 * instruction files </p>
 */
public class WikiAttachmentDAO extends HibernateDaoSupport implements IWikiAttachmentDAO {
	
	private static final String FIND_NB_ATTACHMENT_BY_UUID = "from " + WikiAttachment.class.getName() + " as wiki where wiki.uuid=?";
	private static final String FIND_NB_ATTACHMENT_BY_FILENAME = "from " + WikiAttachment.class.getName() + " as wiki where wiki.filename=?";
	private static final String GET_ATTACHMENT_FROM_CONTENT = "select wiki.attachmentId from " + WikiAttachment.class.getName() + " as wiki where wiki.wikiContent= :wikiContent";
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#retrieveAttachment(java.lang.Long) */
    public WikiAttachment retrieveAttachment(Long attachmentId)
    {
        return (WikiAttachment)this.getHibernateTemplate().get(WikiAttachment.class, attachmentId);
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#retrieveAttachmentByUuid(java.lang.Long) */
    public WikiAttachment retrieveAttachmentByUuid(Long uuid)
    {
        List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_UUID)
		.setLong(0,uuid.longValue())
		.list();

		if(attachments != null && attachments.size() > 0){
			WikiAttachment wiki = (WikiAttachment) attachments.get(0);
			return wiki;
		}
		else
			return null;
	    	
    	
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#retrieveAttachmentByFilename(java.lang.String) */
    public WikiAttachment retrieveAttachmentByFilename(String filename)
    {
    	List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_FILENAME)
		.setString(0, filename)
		.list();

		if(attachments != null && attachments.size() > 0){
			WikiAttachment wiki = (WikiAttachment) attachments.get(0);
			return wiki;
		}
		else
			return null;
    }
    
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#getAttachmentIdsFromContent(org.lams.lams.tool.wiki.WikiContent) */
    public List getAttachmentIdsFromContent(WikiContent wikiContent)
    {
        return (getHibernateTemplate().findByNamedParam(GET_ATTACHMENT_FROM_CONTENT,
	            "wikiContent",
				wikiContent));
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#saveAttachment(org.lams.lams.tool.wiki.WikiContent) */
    public void saveAttachment(WikiAttachment attachment)
    {
        this.getHibernateTemplate().saveOrUpdate(attachment);
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#removeAttachment(org.lams.lams.tool.wiki.WikiContent) */
    public void removeAttachment(WikiAttachment attachment)
    {
    	//this.getHibernateTemplate().delete(attachment);
    	removeAttachment(attachment.getUuid());
    }
    
    /** @see org.lams.lams.tool.wiki.dao.IWikiAttachmentDAO#removeAttachment(java.lang.Long) */
    public void removeAttachment(Long uuid)
    {
        //this.getHibernateTemplate().delete(retrieveAttachmentByUuid(uuid));
    	List attachments = getSession().createQuery(FIND_NB_ATTACHMENT_BY_UUID)
		.setLong(0,uuid.longValue())
		.list();

		if(attachments != null && attachments.size() > 0){
			WikiAttachment wiki = (WikiAttachment) attachments.get(0);
			this.getSession().setFlushMode(FlushMode.AUTO);
			this.getHibernateTemplate().delete(wiki);
			this.getHibernateTemplate().flush();
		}
    	
    }
}
