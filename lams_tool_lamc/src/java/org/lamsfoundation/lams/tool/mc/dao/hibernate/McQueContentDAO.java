/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
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
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.mc.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.pojos.McQueContent;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * Hibernate implementation for database access to McQueContent for the mc tool.
 *
 * @author Ozgur Demirtas
 */
public class McQueContentDAO extends HibernateDaoSupport implements IMcQueContentDAO {

    private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from mcQueContent in class McQueContent where mcQueContent.mcContentId=:mcContentId order by mcQueContent.displayOrder";

    private static final String FIND_QUESTION_CONTENT_BY_UID = "from mcQueContent in class McQueContent where mcQueContent.uid=:uid";

    private static final String REFRESH_QUESTION_CONTENT = "from mcQueContent in class McQueContent where mcQueContent.mcContentId=:mcContentId order by mcQueContent.displayOrder";

    private static final String LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from mcQueContent in class McQueContent where mcQueContent.displayOrder=:displayOrder and mcQueContent.mcContentId=:mcContentUid";

    private static final String SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from mcQueContent in class McQueContent where mcQueContent.mcContentId=:mcContentId order by mcQueContent.displayOrder";

    @Override
    public McQueContent findMcQuestionContentByUid(Long uid) {
	HibernateTemplate templ = this.getHibernateTemplate();
	if (uid != null) {
	    List list = getSession().createQuery(FIND_QUESTION_CONTENT_BY_UID).setLong("uid", uid.longValue()).list();

	    if (list != null && list.size() > 0) {
		McQueContent mcq = (McQueContent) list.get(0);
		return mcq;
	    }
	}
	return null;
    }

    @Override
    public List getQuestionsByContentUid(final long contentUid) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID).setLong("mcContentId", contentUid)
		.list();

	return list;
    }

    @Override
    public List refreshQuestionContent(final Long mcContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(REFRESH_QUESTION_CONTENT).setLong("mcContentId", mcContentId.longValue())
		.list();

	return list;
    }

    @Override
    public McQueContent getQuestionContentByDisplayOrder(final Long displayOrder, final Long mcContentUid) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER)
		.setLong("displayOrder", displayOrder.longValue()).setLong("mcContentUid", mcContentUid.longValue())
		.list();

	if (list != null && list.size() > 0) {
	    McQueContent mcq = (McQueContent) list.get(0);
	    return mcq;
	}
	return null;
    }

    @Override
    public void saveOrUpdateMcQueContent(McQueContent mcQueContent) {
	this.getHibernateTemplate().saveOrUpdate(mcQueContent);
    }

    @Override
    public void updateMcQueContent(McQueContent mcQueContent) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(mcQueContent);
    }

    @Override
    public List getAllQuestionEntriesSorted(final long mcContentId) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER).setLong("mcContentId", mcContentId)
		.list();

	return list;
    }

    @Override
    public void removeMcQueContent(McQueContent mcQueContent) {
	if ((mcQueContent != null) && (mcQueContent.getUid() != null)) {
	    this.getSession().setFlushMode(FlushMode.AUTO);
	    this.getHibernateTemplate().delete(mcQueContent);

	}
    }

    @Override
    public void releaseQuestionFromCache(McQueContent question) {
	getHibernateTemplate().evict(question);
    }
}