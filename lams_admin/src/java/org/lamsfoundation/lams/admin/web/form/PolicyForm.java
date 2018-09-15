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

public class PolicyForm {
    private Long policyUid;
    private Long policyId;
    private String policyName;
    private String summary;
    private String fullPolicy;
    private Integer policyTypeId;
    private String version;
    private Integer policyStateId = 1;
    private Boolean minorChange = false;

    public Long getPolicyUid() {
	return policyUid;
    }

    public void setPolicyUid(Long policyUid) {
	this.policyUid = policyUid;
    }

    public Long getPolicyId() {
	return policyId;
    }

    public void setPolicyId(Long policyId) {
	this.policyId = policyId;
    }

    public String getPolicyName() {
	return policyName;
    }

    public void setPolicyName(String policyName) {
	this.policyName = policyName;
    }

    public String getSummary() {
	return summary;
    }

    public void setSummary(String summary) {
	this.summary = summary;
    }

    public String getFullPolicy() {
	return fullPolicy;
    }

    public void setFullPolicy(String fullPolicy) {
	this.fullPolicy = fullPolicy;
    }

    public Integer getPolicyTypeId() {
	return policyTypeId;
    }

    public void setPolicyTypeId(Integer policyTypeId) {
	this.policyTypeId = policyTypeId;
    }

    public String getVersion() {
	return version;
    }

    public void setVersion(String version) {
	this.version = version;
    }

    public Integer getPolicyStateId() {
	return policyStateId;
    }

    public void setPolicyStateId(Integer policyStateId) {
	this.policyStateId = policyStateId;
    }

    public Boolean getMinorChange() {
	return minorChange;
    }

    public void setMinorChange(Boolean minorChange) {
	this.minorChange = minorChange;
    }

}
