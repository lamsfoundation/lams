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

import org.apache.log4j.Logger;
import org.hibernate.FlushMode;
import org.lamsfoundation.lams.tool.qa.QaContent;
import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaSession;
import org.lamsfoundation.lams.tool.qa.dao.IQaQueUsrDAO;
import org.springframework.orm.hibernate3.HibernateTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

/**
 * @author Ozgur Demirtas
 */
public class QaQueUsrDAO extends HibernateDaoSupport implements IQaQueUsrDAO {
    private static Logger logger = Logger.getLogger(QaQueUsrDAO.class.getName());

    private static final String COUNT_SESSION_USER = "select qaQueUsr.queUsrId from QaQueUsr qaQueUsr where qaQueUsr.qaSession.qaSessionId= :qaSession";
    private static final String LOAD_USER_FOR_SESSION = "from qaQueUsr in class QaQueUsr where  qaQueUsr.qaSession.qaSessionId= :qaSessionId";

    public int countSessionUser(QaSession qaSession) {
	return (getHibernateTemplate().findByNamedParam(COUNT_SESSION_USER, "qaSession", qaSession)).size();
    }

    public QaQueUsr getQaUserBySession(final Long queUsrId, final Long qaSessionId) {

	String strGetUser = "from qaQueUsr in class QaQueUsr where qaQueUsr.queUsrId=:queUsrId and qaQueUsr.qaSession.qaSessionId=:qaSessionId";
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(strGetUser).setLong("queUsrId", queUsrId.longValue()).setLong(
		"qaSessionId", qaSessionId.longValue()).list();

	if (list != null && list.size() > 0) {
	    QaQueUsr usr = (QaQueUsr) list.get(0);
	    return usr;
	}
	return null;
    }

    public List getUserBySessionOnly(final QaSession qaSession) {
	HibernateTemplate templ = this.getHibernateTemplate();
	List list = getSession().createQuery(LOAD_USER_FOR_SESSION).setLong("qaSessionId",
		qaSession.getQaSessionId().longValue()).list();
	return list;
    }

    public void createUsr(QaQueUsr usr) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().save(usr);
    }

    public void updateUsr(QaQueUsr usr) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().update(usr);
    }

    public void deleteQaQueUsr(QaQueUsr qaQueUsr) {
	this.getSession().setFlushMode(FlushMode.AUTO);
	this.getHibernateTemplate().delete(qaQueUsr);
    }

}