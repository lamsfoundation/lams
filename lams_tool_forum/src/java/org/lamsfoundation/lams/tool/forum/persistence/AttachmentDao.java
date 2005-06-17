package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 7/06/2005
 * Time: 12:23:49
 * To change this template use File | Settings | File Templates.
 */
public class AttachmentDao extends GenericEntityDao {

	public Class getPersistentClass() {
        return Attachment.class;
    }
}
