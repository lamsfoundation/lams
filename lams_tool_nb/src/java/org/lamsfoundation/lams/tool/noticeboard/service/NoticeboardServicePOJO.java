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

import java.util.Date;


import org.apache.log4j.Logger;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
import org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardContentDAO;
import org.lamsfoundation.lams.tool.noticeboard.dao.INoticeboardSessionDAO;
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
	
	private NoticeboardSession nbSession;
	private INoticeboardSessionDAO nbSessionDAO = null;
	
	private static Logger log = Logger.getLogger(NoticeboardServicePOJO.class);

	
	/* ==============================================================================
	 * Methods for access to NoticeboardContent objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardByUID(Long)
	 */
	public NoticeboardContent retrieveNoticeboardByUID(Long uid)
	{
	    try
	    {
	        nbContent = nbContentDAO.getNbContentByUID(uid);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("An exception has occured while trying to retrieve noticeboard content: "
	                							+ e.getMessage(),
	                							e);
	    }
	    return nbContent;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboard(Long)
	 */
	public NoticeboardContent retrieveNoticeboard(Long nbContentId)
	{
		try
		{
			nbContent = nbContentDAO.findNbContentById(nbContentId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbContent;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardBySessionID(Long)
	 */
	public NoticeboardContent retrieveNoticeboardBySessionID(Long nbSessionId)
	{
	    try
		{
			nbContent = nbContentDAO.getNbContentBySession(nbSessionId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard content: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbContent;
	}
	
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#updateNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
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
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
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
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboardSessions(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
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
	 
	/**
     * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
     */
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
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeNoticeboard(org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent)
	 */
	public void removeNoticeboard(NoticeboardContent nbContent)
	{
	    try
		{
			nbContentDAO.removeNoticeboard(nbContent);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard content object: "
					+ e.getMessage(), e);
		}
	}
	
	/* ==============================================================================
	 * Methods for access to NoticeboardSession objects
	 * ==============================================================================
	 */
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardSession(Long)
	 */
	public NoticeboardSession retrieveNoticeboardSession(Long nbSessionId)
	{
	    try
	    {
	        nbSession = nbSessionDAO.findNbSessionById(nbSessionId);
	    }
	    catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard session object: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbSession;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#retrieveNoticeboardSessionByUID(Long)
	 */
	
	public NoticeboardSession retrieveNoticeboardSessionByUID(Long uid)
	{
	    try
	    {
	        nbSession = nbSessionDAO.getNbSessionByUID(uid);
	    }
	    catch (DataAccessException e)
		{
			throw new NbApplicationException("An exception has occured when trying to retrieve noticeboard session object: "
                                                         + e.getMessage(),
														   e);
		}
		
		return nbSession;
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#saveNoticeboardSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void saveNoticeboardSession(NoticeboardSession nbSession)
	{
	    try
	    {
	        nbSessionDAO.saveNbSession(nbSession);
	    }
	    catch(DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to save this noticeboard session: "
	                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#updateNoticeboardSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void updateNoticeboardSession(NoticeboardSession nbSession)
	{
	    try
	    {
	        nbSessionDAO.updateNbSession(nbSession);
	    }
	    catch (DataAccessException e)
	    {
	        throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to update this noticeboard session: "
                	+e.getMessage(), e);
	    }
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeSession(Long)
	 */
	public void removeSession(Long nbSessionId)
	{
	    try
		{
			nbSessionDAO.removeNbSession(nbSessionId);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard session object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeSession(org.lamsfoundation.lams.tool.noticeboard.NoticeboardSession)
	 */
	public void removeSession(NoticeboardSession nbSession)
	{
	    try
		{
			nbSessionDAO.removeNbSession(nbSession);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard session object: "
					+ e.getMessage(), e);
		}
	}
	
	/**
	 * @see org.lamsfoundation.lams.tool.noticeboard.service.INoticeboardService#removeSessionByUID(Long)
	 */
	public void removeSessionByUID(Long uid)
	{
	    try
		{
			nbSessionDAO.removeNbSessionByUID(uid);
		}
		catch (DataAccessException e)
		{
			throw new NbApplicationException("EXCEPTION: An exception has occurred while trying to remove this noticeboard session object: "
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
		NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
		removeNoticeboard(toolContentId);
	}
	

	/* ===============Methods implemented from ToolContentManager =============== */
	
	/**
	 * Creates a new noticeboard session with noticeboard
	 * content with id <code>toolContentId</code>
	 * <code>toolSessionId</code> and <code>toolContentId</code> must not be null
	 * 
	 * @param toolSessionId the new noticeboard session id to create
	 * @param toolContentId the tool content id which is associated with the toolSessionId
	 */
	public void createToolSession(Long toolSessionId, Long toolContentId)
	{
	    if (toolSessionId == null || toolContentId == null)
	    {
	        String error = "Noticeboard Session cannot be created, toolContentId or toolSessionId must not be null";
	        throw new NbApplicationException(error);
	    }
	    
	    NoticeboardContent nbContent = retrieveNoticeboard(toolContentId);
	    
	    NoticeboardSession nbSession = new NoticeboardSession(toolSessionId,
	            											  nbContent,
	            											  new Date(System.currentTimeMillis()),
	            											  NoticeboardSession.NOT_ATTEMPTED);
	    
	    saveNoticeboardSession(nbSession);
	    
	}
	
/*	public String leaveToolSession(Long toolSessionId, User learner)
	{
	    
	}
    
    public ToolSessionExportOutputData exportToolSession(Long toolSessionId)
	{
	    
	}

    public ToolSessionExportOutputData exportToolSession(List toolSessionIds)
	{
	    
	}
	
*/	
	
	
	/* getter setter methods to obtain the service bean */
	public INoticeboardContentDAO getNbContentDAO()
	{
		return nbContentDAO;
	}
	
	public void setNbContentDAO(INoticeboardContentDAO nbContentDAO)
	{
		this.nbContentDAO = nbContentDAO;
	}
	
	public INoticeboardSessionDAO getNbSessionDAO()
	{
	    return nbSessionDAO;
	}
	
	public void setNbSessionDAO(INoticeboardSessionDAO nbSessionDAO)
	{
	    this.nbSessionDAO = nbSessionDAO;
	}
}
