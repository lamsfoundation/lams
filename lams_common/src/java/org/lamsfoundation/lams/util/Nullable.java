/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2004 
 * Created on 2004-12-3
 ******************************************************************************** */

package org.lamsfoundation.lams.util;


/**
 * Null Object pattern. This interface is defined to avoid the use of NULL. 
 * The domain object that allows NULL as return value should implement this
 * interface.
 * 
 * @author Jacky Fang 2004-12-3
 * 
 */
public interface Nullable
{
    /**
     * contract to indicate whether current object is null.
     * @return
     */
    public boolean isNull();
}
