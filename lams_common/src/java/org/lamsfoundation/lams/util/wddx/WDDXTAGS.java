/***************************************************************************
 * Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
 * =============================================================
 * 
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 * 
 * http://www.gnu.org/licenses/gpl.txt
 * ************************************************************************
 */
package org.lamsfoundation.lams.util.wddx;

import java.util.Date;



/**
 * @author Manpreet Minhas
 * The tags used in WDDX Packet
 */
public interface WDDXTAGS {
	
	/* General Tags */
	public static final String OBJECT_TYPE = "objectType";
	
	public static final String DESCRIPTION = "description";
	public static final String TITLE = "title";
	public static final String HELP_TEXT ="help_text";
	public static final String XCOORD="xcoord";
	public static final String YCOORD="ycoord";	
	public static final String GROUPING ="grouping";
	public static final String TRANSITIONS ="transitions";
	public static final String ACTIVITIES ="activities";
	
	/*Learning Library specific tags */
	public static final String LEARNING_LIBRARY_ID ="learning_library_id";	
	public static final String LIB_ACTIVITIES="templateActivities";
	public static final String LIB_PACKAGE = "libraries";
	public static final String DESIGN_PACKAGE ="designs";
	
	
	public static final Long NUMERIC_NULL_VALUE_LONG = new Long(-111111);
	public static final String STRING_NULL_VALUE = "string_null_value";
	public static final Integer NUMERIC_NULL_VALUE_INTEGER = new Integer(-111111);
	public static final Date DATE_NULL_VALUE = new Date(0);
	public static final Boolean BOOLEAN_NULL_VALUE = new Boolean("false");
	
	/*Activity specific tags*/
	public static final String ACTIVITY_ID ="activity_id";
	public static final String ACTIVITY_UIID ="activity_ui_id";
	
	public static final String PARENT_ACTIVITY_ID ="parent_activity_id";
	public static final String PARENT_UIID ="parent_activity_ui_id";
	
	public static final String ACTIVITY_TYPE_ID ="learning_activity_type_id";	
	public static final String ORDER_ID ="order_id";
	
	public static final String DEFINE_LATER ="define_later_flag";
	public static final String RUN_OFFLINE ="run_offline";
	public static final String OFFLINE_INSTRUCTIONS ="offline_instructions";
	public static final String LIBRARY_IMAGE ="library_activity_ui_image";
	public static final String LIBRARY_ACTIVITY ="library_activity_id";
	
	/** OptionsActivity specific tags*/
	public static final String MAX_OPTIONS="max_number_of_options";
	public static final String MIN_OPTIONS="min_number_of_options";
	public static final String OPTIONS_INSTRUCTIONS ="options_instructions";
	
	/** ToolActivity specific tags*/
	public static final String TOOL_ID="tool_id";
	public static final String TOOL_CONTENT_ID="tool_content_id";
	
	/** GateActivity specific tags*/	
	public static final String GATE_ACTIVITY_LEVEL_ID ="gate_activity_level_id";
	public static final String GATE_START_DATE ="gate_start_date_time";
	public static final String GATE_END_DATE ="gate_end_date_time";
	public static final String GATE_START_OFFSET="gate_start_time_offset";
	public static final String GATE_END_OFFSET="gate_end_time_offset";
	public static final String GATE_OPEN ="gate_open";
	
	/** Grouping Activity specific tags */
	public static final String CREATE_GROUPING_ID ="create_grouping_id";
	public static final String CREATE_GROUPING_UIID ="create_grouping_ui_id";
	
	/** Grouping specific tags */
	public static final String GROUPING_ID ="grouping_id";
	public static final String GROUPING_UIID ="grouping_ui_id";
	public static final String GROUPING_TYPE_ID ="grouping_type_id";		
	public static final String LEARNERS_PER_GROUP ="learners_per_group";
	public static final String MAX_NUMBER_OF_GROUPS ="max_number_of_groups";
	public static final String NUMBER_OF_GROUPS ="number_of_groups";	
	
	/** Transition specific tags */
	public static final String TRANSITION_ID ="transition_id";
	public static final String TRANSITION_UIID ="transition_ui_id";
	public static final String TRANSITION_TO="to_activity_id";
	public static final String TRANSITION_FROM="from_activity_id";
	public static final String TO_ACTIVITY_UIID ="to_activity_ui_id";
	public static final String FROM_ACTIVITY_UIID ="from_activity_ui_id";
	
	
	/** Tool Specific tags */
	public static final String TOOL_DISPLAY_NAME ="displayName";
	public static final String TOOl_AUTH_URL ="toolAuthoringURL";
	public static final String AUTH_URL ="authoringURLS";
	
	
	
	/** LearningDesign specific tags*/
	public static final String LEARNING_DESIGN_ID="learning_design_id";
	public static final String LEARNING_DESIGN_UIID="learning_design_ui_id";
	public static final String FIRST_ACTIVITY_ID ="first_activity_id";
	public static final String FIRST_ACTIVITY_UIID ="first_activity_ui_id";
	
	public static final String MAX_ID ="max_id";
	public static final String VALID_DESIGN ="valid_design_flag";
	public static final String READ_ONLY ="read_only_flag";
	public static final String DATE_READ_ONLY ="date_read_only";
	public static final String USER_ID="user_id";
	
	public static final String COPY_TYPE="copy_type_id";
	public static final String CREATION_DATE ="create_date_time";
	public static final String VERSION="version";
	public static final String PARENT_DESIGN_ID ="parent_learning_design_id";
	public static final String WORKSPACE_FOLDER_ID= "workspace_folder_id";
	public static final String DURATION ="duration";
	public static final String LICENCE_ID ="license_id";
	public static final String LICENSE_TEXT ="license_text";
	
	/**ComplexActivity specific tags */
	public static final String CHILD_ACTIVITIES ="childActivities";
	
	
	
}
