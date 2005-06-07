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

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;

/**
 * Defines the contract that the tool service provider must follow
 * 
 *
 */
public interface INoticeboardService {
	
	/**
	 * Return the noticeboard object according to the requested content id.
	 *
	 * @param nbContentid The id of the requested noticeboard object
	 * @return NoticeboardContent object or null if not found
	 */

	public NoticeboardContent retrieveNoticeboard(Long nbContentId);
	
	/**
	 * Update the content of the noticeboard object with id corresponding
	 * to that specified in the argument.
	 * 
	 * @param nbContentid The id of the requested noticeboard object
	 *
	 */	
	public void updateNoticeboard(NoticeboardContent nbContent);
	
	
	/**
	 * Save the content of the noticeboard object
	 *
	 */	
	public void saveNoticeboard(NoticeboardContent nbContent);
	
	
	/**
	 * Remove the noticeboard sessions which contain the noticeboard content 
	 * object that is specified in the argument
	 * 
	 * @param nbContent The noticeboard content in which its corresponding sessions
	 * must be removed
	 *
	 */
	
	public void removeNoticeboardSessions(NoticeboardContent nbContent);
	
	/**
	 * Remove the noticeboard object with content id of
	 * that specified in the argument.
	 * 
	 * @param nbContentid The id of the requested noticeboard object
	 *
	 */	
	public void removeNoticeboard(Long nbContentId);
	
	
}
