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

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(1,'notebook_signature','notebook_classname','Notebook','Notebook Description',1,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(2,'noticeboard_signature','noticeboard_classname','Noticeboard','Noticeboard Description',2,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(3,'multiple_choice_signature','multiple_choice_classname','Multiple Choice','Multiple Coice Description',3,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(4,'question_answer_signature','question_answer_classname','Question and Answers','Question and Answers Description',4,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(5,'message_board_signature','message_board_classname','Message Board','Message Board Description',5,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(6,'survey_signature','survey_classname','Survey','Survey Description',6,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(7,'chat_signature','chat_classname','Chat','Chat Description',7,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(8,'shared_resources_signature','shared_resources_classname','Shared Resources','Shared Resources Description',8,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(9,'voting_signature','voting_classname','Voting','Voting Description',9,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(10,'submit_files_signature','submit_files_classname','Submit Files','Submit Files Description',10,0,0,
'learner_url','author_url','define_later_url','export_portfolio_url','monitor_url');

insert into lams_tool (tool_id,tool_signature,class_name,tool_display_name,description,default_tool_content_id,supports_grouping_flag,supports_define_later_flag,
learner_url,author_url,define_later_url,export_portfolio_url,monitor_url)
values(11,'instant_messaging_signature','instant_messaging_classname','Instant Messaging','Instant Messaging Description',11,0,0,
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

-- Populates the lams_learning_activity table with default activity templates that would be avaialbe to the
-- author in the left panel and a default dummy learning_design defination 

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(1,1,'Notebook Template','Notebook',10,20,null,
1,null,null,0,
null,1,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'NotebookImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(2,2,'NoticeBoard Template','NoticeBoard',10,20,null,
1,null,null,0,
null,2,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'NoticeBoardImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(3,3,'Multiple Choice Template','Multiple Choice',10,20,null,
1,null,null,0,
null,3,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'MultipleChoiceImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(4,4,'Question and Answer Template','Question and Answer',10,20,null,
1,null,null,0,
null,4,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'QAImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(5,5,'MessageBoard Template','MessageBoard',10,20,null,
1,null,null,0,
null,5,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'MBImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(6,6,'Survey Template','Survey',10,20,null,
1,null,null,0,
null,6,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'SurveyImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(7,7,'Chat Template','Chat',10,20,null,
1,null,null,0,
null,7,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'ChatImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(8,8,'Shared Resources Template','Shared Resources',10,20,null,
1,null,null,0,
null,8,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'SRImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(9,9,'Voting Template','Voting',10,20,null,
1,null,null,0,
null,9,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'VotingImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(10,10,'Submit Files Template','Submit Files',10,20,null,
1,null,null,0,
null,10,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'SubmitFilesImage');

insert into lams_learning_activity (activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(11,11,'Instant Messaging Template','Instant Messaging',10,20,null,
1,null,null,0,
null,11,'20050101','Offline  Instructions ',
null,null,null,null,
null,null,null,'IMImage');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(12,12,'Optional Activity Description','Optional Activity Title',10,20,null,
8,null,1,0,
1,null,'20050101','Offline Instructions ',
5,3,null,null,
null,'20050101','20050101','image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(13,13,'Parallel Activity Description','Parallel Activity Title',10,20,null,
7,null,1,0,
1,null,'20050101','Parallel Activity Offline  Instructions ',
null,null,null,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(14,14,'Sequence Activity Description','Sequence Activity Title',10,20,null,
9,null,1,0,
1,null,'20050101','Sequence Activity Offline  Instructions ',
null,null,null,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(15,1,'Notebook Activity Description','Notebook Activity Title',10,20,null,
1,null,1,0,
1,1,'20050101','Notebook Activity Offline  Instructions ',
null,null,1,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(16,2,'NoticeBoard Activity','NoticeBoard Activity',10,20,12,
1,null,0,0,
1,2,'20050101','NoticeBoard Activity Offline  Instructions ',
null,null,2,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(17,3,'Multiple Choice Activity ','Multiple Choice',10,20,12,
1,null,1,0,
1,3,'20050101','Tool Activity Offline  Instructions ',
null,null,3,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(18,4,'Question and Answer Activity','Question and Answer',10,20,13,
1,null,0,0,
1,4,'20050101','Tool Activity Offline  Instructions ',
null,null,4,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(19,5,'Message Board Activity','Message Board',10,20,13,
1,null,1,0,
1,5,'20050101','Tool Activity Offline  Instructions ',
null,null,5,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(20,6,'Survey Activity','Survey Title',10,20,14,
1,null,0,0,
1,6,'20050101','Tool Activity Offline  Instructions ',
null,null,6,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(21,7,'Chat Activity','Chat Title',10,20,14,
1,null,1,0,
1,7,'20050101','Tool Activity Offline  Instructions ',
null,null,7,null,
null,null,null,'image');

insert into lams_learning_activity
(activity_id,id,description,title,xcoord,ycoord,parent_activity_id,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,offline_instructions,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_date_time,gate_end_date_time,library_activity_ui_image)
values
(22,8,'Shared Resources Activity','Shared Resources',10,20,14,
1,null,2,0,
1,8,'20050101','Tool Activity Offline  Instructions ',
null,null,8,null,
null,null,null,'image');

# Populates the lams_learning_transition table with various transitions for the dummy design 

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(1,1,'NoteBook to Optional Activity','Notebook to Optional Activity',12,null,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(2,2,'Optional to Child Activity','To NoticeBoard',16,12,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(3,3,'To Multiple Choice','To Multiple Choice',17,16,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(4,4,'To Multiple Choice','To Multiple Choice',13,17,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(5,5,'To Multiple Choice','To Multiple Choice',18,13,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(6,6,'To Multiple Choice','To Multiple Choice',19,18,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(7,7,'To Multiple Choice','To Multiple Choice',14,19,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(8,8,'To Multiple Choice','To Multiple Choice',20,14,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(9,9,'To Multiple Choice','To Multiple Choice',21,20,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(10,10,'To Multiple Choice','To Multiple Choice',22,21,1,'20050207');

insert into lams_learning_transition(transition_id,id,description,title,to_activity_id,from_activity_id,learning_design_id,create_date_time)
values(11,11,'To Multiple Choice','To Multiple Choice',null,22,1,'20050207');

insert into lams_learning_design(id,description,title,first_activity_id,max_id,valid_design_flag,
								 read_only_flag,user_id,help_text,lesson_copy_flag,create_date_time,version,
								 parent_learning_design_id,open_date_time,close_date_time,workspace_folder_id)
								values
								(1,'Test Learning Design','Test Learning Design title',15,1,1,0,1,
								'Help Text',0,'20041223','1.0',null,'20041223','20041223',1);																
SET FOREIGN_KEY_CHECKS=1;