/* ********************************************************************************
 *  Copyright Notice
 *  =================
 * This file contains propriety information of LAMS Foundation. 
 * Copying or reproduction with prior written permission is prohibited.
 * Copyright (c) 2005 
 * Created on 2005-1-7
 ******************************************************************************** */

package org.lamsfoundation.lams.tool;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * <p>ToolAccessMode is implemented using Ordinal-based typesafe enum pattern. 
 * It resolves the performance and potential hard coding error problems of using
 * interface to define enum type.</p>
 * 
 * <p>As a class, it can implement any interface as we want. For now, it implements
 * serializable because we might need to set it into http session. To ensure
 * serializable works properly, <code>readResolve()</code> must be overriden.</p>
 * 
 * 
 * @author Jacky Fang 2005-1-7
 * 
 */
public class ToolAccessMode implements Serializable
{

    private final transient String name;

    //Ordinal of next tool access mode to be created
    private static int nextOrdinal = 0;

    //Assign an ordinal to this tool access mode
    private final int ordinal = nextOrdinal++;

    /**
     * Private construtor to avoid instantiation
     */
    private ToolAccessMode(String name)
    {
        this.name = name;
    }

    public static final ToolAccessMode AUTHOR = new ToolAccessMode("author");
    public static final ToolAccessMode TEACHER = new ToolAccessMode("teacher");
    public static final ToolAccessMode LEARNER = new ToolAccessMode("learner");

    //This is necessary for serialization
    private static final ToolAccessMode[] VALUES = { AUTHOR, TEACHER, LEARNER };

    public String toString() {return name;};
    
    /**
     * Overidden method to ensure it is serializable.
     * @return the object instance
     * @throws ObjectStreamException
     */
    Object readResolve() throws ObjectStreamException
    {
        return VALUES[ordinal];
    }
    
    

}