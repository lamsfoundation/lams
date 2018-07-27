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

package org.lamsfoundation.lams.monitoring.dto;

import org.lamsfoundation.lams.learningdesign.Activity;

/**
 * DTO used to return the activity details needed for the learner progress email
 *
 * @author Fiona Malikoff
 */
public class EmailProgressActivityDTO {

    private Activity activity;
    private Integer numberOfLearners;
    private int depth;

    public EmailProgressActivityDTO(Activity activity, int depth, Integer numberOfUsers) {
	this.activity = activity;
	this.depth = depth;
	this.numberOfLearners = numberOfUsers;
    }

    /**
     * @return Returns the activity
     */
    public Activity getActivity() {
	return activity;
    }

    public Integer getNumberOfLearners() {
	return numberOfLearners;
    }

    public int getDepth() {
	return depth;
    }

}
