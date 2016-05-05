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
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $Id$ */
package org.lamsfoundation.lams.tool.scribe.web.actions;

import java.util.Iterator;

import org.lamsfoundation.lams.tool.scribe.dto.ScribeSessionDTO;
import org.lamsfoundation.lams.tool.scribe.model.ScribeSession;
import org.lamsfoundation.lams.tool.scribe.model.ScribeUser;
import org.lamsfoundation.lams.tool.scribe.util.ScribeUtils;

/**
 * Miscellaneous calls that set up the DTOs used by both the learning screens and export portfolio screen.
 *
 */
public class DTOUtil {

    /**
     * Create the session DTO for a user's session/group. Includes the number of votes
     * for, percentages, etc.
     * 
     * @param scribeSession
     */
    public ScribeSessionDTO createSessionDTO(ScribeSession scribeSession) {
	ScribeSessionDTO sessionDTO = new ScribeSessionDTO(scribeSession);

	int numberOfVotes = 0;
	for (Iterator iter = scribeSession.getScribeUsers().iterator(); iter.hasNext();) {
	    ScribeUser user = (ScribeUser) iter.next();
	    if (user.isReportApproved()) {
		numberOfVotes++;
	    }
	}

	int numberOfLearners = scribeSession.getScribeUsers().size();

	sessionDTO.setNumberOfVotes(numberOfVotes);
	sessionDTO.setNumberOfLearners(numberOfLearners);
	sessionDTO.setVotePercentage(ScribeUtils.calculateVotePercentage(numberOfVotes, numberOfLearners));

	return sessionDTO;
    }

}
