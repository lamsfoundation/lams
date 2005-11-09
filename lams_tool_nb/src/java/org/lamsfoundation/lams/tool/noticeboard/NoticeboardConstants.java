/*
 *Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 *
 *This program is free software; you can redistribute it and/or modify
 *it under the terms of the GNU General Public License as published by
 *the Free Software Foundation; either version 2 of the License, or
 *(at your option) any later version.
 *
 *This program is distributed in the hope that it will be useful,
 *but WITHOUT ANY WARRANTY; without even the implied warranty of
 *MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *GNU General Public License for more details.
 *
 *You should have received a copy of the GNU General Public License
 *along with this program; if not, write to the Free Software
 *Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 *USA
 *
 *http://www.gnu.org/licenses/gpl.txt
 */
package org.lamsfoundation.lams.tool.noticeboard;

/**
 * 
 * <p>This is a constant utility class that defined all constants need to be 
 * shared around the noticeboard tool.</p>
 * 
 * @author mtruong
 */
public class NoticeboardConstants
{
    /**
     * Private Construtor to avoid instantiation.
     */
    private NoticeboardConstants(){}
    
    public final static String TOOL_SIGNATURE="lanb11";
    
    
    // ------------------------------------------
    // DEFAULT IDs for testing purposes 
    // ------------------------------------------
    public static final Long DEFAULT_CONTENT_ID = new Long(2500);
    public static final Long DEFAULT_SESSION_ID = new Long(2400);
    public static final Long DEFAULT_CREATOR_ID = new Long(2300);
    
    // -------------------------------------------
    // Action Forward names
    // -------------------------------------------

    public static final String INSTRUCTIONS = "Instructions";
    
    public static final String AUTHOR_PAGE = "authoringContent";
    
    public static final String DISPLAY_MESSAGE = "displayMessage";
    public static final String DISPLAY_LEARNER_CONTENT = "displayLearnerContent";
    
    public static final String TOOL_ACCESS_MODE_LEARNER = "learner";
    public static final String TOOL_ACCESS_MODE_AUTHOR = "author";
    public static final String TOOL_ACCESS_MODE_TEACHER = "teacher";
    
    public static final String MONITOR_PAGE = "monitorPage";
    
    // ------------------------------
    // Action Names
    // ------------------------------
    
    public static final String DONE = "done";
    public static final String SAVE = "save";
 
    
    // --------------------------------------------
    // Session Attributes
    // --------------------------------------------
    
    //used in monitoring
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String ONLINE_INSTRUCTIONS = "onlineInstructions";
    public static final String OFFLINE_INSTRUCTIONS = "offlineInstructions";
    public static final String TOOL_CONTENT_ID_INMONITORMODE = "toolContentIdInMonitor";
 
    public static final String DEFINE_LATER = "defineLater";
    public static final String PAGE_EDITABLE = "isPageEditable";
    public static final String TOTAL_LEARNERS = "totalLearners";
    public static final String GROUP_STATS_MAP = "groupStatsMap";
    public static final String EXPORT_PORTFOLIO = "exportPortfolio";
    
    //used in authoring
    public static final String TOOL_CONTENT_ID = "toolContentId";  //request + session variable
    public static final String RICH_TEXT_TITLE = "richTextTitle";
    public static final String RICH_TEXT_CONTENT = "richTextContent";
    public static final String RICH_TEXT_ONLINE_INSTRN = "richTextOnlineInstructions";
    public static final String RICH_TEXT_OFFLINE_INSTRN = "richTextOfflineInstructions";
    
    public static final String ATTACHMENT_LIST = "attachmentList";
    
    public static final String READ_ONLY_MODE = "readOnlyMode";
   
    
    public static final String USER_ID = "userId";
    public static final String TOOL_SESSION_ID = "toolSessionId";
    public static final String UUID = "uuid";
    
    // ---------------------------------------------------------------------------
    // Lookup Dispatch Action Keys (used in NbAuthoringAction class, NbLearnerAction),
    // labels used in ApplicationResources.properties
    // ---------------------------------------------------------------------------
    
    public static final String BUTTON_BASIC = "button.basic";
    public static final String BUTTON_ADVANCED = "button.advanced";
    public static final String BUTTON_INSTRUCTIONS = "button.instructions";
    public static final String BUTTON_SAVE = "button.save";
    public static final String BUTTON_DONE = "button.done";
    public static final String BUTTON_FINISH = "button.finish";
   // public static final String BUTTON_CANCEL = "button.cancel";
    public static final String BUTTON_EDIT_ACTIVITY = "button.editActivity";
    public static final String BUTTON_SUMMARY = "button.summary";
    public static final String BUTTON_STATISTICS = "button.statistics";
    public static final String BUTTON_EDIT = "button.edit";
    public static final String BUTTON_UPLOAD = "button.upload";
    public static final String LINK_DELETE="link.delete";
    
    
   //--------------
   // Flags
   //--------------
    
    public static final int FLAG_DEFINE_LATER = 1;
    public static final int FLAG_CONTENT_IN_USE = 2;
    public static final int FLAG_RUN_OFFLINE = 3;
   
    //------------------------------
    // Error Keys/Messages
    //------------------------------
    
    public static final String ERROR_NBAPPLICATION = "error.exception.NbApplication";
    public static final String ERROR_MANDATORY="error.mandatoryField";
    public static final String ERR_MISSING_PARAM = "error.missingParam";
    
}
