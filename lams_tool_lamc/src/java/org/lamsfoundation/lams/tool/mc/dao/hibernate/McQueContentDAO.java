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

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.mc.dao.IMcQueContentDAO;
import org.lamsfoundation.lams.tool.mc.model.McQueContent;
import org.springframework.stereotype.Repository;

/**
 * Hibernate implementation for database access to McQueContent for the mc tool.
 *
 * @author Ozgur Demirtas
 */
@Repository
public class McQueContentDAO extends LAMSBaseDAO implements IMcQueContentDAO {

    private static final String LOAD_QUESTION_CONTENT_BY_CONTENT_ID = "from mcQueContent in class McQueContent where mcQueContent.mcContent.uid=:contentUid order by mcQueContent.displayOrder";

    private static final String FIND_QUESTION_CONTENT_BY_UID = "from mcQueContent in class McQueContent where mcQueContent.uid=:uid";

    private static final String LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from mcQueContent in class McQueContent where mcQueContent.displayOrder=:displayOrder and mcQueContent.mcContent.uid=:mcContentUid";

    private static final String SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER = "from mcQueContent in class McQueContent where mcQueContent.mcContent.uid=:mcContentId order by mcQueContent.displayOrder";

    @Override
    public McQueContent findMcQuestionContentByUid(Long uid) {
	if (uid != null) {
	    List<?> list = getSessionFactory().getCurrentSession().createQuery(FIND_QUESTION_CONTENT_BY_UID)
		    .setParameter("uid", uid).list();

	    if (list != null && list.size() > 0) {
		McQueContent mcq = (McQueContent) list.get(0);
		return mcq;
	    }
	}
	return null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public List<McQueContent> getQuestionsByContentUid(final long contentUid) {
	List<McQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(LOAD_QUESTION_CONTENT_BY_CONTENT_ID).setParameter("contentUid", contentUid).list();

	return list;
    }

    @Override
    public McQueContent getQuestionContentByDisplayOrder(final Integer displayOrder, final Long mcContentUid) {
	List<?> list = getSessionFactory().getCurrentSession().createQuery(LOAD_QUESTION_CONTENT_BY_DISPLAY_ORDER)
		.setParameter("displayOrder", displayOrder).setParameter("mcContentUid", mcContentUid).list();

	if (list != null && list.size() > 0) {
	    McQueContent mcq = (McQueContent) list.get(0);
	    return mcq;
	}
	return null;
    }

    @Override
    public void saveOrUpdateMcQueContent(McQueContent mcQueContent) {
	this.getSession().saveOrUpdate(mcQueContent);
    }

    @Override
    public List<?> getAllQuestionEntriesSorted(final long mcContentId) {
	List<?> list = getSessionFactory().getCurrentSession().createQuery(SORT_QUESTION_CONTENT_BY_DISPLAY_ORDER)
		.setParameter("mcContentId", mcContentId).list();

	return list;
    }

    @Override
    public void removeMcQueContent(McQueContent mcQueContent) {
	if ((mcQueContent != null) && (mcQueContent.getUid() != null)) {
	    this.getSession().delete(mcQueContent);
	}
    }

    @Override
    public void releaseQuestionFromCache(McQueContent question) {
	getSession().evict(question);
    }
}