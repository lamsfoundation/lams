/****************************************************************
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
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 *
 */
public class QaSessionDAO extends HibernateDaoSupport implements IQaSessionDAO {

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#getSurveySessionById(long)
     */
    @Override
    public QaSession getQaSessionById(long qaSessionId) {
	String query = "from QaSession as qus where qus.qaSessionId = ?";
	List list = getSession().createQuery(query).setLong(0, qaSessionId).list();

	if (list != null && list.size() > 0) {
	    QaSession qus = (QaSession) list.get(0);
	    return qus;
	}
	return null;
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#CreateSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    @Override
    public void createSession(QaSession session) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#UpdateSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    @Override
    public void UpdateQaSession(QaSession session) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#deleteSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    @Override
    public void deleteQaSession(QaSession qaSession) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(qaSession);
    }

}
