/*
 * ToolDBActivateTask.java
 *
 * Created on 24 March 2005, 15:04
 */

package org.lamsfoundation.lams.tool.deploy;

import java.io.File;

//tool_id BIGINT(20) NOT NULL AUTO_INCREMENT
//     , tool_signature VARCHAR(64) NOT NULL
//     , service_name VARCHAR(255) NOT NULL
//     , tool_display_name VARCHAR(255) NOT NULL
//     , description TEXT
//     , learning_library_id BIGINT(20)
//     , default_tool_content_id BIGINT(20) NOT NULL
//     , valid_flag TINYINT(1) NOT NULL DEFAULT 1
//     , grouping_support_type_id INT(3) NOT NULL
//     , supports_define_later_flag TINYINT(1) NOT NULL DEFAULT 0
//     , supports_run_offline_flag TINYINT(1) NOT NULL
//     , supports_moderation_flag TINYINT(1) NOT NULL
//     , supports_contribute_flag TINYINT(1) NOT NULL
//     , learner_url TEXT NOT NULL
//     , author_url TEXT NOT NULL
//     , define_later_url TEXT
//     , export_portfolio_url TEXT NOT NULL
//     , monitor_url TEXT NOT NULL
//     , contribute_url TEXT
//     , moderation_url TEXT
//     , create_date_time DATETIME NOT NULL

/**
 *
 * @author chris
 */
public class ToolDBDeployTask extends DBTask
{

    /**
     * Holds value of property toolInsertScript.
     */
    private File toolInsertScript;

    /**
     * Holds value of property toolLibraryInsertScript.
     */
    private File toolLibraryInsertScript;

    /**
     * Holds value of property toolTablesCreatScript.
     */
    private File toolTablesCreatScript;

    /**
     * Holds value of property toolId.
     */
    private long toolId;

    /**
     * Holds value of property learningLibraryId.
     */
    private long learningLibraryId;

    /**
     * Holds value of property toolActivityInsertScript.
     */
    private File toolActivityInsertScript;

    
    /** Creates a new instance of ToolDBActivateTask */
    public ToolDBDeployTask()
    {
    }

    /**
     * Setter for property toolInsertFile.
     * @param toolInsertFile New value of property toolInsertFile.
     */
    public void setToolInsertScript(java.io.File toolInsertScript)
    
    {

        this.toolInsertScript = toolInsertScript;
    }

    /**
     * Setter for property toolLibraryInsertFile.
     * @param toolLibraryInsertFile New value of property toolLibraryInsertFile.
     */
    public void setToolLibraryInsertScript(java.io.File toolLibraryInsertScript)
    
    {

        this.toolLibraryInsertScript = toolLibraryInsertScript;
    }

    /**
     * Setter for property toolTablesCreatScript.
     * @param toolTablesCreatScript New value of property toolTablesCreatScript.
     */
    public void setToolTablesCreatScript(File toolTablesCreatScript)
    {

        this.toolTablesCreatScript = toolTablesCreatScript;
    }

    public void execute() throws DeployException
    {
        //get a tool content id
        
        //put the tool content id into the tool insert file
        
        //run tool insert and get tool id back
        
        //put the tool id into the tool library script
        
        //run tool library script and get the library id back
        
        //put the library id into the activity insert script
        
        //run the activity insert script
        
        //put the tool id and and defualt content id into
        //the tool tables script
        
        //run the tool table script
    }

    /**
     * Getter for property toolId.
     * @return Value of property toolId.
     */
    public long getToolId()
    {

        return this.toolId;
    }

    /**
     * Getter for property learningLibraryId.
     * @return Value of property learningLibraryId.
     */
    public long getLearningLibraryId()
    {

        return this.learningLibraryId;
    }

    /**
     * Setter for property toolActivityInsertScript.
     * @param toolActivityInsertScript New value of property toolActivityInsertScript.
     */
    public void setToolActivityInsertScript(File toolActivityInsertScript)
    {

        this.toolActivityInsertScript = toolActivityInsertScript;
    }
    
}
