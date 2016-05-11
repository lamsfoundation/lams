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

package org.lamsfoundation.lams.tool;

import java.io.Serializable;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * When importing data, which "tool signatures" does a tool support. This maps the 1.0.2 tool types to 2.0 tool
 * signatures, and may be used for 2.1 tool versions to automatically support 2.0 tool data, even if the tools
 * themselves have changed.
 *
 * @hibernate.class table="lams_tool_import_support"
 */
public class ToolImportSupport implements Serializable {

    private static final long serialVersionUID = -6212324577067151495L;

    /** identifier field */
    private Long id;

    /** persistent field */
    private String installedToolSignature;

    /** persistent field */
    private String supportsToolSignature;

    /** full constructor */
    public ToolImportSupport(Long id, String installedToolSignature, String supportsToolSignature) {
	this.id = id;
	this.installedToolSignature = installedToolSignature;
	this.supportsToolSignature = supportsToolSignature;

    }

    /** default constructor */
    public ToolImportSupport() {
    }

    /**
     * @hibernate.id generator-class="native" type="java.lang.Long" column="id"
     */
    public Long getId() {
	return this.id;
    }

    public void setId(Long id) {
	this.id = id;
    }

    /**
     * @hibernate.property column="installed_tool_signature" length="15" not-null="true"
     */
    public String getInstalledToolSignature() {
	return this.installedToolSignature;
    }

    public void setInstalledToolSignature(String installedToolSignature) {
	this.installedToolSignature = installedToolSignature;
    }

    /**
     * @hibernate.property column="supports_tool_signature" length="50" not-null="true"
     */
    public String getSupportsToolSignature() {
	return this.supportsToolSignature;
    }

    public void setSupportsToolSignature(String supportsToolSignature) {
	this.supportsToolSignature = supportsToolSignature;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("id", getId())
		.append("installedToolSignature", getInstalledToolSignature())
		.append("supportsToolSignature", getSupportsToolSignature()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof ToolImportSupport)) {
	    return false;
	}
	ToolImportSupport castOther = (ToolImportSupport) other;
	return new EqualsBuilder().append(this.getId(), castOther.getId())
		.append(this.getInstalledToolSignature(), castOther.getInstalledToolSignature())
		.append(this.getSupportsToolSignature(), castOther.getSupportsToolSignature()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getId()).toHashCode();
    }

}
