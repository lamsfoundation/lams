/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-7
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;



/**
 * 
 * @author Jacky Fang 2004-12-7
 * 
 */
public interface ToolSessionManagerGroupingAware extends ToolSessionManager
{

    public void joinToolSession(Long toolSessionId);
    
}
