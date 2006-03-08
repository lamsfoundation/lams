package org.lamsfoundation.lams.tool.forum.util;

import org.lamsfoundation.lams.tool.forum.persistence.Forum;
import org.lamsfoundation.lams.tool.forum.persistence.ForumException;


/**
 * Contains helper methods used by the Action Servlets
 * 
 * @author Anthony Sukkar
 *
 */
public class ForumWebUtils {

	public static boolean isForumEditable(Forum forum) {
        if ( (forum.isDefineLater() == true) && (forum.isContentInUse()==true) )
        {
            throw new ForumException("An exception has occurred: There is a bug in this tool, conflicting flags are set");
                    //return false;
        }
        else if ( (forum.isDefineLater() == true) && (forum.isContentInUse() == false))
            return true;
        else if ( (forum.isDefineLater() == false) && (forum.isContentInUse() == false))
            return true;
        else //  (content.isContentInUse()==true && content.isDefineLater() == false)
            return false;
	}
	
}
