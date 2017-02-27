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
  'Notebook Tool'
, 'Notebook Tool'
, 'Put some help text here.'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 6
, 'tool/lantbk11/images/icon_notebook.svg'
, 'org.lamsfoundation.lams.tool.notebook.ApplicationResources'
)