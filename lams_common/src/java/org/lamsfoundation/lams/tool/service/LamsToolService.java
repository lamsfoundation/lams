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
package org.lamsfoundation.lams.tool.service;

import java.util.List;

import org.lamsfoundation.lams.tool.IToolVO;
import org.lamsfoundation.lams.tool.Tool;
import org.lamsfoundation.lams.tool.dao.IToolDAO;
import org.lamsfoundation.lams.tool.exception.LamsToolServiceException;


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
	public IToolDAO toolDAO;
    /**
     * TODO Implement me!
     * @see org.lamsfoundation.lams.tool.service.ILamsCoreToolService#getAllPotentialLearners(long)
     */
    public List getAllPotentialLearners(long toolContentID) throws LamsToolServiceException
    {
        return null;
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
}
