
package org.lamsfoundation.lams.contentrepository;

/**
 * A workspace is a grouping of content, which is accessed by a ticket. 
 * One workspace may have many current tickets. 
 * 
 * A workspace may belong to a tool, in which case only the tool can use this workspace.
 */
public interface IWorkspace {
	public String getName();
	public Long getWorkspaceId();
}
