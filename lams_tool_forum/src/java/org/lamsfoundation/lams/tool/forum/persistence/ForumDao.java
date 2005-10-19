package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

import org.hibernate.FlushMode;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * ForumDao
 * @author conradb
 *
 *
 */
public class ForumDao extends HibernateDaoSupport {
	private static final String FIND_INSTRUCTION_FILE = "from " + Attachment.class.getName() 
			+ " as i where content_id=? and i.uuID=? and i.versionID=? and i.type=?";
	
	public void saveOrUpdate(Forum forum) {
		forum.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(forum);
	}

	public Forum getById(Long forumId) {
		return (Forum) getHibernateTemplate().get(Forum.class,forumId);
	}
	/**
	 * NOTE: before call this method, must be sure delete all messages in this forum.
	 * Example code like this:
	 * <pre>
	 * <code>messageDao.deleteForumMessage(forum.getUuid());</code>
	 * </pre>
	 * @param forum
	 */
	public void delete(Forum forum) {
		this.getHibernateTemplate().delete(forum);
	}

	public List findByNamedQuery(String name) {
		return this.getHibernateTemplate().findByNamedQuery(name);
	}

	public Forum getByContentId(Long contentID) {
		List list = getHibernateTemplate().findByNamedQuery("forumByContentId",contentID);
		if(list != null && list.size() > 0)
			return (Forum) list.get(0);
		else
			return null;
	}

	public void deleteInstrcutionFile(Long contentID, Long uuid, Long versionID, String type) {
		HibernateTemplate templ = this.getHibernateTemplate();
		if ( contentID != null && uuid != null && versionID != null ) {
			List list = getSession().createQuery(FIND_INSTRUCTION_FILE)
				.setLong(0,contentID.longValue())
				.setLong(1,uuid.longValue())
				.setLong(2,versionID.longValue())
				.setString(3,type)
				.list();
			if(list != null && list.size() > 0){
				Attachment file = (Attachment) list.get(0);
				this.getSession().setFlushMode(FlushMode.AUTO);
				templ.delete(file);
				templ.flush();
			}
		}
	}

}
