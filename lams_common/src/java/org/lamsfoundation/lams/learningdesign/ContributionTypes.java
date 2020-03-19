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

package org.lamsfoundation.lams.learningdesign;

public class ContributionTypes {

    //---------------------------------------------------------------------
    // Class level constants - Contribution types
    //---------------------------------------------------------------------
    // public static final Integer MODERATION = new Integer(1);
    //the next one was removed and no longer used
    //public static final Integer DEFINE_LATER = new Integer(2);
    public static final Integer PERMISSION_GATE = new Integer(3);
    public static final Integer SYNC_GATE = new Integer(4);
    public static final Integer SCHEDULE_GATE = new Integer(5);
    public static final Integer CHOSEN_GROUPING = new Integer(6);
    public static final Integer CONTRIBUTION = new Integer(7);
    public static final Integer SYSTEM_GATE = new Integer(8);
    public static final Integer CHOSEN_BRANCHING = new Integer(9);
    public static final Integer CONDITION_GATE = new Integer(10);
    // Tool activity was opened for editing but not closed properly
    public static final Integer CONTENT_EDITED = new Integer(11);
    public static final Integer PASSWORD_GATE = 12;
}
