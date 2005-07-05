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

package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.tool.BasicToolVO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;


/**
 * This interface defines all the service available for self contained tool
 * module from lams. Any service that would be used by other lams module
 * such as, lams_learning etc, should not appear in this interface.
 * 
 * @author chris
 * @author Jacky Fang
 * @author Ozgur Demirtas 24/06/2005
 */
public interface ILamsToolService
{
    /**
     * Returns a list of all learners who can use a specific set of tool content.
     * Note that none/some/all of these users may not reach the associated activity
     * so they may not end up using the content.
     * The purpose of this method is to provide a way for tools to do logic based on 
     * completions against potential completions.
     * @param toolContentID a long value that identifies the tool content (in the Tool and in LAMS).
     * @return a List of all the Learners who are scheduled to use the content.
     * @exception in case of any problems.
     */
    public List getAllPotentialLearners(long toolContentID) throws LamsToolServiceException;
    
    public BasicToolVO getToolBySignature(final String toolSignature);
    
    public long getToolDefaultContentIdBySignature(final String toolSignature);
}
