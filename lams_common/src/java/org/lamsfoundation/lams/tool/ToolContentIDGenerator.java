/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 9/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.tool.dao.IToolContentDAO;


/**
 * 
 * @author Jacky Fang 9/02/2005
 * 
 */
public class ToolContentIDGenerator
{
    private IToolContentDAO toolContentDao;

    
    /**
     * @param toolContentDao The toolContentDao to set.
     */
    public void setToolContentDao(IToolContentDAO toolContentDao)
    {
        this.toolContentDao = toolContentDao;
    }
    
    public Long getToolContentIDFor(Tool tool)
    {
        ToolContent newContent = new ToolContent(tool);
        toolContentDao.saveToolContent(newContent);
        return newContent.getToolContentId();
    }
}
