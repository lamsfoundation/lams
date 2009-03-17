/****************************************************************
 * Copyright (C) 2008 LAMS Foundation (http://lamsfoundation.org)
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
package org.lamsfoundation.lams.gradebook.dto;

import java.util.ArrayList;

import org.lamsfoundation.lams.lesson.LearnerProgress;

public class GradeBookUserDTO extends GradeBookGridRow {
    String login;
    String firstName;
    String lastName;
    String status;
    Double totalLessonMark;

    public GradeBookUserDTO() {
    }

    @Override
    public ArrayList<String> toStringArray() {
	ArrayList<String> ret = new ArrayList<String>();

	ret.add(login);
	ret.add(firstName);
	ret.add(lastName);
	ret.add(status);

	if (totalLessonMark != null && totalLessonMark != 0.0) {
	    ret.add(totalLessonMark.toString());
	} else {
	    ret.add("-");
	}

	return ret;
    }

    @Override
    public String getRowId() {
	return login;
    }

    public Double getTotalLessonMark() {
	return totalLessonMark;
    }

    public void setTotalLessonMark(Double totalLessonMark) {
	this.totalLessonMark = totalLessonMark;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
