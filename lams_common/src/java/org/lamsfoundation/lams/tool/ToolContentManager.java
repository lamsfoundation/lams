/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0 
 * as published by the Free Software Foundation.
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

import org.lamsfoundation.lams.tool.exception.DataMissingException;
import org.lamsfoundation.lams.tool.exception.SessionDataExistsException;
import org.lamsfoundation.lams.tool.exception.ToolException;


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
     * Make a copy of requested tool content. It will be needed by LAMS to 
     * create a copy of learning design and start a new tool session. If 
     * no content exists with the given tool content id, then use the 
     * default content id.
     * 
     * @param fromContentId the original tool content id.
     * @param toContentId the destination tool content id.
     * @throws ToolException if an error occurs e.g. defaultContent is missing
     */
    public void copyToolContent(Long fromContentId, Long toContentId)	
    	throws ToolException;
   
    /** This tool content should be define later, that is, the 
     * teacher will define the content at runtime. The toolContentId
     * should already exist in the tool. This method will normally be
     * called after copyToolContent.
     * @param toolContentId the tool content id of the tool content to be changed.
     * @throws DataMissingException if no tool content matches the toolContentId 
     * @throws ToolException if any other error occurs
      */
    public void setAsDefineLater(Long toolContentId)
    	throws DataMissingException, ToolException;


    /** This tool content should be setup to run offline, that is, the 
     * activity will be done offline. The toolContentId
     * should already exist in the tool. This method will normally be
     * called after copyToolContent.
     * @param toolContentId the tool content id of the tool content to be changed.
     * @throws DataMissingException if no tool content matches the toolContentId 
     * @throws ToolException if any other error occurs
     */
    public void setAsRunOffline(Long toolContentId)
    	throws DataMissingException, ToolException;

    /**
     * Remove tool's content according specified the content id. It will be 
     * needed by lams to modify the learning design. 
     * 
     * If the tool content includes files in the content repository (e.g.
     * online and offline instructions) then the files should be removed
     * from the repository.
     * 
     * If session data for this toolContentId exists and removeSessionData = true, 
     * then the tool should delete the session data as well as the content data.
     * 
     * If session data for this toolContentId exists and removeSessionData = false, 
     * then the tool should throw SessionDataExists.
     * 
     * If no matching data exists, the tool should return without throwing 
     * an exception.
     * 
     * @param toolContentId the requested tool content id.
     * @param removeSessionData should it remove any related session data?
     * @throws ToolException if any other error occurs
     */
    public void removeToolContent(Long toolContentId, boolean removeSessionData) 
    	throws SessionDataExistsException, ToolException;
    
    /**
     * Export the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws DataMissingException if no tool content matches the toolSessionId 
     * @throws ToolException if any other error occurs
     */
    public void exportToolContent(Long toolContentId, String toPath) 
    	throws DataMissingException, ToolException;

    /**
     * Import the XML fragment for the tool's content, along with any files needed
     * for the content.
     * @throws ToolException if any other error occurs
     */
    public void importToolContent(Long toolContentId, String toolContentPath) 
    	throws ToolException;
    
 
}
