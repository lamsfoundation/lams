INSERT INTO lams_learning_activity
(
activity_ui_id
, description
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
NULL
, 'Collaborative real-time drawing'
, 'Whiteboard'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 'tool/lawhiteboard11/images/icon_whiteboard.svg'
, 'org.lamsfoundation.lams.tool.whiteboard.ApplicationResources'
)
