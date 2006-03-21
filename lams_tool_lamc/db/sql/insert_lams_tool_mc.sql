-- $Id$

	
INSERT INTO tl_lamc11_content  (
	  uid
     , content_id 
     , title 
     , instructions 
     , creation_date
     , questions_sequenced 
     , username_visible 
     , created_by 
     , monitoring_report_title 
     , report_title 
     , run_offline 
     , define_later 
     , synch_in_monitor 
     , offline_instructions 
     , online_instructions 
     , end_learning_message 
     , content_in_use 
     , retries 
     , show_feedback 
     , show_report
)
VALUES (
	null,
	10,
	'Mc Title',
	'Mc Instructions',
	now(),
	0,
	0,
	1,
	'Monitoring Report',
	'Report',
	0,
	0,
	0,
	'offline instructions',
	'online instructions',
	'End of the activity...',
	0,
	0,
	0,
	0);
	

		
INSERT INTO tl_lamc11_que_content  (
	  uid,
	  question,
	  weight,
	  disabled,
	  display_order,
	  mc_content_id
)VALUES (
	null,
	'a sample question',
	0,
	1,
	1,
	1);
	
	
	INSERT INTO tl_lamc11_options_content  (
	  uid,
	  correct_option,
	  mc_que_content_id,
	  mc_que_option_text
)
VALUES (
	null,
	0,
	1,
	'sample answer 1');
	
	
		
