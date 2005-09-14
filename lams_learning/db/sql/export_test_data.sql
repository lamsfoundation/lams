INSERT INTO lams_user VALUES(1250,'learning test case 1','learning test case 1','Mr','test1','test1',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test1@melcoe.mq.edu.au',0,NOW(),1,null,4,'en','nz',1);
INSERT INTO lams_user VALUES(1251,'learning test case 2','learning test case 2','Mr','test2','test2',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test2@melcoe.mq.edu.au',0,NOW(),1,null,4,'en','nz',1);

INSERT INTO lams_grouping VALUES (1100, null, 3, null, null, 1090, null);
INSERT INTO lams_group VALUES (1090, 1100, 1);

INSERT INTO lams_grouping VALUES (1101, null, 3, null, null, 1091, null);
INSERT INTO lams_group VALUES (1091, 1101, 1);

INSERT INTO lams_user_organisation VALUES (1000,4, 1250);
INSERT INTO lams_user_organisation VALUES (1001,4, 1251);

INSERT INTO lams_user_organisation_role VALUES (500, 1000,5);
INSERT INTO lams_user_organisation_role VALUES (501, 1001,5);

INSERT INTO lams_user_group VALUES (1250,1090);
INSERT INTO lams_user_group VALUES (1251,1090);

INSERT INTO lams_learning_activity VALUES (600, 600, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (601, 601, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (602, 602, null, 'survey', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,6,'now()',0,null,null,null,6,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (603, 603, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (604, 604, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(605,605,'Optional Activity Description','Optional Activity Title',10,20,null,'Help Text for Activity',
7,null,1,0,
1,null,'20050101',0,
1,1,null,null,
null,'20050101','20050101','missing.swf',
3,1,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(606,606,'NoticeBoard Activity','NoticeBoard Activity',10,20,605,'Help Text for Activity',
1,null,0,0,
1,13,'now()',0,
null,null,13,null,
null,null,null,'/lams/images/icon_noticeboard.swf',
1,0,1);

insert into lams_learning_activity
(activity_id,activity_ui_id,description,title,xcoord,ycoord,parent_activity_id,help_text,
learning_activity_type_id,grouping_id,order_id,define_later_flag,
learning_design_id,learning_library_id,create_date_time,run_offline_flag,
max_number_of_options,min_number_of_options,tool_id,tool_content_id,
gate_activity_level_id,gate_start_time_offset,gate_end_time_offset,library_activity_ui_image,
grouping_support_type_id, apply_grouping_flag,activity_category_id)
values
(607,607,'Survey Activity','Survey Activity',10,20,605,'Help Text for Activity',
1,null,0,0,
1,6,'now()',0,
null,null,6,null,
null,null,null,'/lams/images/icon_noticeboard.swf',
1,0,1);

INSERT INTO lams_learning_activity VALUES (608, 608, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);



INSERT INTO lams_learning_design VALUES (700, 700, 'maitest', 'maitest',600,1,1,0,null,1,'help text','online instructons','offline instructions',1, 'now()','1.0',null,2,null,null,null,null,null,null,null,null,null);

UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='600';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='601';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='602';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='603';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='604';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='605';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='606';
UPDATE lams_learning_activity SET learning_design_id='700' WHERE activity_id='607';


INSERT INTO lams_learning_transition VALUES (800,800,'nb to nb', 'nb to nb', 601, 600,700,'now()', 601,600);
INSERT INTO lams_learning_transition VALUES (801,801,'nb to survey', 'nb to survey', 602, 601,700,'now()', 602,601);
INSERT INTO lams_learning_transition VALUES (802,802,'survey to nb', 'survey to nb', 603, 602,700,'now()', 603,602);
INSERT INTO lams_learning_transition VALUES (803,803,'nb to nb', 'nb to nb', 604, 603,700,'now()', 604,603);
INSERT INTO lams_learning_transition VALUES (804,804,'nb to optional', 'nb to optional', 605, 604,700,'now()', 605,604);

INSERT INTO lams_lesson VALUES (900,700,1,'maitestlesson','testing purposes','now()',4,1100,3,null,null,null,null);

INSERT INTO lams_learner_progress VALUES (1300, 1250, 900, 0, 0, 'now()', null, 601, 601, 600, 0);

INSERT INTO lams_progress_completed VALUES (1300, 600);
INSERT INTO lams_progress_completed VALUES (1300, 601);
INSERT INTO lams_progress_completed VALUES (1300, 602);
INSERT INTO lams_progress_completed VALUES (1300, 603);
INSERT INTO lams_progress_completed VALUES (1300, 604);
INSERT INTO lams_progress_completed VALUES (1300, 605);
INSERT INTO lams_progress_completed VALUES (1300, 606);

-- Another Set of Data
INSERT INTO lams_user VALUES(2250,'learning test case 5','learning test case 1','Mr','test1','test1',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test1@melcoe.mq.edu.au',0,NOW(),1,null,4,'en','nz',1);
INSERT INTO lams_user VALUES(2251,'learning test case 6','learning test case 2','Mr','test2','test2',null,null,null,'Sydney','NSW','Australia',null,null,null,null,'test2@melcoe.mq.edu.au',0,NOW(),1,null,4,'en','nz',1);

INSERT INTO lams_grouping VALUES (2100, null, 3, null, null, 2090, null);
INSERT INTO lams_group VALUES (2090, 2100, 1);

INSERT INTO lams_grouping VALUES (2101, null, 3, null, null, 2091, null);
INSERT INTO lams_group VALUES (2091, 2101, 1);

INSERT INTO lams_user_organisation VALUES (2000,4, 2250);
INSERT INTO lams_user_organisation VALUES (2001,4, 2251);

INSERT INTO lams_user_organisation_role VALUES (2500, 2000,5);
INSERT INTO lams_user_organisation_role VALUES (2501, 2001,5);

INSERT INTO lams_user_group VALUES (2250,2090);
INSERT INTO lams_user_group VALUES (2251,2090);



INSERT INTO lams_learning_activity VALUES (2600, 2600, null, 'nb', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2601, 2601, null, 'survey', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,6,'now()',0,null,null,null,6,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2602, 2602, null, 'qa', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,14,'now()',0,null,null,null,14,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2603, 2603, null, 'submit', null, 10, 20, null, null, 1,1,0,null,null,null,0,null,10,'now()',0,null,null,null,10,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2604, 2604, null, 'Optional Activity', null, 10, 20, null, null, 7,3,1,null,null,null,0,null,null,'now()',0,1,1,null,null,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2605, 2605, null, 'NB', null, 10, 20, 2604, null, 1,1,0,null,null,0,0,null,13,'now()',0,null,null,null,13,null,4,null,null,null,null,null,null,null,null,null,null);
INSERT INTO lams_learning_activity VALUES (2606, 2606, null, 'survey', null, 10, 20, 2604, null, 1,1,0,null,null,1,0,null,6,'now()',0,null,null,null,6,null,4,null,null,null,null,null,null,null,null,null,null);

INSERT INTO lams_learning_design VALUES (2701, 701, 'maitest', 'maitest',2600,1,1,0,null,1,'help text','online instructons','offline instructions',1, 'now()','1.0',null,2,null,null,null,null,null,null,null,null,null);

UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2600';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2601';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2602';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2603';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2604';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2605';
UPDATE lams_learning_activity SET learning_design_id='2701' WHERE activity_id='2606';

INSERT INTO lams_learning_transition VALUES (2800,2800,'nb to survey', 'nb to survey', 2601, 2600,2701,'now()', 2601,2600);
INSERT INTO lams_learning_transition VALUES (2801,2801,'survey to qa', 'survey to qa', 2602, 2601,2701,'now()', 2602,2601);
INSERT INTO lams_learning_transition VALUES (2802,2802,'qa to submit', 'qa to submit', 2603, 2602,2701,'now()', 2603,2602);
INSERT INTO lams_learning_transition VALUES (2803,2803,'submit to optional', 'submit to optional', 2604, 2603,2701,'now()', 2604,2603);

INSERT INTO lams_lesson VALUES (2900,2701,1,'maitestlesson','testing purposes','now()',4,2101,3,null,null,null,null);

INSERT INTO lams_learner_progress VALUES (2300, 2250, 2900, 0, 0, 'now()', null, 2606, 2606, 2605, 0);

INSERT INTO lams_progress_completed VALUES (2300, 2600);
INSERT INTO lams_progress_completed VALUES (2300, 2601);
INSERT INTO lams_progress_completed VALUES (2300, 2602);
INSERT INTO lams_progress_completed VALUES (2300, 2603);
INSERT INTO lams_progress_completed VALUES (2300, 2604);
INSERT INTO lams_progress_completed VALUES (2300, 2605);
INSERT INTO lams_progress_completed VALUES (2300, 2606);




