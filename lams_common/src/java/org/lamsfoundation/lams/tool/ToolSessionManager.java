/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-6
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import java.util.List;



/**
 * 
 * @author Jacky Fang 2004-12-6
 * 
 */
public interface ToolSessionManager
{
    public void createToolSession(Long toolSessionId, Long toolContentId);
    
    /**
     * Call the controller service to complete and leave the tool session.
     * @param toolSessionId the runtime tool session id.
     * @return the data object that wraps the progess information.
     */
    public ProgressOutputData leaveToolSession(Long toolSessionId);
    
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId);

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds);
    
}
