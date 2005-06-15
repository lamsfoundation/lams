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
 * This is a constant utility class that defined all constants need to be 
 * shared around the noticeboard tool.
 * 
 *
 */
public class NoticeboardConstants
{
    /**
     * Private Construtor to avoid instantiation.
     */
    private NoticeboardConstants(){}
    
    // ------------------------------------------
    // DEFAULT IDs for testing purposes 
    // ------------------------------------------
    public static final Long DEFAULT_CONTENT_ID = new Long(2500);
    public static final Long DEFAULT_SESSION_ID = new Long(2400);
    public static final Long DEFAULT_CREATOR_ID = new Long(2300);
    
    // -------------------------------------------
    // Action Forward names
    // -------------------------------------------
    
    public static final String LOAD_NB_FORM = "loadNbForm";
    public static final String BASIC_PAGE = "basic";
    public static final String ADVANCED_PAGE = "advanced";
    public static final String INSTRUCTIONS_PAGE = "instructions";
    
    // ------------------------------
    // Action Names
    // ------------------------------
    public static final String DONE = "done";
    public static final String SAVE = "save";
 
    
    // --------------------------------------------
    // Session Attributes
    // --------------------------------------------
    
    public static final String TITLE = "title";
    public static final String CONTENT = "content";
    public static final String ONLINE_INSTRUCTIONS = "onlineInstructions";
    public static final String OFFLINE_INSTRUCTIONS = "offlineInstructions";
    public static final String TOOL_CONTENT_ID = "toolContentId";
    public static final String RICH_TEXT_CONTENT = "richTextContent";
    public static final String RICH_TEXT_ONLINE_INSTRN = "richTextOnlineInstructions";
    public static final String RICH_TEXT_OFFLINE_INSTRN = "richTextOfflineInstructions";
    
    // ---------------------------------------------------------------------------
    // Lookup Dispatch Action Keys (used in NbAuthoringAction class),
    // labels used in ApplicationResources.properties
    // ---------------------------------------------------------------------------
    
    public static final String BUTTON_BASIC = "button.basic";
    public static final String BUTTON_ADVANCED = "button.advanced";
    public static final String BUTTON_INSTRUCTIONS = "button.instructions";
    public static final String BUTTON_SAVE = "button.ok";
    public static final String BUTTON_DONE = "button.done";
   // public static final String BUTTON_CANCEL = "button.cancel";
   
    
    
}
