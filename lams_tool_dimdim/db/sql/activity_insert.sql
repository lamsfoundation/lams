INSERT INTO lams_learning_activity
(
  description
, title
, help_text
, learning_activity_type_id
, grouping_support_type_id
, apply_grouping_flag
, learning_library_id
, create_date_time
, tool_id
, activity_category_id
, library_activity_ui_image
, language_file
)
VALUES
(
  'Dimdim Tool'
, 'Dimdim Tool'
, 'Put some help text here.'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 2
, 'tool/laddim10/images/icon_dimdim.swf'
, 'org.lamsfoundation.lams.tool.dimdim.ApplicationResources'
)