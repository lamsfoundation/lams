/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.lesson;

import org.lamsfoundation.lams.AbstractLamsCommonTestCase;


/**
 * 
 * @author Jacky Fang 2/02/2005
 * 
 */
public class LessonDataAccessTestCase extends AbstractLamsCommonTestCase
{

    /**
     * @param name
     */
    public LessonDataAccessTestCase(String name)
    {
        super(name);
    }

    /**
     * @see org.lamsfoundation.lams.AbstractLamsCommonTestCase#getContextConfigLocation()
     */
    protected String[] getContextConfigLocation()
    {
        return new String[] {
                "/org/lamsfoundation/lams/lesson/lessonApplicationContext.xml"};
    }

    
}
