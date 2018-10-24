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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

package org.lamsfoundation.lams.tool.rsrc.dao;

import java.util.List;
import java.util.Map;

import org.lamsfoundation.lams.tool.rsrc.dto.VisitLogDTO;
import org.lamsfoundation.lams.tool.rsrc.model.ResourceItemVisitLog;
import org.lamsfoundation.lams.usermanagement.service.IUserManagementService;

public interface ResourceItemVisitDAO extends DAO {

    ResourceItemVisitLog getResourceItemLog(Long itemUid, Long userId);

    int getUserViewLogCount(Long sessionId, Long userUid);

    /**
     * Return list which contains key pair which key is resource item uid, value is number view.
     *
     * @param contentId
     * @param sessionId
     * @return
     */
    Map<Long, Integer> getSummary(Long contentId, Long sessionId);

    List<ResourceItemVisitLog> getResourceItemLogBySession(Long sessionId, Long itemUid);

    List<VisitLogDTO> getPagedVisitLogsBySessionAndItem(Long sessionId, Long itemUid, int page, int size, String sortBy,
	    String sortOrder, String searchString, IUserManagementService userManagementService);

    int getCountVisitLogsBySessionAndItem(Long sessionId, Long itemUid, String searchString);

    public Object[] getDateRangeOfAccesses(Long userUid, Long toolSessionId);
}
