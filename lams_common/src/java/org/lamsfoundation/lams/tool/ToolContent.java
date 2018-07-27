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
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 *
 *
 *
*/
public class ToolContent implements Serializable {

    /** identifier field */
    private Long toolContentId;

    /** persistent field */
    private Tool tool;

    /** persistent field */
    private Set activities;

    public ToolContent(Tool tool) {
	this(null, tool, new HashSet());
    }

    /** full constructor */
    public ToolContent(Long toolContentId, Tool tool, Set activities) {
	this.toolContentId = toolContentId;
	this.tool = tool;
	this.activities = activities;
    }

    /** default constructor */
    public ToolContent() {
    }

    /**
     *
     *
     *
     *
     *
     */
    public Long getToolContentId() {
	return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
	this.toolContentId = toolContentId;
    }

    /**
     *
     *
     */
    public Tool getTool() {
	return this.tool;
    }

    public void setTool(Tool tool) {
	this.tool = tool;
    }

    /**
     *
     *
     *
     *
     *
     *
     *
     *
     *
     */
    public Set getActivities() {
	return this.activities;
    }

    public void setActivities(Set activities) {
	this.activities = activities;
    }

    @Override
    public String toString() {
	return new ToStringBuilder(this).append("toolContentId", getToolContentId()).toString();
    }

    @Override
    public boolean equals(Object other) {
	if ((this == other)) {
	    return true;
	}
	if (!(other instanceof ToolContent)) {
	    return false;
	}
	ToolContent castOther = (ToolContent) other;
	return new EqualsBuilder().append(this.getToolContentId(), castOther.getToolContentId()).isEquals();
    }

    @Override
    public int hashCode() {
	return new HashCodeBuilder().append(getToolContentId()).toHashCode();
    }

}
