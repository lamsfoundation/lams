/*
 * Created on 1/02/2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool;

import org.lamsfoundation.lams.usermanagement.User;

/**
 * @author daveg
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class NonGroupedToolSession extends ToolSession {

    /** persistent field */
    private User user;

	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
}
