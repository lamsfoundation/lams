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

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.query.NativeQuery;
import org.lamsfoundation.lams.dao.hibernate.LAMSBaseDAO;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.lamsfoundation.lams.tool.qa.model.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.model.QaSession;
import org.springframework.stereotype.Repository;

/**
 * @author Ozgur Demirtas
 */
@Repository
public class QaQueUsrDAO extends LAMSBaseDAO implements IQaQueUsrDAO {

    private static final String COUNT_SESSION_USER = "select qaQueUsr.queUsrId from QaQueUsr qaQueUsr where qaQueUsr.qaSession.qaSessionId= :qaSession";
    private static final String LOAD_USER_FOR_SESSION = "from qaQueUsr in class QaQueUsr where  qaQueUsr.qaSession.qaSessionId= :qaSessionId";

    @Override
    public int countSessionUser(QaSession qaSession) {
	return doFindByNamedParam(COUNT_SESSION_USER, new String[] { "qaSession" }, new Object[] { qaSession }).size();
    }

    @Override
    public QaQueUsr getQaUserBySession(final Long queUsrId, final Long qaSessionId) {

	String strGetUser = "from qaQueUsr in class QaQueUsr where qaQueUsr.queUsrId=:queUsrId and qaQueUsr.qaSession.qaSessionId=:qaSessionId";
	List<?> list = getSession().createQuery(strGetUser).setParameter("queUsrId", queUsrId)
		.setParameter("qaSessionId", qaSessionId).list();

	if (list != null && list.size() > 0) {
	    QaQueUsr usr = (QaQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    @Override
    public List<?> getUserBySessionOnly(final QaSession qaSession) {
	List<?> list = getSession().createQuery(LOAD_USER_FOR_SESSION)
		.setParameter("qaSessionId", qaSession.getQaSessionId().longValue()).list();
	return list;
    }

    @Override
    public void createUsr(QaQueUsr usr) {
	getSession().save(usr);
    }

    @Override
    public void updateUsr(QaQueUsr usr) {
	getSession().update(usr);
    }

    @Override
    public void deleteQaQueUsr(QaQueUsr qaQueUsr) {
	getSession().delete(qaQueUsr);
    }

    private void buildNameSearch(StringBuilder queryText, String searchString) {
	if (!StringUtils.isBlank(searchString)) {
	    String[] tokens = searchString.trim().split("\\s+");
	    for (String token : tokens) {
		String escToken = StringEscapeUtils.escapeSql(token);
		queryText.append(" WHERE (fullname LIKE '%").append(escToken).append("%' OR username LIKE '%")
			.append(escToken).append("%') ");
	    }
	}
    }

    private static final String GET_COUNT_USERS_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH = "SELECT COUNT(*) FROM tl_laqa11_que_usr user "
	    + " JOIN tl_laqa11_session session ON user.qa_session_id = session.uid AND session.qa_session_id = :toolSessionId ";

    @SuppressWarnings("unchecked")
    @Override
    public int getCountUsersBySessionWithSearch(final Long toolSessionId, String searchString) {

	StringBuilder queryText = new StringBuilder(GET_COUNT_USERS_FOR_SESSION_AND_QUESTION_WITH_NAME_SEARCH);
	buildNameSearch(queryText, searchString);

	NativeQuery<Object[]> query = getSession().createNativeQuery(queryText.toString());
	query.setParameter("toolSessionId", toolSessionId);
	List list = query.list();

	if (list == null || list.size() == 0) {
	    return 0;
	}
	return ((Number) list.get(0)).intValue();
    }
}