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
public class ToolDBDeployTask //extends DBTask
{

    /**
     * Holds value of property toolLibraryScript.
     */
    private File toolLibraryScript;

    /**
     * Holds value of property toolId.
     */
    private long toolId;

    /**
     * Holds value of property defaultContentId.
     */
    private long defaultContentId;

    /**
     * Holds value of property toolSignature.
     */
    private String toolSignature;

    /**
     * Holds value of property serviceName.
     */
    private String serviceName;

    /**
     * Holds value of property displayName.
     */
    private String displayName;

    /**
     * Holds value of property learningLibraryid.
     */
    private long learningLibraryid;

    /**
     * Holds value of property groupingSupportTypeId.
     */
    private int groupingSupportTypeId;

    /**
     * Holds value of property supportsDefineLater.
     */
    private boolean supportsDefineLater;

    /**
     * Holds value of property supportsRunOffline.
     */
    private boolean supportsRunOffline;

    /**
     * Holds value of property supportsModeration.
     */
    private boolean supportsModeration;

    /**
     * Holds value of property supportsContribution.
     */
    private boolean supportsContribution;

    /**
     * Holds value of property learnerUrl.
     */
    private String learnerUrl;

    /**
     * Holds value of property auythorUrl.
     */
    private String auythorUrl;

    /**
     * Holds value of property defineLaterUrl.
     */
    private String defineLaterUrl;

    /**
     * Holds value of property exportPortfolioUrl.
     */
    private String exportPortfolioUrl;

    /**
     * Holds value of property monitorUrl.
     */
    private String monitorUrl;

    /**
     * Holds value of property contributeUrl.
     */
    private String contributeUrl;

    /**
     * Holds value of property moderationUrl.
     */
    private String moderationUrl;

    /**
     * Holds value of property toolDBCreateScript.
     */
    private File toolDBCreateScript;

    /**
     * Holds value of property toolDBInsertScript.
     */
    private File toolDBInsertScript;

    /**
     * Holds value of property toolActivityScript.
     */
    private File toolActivityScript;
    
    /** Creates a new instance of ToolDBActivateTask */
    public ToolDBDeployTask()
    {
    }

    /**
     * Setter for property toolLibraryScript.
     * @param toolLibraryScript New value of property toolLibraryScript.
     */
    public void setToolLibraryScript(File toolLibraryScript)
    {

        this.toolLibraryScript = toolLibraryScript;
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
     * Getter for property defaultContentId.
     * @return Value of property defaultContentId.
     */
    public long getDefaultContentId()
    {

        return this.defaultContentId;
    }

    /**
     * Setter for property toolSignature.
     * @param toolSignature New value of property toolSignature.
     */
    public void setToolSignature(String toolSignature)
    {

        this.toolSignature = toolSignature;
    }

    /**
     * Setter for property serviceName.
     * @param serviceName New value of property serviceName.
     */
    public void setServiceName(String serviceName)
    {

        this.serviceName = serviceName;
    }

    /**
     * Setter for property displayName.
     * @param displayName New value of property displayName.
     */
    public void setDisplayName(String displayName)
    {

        this.displayName = displayName;
    }
//
//    /**
//     * Getter for property description.
//     * @return Value of property description.
//     */
//    public String getDescription()
//    {
//    }

    /**
     * Setter for property description.
     * @param description New value of property description.
     */
    public void setDescription(String description)
    {
    }

    /**
     * Setter for property groupingSupportTypeId.
     * @param groupingSupportTypeId New value of property groupingSupportTypeId.
     */
    public void setGroupingSupportTypeId(int groupingSupportTypeId)
    {

        this.groupingSupportTypeId = groupingSupportTypeId;
    }

    /**
     * Setter for property supportsDefineLater.
     * @param supportsDefineLater New value of property supportsDefineLater.
     */
    public void setSupportsDefineLater(boolean supportsDefineLater)
    {

        this.supportsDefineLater = supportsDefineLater;
    }

    /**
     * Setter for property supportsRunOffline.
     * @param supportsRunOffline New value of property supportsRunOffline.
     */
    public void setSupportsRunOffline(boolean supportsRunOffline)
    {

        this.supportsRunOffline = supportsRunOffline;
    }

    /**
     * Setter for property supportsModeration.
     * @param supportsModeration New value of property supportsModeration.
     */
    public void setSupportsModeration(boolean supportsModeration)
    {

        this.supportsModeration = supportsModeration;
    }

    /**
     * Setter for property supportsContribution.
     * @param supportsContribution New value of property supportsContribution.
     */
    public void setSupportsContribution(boolean supportsContribution)
    {

        this.supportsContribution = supportsContribution;
    }

    /**
     * Setter for property learnerUrl.
     * @param learnerUrl New value of property learnerUrl.
     */
    public void setLearnerUrl(String learnerUrl)
    {

        this.learnerUrl = learnerUrl;
    }

    /**
     * Setter for property auythorUrl.
     * @param auythorUrl New value of property auythorUrl.
     */
    public void setAuythorUrl(String auythorUrl)
    {

        this.auythorUrl = auythorUrl;
    }

    /**
     * Setter for property defineLaterUrl.
     * @param defineLaterUrl New value of property defineLaterUrl.
     */
    public void setDefineLaterUrl(String defineLaterUrl)
    {

        this.defineLaterUrl = defineLaterUrl;
    }

    /**
     * Setter for property exportPortfolioUrl.
     * @param exportPortfolioUrl New value of property exportPortfolioUrl.
     */
    public void setExportPortfolioUrl(String exportPortfolioUrl)
    {

        this.exportPortfolioUrl = exportPortfolioUrl;
    }

    /**
     * Setter for property monitorUrl.
     * @param monitorUrl New value of property monitorUrl.
     */
    public void setMonitorUrl(String monitorUrl)
    {

        this.monitorUrl = monitorUrl;
    }

    /**
     * Setter for property contributeUrl.
     * @param contributeUrl New value of property contributeUrl.
     */
    public void setContributeUrl(String contributeUrl)
    {

        this.contributeUrl = contributeUrl;
    }

    /**
     * Setter for property moderationUrl.
     * @param moderationUrl New value of property moderationUrl.
     */
    public void setModerationUrl(String moderationUrl)
    {

        this.moderationUrl = moderationUrl;
    }

    /**
     * Getter for property learningLibraryid.
     * @return Value of property learningLibraryid.
//     */
//    public long getLearningLibraryid()
//    {
//    }

    /**
     * Setter for property toolDBCreateScript.
     * @param toolDBCreateScript New value of property toolDBCreateScript.
     */
    public void setToolDBCreateScript(File toolDBCreateScript)
    {

        this.toolDBCreateScript = toolDBCreateScript;
    }

    /**
     * Setter for property toolDBInsertScript.
     * @param toolDBInsertScript New value of property toolDBInsertScript.
     */
    public void setToolDBInsertScript(File toolDBInsertScript)
    {

        this.toolDBInsertScript = toolDBInsertScript;
    }

    /**
     * Setter for property toolActivityScript.
     * @param toolActivityScript New value of property toolActivityScript.
     */
    public void setToolActivityScript(File toolActivityScript)
    {

        this.toolActivityScript = toolActivityScript;
    }
    
}
