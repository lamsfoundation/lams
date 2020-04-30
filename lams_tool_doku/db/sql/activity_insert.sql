# Connection: ROOT LOCAL
# Host: localhost
# Saved: 2005-04-07 11:08:32
# 
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
, activity_category_id
, library_activity_ui_image
, language_file
)
VALUES
(
NULL
, 'Collaborative real-time document editor'
, 'doKu'
, 1
, 2
, 0
, ${learning_library_id}
, NOW()
, ${tool_id}
, 4
, 'tool/ladoku11/images/icon_dokumaran.svg'
, 'org.lamsfoundation.lams.tool.dokumaran.ApplicationResources'
)
