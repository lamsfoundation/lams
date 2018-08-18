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



package org.lamsfoundation.lams.tool.mindmap.util.xmlmodel;

import java.util.ArrayList;

/**
 * XML Model Class for Poll Response in Mindmap.
 * Sends to Flash the list of Requests done by other users.
 *
 * @author Ruslan Kazakov
 */
public class PollResponseModel {
    // list of Requests (Actions)
    ArrayList<NotifyRequestModel> actions = new ArrayList<NotifyRequestModel>();

    public ArrayList<NotifyRequestModel> getActions() {
        return actions;
    }

    /** Default Constructor */
    public PollResponseModel() {
    }

    /**
     * Adds NotifyRequest to PollResponse
     * 
     * @param concept
     */
    public void addNotifyRequest(NotifyRequestModel notifyRequestModel) {
	actions.add(notifyRequestModel);
    }
}
