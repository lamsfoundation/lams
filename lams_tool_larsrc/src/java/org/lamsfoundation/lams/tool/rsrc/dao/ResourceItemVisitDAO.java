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
package org.lamsfoundation.lams.tool.rsrc.dao;

import java.util.List;

import org.lamsfoundation.lams.tool.rsrc.dto.Summary;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;

public interface ResourceItemVisitDAO extends DAO {

	
	public ResourceItemVisitLog getResourceItemLog(Long itemUid,Long userId);

	public int getUserViewLogCount(Long sessionId, Long userUid);
	/**
	 * Return list which contains following element: <br>
	 * 
	 * <li>session_id</li>
	 * <li>session_name</li>
	 * <li>ResourceItem.uid</li>
	 * <li>ResourceItem.item_type</li>
	 * <li>ResourceItem.create_by_author</li>
	 * <li>ResourceItem.is_hide</li>
	 * <li>ResourceItem.title</li>
	 * <li>User.login_name</li>
	 * <li>count(resource_item_uid)</li>
	 * 
	 * @param contentId
	 * @return
	 */
	public List<Summary> getSummary(Long contentId);
	
	public List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId,Long itemUid);

}
