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

public class OrgPasswordChangeForm {

    private Integer organisationID;

    private String orgName;

    private boolean staffChange = false;

    private boolean learnerChange = false;

    private String includedLearners = "";

    private String excludedLearners = "[]";

    private String includedStaff = "";

    private String excludedStaff = "[]";

    private String learnerPass;

    private String staffPass;

    private boolean email = false;

    private boolean force = false;

    public Integer getOrganisationID() {
	return organisationID;
    }

    public void setOrganisationID(Integer organisationID) {
	this.organisationID = organisationID;
    }

    public String getOrgName() {
	return orgName;
    }

    public void setOrgName(String orgName) {
	this.orgName = orgName;
    }

    public boolean getStaffChange() {
	return staffChange;
    }

    public void setStaffChange(boolean staffChange) {
	this.staffChange = staffChange;
    }

    public boolean getLearnerChange() {
	return learnerChange;
    }

    public void setLearnerChange(boolean learnerChange) {
	this.learnerChange = learnerChange;
    }

    public String getIncludedLearners() {
	return includedLearners;
    }

    public void setIncludedLearners(String includedLearners) {
	this.includedLearners = includedLearners;
    }

    public String getExcludedLearners() {
	return excludedLearners;
    }

    public void setExcludedLearners(String excludedLearners) {
	this.excludedLearners = excludedLearners;
    }

    public String getIncludedStaff() {
	return includedStaff;
    }

    public void setIncludedStaff(String includedStaff) {
	this.includedStaff = includedStaff;
    }

    public String getExcludedStaff() {
	return excludedStaff;
    }

    public void setExcludedStaff(String excludedStaff) {
	this.excludedStaff = excludedStaff;
    }

    public String getLearnerPass() {
	return learnerPass;
    }

    public void setLearnerPass(String learnerPass) {
	this.learnerPass = learnerPass;
    }

    public String getStaffPass() {
	return staffPass;
    }

    public void setStaffPass(String staffPass) {
	this.staffPass = staffPass;
    }

    public boolean isEmail() {
	return email;
    }

    public void setEmail(boolean email) {
	this.email = email;
    }

    public boolean isForce() {
	return force;
    }

    public void setForce(boolean force) {
	this.force = force;
    }

}
