/*
 * Created on Jul 29, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoticeboardAttachmentDAO extends HibernateDaoSupport implements INoticeboardAttachmentDAO {
    
    private static final String GET_ATTACHMENT_FROM_CONTENT = "select na.attachmentId from NoticeboardAttachment na where na.nbContent= :nbContent";
    
    public NoticeboardAttachment retrieveAttachment(Long attachmentId)
    {
        return (NoticeboardAttachment)this.getHibernateTemplate().get(NoticeboardAttachment.class, attachmentId);
    }
    
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
    
    
    /* get the list of attachment ids which have the toolcontentid equals to that of nbCotnetn.getNbContentId */
    public List getAttachmentIdsFromContent(NoticeboardContent nbContent)
    {
        return (getHibernateTemplate().findByNamedParam(GET_ATTACHMENT_FROM_CONTENT,
	            "nbContent",
				nbContent));
    }
    
    public void saveAttachment(NoticeboardAttachment attachment)
    {
        this.getHibernateTemplate().saveOrUpdate(attachment);
    }
    
    public void removeAttachment(NoticeboardAttachment attachment)
    {
        this.getHibernateTemplate().delete(attachment);
    }

}
