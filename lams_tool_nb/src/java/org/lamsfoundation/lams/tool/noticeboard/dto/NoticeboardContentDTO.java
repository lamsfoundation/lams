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
package org.lamsfoundation.lams.tool.noticeboard.dto;

import org.lamsfoundation.lams.tool.noticeboard.NoticeboardContent;
/**
 * @author mtruong
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */

/**
 * DTO for noticeboard content. This object is stored in the session and 
 * parsed into the jsp in order to access each individual values.
 * @author mtruong
 *
 */

public class NoticeboardContentDTO {

	private Long contentId;
	private String title;
	private String content;
	private String onlineInstructions;
	private String offlineInstructions;
	
	public NoticeboardContentDTO()
	{	
	}
	
	public NoticeboardContentDTO(Long contentId,
									String title,
									String content,
									String onlineInstructions,
									String offlineInstructions)
	{
		this.contentId = contentId;
		this.title = title;
		this.content = content;
		this.onlineInstructions = onlineInstructions;
		this.offlineInstructions = offlineInstructions;
	}
	
	public NoticeboardContentDTO(NoticeboardContent nbContent)
	{
		this.contentId = nbContent.getNbContentId();
		this.title = nbContent.getTitle();
		this.content = nbContent.getContent();
		this.onlineInstructions = nbContent.getOnlineInstructions();
		this.offlineInstructions = nbContent.getOfflineInstructions();
	}
	
	/**
	 * @return Returns the content.
	 */
	public String getContent() {
		return content;
	}
	/**
	 * @return Returns the content_id.
	 */
	public Long getContentId() {
		return contentId;
	}
	/**
	 * @return Returns the offlineInstructions.
	 */
	public String getOfflineInstructions() {
		return offlineInstructions;
	}
	/**
	 * @return Returns the onlineInstructions.
	 */
	public String getOnlineInstructions() {
		return onlineInstructions;
	}
	/**
	 * @return Returns the title.
	 */
	public String getTitle() {
		return title;
	}
}
