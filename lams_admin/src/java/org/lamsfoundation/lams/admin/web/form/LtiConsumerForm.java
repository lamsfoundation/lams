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

package org.lamsfoundation.lams.admin.web.form;

import org.apache.commons.lang.StringUtils;

/**
 * Form for managing LTI tool consumers (ExtServer instances).
 *
 * @author Andrey Balan
 */
public class LtiConsumerForm extends ExtServerCommonForm {

    private String ltiToolConsumerMonitorRoles;

    private String lessonFinishUrl;

    private String userIdParameterName;

    public LtiConsumerForm() {
	userIdParameterName = "user_id";
    }

    public String getLtiToolConsumerMonitorRoles() {
	return ltiToolConsumerMonitorRoles;
    }

    public void setLtiToolConsumerMonitorRoles(String ltiToolConsumerMonitorRoles) {
	this.ltiToolConsumerMonitorRoles = ltiToolConsumerMonitorRoles;
    }

    @Override
    public String getLessonFinishUrl() {
	return lessonFinishUrl;
    }

    @Override
    public void setLessonFinishUrl(String lessonFinishUrl) {
	this.lessonFinishUrl = lessonFinishUrl;
    }

    public String getUserIdParameterName() {
	return userIdParameterName;
    }

    public void setUserIdParameterName(String userIdParameterName) {
	this.userIdParameterName = StringUtils.trim(userIdParameterName);
    }
}