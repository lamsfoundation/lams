package org.lamsfoundation.lams.tool.forum.persistence;

/**
 * ForumDao
 * @author conradb
 *
 *
 */
public class ForumDao extends GenericEntityDao {

	public Class getPersistentClass() {
        return Forum.class;
    }

}
