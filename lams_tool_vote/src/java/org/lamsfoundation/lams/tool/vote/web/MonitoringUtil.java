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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ***********************************************************************/

package org.lamsfoundation.lams.tool.vote.web;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import org.lamsfoundation.lams.tool.vote.VoteAppConstants;
import org.lamsfoundation.lams.tool.vote.dto.VoteGeneralMonitoringDTO;
import org.lamsfoundation.lams.tool.vote.dto.VoteMonitoredUserDTO;
import org.lamsfoundation.lams.tool.vote.util.VoteComparator;
import org.lamsfoundation.lams.tool.vote.web.form.VoteMonitoringForm;

/**
 * More generic monitoring mode functions live here
 * 
 * @author Ozgur Demirtas
 */
public class MonitoringUtil implements VoteAppConstants {

    public static Map<String, VoteMonitoredUserDTO> convertToVoteMonitoredUserDTOMap(List<VoteMonitoredUserDTO> list) {
	Map<String, VoteMonitoredUserDTO> map = new TreeMap<String, VoteMonitoredUserDTO>(new VoteComparator());

	Iterator<VoteMonitoredUserDTO> listIterator = list.iterator();
	Long mapIndex = new Long(1);

	while (listIterator.hasNext()) {
	    VoteMonitoredUserDTO data = listIterator.next();

	    map.put(mapIndex.toString(), data);
	    mapIndex = new Long(mapIndex.longValue() + 1);
	}
	return map;
    }

    public static void repopulateRequestParameters(HttpServletRequest request, VoteMonitoringForm voteMonitoringForm,
	    VoteGeneralMonitoringDTO voteGeneralMonitoringDTO) {

	String toolContentID = request.getParameter(VoteAppConstants.TOOL_CONTENT_ID);
	voteMonitoringForm.setToolContentID(toolContentID);
	voteGeneralMonitoringDTO.setToolContentID(toolContentID);

	String responseId = request.getParameter(VoteAppConstants.RESPONSE_ID);
	voteMonitoringForm.setResponseId(responseId);
	voteGeneralMonitoringDTO.setResponseId(responseId);

	String currentUid = request.getParameter(VoteAppConstants.CURRENT_UID);
	voteMonitoringForm.setCurrentUid(currentUid);
    }

}
