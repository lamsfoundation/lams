-- $Id$

INSERT INTO tl_lasbmt11_content (content_id,title,instruction,define_later,run_offline,content_in_use,lock_on_finished) values(2,"Test Submission","Submit your a file",0,0,0,0);
INSERT INTO tl_lasbmt11_session (session_id,content_id,status) values(3,2,1);
INSERT INTO tl_lasbmt11_report (report_id, comments, marks, date_marks_released) VALUES (1, 'Not much effort', 1, '2005-09-08 16:04:56');
INSERT INTO tl_lasbmt11_session_learners (learner_id, user_id, finished, session_id) VALUES (1, 1, 1, 3);
INSERT INTO tl_lasbmt11_submission_details (submission_id, filePath, fileDescription, date_of_submission, uuid, version_id, session_id, learner_id) VALUES (1, 'myfile.txt', 'My Submission', '2005-09-08 16:04:27', 3, 1, 3, 1);
