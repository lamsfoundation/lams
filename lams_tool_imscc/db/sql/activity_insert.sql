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
, activity_category_id
, library_activity_ui_image
, language_file
)
VALUES
(
  'CommonCartridge'
, 'CommonCartridge'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 4
, 'tool/laimsc11/images/icon_commonCartridge.svg'
, 'org.lamsfoundation.lams.tool.commonCartridge.ApplicationResources'
)