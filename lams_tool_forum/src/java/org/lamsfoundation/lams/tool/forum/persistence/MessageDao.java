/*
 * Created on Jun 1, 2005
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * @author conradb
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class MessageDao extends GenericEntityDao {

	public Class getPersistentClass() {
        return Message.class;
    }

}
