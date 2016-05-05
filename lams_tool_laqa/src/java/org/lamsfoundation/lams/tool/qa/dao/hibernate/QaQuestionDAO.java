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

/* $$Id$$ */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaQuestionDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *
 */
public class QaQuestionDAO extends HibernateDaoSupport implements IQaQuestionDAO {
    private static final String LOAD_QUESTION_BY_CONTENT_UID = "from qaQuestion in class QaQueContent where qaQuestion.qaContent.uid=:uid";
    private static final String LOAD_QUESTION_BY_DISPLAY_ORDER = "from qaQuestion in class QaQueContent where qaQuestion.displayOrder=:displayOrder and qaQuestion.qaContent.uid=:uid";
    private static final String LOAD_QUESTION_BY_QUESTION_UID = "from qaQuestion in class QaQueContent where qaQuestion.uid=:uid";
    private static final String SORT_QUESTION_BY_DISPLAY_ORDER = "from qaQuestion in class QaQueContent where qaQuestion.qaContent.uid=:uid order by qaQuestion.displayOrder";

    @Override
    public QaQueContent getQuestionByDisplayOrder(Long displayOrder, Long contentUid) {
	List<QaQueContent> list = getSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_DISPLAY_ORDER)
		.setLong("displayOrder", displayOrder.longValue()).setLong("uid", contentUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = list.get(0);
	    return qa;
	}
	return null;
    }

    @Override
    public QaQueContent getQuestionByUid(Long questionUid) {
	List<QaQueContent> list = getSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_QUESTION_UID)
		.setLong("uid", questionUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = list.get(0);
	    return qa;
	}
	return null;
    }

    @Override
    public List<QaQueContent> getAllQuestionEntriesSorted(final long contentUid) {
	List<QaQueContent> list = getSession().createQuery(QaQuestionDAO.SORT_QUESTION_BY_DISPLAY_ORDER)
		.setLong("uid", contentUid).list();

	return list;
    }

    @Override
    public List<QaQueContent> getAllQuestionEntries(final long contentUid) {
	List list = getSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_CONTENT_UID).setLong("uid", contentUid)
		.list();

	return list;
    }

    @Override
    public void createQueContent(QaQueContent queContent) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(queContent);
    }

    @Override
    public void saveOrUpdateQaQueContent(QaQueContent qaQuestion) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().saveOrUpdate(qaQuestion);
    }

    @Override
    public void removeQueContent(long qaQueContentId) {
	QaQueContent qaQuestion = (QaQueContent) this.getHibernateTemplate().load(QaQueContent.class,
		new Long(qaQueContentId));
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(qaQuestion);
    }

    @Override
    public void removeQaQueContent(QaQueContent qaQuestion) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(qaQuestion);
    }
}