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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	

package org.lamsfoundation.lams.tool.sbmt.dao;

import java.util.List;

import org.lamsfoundation.lams.dao.IBaseDAO;
import org.lamsfoundation.lams.notebook.service.ICoreNotebookService;
import org.lamsfoundation.lams.tool.sbmt.SubmitUser;
import org.lamsfoundation.lams.tool.sbmt.dto.StatisticDTO;

public interface ISubmitUserDAO extends IBaseDAO  {

	/**
	 * get user from some Tool Session
	 * @param sessionID
	 * @param userID
	 * @return
	 */
	public SubmitUser getLearner(Long sessionID, Integer userID);
	public SubmitUser getContentUser(Long contentId, Integer userID);
	
	public List<SubmitUser> getUsersBySession(Long sessionID);
	
	public void saveOrUpdateUser(SubmitUser user);

	List<Object[]> getUsersForTablesorter(final Long sessionId, int page, int size, int sorting, String searchString, 
		boolean getNotebookEntries, ICoreNotebookService coreNotebookService );
	
	int getCountUsersBySession(final Long sessionId, String searchString);

	List<StatisticDTO> getStatisticsBySession(final Long contentId);
}
