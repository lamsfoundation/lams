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
, library_activity_ui_image
, create_grouping_id
, create_grouping_ui_id
, library_activity_id
, language_file
)
VALUES
(
NULL
, 'Dimdim Tool'
, 'Dimdim Tool'
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
, 2
, NULL
, NULL
, NULL
, NULL
, 'tool/laddim10/images/icon_dimdim.swf'
, NULL
, NULL
, NULL
, 'org.lamsfoundation.lams.tool.dimdim.ApplicationResources'
)
