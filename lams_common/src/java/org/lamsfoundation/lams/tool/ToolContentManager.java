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
 * Tool interface that defines the contract regarding tool content manipulation.
 * 
 * @author Jacky Fang 2004-12-7
 * 
 */
public interface ToolContentManager
{
    /**
     * Make a copy of requested tool content. It will be needed by lams to 
     * create a copy of learning design and start a new tool session.
     * @param fromContentId the original tool content id.
     * @param toContentId the destination tool content id.
     */
    public void copyToolContent(Long fromContentId, Long toContentId);	
    
    /**
     * Remove tool's content according specified the content id. It will be 
     * needed by lams to modify the learning design.
     * @param toolContentId the requested tool content id.
     */
    public void removeToolContent(Long toolContentId);
}
