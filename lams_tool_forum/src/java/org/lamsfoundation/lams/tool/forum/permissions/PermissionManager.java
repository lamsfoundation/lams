package org.lamsfoundation.lams.tool.forum.permissions;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 17/06/2005
 * Time: 14:22:37
 * To change this template use File | Settings | File Templates.
 */
public interface PermissionManager {

   public boolean hasPermission(Long userID, String permission);

}
