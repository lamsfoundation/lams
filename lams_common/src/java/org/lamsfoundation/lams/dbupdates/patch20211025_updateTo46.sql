SET AUTOCOMMIT = 0;
SET FOREIGN_KEY_CHECKS = 0;

-- This patch contains files patch20190119.sql to patch20211025.sql
-- It should upgrade LAMS to version 4.6

-- LDEV-4755 Add configuration settings for ClamAV antivirus
INSERT INTO lams_configuration VALUES
('AntivirusEnable', 'false',     'config.av.enable', 'config.header.antivirus', 'BOOLEAN', 1),
('AntivirusHost',   'localhost', 'config.av.host',   'config.header.antivirus', 'STRING',  0),
('AntivirusPort',   '3310',      'config.av.port',   'config.header.antivirus', 'LONG',    0);

-- LDEV-4587 Direction (asc/desc) of ordered branching. NULL means it is non-ordered tool-based branching
ALTER TABLE lams_learning_activity
ADD COLUMN branching_ordered_asc TINYINT(1);

-- LDEV-4767 Add new event types for log in and log out
INSERT INTO lams_log_event_type VALUES (24, 'TYPE_LOGIN',  'SECURITY'), 
									   (25, 'TYPE_LOGOUT', 'SECURITY');

-- LDEV-4778 Add new event type for sysadmin configuration change
INSERT INTO lams_log_event_type VALUES (26, 'TYPE_CONFIG_CHANGE',  'SECURITY');

-- LDEV-4788 Remove reference to organisation in learning outcomes. They are all global now.
ALTER TABLE lams_outcome_scale DROP FOREIGN KEY FK_lams_outcome_scale_1,
						 	   DROP KEY code_2,
						 	   DROP COLUMN organisation_id,
						 	   DROP COLUMN content_folder_id;
						 
ALTER TABLE lams_outcome DROP FOREIGN KEY FK_lams_outcome_1,
						 DROP KEY code_2,
						 DROP COLUMN organisation_id,
						 DROP COLUMN content_folder_id;

-- LDEV-4819 Add configuration setting for disabling Learning Outcome quick add by authors
INSERT INTO lams_configuration VALUES
('LearningOutcomeQuickAddEnable', 'true', 'config.learning.outcome.add.enable', 'config.header.features', 'BOOLEAN', 1);
						 
CREATE FUNCTION `strip_tags`($str MEDIUMTEXT, $leaveMeaningfulTags BOOLEAN) RETURNS MEDIUMTEXT
BEGIN
    DECLARE $start, $end INT DEFAULT 1;
    DECLARE $tag CHAR(3);
    IF ($str IS NULL) THEN RETURN NULL; END IF;
    main: LOOP
        SET $start = LOCATE("<", $str, $start);
        IF (NOT $start) THEN RETURN $str; END IF;
        SET $end = LOCATE(">", $str, $start);
        IF (NOT $end) THEN SET $end = $start; END IF;
        IF ($leaveMeaningfulTags = FALSE) THEN
        	SET $tag = SUBSTRING($str, $start + 1, 3);
        	IF $tag = 'img' OR $tag = 'IMG' THEN SET $start = $end; ITERATE main; END IF;
        END IF;
        SET $str = INSERT($str, $start, $end - $start + 1, "");
    END LOOP;
END;

-- LDEV-4746 Create Question Bank table structure and migrate existing data

-- AUTO COMMIT stays ON because there is so many ALTER TABLE statements which are committed immediately anyway, that it does not make a difference

-- Create QB question table

CREATE TABLE lams_qb_question (`uid` BIGINT AUTO_INCREMENT,
                               `uuid` BINARY(16) NOT NULL,
                               `type` TINYINT NOT NULL,
                               `question_id` INT NOT NULL,
                               `version` SMALLINT NOT NULL DEFAULT 1,
                               `create_date` DATETIME,
                               `content_folder_id` char(36),
                               `name` TEXT,
                               `description` MEDIUMTEXT,
                               `max_mark` INT,
                               `feedback` TEXT,
                               `penalty_factor` float DEFAULT 0,
                               `answer_required` TINYINT(1) DEFAULT 0,
                               `multiple_answers_allowed` TINYINT(1) DEFAULT 0,
                               `incorrect_answer_nullifies_mark` TINYINT(1) DEFAULT 0,
                               `feedback_on_correct` TEXT,
                               `feedback_on_partially_correct` TEXT,
                               `feedback_on_incorrect` TEXT,
                               `shuffle` TINYINT(1) DEFAULT 0,
                               `prefix_answers_with_letters` TINYINT(1) DEFAULT 0,
                               `case_sensitive` TINYINT(1) DEFAULT 0,
                               `correct_answer` TINYINT(1) DEFAULT 0,
                               `allow_rich_editor` TINYINT(1) DEFAULT 0,
                               `max_words_limit` int(11) DEFAULT 0,
                               `min_words_limit` int(11) DEFAULT 0,
                               `hedging_justification_enabled` TINYINT(1) DEFAULT 0,
                               `tmp_question_id` BIGINT,
                               `owner_id` BIGINT,
                               PRIMARY KEY (uid),
                               INDEX (tmp_question_id),
                               CONSTRAINT UQ_question_version UNIQUE INDEX (question_id, version));
                               
-- Create a trigger to run before insert to generate the UUID for the uuid column
CREATE TRIGGER before_insert_qb_question
  BEFORE INSERT ON lams_qb_question
  FOR EACH ROW
  SET new.uuid = UUID_TO_BIN(UUID());
                               
-- Create a question table from which tools' questions will inherit                           
CREATE TABLE lams_qb_tool_question (`tool_question_uid` BIGINT AUTO_INCREMENT,
                                    `qb_question_uid` BIGINT NOT NULL,
                                    `tool_content_id` BIGINT NOT NULL,
                                    `display_order` TINYINT UNSIGNED NOT NULL DEFAULT 1,
                                    PRIMARY KEY (tool_question_uid),
                                    INDEX (tool_content_id),
                                    CONSTRAINT FK_lams_qb_tool_question_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON UPDATE CASCADE);

-- create Question Bank option
CREATE TABLE lams_qb_option (`uid` BIGINT AUTO_INCREMENT,
                             `qb_question_uid` BIGINT NOT NULL,
                             `display_order` TINYINT NOT NULL DEFAULT 1,
                             `name` TEXT,
                             `matching_pair` TEXT,
                             `numerical_option` float DEFAULT 0,
                             `max_mark` float DEFAULT 0,
                             `accepted_error` float DEFAULT 0,
                             `feedback` TEXT,
                             PRIMARY KEY (uid),
                             INDEX (display_order),
                             CONSTRAINT FK_lams_qb_option_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- create Question Bank question unit (used by numerical type of questions only)
CREATE TABLE lams_qb_question_unit (`uid` BIGINT AUTO_INCREMENT,
                                    `qb_question_uid` BIGINT NOT NULL,
                                    `display_order` TINYINT NOT NULL DEFAULT 1,
                                    `multiplier` float DEFAULT 0,
                                    `name` varchar(255),
                                    PRIMARY KEY (uid),
                                    CONSTRAINT FK_lams_qb_question_unit_1 FOREIGN KEY (qb_question_uid) REFERENCES lams_qb_question (uid) ON DELETE CASCADE ON UPDATE CASCADE);

-- create an answer table from which tools' answers will inherit
CREATE TABLE lams_qb_tool_answer (`answer_uid` BIGINT AUTO_INCREMENT,
                                  `tool_question_uid` BIGINT NOT NULL,
                                  `qb_option_uid` BIGINT DEFAULT NULL,
                                  `answer` MEDIUMTEXT,
                                  PRIMARY KEY (answer_uid),
                                  CONSTRAINT FK_lams_qb_tool_answer_1 FOREIGN KEY (tool_question_uid) 
                                      REFERENCES lams_qb_tool_question (tool_question_uid) ON DELETE CASCADE ON UPDATE CASCADE,
                                  CONSTRAINT FK_lams_qb_tool_answer_2 FOREIGN KEY (qb_option_uid)
                                      REFERENCES lams_qb_option (uid)  ON DELETE CASCADE ON UPDATE CASCADE);
                             
-- Migrate MCQ question into Question Bank question
-- This part of patch should be in MCQ, but it must be run after lams_qb_question is created and it must not run in parallel with other tools' patches,
-- so the safest solution is to place it here
            
-- default value for a concat result is 1024 characters, which can be too little for multiple concatenated answers
-- we choose a value big enough to accept anything
SET group_concat_max_len = 10000000;


-- MULTIPLE CHOICE (MCQ)

-- remove characters that prevent detecting identical questions
UPDATE tl_lamc11_que_content SET `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
UPDATE tl_lamc11_options_content SET `mc_que_option_text` = TRIM(REPLACE(REPLACE(REPLACE(mc_que_option_text, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));

-- create a mapping of MCQ question UID -> its question text + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
-- Also remove all whitespace just for less demanding matching
CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
                           content MEDIUMTEXT)
    AS SELECT q.uid AS question_uid,
              REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(question, mc_que_option_text ORDER BY displayOrder), true)
                                        COLLATE utf8mb4_0900_ai_ci, 
                                ' ', ''),
                       '\t', ''), 
              '&nbsp;', '') AS content          
    FROM tl_lamc11_que_content AS q
    JOIN tl_lamc11_options_content AS o ON q.uid = o.mc_que_content_id
    GROUP BY q.uid;

-- create a mapping of MCQ question UID -> UID of one of MCQ questions which holds the same content
CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
                                 target_uid BIGINT)
    SELECT q.question_uid, merged.question_uid AS target_uid
    FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
    JOIN tmp_question AS q USING (content)
    GROUP BY q.question_uid;
    
ALTER TABLE tmp_question_match ADD INDEX (target_uid);

                                    
-- fill Question Bank question table with unique questions, with manually incremented question ID
SET @question_id = (SELECT IF(MAX(question_id) IS NULL, 0, MAX(question_id)) FROM lams_qb_question);
                               
INSERT INTO lams_qb_question (uid, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id) 
    SELECT NULL, 1, @question_id:=@question_id + 1, 1, IFNULL(c.creation_date, NOW()),
        SUBSTRING(TRIM(REPLACE(REPLACE(strip_tags(mcq.question, false) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', '')), 1, 200),
        mcq.question, IFNULL(mcq.max_mark, 1), mcq.feedback, q.target_uid
    FROM (SELECT uid,
                 question AS question,
                 mark AS max_mark,
                 IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback,
                 mc_content_id
          FROM tl_lamc11_que_content) AS mcq
    JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
        ON mcq.uid = q.target_uid
    JOIN tl_lamc11_content AS c
        ON mcq.mc_content_id = c.uid;

-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
    SELECT q.question_uid, qb.uid, c.content_id, mcq.display_order
    FROM lams_qb_question AS qb
    JOIN tmp_question_match AS q
        ON qb.tmp_question_id = q.target_uid
    JOIN tl_lamc11_que_content AS mcq
        ON q.question_uid = mcq.uid
    JOIN tl_lamc11_content AS c
        ON mcq.mc_content_id = c.uid;
        
-- remove columns from MCQ which are duplicated in Question Bank
ALTER TABLE tl_lamc11_que_content DROP COLUMN question,
                                  DROP COLUMN mark,
                                  DROP COLUMN display_order,
                                  DROP COLUMN feedback;

-- add missing display order in options, if any
UPDATE tl_lamc11_options_content AS c 
  JOIN (SELECT MIN(uid)-1 AS uid_shift, mc_que_content_id
    FROM tl_lamc11_options_content
    WHERE displayOrder IS NULL
    GROUP BY mc_que_content_id) AS s
  USING (mc_que_content_id)
  SET c.displayOrder = c.uid - s.uid_shift
  WHERE c.displayOrder IS NULL;                                  
                                  
-- fill table with options matching unique QB questions inserted above
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, max_mark)
    SELECT q.uid, o.displayOrder, o.mc_que_option_text, o.correct_option
    FROM tl_lamc11_options_content AS o
    JOIN lams_qb_question AS q
        ON o.mc_que_content_id = q.tmp_question_id
    ORDER BY  o.mc_que_content_id, o.displayOrder;

ALTER TABLE tl_lamc11_usr_attempt DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_2, 
                                  DROP FOREIGN KEY FK_tl_lamc11_usr_attempt_3,
                                  DROP INDEX attempt_unique_index;

-- rewrite references from MCQ options to QB options
UPDATE tl_lamc11_usr_attempt AS ua, tl_lamc11_options_content AS mco, lams_qb_tool_question AS tq, lams_qb_option AS qo
    SET ua.mc_que_option_id = qo.uid
    WHERE mco.displayOrder = qo.display_order
        AND ua.mc_que_option_id = mco.uid
        AND qo.qb_question_uid = tq.qb_question_uid
        AND mco.mc_que_content_id = tq.tool_question_uid;

-- prepare for answer inheritance        
INSERT INTO lams_qb_tool_answer
    SELECT uid, mc_que_content_id, mc_que_option_id, NULL FROM tl_lamc11_usr_attempt;

-- clean up
ALTER TABLE tl_lamc11_usr_attempt DROP COLUMN mc_que_content_id,
                                  DROP COLUMN mc_que_option_id;
                                  
DROP TABLE tl_lamc11_options_content;

-- prepare for next tool migration, recreate tables
DROP TABLE tmp_question;
DROP TABLE tmp_question_match;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
                           content MEDIUMTEXT);

CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
                                 target_uid BIGINT);
ALTER TABLE tmp_question_match ADD INDEX (target_uid);


-- SCRATCHIE

-- delete corrupted data
DELETE FROM tl_lascrt11_scratchie_answer WHERE scratchie_item_uid IS NULL;

-- shift Scratchie question UIDs by offset equal to existing UIDs of MCQ in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
-- remove characters that prevent detecting identical questions
UPDATE tl_lascrt11_scratchie_item SET `title` = TRIM(REPLACE(REPLACE(REPLACE(title, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
                                      `description` = TRIM(REPLACE(REPLACE(REPLACE(description, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
                                       uid = uid + @max_tool_question_id ORDER BY uid DESC;
UPDATE tl_lascrt11_scratchie_answer SET `description` = TRIM(REPLACE(REPLACE(REPLACE(description, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
-- UPDATE tl_lascrt11_scratchie_answer SET scratchie_item_uid = scratchie_item_uid + @max_tool_question_id ORDER BY scratchie_item_uid DESC;

-- create a mapping of Scratchie question UID -> its question description + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
INSERT INTO tmp_question
    SELECT q.uid,
             REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.description ORDER BY o.order_id), true)
                                     COLLATE utf8mb4_0900_ai_ci,
                             ' ', ''),
                     '\t', ''),
             '&nbsp;', '')
    FROM tl_lascrt11_scratchie_item AS q
    JOIN tl_lascrt11_scratchie_answer AS o
        ON q.uid = o.scratchie_item_uid
    GROUP BY q.uid;
    
-- create a similar mapping for existing questions in QB
CREATE TABLE tmp_qb_question (question_uid BIGINT PRIMARY KEY,
                              content MEDIUMTEXT)
    AS SELECT q.uid AS question_uid,
              REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.name ORDER BY o.display_order), true)
                                        COLLATE utf8mb4_0900_ai_ci, 
                                ' ', ''),
                       '\t', ''), 
              '&nbsp;', '') AS content
    FROM lams_qb_question AS q
    JOIN lams_qb_option AS o
        ON q.uid = o.qb_question_uid
    GROUP BY q.uid;
    
-- create a table which holds IDs of questions which are already in the question bank
CREATE TABLE tmp_qb_question_match (question_uid BIGINT PRIMARY KEY, qb_question_uid BIGINT)
    AS SELECT q.question_uid, qb.question_uid AS qb_question_uid
    FROM tmp_question AS q
    JOIN tmp_qb_question AS qb
        USING (content)
    GROUP BY q.question_uid;

ALTER TABLE tmp_qb_question_match ADD INDEX (qb_question_uid);

-- create a mapping of Scratchie question UID -> UID of one of Scratchie questions which holds the same content
INSERT INTO tmp_question_match
    SELECT q.question_uid, merged.question_uid
    FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
    JOIN tmp_question AS q
        USING (content)
    LEFT JOIN tmp_qb_question_match AS qb
        ON q.question_uid = qb.question_uid
    WHERE qb.question_uid IS NULL
    GROUP BY q.question_uid;

-- reset column for matching QB questions with Scratchie questions
UPDATE lams_qb_question SET tmp_question_id = -1;
    
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question (uid, `type`, question_id, version, create_date, name, description, max_mark, feedback, tmp_question_id)
    SELECT NULL, 1, @question_id:=@question_id + 1, 1, sq.create_date, 
    TRIM(REPLACE(REPLACE(strip_tags(sq.question, false) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', '')),
    TRIM(sq.description), NULL, NULL, q.target_uid
    FROM (SELECT uid,
                 title AS question,
                 description,
                 create_date
          FROM tl_lascrt11_scratchie_item) AS sq
    JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
    ON sq.uid = q.target_uid;
    
-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
    SELECT q.question_uid, qb.uid, s.content_id, sq.order_id
    FROM lams_qb_question AS qb
    JOIN tmp_question_match AS q
        ON qb.tmp_question_id = q.target_uid
    JOIN tl_lascrt11_scratchie_item AS sq
        ON q.question_uid = sq.uid
    JOIN tl_lascrt11_scratchie AS s
        ON sq.scratchie_uid = s.uid;
    
-- set up references to QB question UIDs for existing questions
INSERT INTO lams_qb_tool_question
    SELECT qb.question_uid, qb.qb_question_uid, s.content_id, sq.order_id
    FROM tmp_qb_question_match AS qb
    JOIN tl_lascrt11_scratchie_item AS sq
        ON qb.question_uid = sq.uid
    JOIN tl_lascrt11_scratchie AS s
        ON sq.scratchie_uid = s.uid;
        
-- update question names generated for MCQ with real names from Scratchie 
UPDATE lams_qb_question AS q 
    JOIN (SELECT * FROM tmp_qb_question_match GROUP BY qb_question_uid) AS m
        ON q.uid = m.qb_question_uid
    JOIN tl_lascrt11_scratchie_item AS si
        ON si.uid = m.question_uid
    SET q.name = TRIM(si.title);
        
-- delete obsolete columns
ALTER TABLE tl_lascrt11_scratchie_item DROP FOREIGN KEY FK_NEW_610529188_F52D1F93EC0D3147, 
                                       DROP COLUMN title,
                                         DROP COLUMN description,
                                          DROP COLUMN create_date,
                                          DROP COLUMN create_by_author,
                                          DROP COLUMN session_uid,
                                          DROP COLUMN order_id;

-- some Scratchie options can be ordered from 0, some from 1
-- shift ones ordered from 0 by +1 to match the other group                                         
CREATE TABLE tmp_scratchie_answer
SELECT scratchie_item_uid FROM tl_lascrt11_scratchie_answer WHERE order_id = 0 AND scratchie_item_uid IS NOT NULL;

ALTER TABLE tmp_scratchie_answer ADD PRIMARY KEY (scratchie_item_uid);
                                          
UPDATE tl_lascrt11_scratchie_answer AS sa
SET order_id = order_id + 1
WHERE EXISTS
    (SELECT 1 FROM tmp_scratchie_answer WHERE scratchie_item_uid = sa.scratchie_item_uid);
        
DROP TABLE tmp_scratchie_answer;


-- fill table with options matching unique QB questions inserted above          
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, max_mark)
    SELECT q.uid, o.order_id, o.description, o.correct
    FROM tl_lascrt11_scratchie_answer AS o
    JOIN lams_qb_question AS q
        ON o.scratchie_item_uid = q.tmp_question_id
    ORDER BY o.scratchie_item_uid, o.order_id;
    

ALTER TABLE tl_lascrt11_answer_log ADD COLUMN scratchie_item_uid BIGINT;

-- shift Scratchie answer UIDs by offset equal to existing UIDs of MCQ answers in lams_qb_tool_answer                                    
SET @max_answer_uid = (SELECT MAX(answer_uid) FROM lams_qb_tool_answer);
UPDATE tl_lascrt11_answer_log SET uid = uid + @max_answer_uid ORDER BY uid DESC;

ALTER TABLE tl_lascrt11_answer_log DROP FOREIGN KEY FK_NEW_610529188_693580A438BF8DFE,
                                   DROP KEY FK_NEW_lascrt11_30113BFC309ED321;

-- rewrite references from Scratchie options to QB options
UPDATE tl_lascrt11_answer_log AS sl, tl_lascrt11_scratchie_answer AS sa, lams_qb_tool_question AS tq, lams_qb_option AS qo
    SET sl.scratchie_answer_uid = qo.uid,
        sl.scratchie_item_uid = tq.tool_question_uid
    WHERE   sa.order_id = qo.display_order
        AND sl.scratchie_answer_uid = sa.uid
        AND qo.qb_question_uid = tq.qb_question_uid
        AND sa.scratchie_item_uid = tq.tool_question_uid;
        
-- prepare for answer inheritance
INSERT INTO lams_qb_tool_answer
    SELECT uid, scratchie_item_uid, scratchie_answer_uid, NULL FROM tl_lascrt11_answer_log;


-- cleanup
ALTER TABLE tl_lascrt11_answer_log DROP COLUMN scratchie_item_uid,
                                   DROP COLUMN scratchie_answer_uid;
                                   
DROP TABLE tl_lascrt11_scratchie_answer;
           
-- prepare for next tool migration, recreate tables
DROP TABLE tmp_question;
DROP TABLE tmp_question_match;
DROP TABLE tmp_qb_question;
DROP TABLE tmp_qb_question_match;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
                           content MEDIUMTEXT);

CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
                                 target_uid BIGINT);
ALTER TABLE tmp_question_match ADD INDEX (target_uid);

CREATE TABLE tmp_qb_question (question_uid BIGINT PRIMARY KEY,
                              content MEDIUMTEXT);

CREATE TABLE tmp_qb_question_match (question_uid BIGINT PRIMARY KEY,
                                    qb_question_uid BIGINT);
ALTER TABLE tmp_qb_question_match ADD INDEX (qb_question_uid);

-- ASSESSMENT

-- shift Assessment question UIDs by offset equal to existing UIDs of MCQ adn Scratchie in lams_qb_tool_question 
SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
-- remove characters that prevent detecting identical questions
UPDATE tl_laasse10_assessment_question SET `title` = TRIM(REPLACE(REPLACE(REPLACE(title, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
                                           `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
                                           uid = uid + @max_tool_question_id ORDER BY uid DESC;
UPDATE tl_laasse10_question_option SET `option_string` = TRIM(REPLACE(REPLACE(REPLACE(option_string, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
                                       `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
UPDATE tl_laasse10_question_result SET `answer_string` = TRIM(REPLACE(REPLACE(REPLACE(answer_string, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));

-- first, process questions with question_type=1 (as MCQ and Scratchie have only this type of questions)

-- create a mapping of Assessment question UID -> its question description + all answers in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
ALTER TABLE tl_laasse10_question_option ADD INDEX (sequence_id),
                                        ADD INDEX tmp_index (sequence_id, question_uid);
                                        
-- check if patch20190423.sql in Assessment has already run
-- if not, create index so we can use it in queries, then drop it                                
SET @exist := (SELECT COUNT(*) from information_schema.statistics WHERE 
    table_name = 'tl_laasse10_option_answer' AND 
    index_name = 'FK_tl_laasse10_option_answer_2' AND 
    table_schema = database());
SET @sqlstmt := IF(@exist > 0,
    'SELECT ''INFO: Index FK_tl_laasse10_option_answer_2 already exists.''',
    'ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 FOREIGN KEY (question_option_uid)    REFERENCES tl_laasse10_question_option (uid)');
PREPARE stmt FROM @sqlstmt;
EXECUTE stmt;

INSERT INTO tmp_question
    SELECT q.uid,
           REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.question, o.option_string ORDER BY o.sequence_id), true)
                                    COLLATE utf8mb4_0900_ai_ci, 
                            ' ', ''),
                   '\t', ''), 
          '&nbsp;', '')
    FROM tl_laasse10_assessment_question AS q
    JOIN tl_laasse10_question_option AS o
        ON q.uid = o.question_uid
    WHERE q.question_type = 1
    GROUP BY q.uid;
    
-- create a similar mapping for existing questions in QB
INSERT INTO tmp_qb_question
    SELECT q.uid AS question_uid,
           REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.description, o.name ORDER BY o.display_order), true)
                                    COLLATE utf8mb4_0900_ai_ci, 
                            ' ', ''),
                   '\t', ''), 
          '&nbsp;', '') AS content
    FROM lams_qb_question AS q
    JOIN lams_qb_option AS o
        ON q.uid = o.qb_question_uid
    GROUP BY q.uid;
    
-- create a mapping which holds IDs of questions which are already in the question bank
INSERT INTO tmp_qb_question_match
    SELECT q.question_uid, qb.question_uid AS qb_question_uid
    FROM tmp_question AS q
    JOIN tmp_qb_question AS qb
        USING (content);
        
-- set up references to QB question UIDs for existing questions
INSERT INTO lams_qb_tool_question
    SELECT qb.question_uid, qb.qb_question_uid, assess.content_id, aq.sequence_id
    FROM tmp_qb_question_match qb
    JOIN tl_laasse10_assessment_question AS aq
        ON qb.question_uid = aq.uid
    JOIN tl_laasse10_assessment AS assess
        ON aq.assessment_uid = assess.uid;
        
        
-- update question names generated for MCQ with real names from Assessment
UPDATE lams_qb_question AS q 
    JOIN (SELECT * FROM tmp_qb_question_match GROUP BY qb_question_uid) AS m
        ON q.uid = m.qb_question_uid
    JOIN tl_laasse10_assessment_question AS aq
        ON aq.uid = m.question_uid
    SET q.name = TRIM(aq.title);

-- create a mapping of Assessment question UID -> UID of one of Assessment questions which holds the same content
INSERT INTO tmp_question_match
    SELECT q.question_uid, merged.question_uid
    FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
    JOIN tmp_question AS q
        USING (content)
    LEFT JOIN lams_qb_tool_question AS qb
        ON qb.tool_question_uid = q.question_uid
    WHERE qb.tool_question_uid IS NULL
    GROUP BY q.question_uid;
    
    
-- second run, process questions with all other question_type
DROP TABLE tmp_question;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
                           content MEDIUMTEXT);

INSERT INTO tmp_question
    SELECT q.uid,
          REPLACE(REPLACE(REPLACE(strip_tags(GROUP_CONCAT(q.question_type,
                                                           IFNULL(q.question, ''),
                                                            q.correct_answer,
                                                         IFNULL(
                                                             CONCAT(IFNULL(o.question, ''),
                                                                   IFNULL(o.option_string, ''),
                                                                   o.option_float,
                                                                   o.correct),
                                                         '')
                                            ORDER BY o.sequence_id), true)
                                    COLLATE utf8mb4_0900_ai_ci,
                          ' ', ''),
                  '\t', ''),
         '&nbsp;', '') 
    FROM tl_laasse10_assessment_question AS q
    LEFT JOIN tl_laasse10_question_option AS o
        ON q.uid = o.question_uid
    WHERE q.question_type != 1
    GROUP BY q.uid;

INSERT INTO tmp_question_match
    SELECT q.question_uid, merged.question_uid
    FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
    JOIN tmp_question AS q
        USING (content)
    GROUP BY q.question_uid;
    
-- reset column for matching QB questions with Assessment questions
UPDATE lams_qb_question SET tmp_question_id = -1;

    
-- fill Question Bank question table with unique questions, with manually incremented question ID
INSERT INTO lams_qb_question SELECT NULL, NULL, aq.question_type, @question_id:=@question_id + 1, 1, IFNULL(assessment.create_date, NOW()), NULL,
                TRIM(REPLACE(REPLACE(strip_tags(aq.question, false) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', ' ')),
                TRIM(aq.description), IFNULL(aq.max_mark, 1), aq.feedback, aq.penalty_factor, aq.answer_required,
                aq.multiple_answers_allowed, aq.incorrect_answer_nullifies_mark, aq.feedback_on_correct, aq.feedback_on_partially_correct,
                aq.feedback_on_incorrect, aq.shuffle, aq.prefix_answers_with_letters, aq.case_sensitive, aq.correct_answer,
                aq.allow_rich_editor, aq.max_words_limit, aq.min_words_limit, aq.hedging_justification_enabled, q.target_uid, NULL
    FROM (SELECT uid,
                 title AS question,
                 question AS description,
                 default_grade AS max_mark,
                 question_type,
                 IF(TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM general_feedback)))= '', 
                     NULL, TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM general_feedback)))) AS feedback,
                 penalty_factor,
                 answer_required,
                 multiple_answers_allowed,
                 incorrect_answer_nullifies_mark,
                 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_correct))) AS feedback_on_correct,
                 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_partially_correct))) AS feedback_on_partially_correct,
                 TRIM(TRIM(BOTH '\r' FROM TRIM(BOTH '\n' FROM feedback_on_incorrect))) AS feedback_on_incorrect,
                 shuffle,
                 prefix_answers_with_letters,
                 case_sensitive,
                 correct_answer,
                 allow_rich_editor,
                 max_words_limit,
                 min_words_limit,
                 hedging_justification_enabled,
                 assessment_uid
          FROM tl_laasse10_assessment_question) AS aq
    JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
        ON aq.uid = q.target_uid
    JOIN tl_laasse10_assessment AS assessment
        ON aq.assessment_uid = assessment.uid;
        
-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
    SELECT q.question_uid, qb.uid, assess.content_id, aq.sequence_id
    FROM lams_qb_question AS qb
    JOIN tmp_question_match AS q
        ON qb.tmp_question_id = q.target_uid
    JOIN tl_laasse10_assessment_question AS aq
        ON q.question_uid = aq.uid
    JOIN tl_laasse10_assessment AS assess
        ON aq.assessment_uid = assess.uid;
    
-- delete obsolete columns
ALTER TABLE tl_laasse10_assessment_question DROP FOREIGN KEY FK_NEW_1720029621_F52D1F93EC0D3147, 
                                       DROP COLUMN title,
                                         DROP COLUMN question,
                                         DROP COLUMN question_type,
                                         DROP COLUMN default_grade,
                                         DROP COLUMN feedback,
                                         DROP COLUMN general_feedback,
                                       DROP COLUMN penalty_factor,
                                       DROP COLUMN answer_required,
                                       DROP COLUMN multiple_answers_allowed,
                                       DROP COLUMN incorrect_answer_nullifies_mark,
                                       DROP COLUMN feedback_on_correct,
                                       DROP COLUMN feedback_on_partially_correct,
                                       DROP COLUMN feedback_on_incorrect,
                                       DROP COLUMN shuffle,
                                       DROP COLUMN prefix_answers_with_letters,
                                       DROP COLUMN case_sensitive,
                                       DROP COLUMN correct_answer,
                                       DROP COLUMN allow_rich_editor,
                                       DROP COLUMN max_words_limit,
                                       DROP COLUMN min_words_limit,  
                                       DROP COLUMN hedging_justification_enabled,
                                          DROP COLUMN session_uid,
                                          DROP COLUMN sequence_id;
                                       

-- delete corrupted data
DELETE a FROM tl_laasse10_option_answer AS a JOIN tl_laasse10_question_option AS o ON a.question_option_uid = o.uid WHERE o.question_uid IS NULL;
DELETE FROM  tl_laasse10_question_option WHERE question_uid IS NULL;

-- some Assessment options can be ordered from 0, some from 1
-- shift ones ordered from 0 by +1 to match the other group        
CREATE TABLE tmp_assessment_option
SELECT DISTINCT question_uid FROM tl_laasse10_question_option WHERE sequence_id = 0;

ALTER TABLE tmp_assessment_option ADD PRIMARY KEY (question_uid);
                                          
UPDATE tl_laasse10_question_option AS o
SET sequence_id = sequence_id + 1
WHERE EXISTS
    (SELECT 1 FROM tmp_assessment_option WHERE question_uid = o.question_uid);
        
DROP TABLE tmp_assessment_option;

-- fill table with options matching unique QB questions inserted above          
INSERT INTO lams_qb_option (qb_question_uid, display_order, name, matching_pair, numerical_option, max_mark, accepted_error, feedback)
    SELECT q.uid, o.sequence_id, IFNULL(o.option_string, ''), o.question, o.option_float,
           IF(o.correct = 1, 1, o.grade), o.accepted_error, o.feedback
    FROM tl_laasse10_question_option AS o
    JOIN lams_qb_question AS q
        ON o.question_uid = q.tmp_question_id
    ORDER BY o.question_uid, o.sequence_id;
    
-- fill table with units matching unique QB questions inserted above
INSERT INTO lams_qb_question_unit (qb_question_uid, display_order, multiplier, name)
    SELECT q.uid, u.sequence_id, u.multiplier, u.unit
    FROM tl_laasse10_assessment_unit AS u
    JOIN lams_qb_question AS q
        ON u.question_uid = q.tmp_question_id
    ORDER BY u.question_uid, u.sequence_id;



ALTER TABLE tl_laasse10_question_result DROP FOREIGN KEY FK_NEW_1720029621_693580A438BF8DFE;
	
-- clean up corrupted Assessment results
DELETE r FROM tl_laasse10_question_result AS r LEFT JOIN tl_laasse10_assessment_result AS a ON r.result_uid = a.uid WHERE a.uid IS NULL;

-- shift Assessment answer UIDs by offset equal to existing UIDs of MCQ and Assessment answers in lams_qb_tool_answer                                    
SET @max_answer_uid = (SELECT MAX(answer_uid) FROM lams_qb_tool_answer);
UPDATE tl_laasse10_question_result SET uid = uid + @max_answer_uid ORDER BY uid DESC;

-- rewrite references from Assessment options to QB options
UPDATE tl_laasse10_question_result AS sl, tl_laasse10_question_option AS o, lams_qb_tool_question AS tq, lams_qb_option AS qo
    SET sl.submitted_option_uid = qo.uid
    WHERE   o.sequence_id = qo.display_order
        AND sl.submitted_option_uid = o.uid
        AND qo.qb_question_uid = tq.qb_question_uid
        AND o.question_uid = tq.tool_question_uid;
        

-- prepare for updating option IDs in tl_laasse10_option_answer
CREATE TABLE tmp_option_answer 
    SELECT DISTINCT sa.question_option_uid, qo.uid
         FROM tl_laasse10_option_answer AS sa
         JOIN tl_laasse10_question_option AS o FORCE INDEX (tmp_index) ON sa.question_option_uid = o.uid
         JOIN lams_qb_option AS qo ON o.sequence_id = qo.display_order
         JOIN lams_qb_tool_question AS tq ON qo.qb_question_uid = tq.qb_question_uid
             AND o.question_uid = tq.tool_question_uid;
 
ALTER TABLE tmp_option_answer ADD PRIMARY KEY (question_option_uid);

-- there can be few malformed answers; delete them
DELETE o 
    FROM tl_laasse10_option_answer AS o
    LEFT JOIN tmp_option_answer AS t USING (question_option_uid)
    WHERE t.question_option_uid IS NULL;

ALTER TABLE tl_laasse10_option_answer DROP FOREIGN KEY FK_tl_laasse10_option_answer_2;

-- proper update
UPDATE tl_laasse10_option_answer AS sa, tmp_option_answer AS t 
    SET sa.question_option_uid = t.uid
    WHERE sa.question_option_uid = t.question_option_uid;

ALTER TABLE tl_laasse10_option_answer ADD CONSTRAINT FK_tl_laasse10_option_answer_2 
    FOREIGN KEY (question_option_uid) REFERENCES lams_qb_option (uid) ON DELETE CASCADE ON UPDATE CASCADE;
        
-- prepare for answer inheritance
INSERT INTO lams_qb_tool_answer
    SELECT uid, assessment_question_uid, submitted_option_uid, answer_string FROM tl_laasse10_question_result;

-- cleanup
ALTER TABLE tl_laasse10_question_result DROP COLUMN assessment_question_uid,
                                        DROP COLUMN submitted_option_uid,
                                        DROP COLUMN answer_string;
                                           
DROP TABLE tl_laasse10_question_option,
           tl_laasse10_assessment_unit;
           
-- prepare for next tool migration, recreate tables
DROP TABLE tmp_question;
DROP TABLE tmp_question_match;

CREATE TABLE tmp_question (question_uid BIGINT PRIMARY KEY,
                           content MEDIUMTEXT);

CREATE TABLE tmp_question_match (question_uid BIGINT PRIMARY KEY,
                                 target_uid BIGINT);
ALTER TABLE tmp_question_match ADD INDEX (target_uid);           
           
           
           
-- QUESTION & ANSWERS (Q&A)

SET @max_tool_question_id = (SELECT MAX(tool_question_uid) FROM lams_qb_tool_question);
-- remove characters that prevent detecting identical questions
UPDATE tl_laqa11_que_content SET `question` = TRIM(REPLACE(REPLACE(REPLACE(question, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
								 `feedback` = TRIM(REPLACE(REPLACE(REPLACE(feedback, '>&nbsp;', '>' ), '\r', '' ), '\n', '')),
								  uid = uid + @max_tool_question_id ORDER BY uid DESC;
UPDATE tl_laqa11_usr_resp SET `answer` = TRIM(REPLACE(REPLACE(REPLACE(answer, '>&nbsp;', '>' ), '\r', '' ), '\n', ''));
                                        
-- create a mapping of Q&A question UID -> its question text in a single column
-- if this column is not *exactly* as in an other row, it means it should be a separate question in QB
-- Also remove all whitespace just for less demanding matching

INSERT INTO tmp_question
    SELECT q.uid AS question_uid,
              REPLACE(REPLACE(REPLACE(strip_tags(question, true) COLLATE utf8mb4_0900_ai_ci, 
                                ' ', ''),
                       '\t', ''), 
              '&nbsp;', '') AS content          
    FROM tl_laqa11_que_content AS q
    GROUP BY q.uid;

-- create a mapping of Q&A question UID -> UID of one of Q&A questions which holds the same content
INSERT INTO tmp_question_match
    SELECT q.question_uid, merged.question_uid AS target_uid
    FROM (SELECT * FROM tmp_question GROUP BY content) AS merged
    JOIN tmp_question AS q USING (content)
    GROUP BY q.question_uid;
                                    
-- reset column for matching QB questions with Scratchie questions
UPDATE lams_qb_question SET tmp_question_id = -1;

INSERT INTO lams_qb_question (uid, `type`, question_id, version, create_date, 
		name,
		description, max_mark, feedback, answer_required, min_words_limit, tmp_question_id) 
    SELECT NULL, 6, @question_id:=@question_id + 1, 1, c.creation_date,
        SUBSTRING(TRIM(REPLACE(REPLACE(strip_tags(qa.question, false) COLLATE utf8mb4_0900_ai_ci, '&nbsp;', ' '), '\t', '')), 1, 200),
        qa.question, 1, qa.feedback, qa.answer_required, qa.min_words_limit, q.target_uid
    FROM (SELECT uid,
                 question AS question,
                 IF(TRIM(feedback) = '', NULL, TRIM(feedback)) AS feedback,
                 answer_required,
                 min_words_limit,
                 qa_content_id
          FROM tl_laqa11_que_content) AS qa
    JOIN (SELECT DISTINCT target_uid FROM tmp_question_match) AS q
        ON qa.uid = q.target_uid
    JOIN tl_laqa11_content AS c
        ON qa.qa_content_id = c.uid;

-- set up references to QB question UIDs created above
INSERT INTO lams_qb_tool_question
    SELECT q.question_uid, qb.uid, c.qa_content_id, qa.display_order
    FROM lams_qb_question AS qb
    JOIN tmp_question_match AS q
        ON qb.tmp_question_id = q.target_uid
    JOIN tl_laqa11_que_content AS qa
        ON q.question_uid = qa.uid
    JOIN tl_laqa11_content AS c
        ON qa.qa_content_id = c.uid;

-- remove columns from Q&A which are duplicated in Question Bank
ALTER TABLE tl_laqa11_que_content DROP COLUMN question,
                                  DROP COLUMN display_order,
                                  DROP COLUMN feedback,
                                  DROP COLUMN answer_required,
                                  DROP COLUMN min_words_limit;
                                  
ALTER TABLE tl_laqa11_usr_resp DROP FOREIGN KEY FK_tl_laqa11_usr_resp_2,
							   -- to keep consistency with other tools
							   RENAME COLUMN response_id TO uid;

-- shift Q&A answer UIDs by offset equal to existing UIDs of Q&A and Assessment answers in lams_qb_tool_answer                                    
SET @max_answer_uid = (SELECT MAX(answer_uid) FROM lams_qb_tool_answer);
UPDATE tl_laqa11_usr_resp SET uid = uid + @max_answer_uid ORDER BY uid DESC;


-- prepare for answer inheritance        
INSERT INTO lams_qb_tool_answer
    SELECT uid, qa_que_content_id, NULL, answer FROM tl_laqa11_usr_resp;

-- fill content_folder_id with real values from learning designs
UPDATE lams_qb_question AS qbque, lams_qb_tool_question AS toolque, lams_learning_activity AS activity, lams_learning_design AS design
 	SET qbque.content_folder_id = design.content_folder_id
 	WHERE qbque.uid = toolque.qb_question_uid
	                AND toolque.tool_content_id = activity.tool_content_id
	                AND activity.learning_design_id = design.learning_design_id;
	                
-- add a random, default content folder ID for activities which were not a part of a LD, like template activities
UPDATE lams_qb_question SET content_folder_id = '93b97f99-a9ad-471b-a71a-5cc58ab2196e' WHERE content_folder_id IS NULL;
	                
-- clean up
ALTER TABLE tl_laqa11_usr_resp DROP COLUMN qa_que_content_id,
							   DROP COLUMN answer;

                               
ALTER TABLE lams_qb_question DROP COLUMN tmp_question_id;
DROP TABLE tmp_question,
           tmp_question_match,
           tmp_qb_question,
           tmp_qb_question_match,
           tmp_option_answer;

-- add a table for generating questionId in QbQuestion and possible other sequences in the future
CREATE TABLE lams_sequence_generator (lams_qb_question_question_id INT);
CREATE UNIQUE INDEX IDX_lams_qb_question_question_id ON lams_sequence_generator(lams_qb_question_question_id);
INSERT INTO lams_sequence_generator(lams_qb_question_question_id) VALUES ((SELECT MAX(question_id) FROM lams_qb_question));


-- find earlierst occurence of questions and fill create date and owner
UPDATE lams_qb_question AS qb,
		   (SELECT qt.qb_question_uid,
		   		   d.user_id AS owner_id,
		   		   MIN(a.create_date_time) AS create_date
		   		FROM lams_qb_tool_question AS qt JOIN
		   			 lams_learning_activity AS a USING (tool_content_id) JOIN
		   			 lams_learning_design AS d USING (learning_design_id)
		   		GROUP BY qb_question_uid
			) AS s
SET qb.create_date = s.create_date,
	qb.owner_id    = s.owner_id
WHERE qb.uid = s.qb_question_uid;

-- fill missing gaps
UPDATE lams_qb_question 
	SET create_date = NOW()
	WHERE create_date IS NULL;

-- fortify date column so there are no NULLs
ALTER TABLE lams_qb_question MODIFY COLUMN create_date DATETIME NOT NULL DEFAULT NOW();

-- LDEV-4827 Add configuration settings for Question Bank
INSERT INTO lams_configuration VALUES
('QbQtiEnable', 				'true',     'config.qb.qti.enable', 			   'config.header.qb', 'BOOLEAN', 1),
('QbWordEnable', 				'true',     'config.qb.word.enable', 			   'config.header.qb', 'BOOLEAN', 1),
('QbCollectionsCreateEnable', 	'true',     'config.qb.collections.create.enable', 'config.header.qb', 'BOOLEAN', 1),
('QbMonitorsReadOnly', 			'false',    'config.qb.monitors.read.only', 	   'config.header.qb', 'BOOLEAN', 1),
('QbStatsMinParticipants',   	'2',      	'config.qb.stats.min.participants',    'config.header.qb', 'LONG', 1),
('QbStatsGroupSize',   			'27',      	'config.qb.stats.group.size',   	   'config.header.qb', 'LONG', 1);

-- LDEV-4828 Add tables for QB collections
CREATE TABLE lams_qb_collection (`uid` BIGINT AUTO_INCREMENT, 
							     `name` VARCHAR(255),
							     `user_id` BIGINT,
							     `personal` TINYINT(1) NOT NULL DEFAULT 0,
							     PRIMARY KEY (uid),
							     INDEX (personal),
							     CONSTRAINT FK_lams_qb_collection_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id) ON DELETE CASCADE ON UPDATE CASCADE
							    );
							    
CREATE TABLE lams_qb_collection_question (`collection_uid`  BIGINT NOT NULL,
									      `qb_question_id` INT NOT NULL,
									   	  CONSTRAINT FK_lams_qb_collection_question_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
											ON DELETE CASCADE ON UPDATE CASCADE,
									   	  CONSTRAINT FK_lams_qb_collection_question_2 FOREIGN KEY (qb_question_id) REFERENCES lams_qb_question (question_id)
											ON DELETE CASCADE ON UPDATE CASCADE
									  );
							   				    
							    
CREATE TABLE lams_qb_collection_organisation (`collection_uid`  BIGINT NOT NULL,
									  		  `organisation_id` BIGINT NOT NULL,
									  		  CONSTRAINT FK_lams_qb_collection_share_1 FOREIGN KEY (collection_uid) REFERENCES lams_qb_collection (uid)
												ON DELETE CASCADE ON UPDATE CASCADE,
									   		  CONSTRAINT FK_lams_qb_collection_share_2 FOREIGN KEY (organisation_id) REFERENCES lams_organisation (organisation_id)
												ON DELETE CASCADE ON UPDATE CASCADE
									  		  );
									  		  
-- add questions to public collection
INSERT INTO lams_qb_collection VALUES (1, 'Public questions', NULL, false);

INSERT INTO lams_qb_collection_question
	SELECT 1, question_id FROM lams_qb_question
	WHERE owner_id IS NULL;
	
-- add questions to private collections
INSERT INTO lams_qb_collection
	SELECT NULL, 'My questions', owner_id, true
	FROM (SELECT DISTINCT owner_id FROM lams_qb_question WHERE owner_id IS NOT NULL) AS qb;

INSERT INTO lams_qb_collection_question
	SELECT c.uid, qb.question_id 
		FROM lams_qb_question AS qb
		JOIN lams_qb_collection AS c ON qb.owner_id = c.user_id
	WHERE qb.owner_id IS NOT NULL;
	
ALTER TABLE lams_qb_question DROP COLUMN owner_id;


INSERT INTO lams_configuration VALUES
('QbCollectionsTransferEnable', 'true', 'config.qb.collections.transfer.enable', 'config.header.qb', 'BOOLEAN', 1);

-- LDEV-4834 Add Learning Outcomes to QB questions
ALTER TABLE lams_outcome_mapping ADD COLUMN qb_question_id INT;
	
-- LDEV-4844 Add a Question Bank event for merging questions 
INSERT INTO lams_log_event_type VALUES (27, 'TYPE_QUESTIONS_MERGED', 'QUESTION_BANK');

-- LDEV-4827 Add configuration settings for Question Bank
INSERT INTO lams_configuration VALUES
('QbMergeEnable',	'true',	'config.qb.merge.enable',	'config.header.qb', 'BOOLEAN', 1);

-- LDEV-4875 Add VSA question type to Assessment and Scratchie
ALTER TABLE lams_qb_question ADD COLUMN autocomplete_enabled TINYINT(1) DEFAULT 0;
	
--  LDEV-4959 Prevent leader and non-leader from creating two GradebookUserLessons at the same time
-- Take care about potential duplicates. For this move all entries to tmp table first.
CREATE TABLE tmp_table SELECT * FROM lams_gradebook_user_lesson;
TRUNCATE TABLE lams_gradebook_user_lesson;
-- Change key to unique
ALTER TABLE `lams_gradebook_user_lesson` 
	DROP FOREIGN KEY `FK_lams_gradebook_user_lesson_1`,
	DROP INDEX `lesson_id`;
ALTER TABLE `lams_gradebook_user_lesson` 
	ADD UNIQUE INDEX `lesson_id` (`lesson_id`,`user_id`),
	ADD CONSTRAINT `FK_lams_gradebook_user_lesson_1` FOREIGN KEY (`lesson_id`) REFERENCES `lams_lesson` (`lesson_id`);
-- Move entries back to lams_gradebook_user_lesson
INSERT IGNORE INTO lams_gradebook_user_lesson SELECT * from tmp_table;
DROP TABLE tmp_table;

--  LDEV-4962 Use complex UUIDs for user portraits 
ALTER TABLE lams_cr_node ADD COLUMN portrait_uuid BINARY(16) AFTER node_id,
						 ADD UNIQUE INDEX IDX_portrait_uuid (portrait_uuid);

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.node_id = u.portrait_uuid
SET n.portrait_uuid = UUID_TO_BIN(UUID());

ALTER TABLE lams_user ADD COLUMN temp BINARY(16) AFTER portrait_uuid;

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.node_id = u.portrait_uuid
SET u.temp = n.portrait_uuid;

ALTER TABLE lams_user DROP COLUMN portrait_uuid;
ALTER TABLE lams_user RENAME COLUMN temp TO portrait_uuid;

CREATE FUNCTION UUID_V4()
    RETURNS CHAR(36)
BEGIN
    -- Generate 8 2-byte strings that we will combine into a UUIDv4
    SET @h1 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');
    SET @h2 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');
    SET @h3 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');
    SET @h6 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');
    SET @h7 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');
    SET @h8 = LPAD(HEX(FLOOR(RAND() * 0xffff)), 4, '0');

    -- 4th section will start with a 4 indicating the version
    SET @h4 = CONCAT('4', LPAD(HEX(FLOOR(RAND() * 0x0fff)), 3, '0'));

    -- 5th section first half-byte can only be 8, 9 A or B
    SET @h5 = CONCAT(HEX(FLOOR(RAND() * 4 + 8)),
                LPAD(HEX(FLOOR(RAND() * 0x0fff)), 3, '0'));

    -- Build the complete UUID
    RETURN LOWER(CONCAT(
        @h1, @h2, '-', @h3, '-', @h4, '-', @h5, '-', @h6, @h7, @h8
    ));
END;

--  LDEV-4962 Use random UUIDs v4 for user portraits 

ALTER TABLE lams_cr_node ADD COLUMN temp BIGINT;

UPDATE lams_cr_node AS n JOIN lams_user AS u USING (portrait_uuid)
SET n.temp = u.user_id,
	n.portrait_uuid = UUID_TO_BIN(UUID_V4());

UPDATE lams_cr_node AS n JOIN lams_user AS u ON n.temp = u.user_id
SET u.portrait_uuid = n.portrait_uuid;

ALTER TABLE lams_cr_node DROP COLUMN temp;

--  LDEV-4746 Allow more options in a QB question
ALTER TABLE lams_qb_tool_question MODIFY COLUMN display_order TINYINT UNSIGNED NOT NULL DEFAULT 1;

--LDEV-5047 Make numeric option more accurate as with large numbers it performs rounding
ALTER TABLE lams_qb_option MODIFY COLUMN numerical_option DOUBLE;

--LDEV-5050 Move "is required" attribute from QB question to activity level
ALTER TABLE lams_qb_tool_question ADD COLUMN answer_required TINYINT NOT NULL DEFAULT 0 AFTER tool_content_id;

UPDATE lams_qb_tool_question AS tq
	JOIN lams_qb_question AS q ON q.uid = tq.qb_question_uid
	SET tq.answer_required = q.answer_required;	
	
ALTER TABLE lams_qb_question DROP COLUMN answer_required;

--LDEV-5058 Move activity categories to only place where they are needed: authoring front end
ALTER TABLE lams_learning_activity DROP FOREIGN KEY FK_lams_learning_activity_12;
ALTER TABLE lams_learning_activity DROP COLUMN activity_category_id;
DROP TABLE lams_activity_category;

--LDEV-5123 Log learner clicks on MCQ questions in Assessment
CREATE TABLE lams_learner_interaction_event (
	uid BIGINT UNSIGNED auto_increment,
	event_type TINYINT UNSIGNED NOT NULL,
	occurred_date_time DATETIME NOT NULL,
	user_id BIGINT NOT NULL,
	qb_tool_question_uid BIGINT,
	option_uid BIGINT,
	PRIMARY KEY (uid),
	INDEX (event_type),
	CONSTRAINT lams_learner_interaction_event_FK1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT lams_learner_interaction_event_FK2 FOREIGN KEY (qb_tool_question_uid) REFERENCES lams_qb_tool_question (tool_question_uid)
		ON DELETE RESTRICT ON UPDATE CASCADE
);

--LDEV-5178 Add password expiration
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required) 
VALUES ('PasswordExpirationMonths', '12', 'config.password.expiration', 'config.header.password.policy', 'LONG', 1),
	   ('PasswordHistoryLimit',     '3',  'config.password.history',    'config.header.password.policy', 'LONG', 1);


ALTER TABLE lams_user ADD COLUMN password_change_date DATETIME AFTER portrait_uuid;

CREATE TABLE lams_user_password_history (
	uid INT UNSIGNED AUTO_INCREMENT,
	user_id BIGINT NOT NULL,
	change_date DATETIME NOT NULL,
	password CHAR(129) NOT NULL,
	PRIMARY KEY (uid),
	CONSTRAINT FK_lams_user_password_history_1 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);

--LDEV-5157 Add column defining support for programming language syntax highlight
ALTER TABLE lams_qb_question ADD COLUMN code_style TINYINT UNSIGNED AFTER min_words_limit;

--LDEV-5208 Add Discussion Sentiment widget
CREATE TABLE lams_discussion_sentiment (
	uid BIGINT AUTO_INCREMENT,
	lesson_id BIGINT NOT NULL,
	tool_question_uid BIGINT NOT NULL,
	burning_question_uid BIGINT,
	user_id BIGINT,
	selected_option TINYINT UNSIGNED,
	PRIMARY KEY (uid),
	INDEX IDX_lams_discussion_sentiment_1 (burning_question_uid),
	UNIQUE INDEX UQ_lams_discussion_sentiment_2 (lesson_id, tool_question_uid, burning_question_uid, user_id),
	CONSTRAINT FK_lams_discussion_sentiment_1 FOREIGN KEY (lesson_id) REFERENCES lams_lesson (lesson_id)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_discussion_sentiment_2 FOREIGN KEY (tool_question_uid) REFERENCES lams_qb_tool_question (tool_question_uid)
		ON DELETE CASCADE ON UPDATE CASCADE,
	CONSTRAINT FK_lams_discussion_sentiment_3 FOREIGN KEY (user_id) REFERENCES lams_user (user_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);

--LDEV-5155 Add new rating style, rubrics
ALTER TABLE lams_rating_criteria ADD COLUMN rating_criteria_group_id MEDIUMINT UNSIGNED AFTER rating_criteria_type_id;

CREATE TABLE lams_rating_rubrics_columns (
	uid INT UNSIGNED AUTO_INCREMENT,
	rating_criteria_group_id MEDIUMINT UNSIGNED,
	rating_criteria_id BIGINT,
	order_id TINYINT UNSIGNED NOT NULL,
	name VARCHAR(2000),
	PRIMARY KEY (uid),
	UNIQUE KEY UQ_lams_rating_rubrics_columns_1 (rating_criteria_group_id, rating_criteria_id, order_id),
	CONSTRAINT FK_lams_rating_rubrics_columns_1 FOREIGN KEY (rating_criteria_id) REFERENCES lams_rating_criteria (rating_criteria_id)
		ON DELETE CASCADE ON UPDATE CASCADE
);

--LDEV-5219 Add "stop-students-at-activity-before-gate" feature
ALTER TABLE lams_learning_activity ADD COLUMN gate_stop_at_preceding_activity TINYINT NOT NULL DEFAULT 0 AFTER gate_password;

--LDEV-5200 Remove learning library groups feature
DROP TABLE IF EXISTS lams_learning_library_group;

-- Update server version so authoring auto rearrange can kick in

UPDATE lams_configuration SET config_value = '4.5' WHERE config_key = 'ServerVersionNumber';

--LDEV-4813 Add indexes for columns used for joins
ALTER TABLE lams_log_event ADD INDEX event_log_user (user_id),
						   ADD INDEX event_log_target_user (target_user_id),
						   ADD INDEX event_log_lesson (lesson_id),
						   ADD INDEX event_log_activity (activity_id);

--LDEV-4288 Prevent duplicate entries
ALTER TABLE lams_gradebook_user_activity DROP KEY activity_id,
										 ADD UNIQUE KEY UQ_lams_gradebook_user_activity_1 (activity_id, user_id);

--LDEV-5250 Delete Pedagogical Planner table
DROP TABLE lams_planner_activity_metadata;
DROP TABLE lams_planner_recent_learning_designs;

ALTER TABLE lams_tool DROP COLUMN pedagogical_planner_url;
ALTER TABLE lams_system_tool DROP COLUMN pedagogical_planner_url;

--LDEV-5253 Make Login As feature configurable
INSERT INTO lams_configuration (config_key, config_value, description_key, header_name, format, required) 
VALUES ('LoginAsEnable', 'true', 'config.login.as.enable', 'config.header.privacy.settings', 'BOOLEAN', 1);

-- If there were no errors, commit and restore autocommit to on
COMMIT;
SET AUTOCOMMIT = 1;
SET FOREIGN_KEY_CHECKS=1;