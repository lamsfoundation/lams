/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao.hibernate;

import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
 */
public class ToolDAO extends HibernateDaoSupport implements IToolDAO
{

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolDAO#getToolByID(java.lang.Long)
     */
    public Tool getToolByID(Long toolID)
    {
        return (Tool)getHibernateTemplate().get(Tool.class,toolID);
    }

}
