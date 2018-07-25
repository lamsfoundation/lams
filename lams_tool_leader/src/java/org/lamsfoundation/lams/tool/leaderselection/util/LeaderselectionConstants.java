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


package org.lamsfoundation.lams.tool.leaderselection.util;

public interface LeaderselectionConstants {
    public static final String TOOL_SIGNATURE = "lalead11";

    // Attribute names
    public static final String ATTR_SESSION_MAP = "sessionMap";
    public static final String ATTR_GROUP_LEADER = "groupLeader";
    public static final String ATTR_GROUP_USERS = "groupUsers";
    public static final String ATTR_TOOL_SESSION_ID = "toolSessionId";
    public static final String ATTR_CONTENT = "content";
    public static final String ATTR_IS_GROUPED_ACTIVITY = "isGroupedActivity";

    public static final String ATTR_SESSION_MAP_ID = "sessionMapID";

    public static final String OUTPUT_NAME_IS_LEARNER_A_LEADER = "user.is.leader";
    public static final String OUTPUT_NAME_LEADER_USERID = "leader.user.id";
    public static final String SUCCESS = "success";
}
