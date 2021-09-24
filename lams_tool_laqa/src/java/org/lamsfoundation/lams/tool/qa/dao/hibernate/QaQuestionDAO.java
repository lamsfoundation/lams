/****************************************************************
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
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQuestionDAO;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *
 */
@Repository
public class QaQuestionDAO extends LAMSBaseDAO implements IQaQuestionDAO {
    private static final String LOAD_QUESTION_BY_CONTENT_UID = "from qaQuestion in class QaQueContent where qaQuestion.qaContent.uid=:uid";
    private static final String LOAD_QUESTION_BY_DISPLAY_ORDER = "from qaQuestion in class QaQueContent where qaQuestion.displayOrder=:displayOrder and qaQuestion.qaContent.uid=:uid";
    private static final String LOAD_QUESTION_BY_QUESTION_UID = "from qaQuestion in class QaQueContent where qaQuestion.uid=:uid";
    private static final String SORT_QUESTION_BY_DISPLAY_ORDER = "from qaQuestion in class QaQueContent where qaQuestion.qaContent.uid=:uid order by qaQuestion.displayOrder";

    @SuppressWarnings("unchecked")
    @Override
    public QaQueContent getQuestionByDisplayOrder(Integer displayOrder, Long contentUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(QaQuestionDAO.LOAD_QUESTION_BY_DISPLAY_ORDER).setParameter("displayOrder", displayOrder)
		.setParameter("uid", contentUid).setCacheable(true).list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = list.get(0);
	    return qa;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public QaQueContent getQuestionByUid(Long questionUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(QaQuestionDAO.LOAD_QUESTION_BY_QUESTION_UID).setParameter("uid", questionUid.longValue())
		.list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = list.get(0);
	    return qa;
	}
	return null;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QaQueContent> getAllQuestionEntriesSorted(final long contentUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(QaQuestionDAO.SORT_QUESTION_BY_DISPLAY_ORDER).setParameter("uid", contentUid)
		.setCacheable(true).list();

	return list;
    }

    @SuppressWarnings("unchecked")
    @Override
    public List<QaQueContent> getAllQuestionEntries(final long contentUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession()
		.createQuery(QaQuestionDAO.LOAD_QUESTION_BY_CONTENT_UID).setParameter("uid", contentUid)
		.setCacheable(true).list();

	return list;
    }

    @Override
    public void createQueContent(QaQueContent queContent) {
	getSession().save(queContent);
    }

    @Override
    public void saveOrUpdate(Object object) {
	getSession().saveOrUpdate(object);
    }

    @Override
    public void removeQueContent(long qaQueContentId) {
	QaQueContent qaQuestion = getSession().load(QaQueContent.class, new Long(qaQueContentId));
	getSession().delete(qaQuestion);
    }

    @Override
    public void removeQaQueContent(QaQueContent qaQuestion) {
	getSession().delete(qaQuestion);
    }
}