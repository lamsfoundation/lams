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
package org.lamsfoundation.lams.tool.noticeboard.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author mtruong
 *         <p>
 *         Hibernate implementation for database access to Noticeboard content for the noticeboard tool.
 *         </p>
 */

public class NoticeboardContentDAO extends HibernateDaoSupport implements INoticeboardContentDAO {

    private static final String FIND_NB_CONTENT = "from " + NoticeboardContent.class.getName()
	    + " as nb where nb.nbContentId=?";

    private static final String LOAD_NB_BY_SESSION = "select nb from NoticeboardContent nb left join fetch "
	    + "nb.nbSessions session where session.nbSessionId=:sessionId";

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#getNbContentByUID(java.lang.Long) */
    @Override
    public NoticeboardContent getNbContentByUID(Long uid) {
	return (NoticeboardContent) this.getHibernateTemplate().get(NoticeboardContent.class, uid);
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#findNbContentById(java.lang.Long) */
    @Override
    public NoticeboardContent findNbContentById(Long nbContentId) {
	String query = "from NoticeboardContent as nb where nb.nbContentId = ?";
	List content = getHibernateTemplate().find(query, nbContentId);

	if (content != null && content.size() == 0) {
	    return null;
	} else {
	    return (NoticeboardContent) content.get(0);
	}

    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#getNbContentBySession(java.lang.Long) */
    @Override
    public NoticeboardContent getNbContentBySession(final Long nbSessionId) {
	return (NoticeboardContent) getHibernateTemplate().execute(new HibernateCallback() {

	    @Override
	    public Object doInHibernate(Session session) throws HibernateException {
		return session.createQuery(LOAD_NB_BY_SESSION).setLong("sessionId", nbSessionId.longValue())
			.uniqueResult();
	    }
	});
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#saveNbContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    @Override
    public void saveNbContent(NoticeboardContent nbContent) {
	this.getHibernateTemplate().save(nbContent);
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#updateNbContent(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    @Override
    public void updateNbContent(NoticeboardContent nbContent) {
	this.getHibernateTemplate().update(nbContent);
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#removeNoticeboard(java.lang.Long) */
    @Override
    public void removeNoticeboard(Long nbContentId) {
	if (nbContentId != null) {
	    //String query = "from org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent as nb where nb.nbContentId=?";
	    List list = getSession().createQuery(FIND_NB_CONTENT).setLong(0, nbContentId.longValue()).list();

	    if (list != null && list.size() > 0) {
		NoticeboardContent nb = (NoticeboardContent) list.get(0);
		this.getSession().setFlushMode(FlushMode.AUTO);
		this.getHibernateTemplate().delete(nb);
		this.getHibernateTemplate().flush();
	    }
	}
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#removeNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    @Override
    public void removeNoticeboard(NoticeboardContent nbContent) {
	removeNoticeboard(nbContent.getNbContentId());
    }

    /** @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#removeNbSessions(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent) */
    @Override
    public void removeNbSessions(NoticeboardContent nbContent) {
	this.getHibernateTemplate().deleteAll(nbContent.getNbSessions());
    }

    /**
     * @see org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO#addNbSession(java.lang.Long,
     *      org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
     */
    @Override
    public void addNbSession(Long nbContentId, NoticeboardSession nbSession) {
	NoticeboardContent content = findNbContentById(nbContentId);
	nbSession.setNbContent(content);
	content.getNbSessions().add(nbSession);
	this.getHibernateTemplate().saveOrUpdate(nbSession);
	this.getHibernateTemplate().saveOrUpdate(content);
    }

    @Override
    public void delete(Object object) {
	getHibernateTemplate().delete(object);
    }

}
