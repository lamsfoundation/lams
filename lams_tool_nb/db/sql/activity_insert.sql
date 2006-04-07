--
-- Copyright (C) 2005 LAMS Foundation (http://lamsfoundation.org)
-- =============================================================
-- License Information: http://lamsfoundation.org/licensing/lams/2.0/
-- 
-- This program is free software; you can redistribute it and/or modify
-- it under the terms of the GNU General Public License version 2.0 as 
-- published by the Free Software Foundation.
-- 
-- This program is distributed in the hope that it will be useful,
-- but WITHOUT ANY WARRANTY; without even the implied warranty of
-- MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
-- GNU General Public License for more details.
-- 
-- You should have received a copy of the GNU General Public License
-- along with this program; if not, write to the Free Software
-- Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
-- USA
-- 
-- http://www.gnu.org/licenses/gpl.txt
-- ****************************************************************
--
-- $Id$

INSERT INTO lams_learning_activity
(
activity_ui_id
, description
, title
, help_text
, xcoord
, ycoord
, parent_activity_id
, parent_ui_id
, learning_activity_type_id
, grouping_support_type_id
, apply_grouping_flag
, grouping_id
, grouping_ui_id
, order_id
, define_later_flag
, learning_design_id
, learning_library_id
, create_date_time
, run_offline_flag
, max_number_of_options
, min_number_of_options
, options_instructions
, tool_id
, tool_content_id
, activity_category_id
, gate_activity_level_id
, gate_open_flag
, gate_start_time_offset
, gate_end_time_offset
, gate_start_date_time
, gate_end_date_time
, library_activity_ui_image
, create_grouping_id
, create_grouping_ui_id
, library_activity_id
, language_file
)
VALUES
(
NULL
, 'Displays a NoticeboardX'
, 'NoticeboardX'
, 'Put some help text here.'
, NULL
, NULL
, NULL
, NULL
, 1
, 2
, 0
, NULL
, NULL
, NULL
, 0
, NULL
, ${learning_library_id}
, NOW()
, 0
, NULL
, NULL
, NULL
, ${tool_id}
, NULL
, 4
, NULL
, NULL
, NULL
, NULL
, NULL
, NULL
, 'tool/lanb11/images/icon_noticeboard.swf'
, NULL
, NULL
, NULL
, 'org.lamsfoundation.lams.tool.noticeboard.ApplicationResources'
)
