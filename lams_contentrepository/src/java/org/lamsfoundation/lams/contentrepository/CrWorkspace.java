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
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */

/* $$Id$$ */	
package org.lamsfoundation.lams.contentrepository;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;


/** 
 *        @hibernate.class
 *         table="lams_cr_workspace"
 * 
 * 		  @hibernate.cache usage = "transactional"
*/
public class CrWorkspace implements IWorkspace,Serializable {

    /** identifier field */
    private Long workspaceId;

    /** persistent field */
    private String name;

    /** persistent field */
    private Set crWorkspaceCredentials;

    /** persistent field */
    private Set crNodes;

    /** full constructor */
    public CrWorkspace(String name, Set crWorkspaceCredentials, Set crNodes) {
        this.name = name;
        this.crWorkspaceCredentials = crWorkspaceCredentials;
        this.crNodes = crNodes;
    }

    /** default constructor */
    public CrWorkspace() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="identity"
     *             type="java.lang.Long"
     *             column="workspace_id"
     *             unsaved-value="0"
     *         
     */
    public Long getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(Long workspaceId) {
        this.workspaceId = workspaceId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="255"
     *             not-null="true"
     *         
     */
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /** 
     * bi-directional one-to-many association to CrWorkspaceCredential
     * 
     *            @hibernate.set lazy="true" inverse="true" cascade="none"
     *            @hibernate.collection-key
     *             column="workspace_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.contentrepository.CrWorkspaceCredential"
     *         
     */
    public Set getCrWorkspaceCredentials() {
        return this.crWorkspaceCredentials;
    }

    public void setCrWorkspaceCredentials(Set crWorkspaceCredentials) {
        this.crWorkspaceCredentials = crWorkspaceCredentials;
    }

    /** 
     * bi-directional one-to-many association to CrNode
     * 
     *            @hibernate.set lazy="true" inverse="true" cascade="none"
     *            @hibernate.collection-key
     *             column="workspace_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.contentrepository.CrNode"
     *         
     */
    public Set getCrNodes() {
        return this.crNodes;
    }

    public void setCrNodes(Set crNodes) {
        this.crNodes = crNodes;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("workspaceId", getWorkspaceId())
            .append("name", getName())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof CrWorkspace) ) return false;
        CrWorkspace castOther = (CrWorkspace) other;
        return new EqualsBuilder()
            .append(this.getWorkspaceId(), castOther.getWorkspaceId())
            .append(this.getName(), castOther.getName())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getWorkspaceId())
            .append(getName())
            .toHashCode();
    }

}
