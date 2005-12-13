package org.lamsfoundation.lams.tool.forum.persistence;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * User: conradb
 * Date: 7/06/2005
 * Time: 12:23:49
 */
public class AttachmentDao extends HibernateDaoSupport {

	public void saveOrUpdate(Attachment attachment) {
		this.getHibernateTemplate().saveOrUpdate(attachment);
	}

	public void delete(Attachment attachment) {
		this.getHibernateTemplate().delete(attachment);
	}
	
	public Attachment getById(final Long attachmentId) {
		Attachment entity = (Attachment) this.getHibernateTemplate().execute(new HibernateCallback() {
            public Object doInHibernate(Session session) throws HibernateException {
                return session.get(Attachment.class,attachmentId);
            }
        });
        return entity;
	}
}
