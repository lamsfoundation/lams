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
-- Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA 02110-1301-- USA
--
-- http://www.gnu.org/licenses/gpl.txt
-- ****************************************************************
--



INSERT INTO lams_tool
(
tool_signature,
service_name,
tool_display_name,
description,
tool_identifier,
tool_version,
learning_library_id,
default_tool_content_id,
valid_flag,
grouping_support_type_id,
supports_run_offline_flag,
learner_url,
learner_preview_url,
learner_progress_url,
author_url,
monitor_url,
define_later_url,
pedagogical_planner_url,
help_url,
language_file,
create_date_time,
modified_date_time,
supports_outputs,
admin_url
)
VALUES
(
'laqa11',
'qaService',
'Question and Answer',
'Q/A Tool',
'qa',
'@tool_version@',
NULL,
NULL,
0,
2,
1,
'tool/laqa11/learningStarter.do?mode=learner',
'tool/laqa11/learningStarter.do?mode=author',
'tool/laqa11/learningStarter.do?mode=teacher',
'tool/laqa11/authoringStarter.do',
'tool/laqa11/monitoringStarter.do',
'tool/laqa11/defineLaterStarter.do',
'tool/laqa11/pedagogicalPlanner.do',
'http://wiki.lamsfoundation.org/display/lamsdocs/laqa11',
'org.lamsfoundation.lams.tool.qa.ApplicationResources',
NOW(),
NOW(),
1,
'tool/laqa11/laqa11admin.do'
)
