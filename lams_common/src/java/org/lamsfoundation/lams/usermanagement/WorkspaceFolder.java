package org.lamsfoundation.lams.usermanagement;

import java.io.Serializable;
import java.util.Set;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/** 
 *        @hibernate.class
 *         table="lams_workspace_folder"
 *     
*/
public class WorkspaceFolder implements Serializable {

    /** identifier field */
    private Integer workspaceFolderId;

    /** persistent field */
    private String name;

    /** persistent field */
    private int workspaceId;

    /** persistent field */
    private WorkspaceFolder parentWorkspaceFolder;

    /** persistent field */
    private Set workspaces;

    /** persistent field */
    private Set childWorkspaceFolders;

    /** full constructor */
    public WorkspaceFolder(String name, int workspaceId, WorkspaceFolder parentWorkspaceFolder, Set workspaces, Set childWorkspaceFolders) {
        this.name = name;
        this.workspaceId = workspaceId;
        this.parentWorkspaceFolder = parentWorkspaceFolder;
        this.workspaces = workspaces;
        this.childWorkspaceFolders = childWorkspaceFolders;
    }

    /** default constructor */
    public WorkspaceFolder() {
    }

    /** 
     *            @hibernate.id
     *             generator-class="native"
     *             type="java.lang.Integer"
     *             column="workspace_folder_id"
     *         
     */
    public Integer getWorkspaceFolderId() {
        return this.workspaceFolderId;
    }

    public void setWorkspaceFolderId(Integer workspaceFolderId) {
        this.workspaceFolderId = workspaceFolderId;
    }

    /** 
     *            @hibernate.property
     *             column="name"
     *             length="64"
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
     *            @hibernate.property
     *             column="workspace_id"
     *             length="11"
     *             not-null="true"
     *         
     */
    public int getWorkspaceId() {
        return this.workspaceId;
    }

    public void setWorkspaceId(int workspaceId) {
        this.workspaceId = workspaceId;
    }

    /** 
     *            @hibernate.many-to-one
     *            @hibernate.column name="parent_folder_id"         
     *         
     */
    public WorkspaceFolder getParentWorkspaceFolder() {
        return this.parentWorkspaceFolder;
    }

    public void setParentWorkspaceFolder(WorkspaceFolder parentWorkspaceFolder) {
        this.parentWorkspaceFolder = parentWorkspaceFolder;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="root_folder_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.Workspace"
     *         
     */
    public Set getWorkspaces() {
        return this.workspaces;
    }

    public void setWorkspaces(Set workspaces) {
        this.workspaces = workspaces;
    }

    /** 
     *            @hibernate.set
     *             lazy="true"
     *             inverse="true"
     *             cascade="none"
     *            @hibernate.collection-key
     *             column="parent_folder_id"
     *            @hibernate.collection-one-to-many
     *             class="org.lamsfoundation.lams.usermanagement.WorkspaceFolder"
     *         
     */
    public Set getChildWorkspaceFolders() {
        return this.childWorkspaceFolders;
    }

    public void setChildWorkspaceFolders(Set childWorkspaceFolders) {
        this.childWorkspaceFolders = childWorkspaceFolders;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("workspaceFolderId", getWorkspaceFolderId())
            .toString();
    }

    public boolean equals(Object other) {
        if ( !(other instanceof WorkspaceFolder) ) return false;
        WorkspaceFolder castOther = (WorkspaceFolder) other;
        return new EqualsBuilder()
            .append(this.getWorkspaceFolderId(), castOther.getWorkspaceFolderId())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getWorkspaceFolderId())
            .toHashCode();
    }

}
