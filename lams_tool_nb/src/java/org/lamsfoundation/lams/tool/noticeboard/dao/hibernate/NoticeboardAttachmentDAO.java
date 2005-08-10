/*
 * Created on Jul 29, 2005
 *
 */
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardAttachment;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;
/**
 * @author mtruong
 *
 * <p>Hibernate implementation for database access of noticeboard attachment,
 * which are generally the uploads, updates and removal of online/offline
 * instruction files </p>
 */
public class NoticeboardAttachmentDAO extends HibernateDaoSupport implements INoticeboardAttachmentDAO {
    
    private static final String GET_ATTACHMENT_FROM_CONTENT = "select na.attachmentId from NoticeboardAttachment na where na.nbContent= :nbContent";
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachment(java.lang.Long) */
    public NoticeboardAttachment retrieveAttachment(Long attachmentId)
    {
        return (NoticeboardAttachment)this.getHibernateTemplate().get(NoticeboardAttachment.class, attachmentId);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachmentByUuid(java.lang.Long) */
    public NoticeboardAttachment retrieveAttachmentByUuid(Long uuid)
    {
        String query = "from NoticeboardAttachment na where na.uuid=?";
        List attachments = getHibernateTemplate().find(query,uuid);
        if (attachments!= null && attachments.size() == 0)
        {
            return null;
        }
        else
        {
            return (NoticeboardAttachment)attachments.get(0);
        }
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#retrieveAttachmentByFilename(java.lang.String) */
    public NoticeboardAttachment retrieveAttachmentByFilename(String filename)
    {
        String query= "from NoticeboardAttachment na where na.filename=?";
        List attachments = getHibernateTemplate().find(query,filename);
        if (attachments!= null && attachments.size() == 0)
        {
            return null;
        }
        else
        {
            return (NoticeboardAttachment)attachments.get(0);
        }
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
        this.getHibernateTemplate().delete(attachment);
    }
    
    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardAttachmentDAO#removeAttachment(java.lang.Long) */
    public void removeAttachment(Long uuid)
    {
        this.getHibernateTemplate().delete(retrieveAttachmentByUuid(uuid));
    }
}
