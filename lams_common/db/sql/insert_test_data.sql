SET FOREIGN_KEY_CHECKS=0;

INSERT INTO lams_organisation VALUES (1, 'Root', 'Root Organisation',null,1,NOW(),null);
INSERT INTO lams_organisation VALUES (2, 'Maquarie Uni', 'Macquarie University',1,2,NOW(),null);
INSERT INTO lams_organisation VALUES (3, 'MELCOE', 'Macquarie E-learning Center',2,3,NOW(),null);
INSERT INTO lams_organisation VALUES (4, 'LAMS', 'Lams Project Team',3,3,NOW(),null);
INSERT INTO lams_organisation VALUES (5, 'MAMS', 'Mams Project Team',3,3,NOW(),null);

INSERT INTO lams_user VALUES(1, 'sysadmin','sysadmin','Mr','Fei','Yang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'fyang@melcoe.mq.edu.au',0,NOW(),1,null,1,1);
INSERT INTO lams_user VALUES(2, 'test','test','Mr','Kevin','Han',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'khan@melcoe.mq.edu.au',0,NOW(),3,null,2,2);
INSERT INTO lams_user VALUES(3, 'lamskh01','dummy','Mr','Jacky','Fang',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'jfang@melcoe.mq.edu.au',0,NOW(),2,null,3,3);

INSERT INTO lams_user_organisation VALUES (1, 1, 1);
INSERT INTO lams_user_organisation VALUES (2, 2, 2);
INSERT INTO lams_user_organisation VALUES (3, 3, 2);
INSERT INTO lams_user_organisation VALUES (4, 4, 2);
INSERT INTO lams_user_organisation VALUES (5, 2, 3);
INSERT INTO lams_user_organisation VALUES (6, 3, 3);

INSERT INTO lams_user_organisation_role VALUES (1, 1, 1);
INSERT INTO lams_user_organisation_role VALUES (2, 2, 2);
INSERT INTO lams_user_organisation_role VALUES (3, 2, 3);
INSERT INTO lams_user_organisation_role VALUES (4, 2, 4);
INSERT INTO lams_user_organisation_role VALUES (5, 3, 2);
INSERT INTO lams_user_organisation_role VALUES (6, 3, 3);
INSERT INTO lams_user_organisation_role VALUES (7, 3, 4);
INSERT INTO lams_user_organisation_role VALUES (8, 4, 2);
INSERT INTO lams_user_organisation_role VALUES (9, 4, 3);
INSERT INTO lams_user_organisation_role VALUES (10, 4, 4);
INSERT INTO lams_user_organisation_role VALUES (11, 4, 5);
INSERT INTO lams_user_organisation_role VALUES (12, 5, 4);
INSERT INTO lams_user_organisation_role VALUES (13, 5, 5);
INSERT INTO lams_user_organisation_role VALUES (14, 6, 3);

INSERT INTO lams_authentication_method VALUES (1, 1, 'LAMS-Database');
INSERT INTO lams_authentication_method VALUES (2, 2, 'Oxford-WebAuth');
INSERT INTO lams_authentication_method VALUES (3, 3, 'MQ-LDAP');

insert into lams_workspace_folder (parent_folder_id,name,workspace_id) values(null,'Trial',1);

-- Populates the lams_learning_library table with default libraries

insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (1,'Notebook','Notebook','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (2,'NoticeBoard','NoticeBoard','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (3,'Multiple Choice Questions','Multiple Choice','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (4,'Questions and Answer','Question And Answer','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (5,'Message Board','Message Board','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (6,'Survey','Survey','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (7,'Chat','Chat','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (8,'Shared Resources','Shared Resources','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (9,'Voting','Voting','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (10,'Submit Files','Submit Files','20050207');
insert into lams_learning_library (learning_library_id,description,title,create_date_time) values (11,'Instant Messaging','Instant Messaging','20050207');

-- Populates the lams_tool table with default tools corresponding to each of the above libraries

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(1,'notebook_signature','notebookService','Notebook','Notebook Description',1,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(2,'noticeboard_signature','noticeboardService','Noticeboard','Noticeboard Description',2,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(3,'multiple_choice_signature','multipleChoiceService','Multiple Choice','Multiple Coice Description',3,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(4,'question_answer_signature','questionAnswerService','Question and Answers','Question and Answers Description',4,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(5,'message_board_signature','messageBoardService','Message Board','Message Board Description',5,1,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(6,'survey_signature','surveyService','Survey','Survey Description',6,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(7,'chat_signature','chatService','Chat','Chat Description',7,1,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(8,'shared_resources_signature','sharedResourcesService','Shared Resources','Shared Resources Description',8,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(9,'voting_signature','votingService','Voting','Voting Description',9,1,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(10,'submit_files_signature','submitFilesService','Submit Files','Submit Files Description',10,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,service_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(11,'instant_messaging_signature','instantMessagingService','Instant Messaging','Instant Messaging Description',11,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

-- Populates the lams_tool_content table with dummy default content for all above tools

insert into lams_tool_content (tool_content_id,tool_id) values (1,1);
insert into lams_tool_content (tool_content_id,tool_id) values (2,2);
insert into lams_tool_content (tool_content_id,tool_id) values (3,3);
insert into lams_tool_content (tool_content_id,tool_id) values (4,4);
insert into lams_tool_content (tool_content_id,tool_id) values (5,5);
insert into lams_tool_content (tool_content_id,tool_id) values (6,6);
insert into lams_tool_content (tool_content_id,tool_id) values (7,7);
insert into lams_tool_content (tool_content_id,tool_id) values (8,8);
insert into lams_tool_content (tool_content_id,tool_id) values (9,9);
insert into lams_tool_content (tool_content_id,tool_id) values (10,10);
insert into lams_tool_content (tool_content_id,tool_id) values (11,11);
insert into lams_tool_content (tool_content_id,tool_id) values (12,1);
insert into lams_tool_content (tool_content_id,tool_id) values (13,2);
insert into lams_tool_content (tool_content_id,tool_id) values (14,3);
insert into lams_tool_content (tool_content_id,tool_id) values (15,4);
insert into lams_tool_content (tool_content_id,tool_id) values (16,5);
insert into lams_tool_content (tool_content_id,tool_id) values (17,6);
insert into lams_tool_content (tool_content_id,tool_id) values (18,7);
insert into lams_tool_content (tool_content_id,tool_id) values (19,8);
insert into lams_tool_content (tool_content_id,tool_id) values (20,4);
insert into lams_tool_content (tool_content_id,tool_id) values (21,4);

--insert a testing grouping
insert into lams_grouping values(99,99,1,1,10,0,2);
insert into lams_grouping values(100,100,2,1,10,0,2);		

insert into lams_group values(87,99,1);
insert into lams_group values(88,100,1);

insert into lams_user_group values(1,87);
insert into lams_user_group values(2,88);

-- Populates the lams_learning_activity table with default activity templates that would be avaialbe to the
-- author in the left panel and a default dummy learning_design defination 

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(1,1,'Notebook Template','Notebook',10,20,null,'Help Text for Activity',
1,null,null,0,
null,1,'20050101','Offline  Instructions ',0,
null,null,1,1,
null,null,null,'NotebookImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(2,2,'NoticeBoard Template','NoticeBoard',10,20,null,'Help Text for Activity',
1,null,null,0,
null,2,'20050101','Offline  Instructions ',0,
null,null,2,2,
null,null,null,'NoticeBoardImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(3,3,'Multiple Choice Template','Multiple Choice',10,20,null,'Help Text for Activity',
1,null,null,0,
null,3,'20050101','Offline  Instructions ',0,
null,null,3,3,
null,null,null,'MultipleChoiceImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(4,4,'Question and Answer Template','Question and Answer',10,20,null,'Help Text for Activity',
1,null,null,0,
null,4,'20050101','Offline  Instructions ',0,
null,null,4,4,
null,null,null,'QAImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(5,5,'MessageBoard Template','MessageBoard',10,20,null,'Help Text for Activity',
1,null,null,0,
null,5,'20050101','Offline  Instructions ',0,
null,null,5,5,
null,null,null,'MBImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(6,6,'Survey Template','Survey',10,20,null,'Help Text for Activity',
1,null,null,0,
null,6,'20050101','Offline  Instructions ',0,
null,null,6,6,
null,null,null,'SurveyImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(7,7,'Chat Template','Chat',10,20,null,'Help Text for Activity',
1,null,null,0,
null,7,'20050101','Offline  Instructions ',0,
null,null,7,7,
null,null,null,'ChatImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(8,8,'Shared Resources Template','Shared Resources',10,20,null,'Help Text for Activity',
1,null,null,0,
null,8,'20050101','Offline  Instructions ',0,
null,null,8,8,
null,null,null,'SRImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(9,9,'Voting Template','Voting',10,20,null,'Help Text for Activity',
1,null,null,0,
null,9,'20050101','Offline  Instructions ',0,
null,null,9,9,
null,null,null,'VotingImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(10,10,'Submit Files Template','Submit Files',10,20,null,'Help Text for Activity',
1,null,null,0,
null,10,'20050101','Offline  Instructions ',0,
null,null,10,10,
null,null,null,'SubmitFilesImage');

insert into lams_learning_activity (activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(11,11,'Instant Messaging Template','Instant Messaging',10,20,null,'Help Text for Activity',
1,null,null,0,
null,11,'20050101','Offline  Instructions ',0,
null,null,11,11,
null,null,null,'IMImage');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(12,12,'Optional Activity Description','Optional Activity Title',10,20,null,'Help Text for Activity',
8,null,1,0,
1,null,'20050101','Offline Instructions ',0,
5,3,null,null,
null,'20050101','20050101','image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(13,13,'Parallel Activity Description','Parallel Activity Title',10,20,null,'Help Text for Activity',
7,null,1,0,
1,null,'20050101','Parallel Activity Offline  Instructions ',0,
null,null,null,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(14,14,'Sequence Activity Description','Sequence Activity Title',10,20,null,'Help Text for Activity',
9,null,1,0,
1,null,'20050101','Sequence Activity Offline  Instructions ',0,
null,null,null,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(15,1,'Notebook Activity Description','Notebook Activity Title',10,20,null,'Help Text for Activity',
1,null,1,0,
1,1,'20050101','Notebook Activity Offline  Instructions ',0,
null,null,1,12,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(16,2,'NoticeBoard Activity','NoticeBoard Activity',10,20,12,'Help Text for Activity',
1,null,0,0,
1,2,'20050101','NoticeBoard Activity Offline  Instructions ',0,
null,null,2,13,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(17,3,'Multiple Choice Activity ','Multiple Choice',10,20,12,'Help Text for Activity',
1,null,1,0,
1,3,'20050101','Tool Activity Offline  Instructions ',0,
null,null,3,14,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(18,4,'Question and Answer Activity','Question and Answer',10,20,13,'Help Text for Activity',
1,null,0,0,
1,4,'20050101','Tool Activity Offline  Instructions ',0,
null,null,4,15,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(19,5,'Message Board Activity','Message Board',10,20,13,'Help Text for Activity',
1,100,1,0,
1,5,'20050101','Tool Activity Offline  Instructions ',0,
null,null,5,16,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(20,6,'Survey Activity','Survey Title',10,20,null,'Help Text for Activity',
1,null,0,0,
1,6,'20050101','Tool Activity Offline  Instructions ',0,
null,null,6,17,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,offline_instructions,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id)
values
(21,7,
'Chat Activity Description','Chat Title','Help Text for Chat Activity',
10,20,
null,null,1,
99,99,0,0,
1,7,'20050101',
1,'Tool Activity Offline  Instructions ',
null,null,null,
7,18,
null,null,
null,null,
null,null,
'ChatImage',
null,null,
7);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image)
values
(22,8,'Shared Resources Activity','Shared Resources',10,20,14,'Help Text for Activity',
1,null,0,0,
1,8,'20050101','Tool Activity Offline  Instructions ',0,
null,null,8,19,
null,null,null,'image');

-- Sample GroupingActivity with RandomGrouping 

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,offline_instructions,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id)
values
(23,23,
'Random Grouping Activity Description','RandomGroupingActivity Title','Help Text for RandomGroupingActivity',
10,20,
null,null,2,
null,null,null,0,
1,null,'20050101',
0,null,
null,null,null,
null,null,
null,null,
null,null,
null,null,
'RandomGroupingActivityImage',
99,99,
null);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,offline_instructions,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id)
values
(24,24,
'QNA Activity Description','QNA Activity Title','Help Text for QNA Activity',
10,20,
null,null,1,
99,99,null,0,
1,4,'20050101',
1,'QNA Activity Offline  Instructions ',
null,null,null,
4,20,
null,null,
null,null,
null,null,
'QNAImage',
null,null,
4);

insert into lams_learning_activity
(activity_id,activity_ui_id,
description,title,help_text,
xcoord,ycoord,
parent_activity_id,parent_ui_id,learning_activity_type_id,
grouping_id,grouping_ui_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,
run_offline_flag,offline_instructions,
max_number_of_options,min_number_of_options,options_instructions,
tool_id,tool_content_id,
gate_activity_level_id,gate_open_flag,
gate_start_time_offset,gate_end_time_offset,
gate_start_date_time,gate_end_date_time,
library_activity_ui_image,
create_grouping_id,create_grouping_ui_id,
library_activity_id)
values
(25,25,
'QNA Activity Description','QNA Activity Title','Help Text for QNA Activity',
10,20,
14,14,1,
null,null,1,0,
1,4,'20050101',
1,'QNA Activity Offline  Instructions ',
null,null,null,
4,21,
null,null,
null,null,
null,null,
'QNAImage',
null,null,
4);

-- Populates the lams_learning_transition table with various transitions for the dummy design 

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(1,1,'Survey to NoteBook','Survey to NoteBook',15,20,1,'20050207',1,6);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(2,2,'Notebook to GroupingActivity','Notebook to GroupingActivity',23,15,1,'20050207',23,1);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(3,3,'GroupingActivity to Chat','GroupingActivity to Chat',21,23,1,'20050207',7,23);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(4,4,'Chat to QNA','Chat to QNA',24,21,1,'20050207',24,7);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(5,5,'QNA to OptionsActivity','QNA to OptionsActivity',12,24,1,'20050207',12,24);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(6,6,'OptionsActivity to Notebook','OptionsActivity to Notebook',16,12,1,'20050207',2,12);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(7,7,'Notebook to MultipleChoice','Notebook to MultipleChoice',17,16,1,'20050207',3,2);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(8,8,'MultipleChoice to ParallelActivity','MultipleChoice to ParallelActivity',13,17,1,'20050207',13,3);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(9,9,'ParallelActivity to QNA','ParallelActivity to QNA',18,13,1,'20050207',4,13);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(10,10,'QNA to MessageBoard','QNA to MessageBoard',19,18,1,'20050207',5,4);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(11,11,'MessageBoard to SequenceActivity','MessageBoard to SequenceActivity',14,19,1,'20050207',14,5);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(12,12,'SequenceActivity to Shared Resources','SequenceActivity to Shared Resources',22,14,1,'20050207',8,14);

insert into lams_learning_transition(transition_id,transition_ui_id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time,to_ui_id,from_ui_id)
values(13,13,'Shared Resources to QNA','Shared Resources to QNA',25,22,1,'20050207',25,8);


insert into lams_learning_design(learning_design_ui_id,description,title,first_activity_id,max_id,valid_design_flag,
								 read_only_flag,user_id,help_text,copy_type_id,create_date_time,version,
								 parent_learning_design_id,workspace_folder_id)
								values
								(1,'Test Learning Design','Test Learning Design title',20,1,1,0,1,
								'Help Text',1,'20041223','1.0',null,1);																
SET FOREIGN_KEY_CHECKS=1;