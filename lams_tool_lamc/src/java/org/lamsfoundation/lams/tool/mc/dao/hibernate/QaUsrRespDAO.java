/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.qa.dao.hibernate;

import net.sf.hibernate.Hibernate;

import org.lamsfoundation.lams.tool.qa.QaQueUsr;
import org.lamsfoundation.lams.tool.qa.QaUsrResp;
import org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * 
 * @author Ozgur Demirtas
 * 
 */
public class QaUsrRespDAO extends HibernateDaoSupport implements IQaUsrRespDAO
{

	public QaQueUsr getUserById(long userId)
    {
        return (QaQueUsr)this.getHibernateTemplate().load(QaQueUsr.class,new Long(userId));
        
    }

	public void createUserResponse(QaUsrResp qaUsrResp)
    {
    	this.getHibernateTemplate().save(qaUsrResp);    	
    }

	public QaUsrResp retrieveQaUsrResp(long responseId)
    {
    	logger.debug(logger + " " + this.getClass().getName() +  " " + "retrieveUserResponse called with: " + responseId);
        return (QaUsrResp) this.getHibernateTemplate().get(QaUsrResp.class, new Long(responseId));
    }

	
	/**
     * @see org.lamsfoundation.lams.tool.qa.dao.interfaces.IQaUsrRespDAO#saveUserResponse(com.lamsinternational.tool.qa.domain.QaUsrResp)
     */
    public void saveUserResponse(QaUsrResp resp)
    {
        this.getHibernateTemplate().save(resp);
    }

    /**
     * @see org.lamsfoundation.lams.tool.qa.dao.IQaUsrRespDAO#updateUserResponse(org.lamsfoundation.lams.tool.qa.QaUsrResp)
     */
    public void updateUserResponse(QaUsrResp resp)
    {
        this.getHibernateTemplate().update(resp);        
    }
    
    public void removeUserResponse(QaUsrResp resp)
    {
        this.getHibernateTemplate().delete(resp);        
    }

    
    public void removeUserResponseByQaQueId(Long qaQueId)
    {
    	String query = "from resp in class org.lamsfoundation.lams.tool.qa.QaUsrResp"
            + " where resp.qaQueContentId = ?";
            this.getHibernateTemplate().delete(query,qaQueId,Hibernate.LONG);
	}

}


