/*
 * InsertToolDBRecordsTask.java
 *
 * Created on 24 March 2005, 11:01
 */

package org.lamsfoundation.lams.tool.deploy;


import org.apache.tools.ant.taskdefs.JDBCTask;
import org.apache.tools.ant.BuildException;
import java.io.File;
import java.io.IOException;
/**
 * Ant task runs the creates & inserts for the LAMS aand Tool DB tables
 * @author chris
 *
 * 
 */
public class ToolDBActivateTask extends DBTask
{
    private long defaultContentId;
    
    private long toolId;

    /**
     * Holds value of property learningLibraryId.
     */
    private long learningLibraryId;
    
    /** Creates a new instance of InsertToolDBRecordsTask */
    public ToolDBActivateTask()
    {
    }
    
    public void execute() throws BuildException
    {
        //run tool create db script
        
        //-->transaction
        //insert into tool content table
        //get tool content id (for use as default content id
        //insert into tool table
        //get tool id
        //insert into library and activity table
        
        //update tool insert script with tool id and default content id
       
        //execute tool insert script
        //<--end transaction
    }

    /**
     * Setter for property toolId.
     * @param toolId New value of property toolId.
     */
    public void setToolId(long toolId)
    {

        this.toolId = toolId;
    }

    /**
     * Setter for property learningLibraryId.
     * @param learningLibraryId New value of property learningLibraryId.
     */
    public void setLearningLibraryId(long learningLibraryId)
    {

        this.learningLibraryId = learningLibraryId;
    }
    
    
    
}
