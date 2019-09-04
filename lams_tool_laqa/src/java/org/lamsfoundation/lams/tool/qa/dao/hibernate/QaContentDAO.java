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
import org.lamsfoundation.lams.tool.qa.dao.IQaContentDAO;
import org.lamsfoundation.lams.tool.qa.model.QaCondition;
import org.lamsfoundation.lams.tool.qa.model.QaContent;
import org.lamsfoundation.lams.tool.qa.model.QaQueContent;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *
 */
@Repository
public class QaContentDAO extends LAMSBaseDAO implements IQaContentDAO {

    @Override
    public QaContent getQaByContentId(long qaId) {
	String query = "from QaContent as qa where qa.qaContentId = :qaContentId";
	List<?> list = getSessionFactory().getCurrentSession().createQuery(query).setParameter("qaContentId", qaId)
		.list();

	if (list != null && list.size() > 0) {
	    QaContent qa = (QaContent) list.get(0);
	    return qa;
	}
	return null;
    }

    @Override
    public void saveOrUpdateQa(QaContent qa) {
	getSession().saveOrUpdate(qa);
    }

    @Override
    public void removeAllQaSession(QaContent qaContent) {
	deleteAll(qaContent.getQaSessions());
    }

    @Override
    public void removeQa(Long qaContentId) {
	if (qaContentId != null) {
	    String query = "from QaContent qa where qa.qaContentId = :qaContentId";
	    Object obj = getSessionFactory().getCurrentSession().createQuery(query)
		    .setParameter("qaContentId", qaContentId.longValue()).uniqueResult();
	    getSession().delete(obj);
	}
    }

    @Override
    public void deleteQa(QaContent qaContent) {
	getSession().delete(qaContent);
    }

    @Override
    public void removeQaById(Long qaId) {
	removeQa(qaId);
    }

    @Override
    public void flush() {
	getSession().flush();
    }

    @Override
    public void deleteCondition(QaCondition condition) {
	if (condition != null && condition.getConditionId() != null) {
	    getSession().delete(condition);
	}
    }

    @Override
    public void removeQaContentFromCache(QaContent qaContent) {
	if (qaContent != null) {
	    getSession().evict(qaContent);
	}

    }

    @Override
    public void removeQuestionsFromCache(QaContent qaContent) {
	if (qaContent != null) {

	    for (QaQueContent question : qaContent.getQaQueContents()) {
		getSession().evict(question);
	    }
	}

    }

    @Override
    public void delete(Object object) {
	getSession().delete(object);
    }
}