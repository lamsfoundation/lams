/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams;

import junit.framework.TestCase;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;


/**
 * 
 * @author Jacky Fang 2/02/2005
 * 
 */
public abstract class AbstractLamsCommonTestCase extends TestCase
{
    protected ApplicationContext ac;
    
    /**
     * 
     */
    public AbstractLamsCommonTestCase(String name)
    {
        super(name);
    }
    
    /**
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception
    {
        super.setUp();
        ac = new ClassPathXmlApplicationContext(getContextConfigLocation());
    }
    
	protected abstract String[] getContextConfigLocation();
	
    /**
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception
    {
        super.tearDown();
    }
}
