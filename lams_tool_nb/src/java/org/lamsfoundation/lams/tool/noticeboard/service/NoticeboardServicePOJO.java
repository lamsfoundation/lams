/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */

package org.lamsfoundation.lams.tool.noticeboard.service;

import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.springframework.dao.DataAccessException;
import org.lamsfoundation.lams.tool.noticeboard.NbApplicationException;
import org.lamsfoundation.lams.tool.ToolContentManager;

/**
 * An implementation of the NoticeboardService interface.
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NoticeboardServicePOJO implements INoticeboardService, ToolContentManager {

	private NoticeboardContent nbContent;
	private INoticeboardContentDAO nbContentDAO=null;
	
	private static Logger log = Logger.getLogger(NoticeboardServicePOJO.class);

	
	public NoticeboardContent retrieveNoticeboard(Long nbContentId)
	{
		try
		{
			nbContent = nbContentDAO.getNbContentById(nbContentId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard content object: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbContent;
	}
	
	public void updateNoticeboard(NoticeboardContent nbContent)
	{
		try
		{
			nbContentDAO.updateNbContent(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured while trying to update a noticeboard content object: "
                                                         + e.getMessage(),
														   e);
		}

	}
	
	public void saveNoticeboard(NoticeboardContent nbContent)
	{
		try
		{
			nbContentDAO.saveNbContent(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save the noticeboard content object: "
														+ e.getMessage(), e);
		}
	}
	
	public void removeNoticeboardSessions(NoticeboardContent nbContent)
	{
		try
		{
			nbContentDAO.removeNbSessions(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove the sessions associated with this noticeboard content object: "
														+ e.getMessage(), e);
		}
		
	}
	
	public void removeNoticeboard(Long nbContentId)
	{
		try
		{
			nbContentDAO.removeNoticeboard(nbContentId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard content object: "
					+ e.getMessage(), e);
		}
	}
	
	/* ===============Methods implemented from ToolContentManager =============== */
	
	public void copyToolContent(Long fromContentId, Long toContentId) throws NbApplicationException
	{
		
		NoticeboardContent originalNb = retrieveNoticeboard(fromContentId);
		
		try
		{
			if (originalNb != null)
			{
				NoticeboardContent newNbContent = NoticeboardContent.newInstance(originalNb, toContentId);
				if (newNbContent == null)
					throw new NbApplicationException("Error: Can not create a new copy fromContentId" + fromContentId);
				
				saveNoticeboard(newNbContent);
			}
			else
			{
				throw new NbApplicationException("ERROR: Can not retrieve original NoticeboardContent, null object returned");
			}
			
			
		}
		catch (DataAccessException e)
		{
			 throw new NbApplicationException("Exception occured when copying tool content from ID:"+ fromContentId + 
					" to ID:" + toContentId 
                          	+ e.getMessage(),e);
		}
		
	}
	
	public void setAsDefineLater(Long toolContentId)
	{
		NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
		nbContent.setDefineLater(true);
		updateNoticeboard(nbContent);
		
	}

	public void setAsRunOffline(Long toolContentId)
	{
		NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
		nbContent.setForceOffline(true);
		updateNoticeboard(nbContent);
	}
	   
	public void removeToolContent(Long toolContentId)
	{
		//all sessions relating to this content id is removed first
		//then remove the tool that is associated with this toolContentId
		
		NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
		removeNoticeboardSessions(nbContent);
		
		removeNoticeboard(toolContentId);
	}
	
/* getter setter methods to obtain the service bean */
	public INoticeboardContentDAO getNbContentDAO()
	{
		return nbContentDAO;
	}
	
	public void setNbContentDAO(INoticeboardContentDAO nbContentDAO)
	{
		this.nbContentDAO = nbContentDAO;
	}
}
