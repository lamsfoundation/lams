package org.lamsfoundation.lams.tool.forum.permissions;

/**
 * Created by IntelliJ IDEA.
 * User: conradb
 * Date: 14/06/2005
 * Time: 13:08:40
 * To change this template use File | Settings | File Templates.
 */
public class PermissionManagerImpl implements PermissionManager {

   /*
    * TODO for now return true
    *
   */
    public boolean hasPermission(Long userID, byte permission) {
        return true;
    }
}
