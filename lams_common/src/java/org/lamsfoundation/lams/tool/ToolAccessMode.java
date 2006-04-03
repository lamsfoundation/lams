/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool;

import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * <p>ToolAccessMode is implemented using Ordinal-based typesafe enum pattern. 
 * It resolves the performance and potential hard coding error problems of using
 * interface to define enum type.</p>
 * 
 * <p>This set of modes is a helper for tools. Some tools may want to use the same
 * code for the three versions of the learner screen (ordinary, learner progress and
 * preview) or for the two versions of export portfolio (leaner and teacher).
 * If a tool wants to do this, they should include mode=author, mode=teacher
 * and mode=learner in their url definitions, and use the helper methods
 * WebUtil.readToolAccessModeParam() and WebUtil.getToolAccessMode() to access
 * the modes.</p>
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