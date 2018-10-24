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
package org.lamsfoundation.lams.tool.survey.dto;

import org.lamsfoundation.lams.tool.survey.model.SurveyUser;

/**
 *
 * @author Dapeng Ni
 *
 */
public class ReflectDTO {
    private Long userUid;
    private String fullName;
    private String loginName;
    private boolean hasRefection;
    private String reflectInstrctions;
    private boolean finishReflection;
    private String reflect;

    public ReflectDTO(SurveyUser user) {
	this.setLoginName(user.getLoginName());
	this.setFullName(user.getFirstName() + " " + user.getLastName());
	this.setUserUid(user.getUid());
    }

    public boolean isFinishReflection() {
	return finishReflection;
    }

    public void setFinishReflection(boolean finishReflection) {
	this.finishReflection = finishReflection;
    }

    public String getFullName() {
	return fullName;
    }

    public void setFullName(String fullName) {
	this.fullName = fullName;
    }

    public boolean isHasRefection() {
	return hasRefection;
    }

    public void setHasRefection(boolean hasRefection) {
	this.hasRefection = hasRefection;
    }

    public String getLoginName() {
	return loginName;
    }

    public void setLoginName(String loginName) {
	this.loginName = loginName;
    }

    public String getReflect() {
	return reflect;
    }

    public void setReflect(String reflect) {
	this.reflect = reflect;
    }

    public String getReflectInstrctions() {
	return reflectInstrctions;
    }

    public void setReflectInstrctions(String reflectInstrctions) {
	this.reflectInstrctions = reflectInstrctions;
    }

    public Long getUserUid() {
	return userUid;
    }

    public void setUserUid(Long userUid) {
	this.userUid = userUid;
    }
}
