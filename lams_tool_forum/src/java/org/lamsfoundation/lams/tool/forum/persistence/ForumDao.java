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
			+ " as i where forum_uid=? and i.fileUuid=? and i.fileVersionId=? and i.fileType=?";
	private static final String FIND_FORUM_BY_CONTENTID = "from Forum forum where forum.contentId=?";
	
	public void saveOrUpdate(Forum forum) {
		forum.updateModificationData();
		this.getHibernateTemplate().saveOrUpdate(forum);
		this.getHibernateTemplate().flush();
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

	public Forum getByContentId(Long contentID) {
		List list = getHibernateTemplate().find(FIND_FORUM_BY_CONTENTID,contentID);
		if(list != null && list.size() > 0)
			return (Forum) list.get(0);
		else
			return null;
	}
	/**
	 * Delete content instruction files according to contentID, file version ID and its type.
	 * 
	 * @param contentID
	 * @param uuid
	 * @param versionID
	 * @param type
	 */
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

	public void flush() {
		this.getHibernateTemplate().flush();
	}

}
