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
 * <p>
 * This generator is designed to create new content id for a particular tool.
 * </p>
 * <p>
 * It is configured as a Spring singleton bean. Transaction demarcation has
 * been set to <code>REQUIRED_NEW</code> to ensure it is always runing within
 * its own transaction. And THE isolation level is set to 
 * <code>READ_COMMITTED</code>. 
 * 
 * We are using MySql auto-increment generator to ensure the data correctness
 * under concurrency contention.
 * 
 * @author Jacky Fang 9/02/2005
 * 
 */
public class ToolContentIDGenerator
{
    private IToolContentDAO toolContentDao;
    
    /**
     * Method injection for Spring configuration.
     * @param toolContentDao The toolContentDao to set.
     */
    public void setToolContentDao(IToolContentDAO toolContentDao)
    {
        this.toolContentDao = toolContentDao;
    }
    
    /**
     * Get the next tool content id for a tool.
     * @param tool the target tool.
     * @return the next id object.
     */
    public Long getNextToolContentIDFor(Tool tool)
    {
        ToolContent newContent = new ToolContent(tool);
        toolContentDao.saveToolContent(newContent);
        return newContent.getToolContentId();
    }
}
