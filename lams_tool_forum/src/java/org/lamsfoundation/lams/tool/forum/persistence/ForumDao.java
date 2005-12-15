package org.lamsfoundation.lams.tool.forum.persistence;

import java.util.List;

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
	public void flush() {
		this.getHibernateTemplate().flush();
	}

}
