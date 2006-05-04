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
package org.lamsfoundation.lams.tool.service;

import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.ToolSession;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.dao.IToolSessionDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;
import org.lamsfoundation.lams.usermanagement.User;


/**
 * 
 * @author Jacky Fang
 * @since  2005-3-17
 * @version
 * 
 * @author Ozgur Demirtas 24/06/2005
 * 
 */
public class LamsToolService implements ILamsToolService
{
	private static Logger log = Logger.getLogger(LamsToolService.class);
 
	public IToolDAO toolDAO;
	public IToolSessionDAO toolSessionDAO;
	
    /**
     * @see org.lamsfoundation.lams.tool.service.ILamsCoreToolService#getAllPotentialLearners(long)
     */
    public Set<User> getAllPotentialLearners(long toolSessionId) throws LamsToolServiceException
    {
    	
    	ToolSession session = toolSessionDAO.getToolSession(toolSessionId);
    	if ( session != null ) {
    		return session.getLearners();
    	} else {
    		log.error("No tool session found for "+toolSessionId+". No potential learners being returned.");
    		return new HashSet<User>();
    	}
    }
    
    public IToolVO getToolBySignature(final String toolSignature)
    {
    	Tool tool = toolDAO.getToolBySignature(toolSignature);  	
    	return tool.createBasicToolVO();    	
    }

    public long getToolDefaultContentIdBySignature(final String toolSignature)
    {
    	return toolDAO.getToolDefaultContentIdBySignature(toolSignature);
    }
	
	/**
	 * @return Returns the toolDAO.
	 */
	public IToolDAO getToolDAO() {
		return toolDAO;
	}
	/**
	 * @param toolDAO The toolDAO to set.
	 */
	public void setToolDAO(IToolDAO toolDAO) {
		this.toolDAO = toolDAO;
	}

	public IToolSessionDAO getToolSessionDAO() {
		return toolSessionDAO;
	}

	public void setToolSessionDAO(IToolSessionDAO toolSessionDAO) {
		this.toolSessionDAO = toolSessionDAO;
	}
}
