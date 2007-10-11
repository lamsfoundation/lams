-- Update the Multiple Choice tables from version 20070214 to 20070820
-- This is for the LAMS 2.0.1 release.

ALTER TABLE tl_lamc11_que_usr 
ADD COLUMN last_attempt_order INTEGER
, ADD COLUMN last_attempt_total_mark INTEGER;

ALTER TABLE tl_lamc11_content
ADD COLUMN displayAnswers TINYINT(1) NOT NULL DEFAULT 1 AFTER randomize;

-- Fix problems with the marks in the attempt table. It was recording the number of questions answered correctly for the activity.
-- Now it records the mark for each question. The overall mark is put in the  que_usr table.
update tl_lamc11_usr_attempt a
set a.mark = (select c.mark from tl_lamc11_que_content c where c.uid = a.mc_que_content_id) 
where a.isAttemptCorrect = 1;

update tl_lamc11_usr_attempt a
set a.mark = 0
where a.isAttemptCorrect = 0;

-- Now that the individual marks are right, we can recalculate the overall mark for an attempt. We need to find just the
-- final attempt for each user. At present, there can only be one option selected for a question, so we don't
-- need to worry about repeated data.
update tl_lamc11_que_usr qu1 set qu1.last_attempt_order = 
( select max(attemptOrder) from tl_lamc11_usr_attempt att where att.que_usr_id = qu1.uid );

update tl_lamc11_que_usr qu1 set qu1.last_attempt_total_mark = 
( select sum(mark) from tl_lamc11_usr_attempt att where att.que_usr_id = qu1.uid and att.attemptOrder = qu1.last_attempt_order);

-- update the tool version - special code that should only be executed if the upgrade is being done manually.
-- if it is being done via the tool deployer then it will update the version automatically.
-- update lams_tool set tool_version = "20070820" where tool_signature = "lamc11";