/*
 * Created on 1/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.learningdesign.Group;

/**
 * @author daveg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class GroupedToolSession extends ToolSession {

    /** persistent field */
    private Group group;

	public Group getGroup() {
		return group;
	}
	public void setGroup(Group group) {
		this.group = group;
	}
}
