INSERT INTO lams_learning_activity
(
  description
, title
, learning_activity_type_id
, grouping_support_type_id
, apply_grouping_flag
, learning_library_id
, create_date_time
, tool_id
, library_activity_ui_image
, language_file
)
VALUES
(
  'Collecting data with custom structure.'
, 'Data Collection'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 'tool/ladaco10/images/icon_daco.svg'
, 'org.lamsfoundation.lams.tool.daco.ApplicationResources'
)