/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.AbstractLamsCommonTestCase;
import org.lamsfoundation.lams.tool.dao.IToolContentDAO;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolContentDAO;
import org.lamsfoundation.lams.tool.dao.hibernate.ToolDAO;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * 
 */
public class ToolDataAccessTestCase extends AbstractLamsCommonTestCase
{

    protected IToolContentDAO toolContentDao;
    protected IToolDAO toolDao;
    
    //Test tool id - survey tool
    protected final Long TEST_TOOL_ID = new Long(6);
    protected Tool testTool;
    /*
     * @see AbstractLamsCommonTestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        
        toolContentDao = (ToolContentDAO)this.ac.getBean("toolContentDAO");
        toolDao = (ToolDAO)this.ac.getBean("toolDAO");
        
    }

    /*
     * @see AbstractLamsCommonTestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }

    /**
     * Constructor for ToolDataAccessTestCase.
     * @param arg0
     */
    public ToolDataAccessTestCase(String arg0)
    {
        super(arg0);
    }
    /**
     * @see org.lamsfoundation.lams.AbstractLamsCommonTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
        return new String[] { "/org/lamsfoundation/lams/tool/toolApplicationContext.xml",
                			  "applicationContext.xml"};
    }
    
    
}
