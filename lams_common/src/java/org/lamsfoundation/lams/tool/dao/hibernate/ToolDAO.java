/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao.hibernate;

import java.util.List;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;

import org.lamsfoundation.lams.tool.BasicToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.springframework.orm.hibernate.HibernateCallback;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * @author Ozgur Demirtas 24/06/2005
 * 
 */
public class ToolDAO extends HibernateDaoSupport implements IToolDAO
{
	private static final String FIND_ALL = "from obj in class " + Tool.class.getName();
	private static final String LOAD_TOOL_BY_SIG = "from tool in class BasicToolVO where tool.toolSignature=:toolSignature";
	
	
    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolDAO#getToolByID(java.lang.Long)
     */
    public Tool getToolByID(Long toolID)
    {
        return (Tool)getHibernateTemplate().get(Tool.class,toolID);
    }
   
    public List getAllTools(){    	
    	return this.getHibernateTemplate().find(FIND_ALL);
    }
    
    public BasicToolVO getToolBySignature(final String toolSignature)
    {
        return (BasicToolVO) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
                 return session.createQuery(LOAD_TOOL_BY_SIG)
                               .setString("toolSignature",toolSignature)
                               .uniqueResult();
             }
         });
    }

    public long getToolDefaultContentIdBySignature(final String toolSignature)
    {
    	BasicToolVO tool= (BasicToolVO) getHibernateTemplate().execute(new HibernateCallback()
         {
             public Object doInHibernate(Session session) throws HibernateException
             {
                 return session.createQuery(LOAD_TOOL_BY_SIG)
                               .setString("toolSignature",toolSignature)
                               .uniqueResult();
             }
         });
        
        if (tool != null)
        	return tool.getDefaultToolContentId();
        else
        	return 0;
    }


}
