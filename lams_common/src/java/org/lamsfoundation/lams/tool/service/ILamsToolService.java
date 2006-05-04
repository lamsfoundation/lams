/***************************************************************************
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
 * ***********************************************************************/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.service;

import java.util.Set;

import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;


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
    public Set<User> getAllPotentialLearners(long toolSessionID) throws LamsToolServiceException;
    
    public IToolVO getToolBySignature(final String toolSignature);
    
    public long getToolDefaultContentIdBySignature(final String toolSignature);
}
