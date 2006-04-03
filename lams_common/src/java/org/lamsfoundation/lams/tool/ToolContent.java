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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */
/* $$Id$$ */
package org.lamsfoundation.lams.tool;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_tool_content"
 *     
*/
public class ToolContent implements Serializable {

    /** identifier field */
    private Long toolContentId;

    /** persistent field */
    private Tool tool;

    /** persistent field */
    private Set activities;

    public ToolContent(Tool tool){
        this(null,tool,new HashSet());
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
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="tool_content_id"
     *         
     */
    public Long getToolContentId() {
        return this.toolContentId;
    }

    public void setToolContentId(Long toolContentId) {
        this.toolContentId = toolContentId;
    }

    /** 
     * @hibernate.many-to-one not-null="true"
     * @hibernate.column name="tool_id"     
     */
    public Tool getTool() {
        return this.tool;
    }

    public void setTool(Tool tool) {
        this.tool = tool;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="tool_content_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.learningdesign.Activity"
     *         
     */
    public Set getActivities() {
        return this.activities;
    }

    public void setActivities(Set activities) {
        this.activities = activities;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("toolContentId", getToolContentId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ToolContent) ) return false;
        ToolContent castOther = (ToolContent) other;
        return new EqualsBuilder()
            .append(this.getToolContentId(), castOther.getToolContentId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getToolContentId())
            .toHashCode();
    }

}
