/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 8/02/2005
 ******************************************************************************** */

package org.lamsfoundation.lams.tool.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.Tool;


/**
 * 
 * @author Jacky Fang 8/02/2005
 * updated: Ozgur Demirtas 24/06/2005
 * 
 */
public interface IToolDAO
{

    public Tool getToolByID(Long toolID);
    public List getAllTools();
    public Tool getToolBySignature(final String toolSignature);
    public long getToolDefaultContentIdBySignature(final String toolSignature);
}
