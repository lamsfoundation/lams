/* 
Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)

This program is free software; you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation; either version 2 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
USA

http://www.gnu.org/licenses/gpl.txt 
*/
/* $$Id$$ */
package org.lamsfoundation.lams.tool.deploy.libraryActivity;

import java.util.ArrayList;

/**
 * @author mtruong
 */
public class LearningLibrary {
    
    private String libraryInsertScriptPath;
    private String templateActivityInsertScriptPath;
    private ArrayList toolActivityList;
   
 
   public LearningLibrary()
   {       
   }
   
   public LearningLibrary(String libraryInsertScript, String templateActivityInsertScript, ArrayList toolActivityList)
   {
       this.libraryInsertScriptPath = libraryInsertScript;
       this.templateActivityInsertScriptPath = templateActivityInsertScript;
       this.toolActivityList = toolActivityList;
   }
   
  
    /**
     * @return Returns the libraryActivityInsertScriptPath.
     */
    public String getTemplateActivityInsertScriptPath() {
        return templateActivityInsertScriptPath;
    }
    /**
     * @param libraryActivityInsertScriptPath The libraryActivityInsertScriptPath to set.
     */
    public void setTemplateActivityInsertScriptPath(
            String libraryActivityInsertScriptPath) {
        this.templateActivityInsertScriptPath = libraryActivityInsertScriptPath;
    }
    /**
     * @return Returns the toolActivities.
     */
    public ArrayList getToolActivityList() {
        return toolActivityList;
    }
    /**
     * @param toolActivities The toolActivities to set.
     */
    public void setToolActivityList(ArrayList toolActivities) {
        this.toolActivityList = toolActivities;
    }
        
    /**
     * @return Returns the libraryInsertScriptPath.
     */
    public String getLibraryInsertScriptPath() {
        return libraryInsertScriptPath;
    }
    /**
     * @param libraryInsertScriptPath The libraryInsertScriptPath to set.
     */
    public void setLibraryInsertScriptPath(String libraryInsertScriptPath) {
        this.libraryInsertScriptPath = libraryInsertScriptPath;
    }
}
