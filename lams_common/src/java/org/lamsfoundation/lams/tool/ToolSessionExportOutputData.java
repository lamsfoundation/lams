/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-7
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import java.util.List;


/**
 * Value object for exporting output. We expect this object to be immutable once
 * it is created.
 * @author Jacky Fang 2004-12-7
 * 
 */
public class ToolSessionExportOutputData
{
    private List toolSessionIds;
    private String exportData;

    /**
     * Construtor
     */
    public ToolSessionExportOutputData(String exportData,List toolSessionIds)
    {
        this.exportData=exportData;
        this.toolSessionIds=toolSessionIds;
    }

    /**
     * @return Returns the exportData.
     */
    public String getExportData()
    {
        return exportData;
    }
    /**
     * @return Returns the toolSessionIds.
     */
    public List getToolSessionIds()
    {
        return toolSessionIds;
    }
}
