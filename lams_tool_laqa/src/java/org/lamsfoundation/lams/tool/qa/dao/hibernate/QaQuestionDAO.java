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

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.QaQueContent;
import org.lamsfoundation.lams.tool.qa.dao.IQaQuestionDAO;
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

    public QaQueContent getQuestionByDisplayOrder(Long displayOrder, Long contentUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_DISPLAY_ORDER).setLong(
		"displayOrder", displayOrder.longValue()).setLong("uid", contentUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = (QaQueContent) list.get(0);
	    return qa;
	}
	return null;
    }
    
    public QaQueContent getQuestionByUid(Long questionUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_QUESTION_UID).setLong(
		"uid", questionUid.longValue()).list();

	if (list != null && list.size() > 0) {
	    QaQueContent qa = (QaQueContent) list.get(0);
	    return qa;
	}
	return null;
    }

    public List<QaQueContent> getAllQuestionEntriesSorted(final long contentUid) {
	List<QaQueContent> list = getSessionFactory().getCurrentSession().createQuery(QaQuestionDAO.SORT_QUESTION_BY_DISPLAY_ORDER).setLong(
		"uid", contentUid).list();

	return list;
    }
    
    public List<QaQueContent> getAllQuestionEntries(final long contentUid) {
	List list = getSessionFactory().getCurrentSession().createQuery(QaQuestionDAO.LOAD_QUESTION_BY_CONTENT_UID).setLong(
		"uid", contentUid).list();

	return list;
    }

    public void createQueContent(QaQueContent queContent) {
	getSession().save(queContent);
    }

    public void saveOrUpdateQaQueContent(QaQueContent qaQuestion) {
	getSession().saveOrUpdate(qaQuestion);
    }

    public void removeQueContent(long qaQueContentId) {
	QaQueContent qaQuestion = (QaQueContent) getSession().load(QaQueContent.class,
		new Long(qaQueContentId));
	getSession().delete(qaQuestion);
    }

    public void removeQaQueContent(QaQueContent qaQuestion) {
	getSession().delete(qaQuestion);
    }
}