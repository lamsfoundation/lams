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

package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import java.util.List;

import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaSessionDAO;
import org.lamsfoundation.lams.tool.qa.model.QaSession;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 *
 */
@Repository
public class QaSessionDAO extends LAMSBaseDAO implements IQaSessionDAO {

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#getSurveySessionById(long)
     */
    @Override
    public QaSession getQaSessionById(long qaSessionId) {
	String query = "from QaSession as qus where qus.qaSessionId = :qaSessionId";
	List<?> list = getSessionFactory().getCurrentSession().createQuery(query)
		.setParameter("qaSessionId", qaSessionId).setCacheable(true).list();

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
	getSession().save(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#UpdateSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    @Override
    public void UpdateQaSession(QaSession session) {
	getSession().update(session);
    }

    /**
     * @see org.lamsfoundation.lams.tool.survey.dao.interfaces.ISurveySessionDAO#deleteSurveySession(com.lamsinternational.tool.survey.domain.SurveySession)
     */
    @Override
    public void deleteQaSession(QaSession qaSession) {
	getSession().delete(qaSession);
    }

}
