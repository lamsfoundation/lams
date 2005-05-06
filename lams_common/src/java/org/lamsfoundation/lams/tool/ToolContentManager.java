/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
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
 * ***********************************************************************/
package org.lamsfoundation.lams.tool;


/**
 * Tool interface that defines the contract regarding tool content manipulation.
 * 
 * @author Jacky Fang 2004-12-7
 * @author Fiona Malikoff May 2005
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
    
    /** This tool content should be define later, that is, the 
     * teacher will define the content at runtime. The toolContentId
     * should already exist in the tool. This method will normally be
     * called after copyToolContent.
     * @param toolContentId the tool content id of the tool content to be changed.
     */
    public void setAsDefineLater(Long toolContentId);

    /** This tool content should be setup to run offline, that is, the 
     * activity will be done offline. The toolContentId
     * should already exist in the tool. This method will normally be
     * called after copyToolContent.
     * @param toolContentId the tool content id of the tool content to be changed.
     */
    public void setAsRunOffline(Long toolContentId);
    
    /**
     * Remove tool's content according specified the content id. It will be 
     * needed by lams to modify the learning design.
     * @param toolContentId the requested tool content id.
     */
    public void removeToolContent(Long toolContentId);
}
