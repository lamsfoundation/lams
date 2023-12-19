/****************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * License Information: http://lamsfoundation.org/licensing/lams/2.0/
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License version 2.0
 * as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301
 * USA
 *
 * http://www.gnu.org/licenses/gpl.txt
 * ****************************************************************
 */


package org.lamsfoundation.lams.tool.noticeboard;

/**
 *
 * <p>
 * This is a constant utility class that defined all constants need to be
 * shared around the noticeboard tool.
 * </p>
 *
 * @author mtruong
 */
public interface NoticeboardConstants {
    /**
     * Private Construtor to avoid instantiation.
     */
    

    public final static String TOOL_SIGNATURE = "lanb11";

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

    public static final String MONITOR_PAGE = "monitorPage";
    public static final String MONITOR_REFLECTION_PAGE = "monitorReflectionPage";
    public static final String MONITOR_COMMENTS_PAGE = "monitorCommentsPage";

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
    public static final String TOOL_CONTENT_ID_INMONITORMODE = "toolContentIdInMonitor";

    public static final String DEFINE_LATER = "defineLater";
    public static final String PAGE_EDITABLE = "isPageEditable";

    //used in authoring
    public static final String TOOL_CONTENT_ID = "toolContentID"; //request + session variable
    public static final String CONTENT_FOLDER_ID = "contentFolderID"; //request variable
    public static final String RICH_TEXT_TITLE = "richTextTitle";
    public static final String RICH_TEXT_CONTENT = "richTextContent";

    public static final String READ_ONLY_MODE = "readOnlyMode";

    public static final String USER_ID = "userID";
    public static final String TOOL_SESSION_ID = "toolSessionID";

    // ---------------------------------------------------------------------------
    // Lookup Dispatch Action Keys (used in NbAuthoringAction class, NbLearnerAction),
    // labels used in ApplicationResources.properties
    // ---------------------------------------------------------------------------

    public static final String BUTTON_SAVE = "button.save";
    public static final String BUTTON_DONE = "button.done";
    public static final String BUTTON_FINISH = "button.finish";
    public static final String BUTTON_EDIT = "button.edit";
    public static final String BUTTON_CONTINUE = "button.continue";

    //--------------
    // Flags
    //--------------

    public static final int FLAG_DEFINE_LATER = 1;
    public static final int FLAG_CONTENT_IN_USE = 2;

    //------------------------------
    // Error Keys/Messages
    //------------------------------

    public static final String ERROR_NBAPPLICATION = "error.exception.NbApplication";
    public static final String ERROR_MANDATORY = "error.mandatoryField";
    public static final String ERR_MISSING_PARAM = "error.missingParam";
}