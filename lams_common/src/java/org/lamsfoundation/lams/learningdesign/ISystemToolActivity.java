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


package org.lamsfoundation.lams.learningdesign;

import org.lamsfoundation.lams.tool.SystemTool;

/**
 * An activity that implements the ISystemToolActivity interface has a matching record in the lams_system_tool table.
 *
 * Can't be done easily with regular inheritance as some system tools (Gates, Grouping) are simple activities, whereas
 * others (branching) are complex activities.
 *
 * If you add a new activity that is a system tool, just make sure you set up the system_tool_id column in the
 * hibernate mapping, add the system tool to the constructor and add a line to the createCopy method that copies
 * the system tool field.
 *
 */
public interface ISystemToolActivity {

    public SystemTool getSystemTool();

    public void setSystemTool(SystemTool systemTool);

}
