/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao.hibernate;

import org.lamsfoundation.lams.tool.ToolContent;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.springframework.orm.hibernate.support.HibernateDaoSupport;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
 */
public class ToolContentDAO extends HibernateDaoSupport implements IToolContentDAO
{

    /**
     * 
     */
    public ToolContentDAO()
    {
        super();
    }

    /**
     * @see org.lamsfoundation.lams.tool.dao.IToolContentDAO#saveToolContent(org.lamsfoundation.lams.tool.ToolContent)
     */
    public void saveToolContent(ToolContent toolContent)
    {
        getHibernateTemplate().save(toolContent);
    }

}
